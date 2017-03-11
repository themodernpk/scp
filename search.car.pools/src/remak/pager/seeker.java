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
import remak.pager.provider.CheckInternet_p;

public class seeker extends Fragment implements OnRefreshListener {

	// ExpandableListView
	ExpandableListView ex_list_seeker;
//	WrapperExpandableListAdapter wrapperAdapter_seeker;
//	TextView tv_seeker;
//	View header_seeker;
	ArrayList<ad_array_list> child_seeker;
	private ExpandListSearchAdapter ExpAdapter_seeker;
	ArrayList<Header> list_header_seeker = new ArrayList<Header>();
	String t_date_seeker = null;

	int ad_id_seeker = 0;

	String url_seeker;
	boolean loadingMore_seeker = false;
	ProgressBar p_bar;
	String s_city_seeker = "", s_cat_seeker = "null", s_search_seeker = "1", fro_m_seeker = "null", t_o_seeker = "null";
	custom_post_list adapter_seeker;
	private SQLiteDatabase database_seeker;
	TextView tv_internet_status_seeker;
	// ConnectionDetector cd_seeker;
	private SwipeRefreshLayout swipeRefreshLayout_seeker;
	boolean visit_again_seeker = false, scrollDone_seeker = false;
	
	int page_seeker=1; // for url
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// if (cd_seeker.isConnectingToInternet()) {
		url_seeker  = new url().search_list +"&city_id=" + s_city_seeker+"&type_id=" + s_search_seeker + "&page="+page_seeker;
		//Log.i("log", "OnStart_seeker url: "+url_seeker);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.search_result, container, false);
		// cd_seeker = new ConnectionDetector(getActivity());
		tv_internet_status_seeker = (TextView) rootView.findViewById(R.id.tv_internet_status);
		p_bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		tv_internet_status_seeker.setVisibility(View.GONE);
		//SharedPreferences sharedpreferences = getActivity().getPreferences(0);
		Bundle b_seeker = getActivity().getIntent().getExtras();
		if (b_seeker!=null)
		s_city_seeker = b_seeker.getString("city");
		s_city_seeker = new database_method().get_data(getActivity(),
				"select id from city where city_name='" + s_city_seeker + "'");
//		s_cat_seeker = new database_method().get_data(getActivity(),
//				"select cat_id from category where cat_name='" + s_cat_seeker + "'");
		// s_search = new database_method().get_data(getActivity(),
		// "select type_id from type where type_name='" + s_search + "'");
//		url_seeker = new url().search_list + "id=0&city=" + s_city_seeker + "&fro_m=" + fro_m_seeker + "&t_o="
//				+ t_o_seeker + "&category=" + s_cat_seeker + "&search_for=" + s_search_seeker
//				+ "&corporate=null&company_id=null";

		url_seeker  = new url().search_list +"&city_id=" + s_city_seeker+"&type_id=" + s_search_seeker + "&page="+page_seeker;
		//Log.i("log", "OnCreateView_seeker url: "+url_seeker);
		
		// swipe layout to refresh
		swipeRefreshLayout_seeker = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout_seeker.setOnRefreshListener(this);
		swipeRefreshLayout_seeker.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		checkInternet_seeker();
		ex_list_seeker = (ExpandableListView) rootView.findViewById(R.id.ex_list_cities);
		ex_list_seeker.setDivider(null);
//		header_seeker = getActivity().getLayoutInflater().inflate(R.layout.sample_activity_list_header, ex_list_seeker, false);
		ExpAdapter_seeker = new ExpandListSearchAdapter(getActivity(), list_header_seeker);
//		wrapperAdapter_seeker = new WrapperExpandableListAdapter(ExpAdapter_seeker);
		ex_list_seeker.setAdapter(ExpAdapter_seeker);
		
		ex_list_seeker.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition,
					long id) {
				// TODO Auto-generated method stub

				final ad_array_list child_seeker = (ad_array_list) getChild_seeker(groupPosition, childPosition,
						list_header_seeker);
				Intent i_seeker = new Intent(getActivity(), post_details_main.class);
				i_seeker.putExtra("category", child_seeker.getType());
				i_seeker.putExtra("user_email", child_seeker.getEmail());
				i_seeker.putExtra("post_id", String.valueOf(child_seeker.getId()));
				startActivity(i_seeker);
				return true;
			}
		});

		ex_list_seeker.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int threshold = 1;
				int count = ex_list_seeker.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((ex_list_seeker.getLastVisiblePosition() >= count - threshold) && loadingMore_seeker == false) {
//						url_seeker = new url().search_list + "id=" + ad_id_seeker + "&city=" + s_city_seeker + "&fro_m="
//								+ fro_m_seeker + "&t_o=" + t_o_seeker + "&category=" + s_cat_seeker + "&search_for="
//								+ s_search_seeker + "&corporate=null&company_id=null";
						scrollDone_seeker = true;
						page_seeker+=1;
						int i_seeker = url_seeker.lastIndexOf("&");
						if (i_seeker != -1)  
					    {
					        url_seeker = url_seeker.substring(0, i_seeker); // not forgot to put check if(endIndex != -1)
					    }
						url_seeker+= "&page="+page_seeker;
						//Log.i("log", "On scroll Url"+url_seeker);
						//Log.i("log", "Page: "+page_seeker);
						loadingMore_seeker = true;
						checkInternet_seeker();
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

				// if ((lastInScreen == totalItemCount) && loadingMore_seeker ==
				// false) {
				// if (list_header_seeker.size() == 0) {
				// ad_id_seeker = 0;
				// }
				// url_seeker = new url().search_list + "id=" + ad_id_seeker +
				// "&city=" + s_city_seeker + "&fro_m=" + fro_m_seeker + "&t_o="
				// + t_o_seeker
				// + "&category=" + s_cat_seeker + "&search_for=" +
				// s_search_seeker + "&corporate=null&company_id=null";
				//
				//// if (cd_seeker.isConnectingToInternet()) {
				//// accessWebService_seeker();
				//// tv_internet_status_seeker.setVisibility(View.GONE);
				//// } else {
				//// tv_internet_status_seeker.setVisibility(View.VISIBLE);
				//// }
				// checkInternet_seeker();
				// }
			}
		});

		return rootView;
	}

	public Object getChild_seeker(int groupPosition, int childPosition, ArrayList<Header> groups) {
		ArrayList<ad_array_list> chList_seeker = groups.get(groupPosition).getItems();
		return chList_seeker.get(childPosition);
	}

	private class SoapAccessTask_seeker extends AsyncTask<String, Void, String> {
		
		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh_seeker = new ServiceHandler();
			String url1_seeker = url_seeker.replace(" ", "%20");
			//Log.d("url: ", "> " + url1_seeker);
			String jsonStr_seeker = sh_seeker.makeServiceCall(url1_seeker, ServiceHandler.GET);
			//Log.d("Response: ", "> " + jsonStr_seeker);
			if (jsonStr_seeker != null) {
				try {

					JSONObject jsonObj = new JSONObject(jsonStr_seeker);
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
							if (t_date_seeker == null && i == 0) {
								child_seeker = null;
								child_seeker = new ArrayList<ad_array_list>();
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_seeker = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
							} else if (list_header_seeker.size() != 0 && t_date_seeker != null && i == 0) {
								list_header_seeker.remove(list_header_seeker.size() - 1);
							}
							String[] dates = (obj_ad.getString("date")).split("-");
							String date_local = new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1]) - 1] + " "
									+ dates[0];
							if (t_date_seeker.equals(date_local)) {
								child_seeker.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
								//Log.i("log", "username> " + obj_user.getString("name") + child_seeker.get(child_seeker.size() - 1).getUser_name());
								if (i == (taskarray.length() - 1)) {
									list_header_seeker.add(new Header(t_date_seeker, child_seeker));
								}
							} else {
								list_header_seeker.add(new Header(t_date_seeker, child_seeker));
								child_seeker = null;
								child_seeker = new ArrayList<ad_array_list>();
	
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_seeker = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
	
								child_seeker.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
							}
	
							ad_id_seeker = obj_ad.getInt("id");
	
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
				loadingMore_seeker = false;
//				ex_list_seeker.addHeaderView(header_seeker);
			}
//			tv_seeker = new TextView(getActivity());
//			tv_seeker.setText("No data found.\nPlease refine your search.");
//			tv_seeker.setTextSize(25);
//			tv_seeker.setGravity(Gravity.CENTER);
//			if (result.equals("true") && list_header_seeker.size() == 0) {
//				if (ex_list_seeker.removeFooterView(tv_seeker) != true) {
//					ex_list_seeker.addFooterView(tv_seeker);
////					ex_list_seeker.removeHeaderView(header_seeker);
//				} else {
//					ex_list_seeker.removeFooterView(tv_seeker);
//					tv_seeker.setVisibility(View.GONE);
//					ex_list_seeker.refreshDrawableState();
//					// ex_list.setAdapter(ExpAdapter);
//				}
//			}
			ExpAdapter_seeker.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);
		}

	}

	public void accessWebService_seeker() {
		SoapAccessTask_seeker task_seeker = new SoapAccessTask_seeker();
		loadingMore_seeker = true;
		// passes values for the urls strin`g array
		task_seeker.execute(new String[] { "USD", "LKR" });
	}

	public void onBackPressed() {
		SoapAccessTask_seeker lmib_seeker = new SoapAccessTask_seeker();
		CheckInternet_seeker check_seeker = new CheckInternet_seeker();
		if (lmib_seeker.getStatus() == AsyncTask.Status.RUNNING || check_seeker.getStatus()==AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib_seeker.cancel(true);
			check_seeker.cancel(true);
			p_bar.setVisibility(View.GONE);

		} else if (lmib_seeker.getStatus() == AsyncTask.Status.FINISHED || check_seeker.getStatus()==AsyncTask.Status.RUNNING) {
			lmib_seeker.cancel(true);
			check_seeker.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			page_seeker=1;
			getActivity().finish();
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout_seeker.setRefreshing(false);
				ConnectionDetector cd = new ConnectionDetector(getActivity());
				if (cd.isNetworkAvailable(getActivity())) {
					list_header_seeker.clear();
					page_seeker=1;
					initializeStart();
//					ex_list_seeker.removeFooterView(tv_seeker);
					tv_internet_status_seeker.setVisibility(View.GONE);
				} else {
					list_header_seeker.clear();
					ExpAdapter_seeker.notifyDataSetChanged();
					tv_internet_status_seeker.setVisibility(View.VISIBLE);
				}
			}
		}, 5000);
	}

	protected void initializeStart() {
//		String s_city_seeker = "", s_cat_seeker = "null", s_search_seeker = "1", fro_m_seeker = "null",
//				t_o_seeker = "null";
//		Bundle b_seeker = getActivity().getIntent().getExtras();
//		if (b_seeker!=null)
//		s_city_seeker = b_seeker.getString("city");
//		s_city_seeker = new database_method().get_data(getActivity(),
//				"select id from city where city_name='" + s_city_seeker + "'");
		page_seeker=1;
		url_seeker  = new url().search_list +"&city_id=" + s_city_seeker+"&type_id=" + s_search_seeker + "&page="+page_seeker;
		//Log.i("log", "OnCreateView_seeker url: "+url_seeker);
		t_date_seeker = null;
		ad_id_seeker = 0;
		checkInternet_seeker();
		ExpAdapter_seeker.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		list_header_seeker.clear();
		ExpAdapter_seeker.notifyDataSetChanged();
		// list_header=null;
		visit_again_seeker = true;
		page_seeker=1;
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

	public void checkInternet_seeker() {
		CheckInternet_seeker check = new CheckInternet_seeker();
		check.execute();
	}

	class CheckInternet_seeker extends AsyncTask<String, Void, String> {

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
				// list_header.clear();
				// list_header.clear();
				if (scrollDone_seeker) {
					accessWebService_seeker();
					scrollDone_seeker = false;
				} else if (visit_again_seeker) {
					ad_id_seeker = 0;
					list_header_seeker.clear();
					initializeStart();
					visit_again_seeker=false;
					// Log.i("log", "onCreateView inside");
				} else {
					list_header_seeker.clear();
					accessWebService_seeker();
					// Log.i("log", "onCreateView outside");
				}
				tv_internet_status_seeker.setVisibility(View.GONE);
			} else {
				tv_internet_status_seeker.setVisibility(View.VISIBLE);
				p_bar.setVisibility(View.GONE);
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}