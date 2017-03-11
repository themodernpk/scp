package com.example.search.car.pools;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import array.list.comp_array_list;
import custom.list.DialogAdapter;
import custom.list.database_method;
import data.service.ServiceHandler;
import data.service.url;

public class company_list extends Activity implements OnClickListener {
	ListView list;
	int ad_id = 0;
	ArrayList<comp_array_list> company_item;

	boolean loadingMore = false;

	String s_city = "";
	custom_company_list adapter;

	ProgressBar p_bar;
	private SwipeRefreshLayout swipeRefreshLayout;

	int page = 1;
	boolean scrollDone_company = false;

	TextView tv_internet_status;// t1;
	EditText et_comp_name;
	ImageView b_search;
	String url_search_company = "";
	String city_id = "";

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	LinearLayout l_no_result, l_search;
	ImageView watermark;
	RelativeLayout rl_add_company;

	// Add company
	EditText et_c_name, et_c_address, et_name, et_official_email, et_contact_number, et_designation;
	TextView sp_city, t1;
	Button b_register;
	String url = "";
	RelativeLayout rl;
	ImageView i1, i01, i2, i3, i4, i5, i6, i7, i8, i02, i05, i06, i07, i08;
	boolean add_company = false, search_company = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company_search);
		list = (ListView) findViewById(R.id.list_cities);
		p_bar = (ProgressBar) findViewById(R.id.progressBar1);
		p_bar.setVisibility(View.GONE);
		list.setDividerHeight(2);
		company_item = new ArrayList<comp_array_list>();

		l_no_result = (LinearLayout) findViewById(R.id.l_no_result);
		l_no_result.setVisibility(View.GONE);

		l_search = (LinearLayout) findViewById(R.id.l_search);
		l_search.setOnClickListener(this);
		// Drawable d = ContextCompat.getDrawable(company_list.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// prevent
		// keyboard
		// from
		// popping
		Bundle b = getIntent().getExtras();
		if (b != null) {
			city_id = b.getString("city_id");
		}
		// url = new url().search_company_list + "&city_id=" + city_id +
		// "&page=" + page;

		// swipe layout to refresh
		// swipeRefreshLayout = (SwipeRefreshLayout)
		// findViewById(R.id.swipe_refresh_layout);
		// swipeRefreshLayout.setOnRefreshListener(this);
		// swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color,
		// android.R.color.holo_green_light,
		// android.R.color.holo_orange_light, android.R.color.holo_red_light);

		list.setDividerHeight(2);
		// checkInternet();
		adapter = new custom_company_list(this, company_item);
		list.setAdapter(adapter);

		tv_internet_status = (TextView) findViewById(R.id.tv_internet_status);
		tv_internet_status.setVisibility(View.GONE);

		b_search = (ImageView) findViewById(R.id.b_find_company);
		SVG svg_search = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_search);
		b_search.setImageDrawable(svg_search.createPictureDrawable());
		
		watermark = (ImageView) findViewById(R.id.iv_logo_watermark);
		SVG svg_watermark = SVGParser.getSVGFromResource(getResources(), R.raw.logo_watermark);
		watermark.setImageDrawable(svg_watermark.createPictureDrawable());
		
		rl_add_company = (RelativeLayout)findViewById(R.id.rl_add_company);
		rl_add_company.setVisibility(View.GONE);
		
		url_search_company = new url().search_company;

		et_comp_name = (EditText) findViewById(R.id.et_company_name_search);
		b_search.setOnClickListener(this);

		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("company_name", company_item.get(position).getCompany_name());
				intent.putExtra("company_id", String.valueOf(company_item.get(position).getCompany_id()));
				setResult(2, intent);
				finish();

			}
		});
		list.setOnScrollListener(new OnScrollListener() {
			// useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = list.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((list.getLastVisiblePosition() >= count - threshold) && loadingMore == false) {

						page += 1;
						int i = url_search_company.lastIndexOf("&");
						if (i != -1) {
							url_search_company = url_search_company.substring(0, i); // not
																						// forgot
																						// to
																						// put
							// check if(endIndex !=
							// -1)
						}
						url_search_company += "&page=" + page;
						//Log.i("log", "On scroll Url" + url_search_company);
						//Log.i("log", "Page: " + page);
						loadingMore = true;
						search_company = true;
						checkInternet();
					}
				}
			}

			// dumdumdum
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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

		d1 = ContextCompat.getDrawable(company_list.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(company_list.this, R.drawable.touch_blue_back_color);
		l_back.setBackground(d2);
		l_handle.setBackground(d2);
		l_logo.setBackground(d2);
		l_nav_search.setBackground(d2);
		l_menu.setBackground(d2);

		SVG svg_back = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_back);
		SVG svg_handle = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_menu);
		SVG svg_logo = SVGParser.getSVGFromResource(getResources(), R.raw.logo_splash);
		// SVG svg_search = SVGParser.getSVGFromResource(getResources(),
		// R.raw.actionbar_search);
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
		
//		ib_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		l_handle.setVisibility(View.GONE);
		l_menu.setVisibility(View.GONE);
		// popup = new PopupMenu(welcome.this, ib_menu);

		l_nav_search.setVisibility(View.GONE);
//		ib_logo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//				// Intent i = new Intent(company_list.this, welcome.class);
//				// startActivity(i);
//			}
//		});
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		//-------- Navigation bar End -------------
		
		// Add Company
		
		et_c_name = (EditText) findViewById(R.id.et_company_name);
		et_c_address = (EditText) findViewById(R.id.et_company_address);
		et_name = (EditText) findViewById(R.id.et_your_name);
		et_official_email = (EditText) findViewById(R.id.et_offical_email);
		et_contact_number = (EditText) findViewById(R.id.et_contact_number);
		et_designation = (EditText) findViewById(R.id.et_designation);
		sp_city = (TextView) findViewById(R.id.sp_sec_city);
		b_register = (Button) findViewById(R.id.b_company_register);
		b_register.setOnClickListener(this);
		sp_city.setOnClickListener(this);
		
		i1 = (ImageView) findViewById(R.id.iv_1);
		i01 = (ImageView) findViewById(R.id.iv_01);
		i02 = (ImageView) findViewById(R.id.iv_02);
		i05 = (ImageView) findViewById(R.id.iv_05);
		i06 = (ImageView) findViewById(R.id.iv_06);
		i07 = (ImageView) findViewById(R.id.iv_07);
		i08 = (ImageView) findViewById(R.id.iv_08);

		i2 = (ImageView) findViewById(R.id.iv_2);
		i3 = (ImageView) findViewById(R.id.iv_3);
		i4 = (ImageView) findViewById(R.id.iv_4);
		i5 = (ImageView) findViewById(R.id.iv_5);
		i6 = (ImageView) findViewById(R.id.iv_6);
		i7 = (ImageView) findViewById(R.id.iv_7);
		i8 = (ImageView) findViewById(R.id.iv_8);
		t1 = (TextView) findViewById(R.id.tv_2_create_list);
		rl = (RelativeLayout) findViewById(R.id.l_city_create_list);
		rl.setOnClickListener(this);

		Typeface tf = Typeface.createFromAsset(company_list.this.getAssets(), "AvenirLTStd_Book.otf");
		t1.setTypeface(tf);
		et_c_name.setTypeface(tf);
		et_c_address.setTypeface(tf);
		et_name.setTypeface(tf);
		et_official_email.setTypeface(tf);
		et_contact_number.setTypeface(tf);
		et_designation.setTypeface(tf);
		sp_city.setTypeface(tf);
		b_register.setTypeface(tf);
		et_comp_name.setTypeface(tf);
		
		TextView t1 = (TextView)findViewById(R.id.tv_no_result1);
		TextView t2 = (TextView)findViewById(R.id.tv_no_result2);
		t1.setTypeface(tf);
		t2.setTypeface(tf);
		
		et_c_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i1.setVisibility(View.INVISIBLE);
				i01.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i1.setVisibility(View.VISIBLE);
					i01.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_c_address.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i2.setVisibility(View.INVISIBLE);
				i02.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i2.setVisibility(View.VISIBLE);
					i02.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i5.setVisibility(View.INVISIBLE);
				i05.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i5.setVisibility(View.VISIBLE);
					i05.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_official_email.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i6.setVisibility(View.INVISIBLE);
				i06.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i6.setVisibility(View.VISIBLE);
					i06.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_contact_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i7.setVisibility(View.INVISIBLE);
				i07.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i7.setVisibility(View.VISIBLE);
					i07.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_designation.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i8.setVisibility(View.INVISIBLE);
				i08.setVisibility(View.VISIBLE);
				if (s.length() < 1) {
					i8.setVisibility(View.VISIBLE);
					i08.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		
		
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url_search_company.replace(" ", "%20");
			//Log.d("log", "> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("log", "> " + jsonStr);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONArray taskarray = jsonObj.getJSONArray("data");
					if (taskarray.length() != 0) {
						for (int i = 0; i < taskarray.length(); i++) {

							final JSONObject jsonObject = taskarray.getJSONObject(i);
							company_item.add(new comp_array_list(jsonObject.getInt("company_id"),
									jsonObject.getString("company_name"), jsonObject.getString("company_add")));
							ad_id = jsonObject.getInt("company_id");
							webResponse = "false";
						}
					} else {
						webResponse = "true";
					}
				} catch (JSONException e) {
					//e.printStackTrace();
				}
			} else {
				//Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			//Log.d("log", "Webresponse: " + webResponse);
			return webResponse;
		}

		protected void onPostExecute(String result) {
			if (result.equals("false")) {
				loadingMore = false;
				remove_data();
				rl_add_company.setVisibility(View.GONE);
				l_no_result.setVisibility(View.GONE);
//				list.setVisibility(View.VISIBLE);
			} else if (result.equals("true") && company_item.size() == 0) {
				// if (result.equals("true") && company_item.size() == 0)
				// ConnectionDetector cd = new
				// ConnectionDetector(getApplicationContext());
				// if (cd.isConnectingToInternet()) {
				// accessWebService();
				// } else {
				// Toast.makeText(getApplication(), "No Internet Connection",
				// Toast.LENGTH_LONG).show();
				//
				// }
//				list.setVisibility(View.GONE);
				adapter.clear();
				adapter.notifyDataSetChanged();
				l_no_result.setVisibility(View.VISIBLE);
				remove_data();
//				et_c_name.setHint("Company Name");
//				et_c_name.setText(et_comp_name.getText().toString());
				rl_add_company.setVisibility(View.VISIBLE);
			}
			b_search.setEnabled(true);
			l_search.setEnabled(true);
			watermark.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);
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
		CheckInternet check = new CheckInternet();
		if (lmib.getStatus() == AsyncTask.Status.RUNNING || check.getStatus() == AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib.cancel(true);
			check.cancel(true);
			p_bar.setVisibility(View.GONE);

		}

		if (lmib.getStatus() == AsyncTask.Status.FINISHED || check.getStatus() == AsyncTask.Status.FINISHED) {
			lmib.cancel(true);
			check.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			Intent intent = new Intent();
			intent.putExtra("company_name", "Select Company");
			intent.putExtra("company_id", "0");
			setResult(0, intent);
			finish();
		}
	}

	public class custom_company_list extends ArrayAdapter<comp_array_list> {

		private final Activity context;
		private ArrayList<comp_array_list> ad_item;

		public custom_company_list(Context con, ArrayList<comp_array_list> ad_item) {
			super(con, R.layout.custom_company_list, ad_item);
			this.context = (Activity) con;
			this.ad_item = ad_item;

		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater inflater = context.getLayoutInflater();
			View rowView = inflater.inflate(R.layout.custom_company_list, null, true);
			RelativeLayout l = (RelativeLayout) rowView.findViewById(R.id.l1_top_company_list);

			l.setVisibility(View.GONE);

			l.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// FragmentManager fm = getFragmentManager();
					// android.support.v4.app.FragmentTransaction
					// fragmentTransaction = fm.beginTransaction();
					// fragmentTransaction.replace(R.id.content_frame, new
					// add_company());
					// fragmentTransaction.commit();
				}
			});

			ImageView iv = (ImageView) rowView.findViewById(R.id.img_company_list_svg);
			SVG svg1 = SVGParser.getSVGFromResource(context.getResources(), R.raw.circle_gravatar);
			iv.setImageDrawable(svg1.createPictureDrawable());

			LinearLayout ll_main = (LinearLayout) rowView.findViewById(R.id.ll_main);
			RelativeLayout l1 = (RelativeLayout) rowView.findViewById(R.id.ll_company_image);
			LinearLayout l2 = (LinearLayout) rowView.findViewById(R.id.ll_company_details);
			if (!(position % 2 == 0)) {
				ll_main.removeAllViews();
				ll_main.addView(l2);
				ll_main.addView(l1);
			}
			TextView name = (TextView) rowView.findViewById(R.id.tv_company_name);
			TextView address = (TextView) rowView.findViewById(R.id.tv_address);
			name.setText(getCapitalWords(ad_item.get(position).getCompany_name()));
			address.setText(getCapitalWords(ad_item.get(position).getCompany_add()));

			Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Heavy.otf");
			name.setTypeface(tf);
			address.setTypeface(tf);
			TextView t1 = (TextView) rowView.findViewById(R.id.tv_add_company);
//			TextView t2 = (TextView) rowView.findViewById(R.id.tv_company);
			t1.setTypeface(tf);
//			t2.setTypeface(tf);

			return rowView;
		}

		private CharSequence getCapitalWords(String sentence) {
			String[] words = null;
			if (sentence != null && sentence.length() > 2) {
				try {
					words = sentence.split(" ");
					for (int i = 0; i < words.length; i++) {
						words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
				return TextUtils.join(" ", words);
			} else
				return sentence;
		}
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			p_bar.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Log.i("log", "Network Status during doInBackGround: " +
			// netWorking);
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
					//e.printStackTrace();
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
			if (result.contentEquals("true") && search_company == true) {
				accessWebService();
				search_company = false;
			} else if (result.contentEquals("true") && add_company == true) {
				new SoapAccessTaskAddCompany().execute();
				add_company = false;
			} else {
				Toast.makeText(company_list.this, "Server not reachable", Toast.LENGTH_SHORT).show();
				p_bar.setVisibility(View.GONE);
				b_search.setEnabled(true);
				l_search.setEnabled(true);
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		l_back.setBackground(d2);
		l_handle.setBackground(d2);
		l_logo.setBackground(d2);
		l_nav_search.setBackground(d2);
		l_menu.setBackground(d2);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(sp_city) || v.equals(rl)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		} else if (v.equals(b_register)) {
			if (et_c_name.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Company Name", Toast.LENGTH_LONG).show();
				et_c_name.setError("Required");
			}
			else if (et_c_address.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Company Address", Toast.LENGTH_LONG).show();
				et_c_address.setError("Required");
			}
			else if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
//				Toast.makeText(company_list.this, "Please Select City", Toast.LENGTH_LONG).show();
				sp_city.setError("Required");
			} else if (et_name.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Your Name", Toast.LENGTH_LONG).show();
				et_name.setError("Required");
			}
			else if (!(new EmailAndPhoneChecker().validEmail(et_official_email.getText().toString()))){
//				Toast.makeText(company_list.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
				et_official_email.setError("Required");
			}
			else if (et_contact_number.getText().toString().length() < 8
					|| et_contact_number.getText().toString().length() > 14) {
//				Toast.makeText(company_list.this, "Please Enter a valid Contact No.", Toast.LENGTH_LONG).show();
				et_contact_number.setError("Required");
			}
			else if (et_designation.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Your Designation", Toast.LENGTH_LONG).show();
				et_designation.setError("Required");
			}
			else {
				String s_city = new database_method().get_data(company_list.this,
						"select id from city where city_name='" + sp_city.getText().toString() + "'");
				try {
					url = new url().create_company + "&comp_name="
							+ URLEncoder.encode(et_c_name.getText().toString(), "UTF-8") + "&comp_add="
							+ URLEncoder.encode(et_c_address.getText().toString(), "UTF-8") + "&city_id=" + s_city
							+ "&c_name=" + URLEncoder.encode(et_name.getText().toString(), "UTF-8") + "&c_email="
							+ URLEncoder.encode(et_official_email.getText().toString(), "UTF-8") + "&c_phone="
							+ et_contact_number.getText().toString() + "&c_designation="
							+ URLEncoder.encode(et_designation.getText().toString(), "UTF-8");
					SharedPreferences task = company_list.this.getSharedPreferences("user", company_list.this.MODE_PRIVATE);
					// if (task.getString("user_id", null) != null) {
					// ConnectionDetector cd = new
					// ConnectionDetector(AddCompany.this);
					// if (cd.isConnectingToInternet()) {
					// accessWebService();
					// } else {
					// Toast.makeText(AddCompany.this, "No Internet Connection",
					// Toast.LENGTH_LONG).show();
					// }
					add_company = true;
					checkInternet();
				} catch (Exception e) {
					//e.printStackTrace();
				}
				// } else {
				// Toast.makeText(AddCompany.this, "Please Login first",
				// Toast.LENGTH_LONG).show();
				// }
			}
		} else if (v.equals(b_search) || v.equals(l_search)) {
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(l_search.getWindowToken(), 0);
			remove_data();
			rl_add_company.setVisibility(View.GONE);
			l_no_result.setVisibility(View.GONE);
			adapter.clear();
			adapter.notifyDataSetChanged();
				if (!et_comp_name.getText().toString().contentEquals("")) {
					page=1;
					url_search_company += "&city_id=" + city_id + "&comp_name=" + et_comp_name.getText().toString()
							+ "&page=" + page;
					//Log.i("log", "url on click: " + url_search_company);
					adapter.clear();
					adapter.notifyDataSetChanged();
					search_company = true;
					b_search.setEnabled(false);
					l_search.setEnabled(false);
					checkInternet();
				} else {
					Toast.makeText(company_list.this, "Enter Company Name to search", Toast.LENGTH_SHORT).show();
				}
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
			Intent intent = new Intent();
			intent.putExtra("company_name", "Select Company");
			intent.putExtra("company_id", "0");
			setResult(0, intent);
			finish();
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
		} 
		
		
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(company_list.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(company_list.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(company_list.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				if (tv.equals(sp_city)) {
					i3.setVisibility(View.VISIBLE);
					i4.setVisibility(View.GONE);
					t1.setVisibility(View.VISIBLE);
				} else {

				}
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

	private class SoapAccessTaskAddCompany extends AsyncTask<String, Void, String> {

		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(company_list.this, null, "Adding ! please Wait...");

		}

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url.replace(" ", "%20");
			//Log.d("url: ", "> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("Response: ", "> " + jsonStr);
			if (jsonStr != null) {
				webResponse = jsonStr;
			} else {
				//Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return webResponse;
		}

		protected void onPostExecute(String result) {
			progress.dismiss();
			if (result.toUpperCase().equals("COMPANY CREATED SUCCESSFULLY")) {
				remove_data();
				Toast.makeText(company_list.this, result, Toast.LENGTH_LONG).show();
				rl_add_company.setVisibility(View.GONE);
				adapter.notifyDataSetChanged();
				p_bar.setVisibility(View.GONE);
			} else {
				Toast.makeText(company_list.this, "Error Processing request, please try again", Toast.LENGTH_LONG).show();

			}
		}
	}

	public void remove_data() {
		et_c_name.setText("");
		et_c_address.setText("");
		et_name.setText("");
		et_official_email.setText("");
		et_contact_number.setText("");
		et_designation.setText("");
		sp_city.setText("Select City");
		i3.setVisibility(View.GONE);
		i4.setVisibility(View.VISIBLE);
		t1.setVisibility(View.GONE);
	}
	
}