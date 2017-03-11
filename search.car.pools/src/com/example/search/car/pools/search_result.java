package com.example.search.car.pools;

import java.net.URLEncoder;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import array.list.Header;
import array.list.ad_array_list;
import custom.list.DialogAdapter;
import custom.list.ExpandListSearchAdapter;
import custom.list.custom_post_list;
import custom.list.database_method;
import dash.board.dashboard_main;
import data.service.ServiceHandler;
import data.service.url;
import post.details.post_details_main;

public class search_result extends Activity implements OnClickListener, OnRefreshListener {

	// ExpandableListView
	// FloatingGroupExpandableListView
	ExpandableListView ex_list; // ParallaxExpandableListView
	ArrayList<ad_array_list> child;
	private ExpandListSearchAdapter ExpAdapter;
	// WrapperExpandableListAdapter wrapperAdapter;// = new
	// WrapperExpandableListAdapter(adapter);
	ArrayList<Header> list_header = new ArrayList<Header>();
	String t_date = null;

	int ad_id = 0;

	Boolean isInternetPresent = false;
	String url;
	boolean loadingMore = false;
	ProgressBar p_bar;

	String s_city = "", s_cat = "", s_search = "", fro_m = "", t_o = "";
	int company_id = 0;
	custom_post_list adapter;
	TextView tv_internet_status;
	// ConnectionDetector cd;
	MenuItem bedMenuItem;
	SharedPreferences task;

	// search dialog widgets
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	RelativeLayout close;
	TextView tv;
	int page = 1; // for url
	// View header;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	private SwipeRefreshLayout swipeRefreshLayout;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		task = getSharedPreferences("user", MODE_PRIVATE);
		// cd = new ConnectionDetector(getApplicationContext());
		tv_internet_status = (TextView) findViewById(R.id.tv_internet_status);
		tv_internet_status.setVisibility(View.GONE);

		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// Drawable d = ContextCompat.getDrawable(search_result.this,
		// R.drawable.logo12_copy);
		// getActionBar().setIcon(d);
		// ImageView view = (ImageView)findViewById(android.R.id.home);
		// view.setPadding(10, 0, 0, 0);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));

		// Drawable d = ContextCompat.getDrawable(search_result.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			s_city = extras.getString("city");
			s_cat = extras.getString("category");
			s_search = extras.getString("search_for");
			fro_m = extras.getString("from");
			t_o = extras.getString("to");
			company_id = extras.getInt("company_id");
			// if (fro_m.length() <= 1) {
			// fro_m = "null";
			// }
			// if (t_o.length() <= 1) {
			// t_o = "null";
			// }
		}
		p_bar = (ProgressBar) findViewById(R.id.progressBar1);
		p_bar.setVisibility(View.GONE);
		s_city = new database_method().get_data(this, "select id from city where city_name='" + s_city + "'");
		s_cat = new database_method().get_data(this, "select cat_id from category where cat_name='" + s_cat + "'");
		s_search = new database_method().get_data(this, "select type_id from type where type_name='" + s_search + "'");
		url = new url().search_list + "&city_id=" + s_city;
		if (fro_m.length() != 0)
			try {
				url += "&from=" + URLEncoder.encode(fro_m, "UTF-8");

				if (t_o.length() != 0)
					url += "&to=" + URLEncoder.encode(t_o, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace()
			}
		if (!s_cat.contentEquals("null"))
			url += "&cat_id=" + s_cat;
		if (!s_search.contentEquals("null"))
			url += "&type_id=" + s_search;

		if (company_id == 0) {

		} else {
			url += "&comp_id=" + company_id;
		}

		url += "&page=" + page;
		// Log.i("log", "category> " + s_cat);// "&t_o=" + t_o + "&category=" +
		// s_cat
		// Log.i("log", "search for> " + s_search); // + "&search_for=" +
		// s_search
		// + "&corporate=null" +
		// "&company_id=null";
		// Log.i("log", "url> " + url);

		// swipe layout to refresh
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		// if (cd.isConnectingToInternet()) {
		// accessWebService();
		// tv_internet_status.setVisibility(View.GONE);
		// } else {
		// tv_internet_status.setVisibility(View.VISIBLE);
		// }
		// accessWebService();
		checkInternet();
		ex_list = (ExpandableListView) findViewById(R.id.ex_list_cities);
		ex_list.setDivider(null);
		// header =
		// getLayoutInflater().inflate(R.layout.sample_activity_list_header,
		// ex_list, false);
		// ex_list.addHeaderView(header);
		ExpAdapter = new ExpandListSearchAdapter(this, list_header);
		// wrapperAdapter = new WrapperExpandableListAdapter(ExpAdapter);
		ex_list.setAdapter(ExpAdapter);

		// ExpAdapter.notifyDataSetChanged();

		ex_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition,
					long id) {
				// TODO Auto-generated method stub

				final ad_array_list child = (ad_array_list) getChild(groupPosition, childPosition, list_header);
				Intent i = new Intent(search_result.this, post_details_main.class);
				i.putExtra("category", child.getType());
				i.putExtra("user_email", child.getEmail());
				i.putExtra("post_id", String.valueOf(child.getId()));
				startActivity(i);
				return true;
			}
		});

		final LinearLayout ll_above_ex_list = (LinearLayout) findViewById(R.id.ll_above_ex_list);
		ex_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int threshold = 1;
				int count = ex_list.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((ex_list.getLastVisiblePosition() >= count - threshold) && loadingMore == false) {
						// url = new url().search_list + "id=" + ad_id +
						// "&city=" + s_city + "&fro_m=" + fro_m + "&t_o="
						// + t_o + "&category=" + s_cat + "&search_for=" +
						// s_search
						// + "&corporate=null&company_id=null";
						page += 1;
						int i = url.lastIndexOf("&");
						if (i != -1) {
							url = url.substring(0, i); // not forgot to put
														// check if(endIndex !=
														// -1)
						}
						url += "&page=" + page;
						// Log.i("log", "On scroll Url" + url);
						// Log.i("log", "Page: " + page);
						loadingMore = true;
						checkInternet();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom item that is visible
				// int lastInScreen = firstVisibleItem + visibleItemCount;
				// // is the bottom item visible & not loading more already ?
				// Load
				// // more!
				// // SoapAccessTask lmib = new SoapAccessTask();
				// if ((lastInScreen == totalItemCount) && loadingMore == false)
				// {
				// url = new url().search_list + "id=" + ad_id + "&city=" +
				// s_city + "&fro_m=" + fro_m + "&t_o=" + t_o
				// + "&category=" + s_cat + "&search_for=" + s_search +
				// "&corporate=null&company_id=null";
				//
				// if (cd.isConnectingToInternet()) {
				// accessWebService();
				// tv_internet_status.setVisibility(View.GONE);
				// } else {
				// tv_internet_status.setVisibility(View.VISIBLE);
				// }
				//
				// }
			}
		});

		// custom navigation bar
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);
		View mCustomView = mInflater.inflate(R.layout.layout_navigation, null);

		ib_back = (ImageButton) mCustomView.findViewById(R.id.ib_navigation_back);
		ib_handle = (ImageButton) mCustomView.findViewById(R.id.ib_navigation_handle);
		ib_logo = (ImageButton) mCustomView.findViewById(R.id.ib_navigation_logo);
		ib_search = (ImageButton) mCustomView.findViewById(R.id.ib_navigation_search);
		ib_menu = (ImageButton) mCustomView.findViewById(R.id.ib_navigation_menu);

		l_back = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_back);
		l_handle = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_handle);
		l_logo = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_logo);
		l_nav_search = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_search);
		l_menu = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_menu);

		d1 = ContextCompat.getDrawable(search_result.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(search_result.this, R.drawable.touch_blue_back_color);
		l_back.setBackground(d2);
		l_handle.setBackground(d2);
		l_logo.setBackground(d2);
		l_nav_search.setBackground(d2);
		l_menu.setBackground(d2);

		SVG svg_back = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_back);
		SVG svg_handle = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_menu);
		SVG svg_logo = SVGParser.getSVGFromResource(getResources(), R.raw.logo_splash);
		SVG svg_search = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_search);
		SVG svg_menu = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_triple_dot);

		ib_back.setImageDrawable(svg_back.createPictureDrawable());
		ib_handle.setImageDrawable(svg_handle.createPictureDrawable());
		ib_logo.setImageDrawable(svg_logo.createPictureDrawable());
		ib_search.setImageDrawable(svg_search.createPictureDrawable());
		ib_menu.setImageDrawable(svg_menu.createPictureDrawable());

		ib_back.setOnClickListener(this);
		ib_handle.setOnClickListener(this);
		ib_logo.setOnClickListener(this);
		ib_search.setOnClickListener(this);
		ib_menu.setOnClickListener(this);
		l_back.setOnClickListener(this);
		l_handle.setOnClickListener(this);
		l_logo.setOnClickListener(this);
		l_nav_search.setOnClickListener(this);
		l_menu.setOnClickListener(this);
		// ib_back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		l_handle.setVisibility(View.GONE);

		// ib_search.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// ib_logo.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// ib_menu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	public void initializeStart() {
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			s_city = extras.getString("city");
			s_cat = extras.getString("category");
			s_search = extras.getString("search_for");
			fro_m = extras.getString("from");
			t_o = extras.getString("to");
			company_id = extras.getInt("company_id");

		}

		s_city = new database_method().get_data(this, "select id from city where city_name='" + s_city + "'");
		s_cat = new database_method().get_data(this, "select cat_id from category where cat_name='" + s_cat + "'");
		s_search = new database_method().get_data(this, "select type_id from type where type_name='" + s_search + "'");
		url = new url().search_list + "&city_id=" + s_city;
		if (fro_m.length() != 0)
			try {
				url += "&from=" + URLEncoder.encode(fro_m, "UTF-8");

				if (t_o.length() != 0)
					url += "&to=" + URLEncoder.encode(t_o, "UTF-8");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace()
			}
		if (!s_cat.contentEquals("null"))
			url += "&cat_id=" + s_cat;
		if (!s_search.contentEquals("null"))
			url += "&type_id=" + s_search;
		if (company_id == 0) {

		} else {
			url += "&comp_id=" + company_id;
		}
		page = 1;
		url += "&page=" + page;
		t_date = null;
		ad_id = 0;
		checkInternet();
		ExpAdapter.notifyDataSetChanged();
	}

	public Object getChild(int groupPosition, int childPosition, ArrayList<Header> groups) {
		ArrayList<ad_array_list> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url.replace(" ", "%20");
			// Log.d("log", "url> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);

			// Log.d("log", "response> " + jsonStr);
			if (jsonStr != null) {
				try {

					JSONObject jsonObj = new JSONObject(jsonStr);
					String status = jsonObj.getString("status");
					// Log.i("log", "status: " + status);
					if (status.contentEquals("success")) {
						JSONArray taskarray = jsonObj.getJSONArray("data");
						// Log.i("log", "JSON Object Data: "+obj);
						for (int i = 0; i < taskarray.length(); i++) {
							JSONObject obj = taskarray.getJSONObject(i);
							JSONObject obj_ad = obj.getJSONObject("ad");
							JSONObject obj_user = obj.getJSONObject("user");
							// Log.i("log", "JSON Object Ad: " +
							// obj_ad.toString());
							// Log.i("log", "JSON Object User: " +
							// obj_user.toString());
							if (t_date == null && i == 0) {
								child = null;
								child = new ArrayList<ad_array_list>();
								String[] date = (obj_ad.getString("date")).split("-");
								t_date = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " "
										+ date[0];
							} else if (list_header.size() != 0 && t_date != null && i == 0) {
								list_header.remove(list_header.size() - 1);
							}
							String[] dates = (obj_ad.getString("date")).split("-");
							String date_local = new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1]) - 1]
									+ " " + dates[0];
							if (t_date.equals(date_local)) {
								child.add(new ad_array_list(obj_ad.getInt("id"), obj_ad.getString("type"),
										obj_ad.getString("date"), obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"), obj_user.getString("name"),
										obj_user.getString("profession"), obj_ad.getString("desc"),
										obj_user.getString("email"), obj_user.getString("gender")));
								// Log.i("log", "username> " +
								// obj_user.getString("name") +
								// child.get(child.size() - 1).getUser_name());
								if (i == (taskarray.length() - 1)) {
									list_header.add(new Header(t_date, child));
								}
							} else {
								list_header.add(new Header(t_date, child));
								child = null;
								child = new ArrayList<ad_array_list>();

								String[] date = (obj_ad.getString("date")).split("-");
								t_date = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " "
										+ date[0];

								child.add(new ad_array_list(obj_ad.getInt("id"), obj_ad.getString("type"),
										obj_ad.getString("date"), obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"), obj_user.getString("name"),
										obj_user.getString("profession"), obj_ad.getString("desc"),
										obj_user.getString("email"), obj_user.getString("gender")));
							}

							ad_id = obj_ad.getInt("id");

							webResponse = "false";
						}
					}

				} catch (Exception e) {
					//e.printStackTrace()
					// Log.d("log", "Error: " +e.toString());

				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return webResponse;
		}

		protected void onPostExecute(String result) {
			p_bar.setVisibility(View.GONE);
			if (result.equals("false")) {
				loadingMore = false;
				// ex_list.addHeaderView(header);
			}
			if (result.equals("true") && list_header.size() == 0) {
				tv = new TextView(search_result.this);
				tv.setText("No data found.\nPlease refine your search.");
				tv.setTextSize(25);
				tv.setGravity(Gravity.CENTER);
				if (ex_list.removeFooterView(tv) != true) {
					ex_list.addFooterView(tv);
					// ex_list.removeHeaderView(header);
				} else {
					ex_list.removeFooterView(tv);
					tv.setVisibility(View.GONE);
					ex_list.refreshDrawableState();
					// ex_list.setAdapter(ExpAdapter);
				}
				// if (!tv.isAttachedToWindow())//getVisibility()!=View.VISIBLE)
				// ex_list.addFooterView(tv);
				// else
				// ex_list.removeFooterView(tv);
				// if (cd.isConnectingToInternet()) {
				// accessWebService();
				// tv_internet_status.setVisibility(View.GONE);
				// } else {
				// tv_internet_status.setVisibility(View.VISIBLE);
				// }

			}

			ExpAdapter.notifyDataSetChanged();

		}
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();
		loadingMore = true;
		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	public void onBackPressed() {
		SoapAccessTask lmib = new SoapAccessTask();

		if (lmib.getStatus() == AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib.cancel(true);
			p_bar.setVisibility(View.GONE);
		}

		if (lmib.getStatus() == AsyncTask.Status.FINISHED) {

			lmib.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			Intent i = new Intent(search_result.this, welcome.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			page = 1;
			startActivity(i);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		// bedMenuItem = menu.findItem(R.id.menu_login);
		//
		// if (task.getString("user_id", null) != null) {
		// bedMenuItem.setTitle("Logout");
		// } else {
		// bedMenuItem.setTitle("Login/Register");
		// }

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.menu_add_new_list).setVisible(!(task.getString("user_id",
		// null) == null));
		// menu.findItem(R.id.menu_dashboard).setVisible(!(task.getString("user_id",
		// null) == null));
		return super.onPrepareOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		l_back.setBackground(d2);
		l_handle.setBackground(d2);
		l_logo.setBackground(d2);
		l_nav_search.setBackground(d2);
		l_menu.setBackground(d2);
		// if (bedMenuItem != null) {
		// if (task.getString("user_id", null) != null) {
		// bedMenuItem.setTitle("Logout");
		// } else {
		// bedMenuItem.setTitle("Login/Register");
		// }
		// }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		Intent i;
		// Handle action buttons
		// switch (item.getItemId()) {
		// case R.id.menu_add_new_list:
		// if (task.getString("user_id", null) != null) {
		// i = new Intent(getBaseContext(), create_activity.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(i);
		// } else {
		// Toast.makeText(this, "Please Login first", Toast.LENGTH_LONG).show();
		// }
		// return true;
		// case R.id.menu_dashboard:
		// if (task.getString("user_id", null) != null) {
		// i = new Intent(this, dashboard_main.class);
		// i.putExtra("edit", "12344");
		// startActivity(i);
		// } else {
		// Toast.makeText(this, "Please Login first", Toast.LENGTH_LONG).show();
		// }
		// return true;
		// case R.id.menu_login:
		// if (bedMenuItem.getTitle().equals("Logout")) {
		// SharedPreferences.Editor editor = getSharedPreferences("user",
		// MODE_PRIVATE).edit();
		// editor.clear();
		// editor.commit();
		// bedMenuItem.setTitle("Login/Register");
		// } else {
		// Intent i_user = new Intent(getBaseContext(), user_login.class);
		// startActivity(i_user);
		// }
		//
		// return true;
		//
		// // case R.id.menu_search:
		// // showSearchDialog();
		// // return true;
		//
		// case android.R.id.home:
		//
		// finish();
		//
		// return super.onOptionsItemSelected(item);
		// default:
		return super.onOptionsItemSelected(item);

	}

	private void showSearchDialog() {
		promptsView = new Dialog(this);
		promptsView.requestWindowFeature(Window.FEATURE_NO_TITLE);
		promptsView.setContentView(R.layout.search);
		promptsView.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		RelativeLayout rl = (RelativeLayout) promptsView.findViewById(R.id.RelativeLayout1);
		rl.setBackgroundColor(Color.parseColor("#00000000"));
		l_1 = (LinearLayout) promptsView.findViewById(R.id.dgdf);
		l_2 = (LinearLayout) promptsView.findViewById(R.id.ddf);
		l_3 = (LinearLayout) promptsView.findViewById(R.id.gdf);
		et_from = (EditText) promptsView.findViewById(R.id.et_search_from);
		et_to = (EditText) promptsView.findViewById(R.id.et_search_to);
		sp_city = (TextView) promptsView.findViewById(R.id.sp_sec_city);
		sp_category = (TextView) promptsView.findViewById(R.id.sp_category);
		sp_search_for = (TextView) promptsView.findViewById(R.id.sp_search_for);
		b_search = (Button) promptsView.findViewById(R.id.b_search);
		close = (RelativeLayout) promptsView.findViewById(R.id.iv_close);
		close.setVisibility(View.VISIBLE);
		close.setOnClickListener(this);
		b_search.setOnClickListener(this);
		sp_city.setOnClickListener(this);
		sp_category.setOnClickListener(this);
		sp_search_for.setOnClickListener(this);
		l_1.setOnClickListener(this);
		l_2.setOnClickListener(this);
		l_3.setOnClickListener(this);
		Typeface tf = Typeface.createFromAsset(search_result.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(search_result.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(search_result.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(search_result.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				dialog.hide();
			}
		});
		final RelativeLayout l_close = (RelativeLayout) dialog.findViewById(R.id.l_close);
		l_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if ((v.equals(l_1) || (v.equals(sp_city)))) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		} else if ((v.equals(l_2) || (v.equals(sp_category)))) {
			String[] category = { "All", "Carpool", "Cab", "Rideshare" };
			dialog("Category", category, sp_category);
		} else if ((v.equals(l_3) || (v.equals(sp_search_for)))) {
			String[] search_for = { "Seeker", "Provider", "Both" };
			dialog(" Search For", search_for, sp_search_for);
		} else if (v.equals(close)) {
			promptsView.dismiss();
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(search_result.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(search_result.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_id", 0);
				startActivity(i);
				promptsView.dismiss();
				search_result.this.finish();
			}
		} else if (v.equals(l_nav_search) || v.equals(ib_search)) {
			final int DELAY = 200;
			// ColorDrawable f = new
			// ColorDrawable(Color.parseColor("#0087ca"));
			// ColorDrawable f1 = new
			// ColorDrawable(Color.parseColor("#3398ca"));

			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_nav_search.setBackground(a);
			a.start();
			showSearchDialog();
		} else if (v.equals(l_logo) || v.equals(ib_logo)) {
			final int DELAY = 200;
			// ColorDrawable f = new
			// ColorDrawable(Color.parseColor("#0087ca"));
			// ColorDrawable f1 = new
			// ColorDrawable(Color.parseColor("#3398ca"));

			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_logo.setBackground(a);
			a.start();
			Intent i = new Intent(search_result.this, welcome.class);
			startActivity(i);
		} else if (v.equals(l_menu) || v.equals(ib_menu)) {
			final int DELAY = 200;
			// ColorDrawable f = new
			// ColorDrawable(Color.parseColor("#0087ca"));
			// ColorDrawable f1 = new
			// ColorDrawable(Color.parseColor("#3398ca"));

			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_menu.setBackground(a);
			a.start();

			final PopupMenu popup = new PopupMenu(search_result.this, v);
			popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

			bedMenuItem = popup.getMenu().findItem(R.id.menu_login);
			final SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
			popup.getMenu().findItem(R.id.menu_add_new_list).setVisible(!(task.getString("user_id", null) == null));
			popup.getMenu().findItem(R.id.menu_dashboard).setVisible(!(task.getString("user_id", null) == null));

			if (task.getString("user_id", null) != null) {
				bedMenuItem.setTitle("Logout");
			} else {
				bedMenuItem.setTitle("Login/Register");
			}

			if (task.getString("user_id", null) != null) {
				popup.getMenu().findItem(R.id.menu_login).setTitle("Logout");
			} else {
				popup.getMenu().findItem(R.id.menu_login).setTitle("Login/Register");
			}
			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					int id = item.getItemId();
					Intent i;
					switch (id) {
					case R.id.menu_add_new_list:
						if (task.getString("user_id", null) != null) {
							i = new Intent(getBaseContext(), create_activity.class);
							i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(i);
						} else {
							Toast.makeText(search_result.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(search_result.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(search_result.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_login:
						if (bedMenuItem.getTitle().equals("Logout")) {
							SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							bedMenuItem.setTitle("Login/Register");
						} else {
							Intent i_user = new Intent(getBaseContext(), user_login.class);
							startActivity(i_user);
						}

						return true;
					}
					return false;
				}
			});
			popup.show();
			// } else {
			// openOptionsMenu();
			// }
		} else if (v.equals(l_back) || v.equals(ib_back)) {
			final int DELAY = 200;
			// ColorDrawable f = new
			// ColorDrawable(Color.parseColor("#0087ca"));
			// ColorDrawable f1 = new
			// ColorDrawable(Color.parseColor("#3398ca"));

			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_back.setBackground(a);
			a.start();
			finish();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				ConnectionDetector cd = new ConnectionDetector(search_result.this);
				if (cd.isNetworkAvailable(search_result.this)) {
					ad_id = 0;
					list_header.clear();
					initializeStart();
					ex_list.removeFooterView(tv);
					tv_internet_status.setVisibility(View.GONE);
				} else {
					list_header.clear();
					ExpAdapter.notifyDataSetChanged();
					tv_internet_status.setVisibility(View.VISIBLE);
				}
			}
		}, 4000);
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// Log.i("log", "On PreExecute");
			p_bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Log.i("log", "Network Status during doInBackGround: " +
			// netWorking);
			// Log.i("log", "On doinBackground");
			String deviceVersion = Build.VERSION.RELEASE;
			if (deviceVersion.contentEquals("4.3")) {
//				Log.d("log", "Jelly Bean");
				NetworkInfo localNetworkInfo = ((ConnectivityManager) getSystemService("connectivity"))
						.getActiveNetworkInfo();
				if ((localNetworkInfo != null) && (localNetworkInfo.isConnectedOrConnecting())) {
					return "true";
				}
				
			} else {
//				Log.d("log", "No Jelly Bean");
				String bool = "false";
				try {
					Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 www.searchcarpools.com");
					int returnVal = p1.waitFor();
					if (returnVal == 0) {
						bool = "true";
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace()
				}
				// Log.i("log", "Network Status after doInBackGround: " +
				// netWorking);
				return bool;
			}
			return "false";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Log.i("log", "Network Status onpostexecute: " + netWorking);
			if (result.contentEquals("true")) {
				accessWebService();
				tv_internet_status.setVisibility(View.GONE);
			} else {
				tv_internet_status.setVisibility(View.VISIBLE);
				p_bar.setVisibility(View.GONE);
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}
