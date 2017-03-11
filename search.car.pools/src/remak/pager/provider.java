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

public class provider extends Fragment implements OnRefreshListener {

	// ExpandableListView
	ExpandableListView ex_list_p;
//	WrapperExpandableListAdapter wrapperAdapter_p;
//	TextView tv_p;
//	View header_p;
	
	ArrayList<ad_array_list> child_p;
	private ExpandListSearchAdapter ExpAdapter_p;
	ArrayList<Header> list_header_p = new ArrayList<Header>();
	String t_date_p = null;
	
	int ad_id_p = 0;

	String url_p;
	boolean loadingMore_p = false;
	ProgressBar p_bar;
	String s_city_p = "", s_cat_p = "null", s_search_p = "2", fro_m_p = "null", t_o_p = "null";
	custom_post_list adapter_p;
	private SQLiteDatabase database_p;
	TextView tv_internet_status_p;

	// ConnectionDetector cd_p;
	private SwipeRefreshLayout swipeRefreshLayout;
	boolean visit_again_p = false, scrollDone_p = false;

	int page_p = 1;// for url
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// if (cd_p.isConnectingToInternet()) {
//		url_p = new url().search_list + "id=0&city=" + s_city_p + "&fro_m=" + fro_m_p + "&t_o=" + t_o_p + "&category="
//				+ s_cat_p + "&search_for=" + s_search_p + "&corporate=null&company_id=null";
		// Log.i("log", "OnStart Provider Url: "+url_p);
		// if (ad_id_p == 0) {
		// // accessWebService();
		// }
		// tv_internet_status_p.setVisibility(View.GONE);
		// } else {
		// tv_internet_status_p.setVisibility(View.VISIBLE);
		// }
		url_p = new url().search_list +"&city_id=" + s_city_p+"&type_id="+s_search_p + "&page="+page_p;
		//Log.i("log", "OnStart_p url: "+url_p);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.search_result, container, false);
		// cd_p = new ConnectionDetector(getActivity());
		tv_internet_status_p = (TextView) rootView.findViewById(R.id.tv_internet_status);
		p_bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		tv_internet_status_p.setVisibility(View.GONE);

		//SharedPreferences sharedpreferences_p = getActivity().getPreferences(0);
		Bundle b_p = getActivity().getIntent().getExtras();
		if (b_p!=null)
		s_city_p = b_p.getString("city");
		s_city_p = new database_method().get_data(getActivity(),
				"select id from city where city_name='" + s_city_p + "'");
		s_cat_p = new database_method().get_data(getActivity(),
				"select cat_id from category where cat_name='" + s_cat_p + "'");
		// s_search_p = new database_method().get_data(getActivity(),
		// "select type_id from type where type_name='" + s_search_p + "'");
//		url_p = new url().search_list + "id=0&city=" + s_city_p + "&fro_m=" + fro_m_p + "&t_o=" + t_o_p + "&category="
//				+ s_cat_p + "&search_for=" + s_search_p + "&corporate=null&company_id=null";
		url_p = new url().search_list +"&city_id=" + s_city_p + "&type_id="+s_search_p + "&page="+page_p;
		//Log.i("log", "OncreateView Provider Url: " + url_p);
		
		checkInternet_p();
		ex_list_p = (ExpandableListView) rootView.findViewById(R.id.ex_list_cities);
		ex_list_p.setDivider(null);
//		header_p = getActivity().getLayoutInflater().inflate(R.layout.sample_activity_list_header, ex_list_p, false);
		ExpAdapter_p = new ExpandListSearchAdapter(getActivity(), list_header_p);
//		wrapperAdapter_p = new WrapperExpandableListAdapter(ExpAdapter_p);
		ex_list_p.setAdapter(ExpAdapter_p);

		// swipe layout to refresh
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		ex_list_p.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition,
					long id) {
				// TODO Auto-generated method stub

				final ad_array_list child_p = (ad_array_list) getChild_p(groupPosition, childPosition, list_header_p);
				Intent i = new Intent(getActivity(), post_details_main.class);
				i.putExtra("category", child_p.getType());
				i.putExtra("user_email", child_p.getEmail());
				i.putExtra("post_id", String.valueOf(child_p.getId()));
				startActivity(i);
				return true;
			}
		});

		ex_list_p.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int threshold = 1;
				int count = ex_list_p.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((ex_list_p.getLastVisiblePosition() >= count - threshold) && loadingMore_p == false) {
//						url_p = new url().search_list + "id=" + ad_id_p + "&city=" + s_city_p + "&fro_m=" + fro_m_p
//								+ "&t_o=" + t_o_p + "&category=" + s_cat_p + "&search_for=" + s_search_p
//								+ "&corporate=null&company_id=null";
						scrollDone_p = true;
						page_p+=1;
						int i_p = url_p.lastIndexOf("&");
						if (i_p != -1)  
					    {
					        url_p = url_p.substring(0, i_p); // not forgot to put check if(endIndex != -1)
					    }
						url_p+= "&page="+page_p;
						//Log.i("log", "On scroll Url"+url_p);
						//Log.i("log", "Page: "+page_p);
						loadingMore_p = true;
						checkInternet_p();
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom item that is visible
				
			}
		});

		return rootView;
	}

	public Object getChild_p(int groupPosition, int childPosition, ArrayList<Header> groups) {
		ArrayList<ad_array_list> chList = groups.get(groupPosition).getItems();
		return chList.get(childPosition);
	}

	private class SoapAccessTask_p extends AsyncTask<String, Void, String> {

		
		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url_p1 = url_p.replace(" ", "%20");
			//Log.d("url: ", "> " + url_p1);
			String jsonStr = sh.makeServiceCall(url_p1, ServiceHandler.GET);
			//Log.d("Response: ", "> " + jsonStr);
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
							if (t_date_p == null && i == 0) {
								child_p = null;
								child_p = new ArrayList<ad_array_list>();
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_p = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
							} else if (list_header_p.size() != 0 && t_date_p != null && i == 0) {
								list_header_p.remove(list_header_p.size() - 1);
							}
							String[] dates = (obj_ad.getString("date")).split("-");
							String date_local = new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1]) - 1] + " "
									+ dates[0];
							if (t_date_p.equals(date_local)) {
								child_p.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
								//Log.i("log", "username> " + obj_user.getString("name") + child_p.get(child_p.size() - 1).getUser_name());
								if (i == (taskarray.length() - 1)) {
									list_header_p.add(new Header(t_date_p, child_p));
								}
							} else {
								list_header_p.add(new Header(t_date_p, child_p));
								child_p = null;
								child_p = new ArrayList<ad_array_list>();
	
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_p = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
	
								child_p.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
							}
	
							ad_id_p = obj_ad.getInt("id");
	
							webResponse = "false";
						}
					}

				} catch (JSONException e) {
					//e.printStackTrace();
				}
			} else {
				//Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			return webResponse;
		}

		protected void onPostExecute(String result) {
			if (result.equals("false")) {
				loadingMore_p = false;
//				ex_list_p.addHeaderView(header_p);
			}
//			tv_p = new TextView(getActivity());
//			tv_p.setText("No data found.\nPlease refine your search.");
//			tv_p.setTextSize(25);
//			tv_p.setGravity(Gravity.CENTER);
//			if (result.equals("true") && list_header_p.size() == 0) {
//				if (ex_list_p.removeFooterView(tv_p) != true) {
//					ex_list_p.addFooterView(tv_p);
////					ex_list_p.removeHeaderView(header_p);
//				} else {
//					ex_list_p.removeFooterView(tv_p);
//					tv_p.setVisibility(View.GONE);
//					ex_list_p.refreshDrawableState();
//					// ex_list.setAdapter(ExpAdapter);
//				}
//			}

			ExpAdapter_p.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);

		}

	}

	public void accessWebService_p() {
		SoapAccessTask_p task_p = new SoapAccessTask_p();
		loadingMore_p = true;
		// passes values for the urls strin`g array
		task_p.execute(new String[] { "USD", "LKR" });
	}

	public void onBackPressed() {
		SoapAccessTask_p lmib_p = new SoapAccessTask_p();
		CheckInternet_p check_p = new CheckInternet_p();
		if (lmib_p.getStatus() == AsyncTask.Status.RUNNING || check_p.getStatus()==AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib_p.cancel(true);
			check_p.cancel(true);
			p_bar.setVisibility(View.GONE);
		} else if (lmib_p.getStatus() == AsyncTask.Status.FINISHED || check_p.getStatus()==AsyncTask.Status.FINISHED) {
			lmib_p.cancel(true);
			check_p.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			page_p=1;
			getActivity().finish();
		}
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
					list_header_p.clear();
					page_p=1;
					initializeStart_p();
//					ex_list_p.removeFooterView(tv_p);
					tv_internet_status_p.setVisibility(View.GONE);
				} else {
					list_header_p.clear();
					ExpAdapter_p.notifyDataSetChanged();
					tv_internet_status_p.setVisibility(View.VISIBLE);
				}
			}
		}, 5000);
	}

	protected void initializeStart_p() {
//		String s_city_p = "", s_cat_p = "null", s_search_p = "2", fro_m_p = "null", t_o_p = "null";
//		Bundle b_p = getActivity().getIntent().getExtras();
//		if (b_p!=null)
//		s_city_p = b_p.getString("city");
//		s_city_p = new database_method().get_data(getActivity(),
//				"select id from city where city_name='" + s_city_p + "'");
		url_p = new url().search_list +"&city_id=" + s_city_p + "&type_id="+s_search_p ;
		page_p=1;
		url_p+= "&page="+page_p;
		//Log.i("log", "OncreateView Provider Url: " + url_p);
		
		//Log.i("log", "InitializeStart Provider Url: " + url_p);
		t_date_p = null;
		ad_id_p = 0;
		checkInternet_p();
		// ExpAdapter_p.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		list_header_p.clear();
		ExpAdapter_p.notifyDataSetChanged();
		// list_header_p=null;
		visit_again_p = true;
		page_p=1;
		//Log.i("log", "onDestroyView");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Log.i("log", "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// list_header_p.clear();
		// ExpAdapter_p.notifyDataSetChanged();
		// start=false;
		// visit_again=false;
		//Log.i("log", "onPause");
	}

	public void checkInternet_p() {
		CheckInternet_p check = new CheckInternet_p();
		check.execute();
	}

	class CheckInternet_p extends AsyncTask<String, Void, String> {

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
			if (result.contentEquals("true")) {
				// list_header_p.clear();
				if (scrollDone_p) {
					accessWebService_p();
					scrollDone_p = false;
				} else if (visit_again_p) {
					// list_header_p = new ArrayList<Header>();
					// list_header_p.clear();
					// accessWebService();
					ad_id_p = 0;
					list_header_p.clear();
					initializeStart_p();
					visit_again_p=false;
					//Log.i("log", "onCreateView inside");
				} else {
					list_header_p.clear();
					accessWebService_p();
					//Log.i("log", "onCreateView outside");
				}
				tv_internet_status_p.setVisibility(View.GONE);
			} else {
				tv_internet_status_p.setVisibility(View.VISIBLE);
				p_bar.setVisibility(View.GONE);
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}