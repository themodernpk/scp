package remak.pager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.example.search.car.pools.ConnectionDetector;
import com.example.search.car.pools.R;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import array.list.Header;
import array.list.ad_array_list;
import custom.list.ExpandListSearchAdapter;
import custom.list.custom_post_list;
import custom.list.database_method;
import data.service.ServiceHandler;
import data.service.url;
import post.details.post_details_main;

public class all extends Fragment implements OnRefreshListener {

	// ExpandableListView
	ExpandableListView ex_list;
//	WrapperExpandableListAdapter wrapperAdapter;
//	View header;
//	TextView tv;
	
	ArrayList<ad_array_list> child;
	private ExpandListSearchAdapter ExpAdapter;
	ArrayList<Header> list_header = new ArrayList<Header>();
	String t_date = null;

	int ad_id = 0;

	String url; int page=1; // for url
	boolean loadingMore = false;

	String s_city = "", s_cat = "null", s_search = "null", fro_m = "null", t_o = "null";
	custom_post_list adapter;
	private SQLiteDatabase database;
	ProgressBar p_bar;
	TextView tv_internet_status;
	
	// ConnectionDetector cd;

	private SwipeRefreshLayout swipeRefreshLayout;
	boolean visit_again = false, scrollDone = false;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		url = new url().search_list +"&city_id=" + s_city;
		url+= "&page="+page;
		//Log.i("log", "OnStart url: "+url);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.search_result, container, false);
		// cd = new ConnectionDetector(getActivity());
		tv_internet_status = (TextView) rootView.findViewById(R.id.tv_internet_status);
		p_bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		tv_internet_status.setVisibility(View.GONE);

		//SharedPreferences sharedpreferences = getActivity().getPreferences(0);
		Bundle b = getActivity().getIntent().getExtras();
		if (b!=null)
		s_city = b.getString("city");
		s_city = new database_method().get_data(getActivity(), "select id from city where city_name='" + s_city + "'");

//		s_cat = new database_method().get_data(getActivity(),
//				"select cat_id from category where cat_name='" + s_cat + "'");
//		s_search = new database_method().get_data(getActivity(),
//				"select type_id from type where type_name='" + s_search + "'");
//		url = new url().search_list + "id=0&city=" + s_city + "&fro_m=" + fro_m + "&t_o=" + t_o + "&category=" + s_cat
//				+ "&search_for=" + s_search + "&corporate=null&company_id=null";
		url = new url().search_list +"&city_id=" + s_city;
		url+= "&page="+page;
		//Log.i("log", "OncreateView url: "+url);
		checkInternet();
		ex_list = (ExpandableListView) rootView.findViewById(R.id.ex_list_cities);
		ex_list.setDivider(null);
//		header = getActivity().getLayoutInflater().inflate(R.layout.sample_activity_list_header, ex_list, false);
		ExpAdapter = new ExpandListSearchAdapter(getActivity(), list_header);
//		wrapperAdapter = new WrapperExpandableListAdapter(ExpAdapter);
		ex_list.setAdapter(ExpAdapter);

		// swipe layout to refresh
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		// Toast.makeText(getActivity(), "OnCreateView",
		// Toast.LENGTH_SHORT).show();
		ex_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition,
					long id) {
				// TODO Auto-generated method stub

				final ad_array_list child = (ad_array_list) getChild(groupPosition, childPosition, list_header);
				Intent i = new Intent(getActivity(), post_details_main.class);
				i.putExtra("category", child.getType());
				i.putExtra("user_email", child.getEmail());
				i.putExtra("post_id", String.valueOf(child.getId()));
				startActivity(i);
				return true;
			}
		});

		ex_list.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int threshold = 1;
				int count = ex_list.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((ex_list.getLastVisiblePosition() >= count - threshold) && loadingMore == false) {
//						url = new url().search_list + "id=" + ad_id + "&city=" + s_city + "&fro_m=" + fro_m + "&t_o="
//								+ t_o + "&category=" + s_cat + "&search_for=" + s_search
//								+ "&corporate=null&company_id=null";
						scrollDone = true;
						page+=1;
						int i = url.lastIndexOf("&");
						if (i != -1)  
					    {
					        url = url.substring(0, i); // not forgot to put check if(endIndex != -1)
					    }
						url+= "&page="+page;
						//Log.i("log", "On scroll Url"+url);
						//Log.i("log", "Page: "+page);
						loadingMore = true;
						checkInternet();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom item that is visible
				int lastInScreen = firstVisibleItem + visibleItemCount;
				// is the bottom item visible & not loading more already ? Load
				// more!
				// SoapAccessTask lmib = new SoapAccessTask();

				// if ((lastInScreen == totalItemCount) && loadingMore == false)
				// {
				// url = new url().search_list + "id=" + ad_id + "&city=" +
				// s_city + "&fro_m=" + fro_m + "&t_o=" + t_o
				// + "&category=" + s_cat + "&search_for=" + s_search +
				// "&corporate=null&company_id=null";
				//
				// // if (cd.isConnectingToInternet()) {
				// // accessWebService();
				// // tv_internet_status.setVisibility(View.GONE);
				// // } else {
				// // tv_internet_status.setVisibility(View.VISIBLE);
				// // }
				//// accessWebService();
				//
				// }
			}
		});

		return rootView;
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
			// Log.d("url: ", "> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			// Log.d("Response: ", "> " + jsonStr);
			if (jsonStr != null) {
				try {

					JSONObject jsonObj = new JSONObject(jsonStr);
					String status = jsonObj.getString("status");
					//Log.i("log", "status: "+status);
					if (status.contentEquals("success")){
						JSONArray taskarray = jsonObj.getJSONArray("data");
//						//Log.i("log", "JSON Object Data: "+obj);
						for (int i = 0; i < taskarray.length(); i++) {
							JSONObject obj = taskarray.getJSONObject(i);
							JSONObject obj_ad = obj.getJSONObject("ad");
							JSONObject obj_user = obj.getJSONObject("user");
							//Log.i("log", "JSON Object Ad: "+obj_ad.toString());
							//Log.i("log", "JSON Object User: "+obj_user.toString());
							if (t_date == null && i == 0) {
								child = null;
								child = new ArrayList<ad_array_list>();
								String[] date = (obj_ad.getString("date")).split("-");
								t_date = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
							} else if (list_header.size() != 0 && t_date != null && i == 0) {
								list_header.remove(list_header.size() - 1);
							}
							String[] dates = (obj_ad.getString("date")).split("-");
							String date_local = new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1]) - 1] + " "
									+ dates[0];
							if (t_date.equals(date_local)) {
								child.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
								//Log.i("log", "username> " + obj_user.getString("name") + child.get(child.size() - 1).getUser_name());
								if (i == (taskarray.length() - 1)) {
									list_header.add(new Header(t_date, child));
								}
							} else {
								list_header.add(new Header(t_date, child));
								child = null;
								child = new ArrayList<ad_array_list>();
	
								String[] date = (obj_ad.getString("date")).split("-");
								t_date = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
	
								child.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
							}
	
							ad_id = obj_ad.getInt("id");
	
							webResponse = "false";
						}
					}

				} catch (JSONException e) {
					//e.printStackTrace();
				}
			} else {
				// Log.e("ServiceHandler", "Couldn't get any data from the
				// url");
			}
			return webResponse;
		}

		protected void onPostExecute(String result) {
			if (result.equals("false")) {
				loadingMore = false;
//				ex_list.addHeaderView(header);
			}
//			tv = new TextView(getActivity());
//			tv.setText("No data found.\nPlease refine your search.");
//			tv.setTextSize(25);
//			tv.setGravity(Gravity.CENTER);
//			if (result.equals("true") && list_header.size() == 0) {
//				if (ex_list.removeFooterView(tv) != true) {
//					ex_list.addFooterView(tv);
////					ex_list.removeHeaderView(header);
//				} else {
//					ex_list.removeFooterView(tv);
//					tv.setVisibility(View.GONE);
//					ex_list.refreshDrawableState();
//					// ex_list.setAdapter(ExpAdapter);
//				}
//			}
			ExpAdapter.notifyDataSetChanged();
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
		if ((lmib.getStatus() == AsyncTask.Status.RUNNING) || (check.getStatus() == AsyncTask.Status.RUNNING)) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib.cancel(true);
			check.cancel(true);
			p_bar.setVisibility(View.GONE);
		} else if (lmib.getStatus() == AsyncTask.Status.FINISHED || check.getStatus()==AsyncTask.Status.FINISHED) {
			lmib.cancel(true);
			check.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			page=1;
			getActivity().finish();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == android.R.id.home) {
			onBackPressed();
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				ConnectionDetector cd = new ConnectionDetector(getActivity());
				if (cd.isNetworkAvailable(getActivity())) {
					ad_id = 0;
					list_header.clear();
					page=1;
					initializeStart();
//					ex_list.removeFooterView(tv);
					tv_internet_status.setVisibility(View.GONE);
				} else {
					list_header.clear();
					ExpAdapter.notifyDataSetChanged();
					tv_internet_status.setVisibility(View.VISIBLE);
				}
			}
		}, 4000);
	}

	public void initializeStart() {
//		Bundle b = getActivity().getIntent().getExtras();
//		if (b!=null)
//		s_city = b.getString("city");
//		s_city = new database_method().get_data(getActivity(), "select id from city where city_name='" + s_city + "'");

//		s_cat = new database_method().get_data(getActivity(),
//				"select cat_id from category where cat_name='" + s_cat + "'");
//		s_search = new database_method().get_data(getActivity(),
//				"select type_id from type where type_name='" + s_search + "'");
//		url = new url().search_list + "id=0&city=" + s_city + "&fro_m=" + fro_m + "&t_o=" + t_o + "&category=" + s_cat
//				+ "&search_for=" + s_search + "&corporate=null&company_id=null";
		url = new url().search_list +"&city_id=" + s_city;
		url+= "&page="+page;
		//Log.i("log", "On Refresh url: "+url);
		
		t_date = null;
		ad_id = 0;
		checkInternet();
		ExpAdapter.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		list_header.clear();
		ExpAdapter.notifyDataSetChanged();
		// list_header=null;
		visit_again = true;
		page=1;
		// Log.i("log", "onDestroyView");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// Log.i("log", "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// list_header.clear();
		// ExpAdapter.notifyDataSetChanged();
		// start=false;
		// visit_again=false;
		// Log.i("log", "onPause");
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
				NetworkInfo localNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity"))
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
					////e.printStackTrace();
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
				// list_header.clear();
				if (scrollDone) {
					accessWebService();
					scrollDone = false;
				} else if (visit_again) {
					ad_id = 0;
					list_header.clear();
					initializeStart();
					visit_again=false;
					//Log.i("log", "CheckInternet inside");
				} else {
					list_header.clear();
					accessWebService();
					//Log.i("log", "CheckInternet outside");
				}
				tv_internet_status.setVisibility(View.GONE);
			} else {
				tv_internet_status.setVisibility(View.VISIBLE);
				p_bar.setVisibility(View.GONE);
			}

			// //Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}