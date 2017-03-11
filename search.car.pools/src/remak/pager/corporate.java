package remak.pager;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import post.details.post_details_main;
import remak.pager.seeker.CheckInternet_seeker;
import android.app.ProgressDialog;
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
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Toast;
import array.list.Header;
import array.list.ad_array_list;

import com.diegocarloslima.fgelv.lib.FloatingGroupExpandableListView;
import com.diegocarloslima.fgelv.lib.WrapperExpandableListAdapter;
import com.example.search.car.pools.ConnectionDetector;
import com.example.search.car.pools.R;
import com.example.search.car.pools.search_result;
import com.nirhart.parallaxscroll.views.ParallaxExpandableListView;

import custom.list.ExpandListSearchAdapter;
import custom.list.custom_post_list;
import custom.list.database_method;
import data.service.*;

public class corporate extends Fragment implements OnRefreshListener {
	// ExpandableListView
	ExpandableListView ex_list_corp;
//	WrapperExpandableListAdapter wrapperAdapter_corp;
//	TextView tv_corp;
//	View header_corp;
	
	ArrayList<ad_array_list> child_corp;
	private ExpandListSearchAdapter ExpAdapter_corp;
	ArrayList<Header> list_header_corp = new ArrayList<Header>();
	String t_date_corp = null;

	int ad_id_corp = 0;

	String url_corp;
	boolean loadingMore_corp = false;
	ProgressBar p_bar;
	String s_city_corp = "", s_cat_corp = "null", s_search_corp = "null", fro_m_corp = "null", t_o_corp = "null";
	custom_post_list adapter_corp;
	private SQLiteDatabase database_corp;
	TextView tv_internet_status;

//	ConnectionDetector cd;
	private SwipeRefreshLayout swipeRefreshLayout;
	boolean visit_again_corp=false, scrollDone_corp=false;
	
	int page_corp=1;
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
//		if (cd.isConnectingToInternet()) {
//			url_corp = new url().search_list + "id=0&city=" + s_city_corp + "&fro_m=" + fro_m_corp + "&t_o=" + t_o_corp + "&category="
//					+ s_cat_corp + "&search_for=" + s_search_corp + "&corporate=1&company_id=null";
//			if (ad_id_corp == 0) {
////				accessWebService();
//			}
//			tv_internet_status.setVisibility(View.GONE);
//		} else {
//			tv_internet_status.setVisibility(View.VISIBLE);
//		}

		url_corp = new url().search_list +"&city_id=" + s_city_corp+"&corporate=0" + "&page="+page_corp;
		//Log.i("log", "OnStart_p url: "+url_corp);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.search_result, container, false);
//		cd = new ConnectionDetector(getActivity());
		tv_internet_status = (TextView) rootView.findViewById(R.id.tv_internet_status);
		ex_list_corp = (ExpandableListView) rootView.findViewById(R.id.ex_list_cities);
		p_bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		tv_internet_status.setVisibility(View.GONE);

		// swipe layout to refresh
				swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
				swipeRefreshLayout.setOnRefreshListener(this);
				swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, 
			            android.R.color.holo_green_light, 
			            android.R.color.holo_orange_light, 
			            android.R.color.holo_red_light);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		
		//SharedPreferences sharedpreferences_corp = getActivity().getPreferences(0);
		Bundle b_corp = getActivity().getIntent().getExtras();
		if (b_corp!=null)
		s_city_corp = b_corp.getString("city");
		s_city_corp = new database_method().get_data(getActivity(), "select id from city where city_name='" + s_city_corp + "'");
//		s_cat_corp = new database_method().get_data(getActivity(),
//				"select cat_id from category where cat_name='" + s_cat_corp + "'");
//		s_search_corp = new database_method().get_data(getActivity(),
//				"select type_id from type where type_name='" + s_search_corp + "'");
//		url_corp = new url().search_list + "id=0&city=" + s_city_corp + "&fro_m=" + fro_m_corp + "&t_o=" + t_o_corp + "&category=" + s_cat_corp
//				+ "&search_for=" + s_search_corp + "&corporate=1&company_id=null";
		url_corp = new url().search_list +"&city_id=" + s_city_corp+"&corporate=0" + "&page="+page_corp;
		//Log.i("log", "OnStart_p url: "+url_corp);
		
		checkInternet_corporate();
		ex_list_corp.setDivider(null);
//		header_corp = getActivity().getLayoutInflater().inflate(R.layout.sample_activity_list_header, ex_list_corp, false);
		ExpAdapter_corp = new ExpandListSearchAdapter(getActivity(), list_header_corp);
//		wrapperAdapter_corp = new WrapperExpandableListAdapter(ExpAdapter_corp);
		ex_list_corp.setAdapter(ExpAdapter_corp);
//		int count = ExpAdapter.getGroupCount();
//		for (int position = 1; position <= count; position++)
//		    ex_list.expandGroup(position - 1);
		
		ex_list_corp.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, final int childPosition, long id) {
				// TODO Auto-generated method stub
				
				final ad_array_list child_corp = (ad_array_list)getChild_corp(groupPosition, childPosition, list_header_corp);
				Intent i_corp = new Intent(getActivity(), post_details_main.class);
				i_corp.putExtra("category", child_corp.getType());
				i_corp.putExtra("user_email", child_corp.getEmail());
				i_corp.putExtra("post_id", String.valueOf(child_corp.getId()));
				startActivity(i_corp);
				return true;
			}
		});
		
		ex_list_corp.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				int threshold = 1;
				int count = ex_list_corp.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((ex_list_corp.getLastVisiblePosition() >= count - threshold) && loadingMore_corp == false) {
//						url_corp = new url().search_list + "id=" + ad_id_corp + "&city=" + s_city_corp + "&fro_m=" + fro_m_corp + "&t_o=" + t_o_corp
//								+ "&category=" + s_cat_corp + "&search_for=" + s_search_corp + "&corporate=1&company_id=null";
						scrollDone_corp = true;
						page_corp+=1;
						int i_corp = url_corp.lastIndexOf("&");
						if (i_corp != -1)  
					    {
					        url_corp = url_corp.substring(0, i_corp); // not forgot to put check if(endIndex != -1)
					    }
						url_corp+= "&page="+page_corp;
						//Log.i("log", "On scroll Url"+url_corp);
						//Log.i("log", "Page: "+page_corp);
						loadingMore_corp = true;
						checkInternet_corporate();
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom item that is visible

			}
		});
		
	}

	public Object getChild_corp(int groupPosition, int childPosition, ArrayList<Header> groups) {
    	ArrayList<ad_array_list> chList = groups.get(groupPosition).getItems();
        return chList.get(childPosition);
    }
	
	private class SoapAccessTask_corp extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh_corp = new ServiceHandler();
			String url1 = url_corp.replace(" ", "%20");
			//Log.d("url: ", "> " + url1);
			String jsonStr = sh_corp.makeServiceCall(url1, ServiceHandler.GET);
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
							if (t_date_corp == null && i == 0) {
								child_corp = null;
								child_corp = new ArrayList<ad_array_list>();
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_corp = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
							} else if (list_header_corp.size() != 0 && t_date_corp != null && i == 0) {
								list_header_corp.remove(list_header_corp.size() - 1);
							}
							String[] dates = (obj_ad.getString("date")).split("-");
							String date_local = new DateFormatSymbols().getMonths()[Integer.parseInt(dates[1]) - 1] + " "
									+ dates[0];
							if (t_date_corp.equals(date_local)) {
								child_corp.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
								//Log.i("log", "username> " + obj_user.getString("name") + child_corp.get(child_corp.size() - 1).getUser_name());
								if (i == (taskarray.length() - 1)) {
									list_header_corp.add(new Header(t_date_corp, child_corp));
								}
							} else {
								list_header_corp.add(new Header(t_date_corp, child_corp));
								child_corp = null;
								child_corp = new ArrayList<ad_array_list>();
	
								String[] date = (obj_ad.getString("date")).split("-");
								t_date_corp = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1] + " " + date[0];
	
								child_corp.add(new ad_array_list(obj_ad.getInt("id"),
										obj_ad.getString("type"), obj_ad.getString("date"),
										obj_ad.getString("cat_id"), obj_ad.getString("from"),
										obj_ad.getString("to"), obj_ad.getString("route"),
										obj_user.getString("name"), obj_user.getString("profession"),
										obj_ad.getString("desc"), obj_user.getString("email"), obj_user.getString("gender")));
							}
	
							ad_id_corp = obj_ad.getInt("id");
	
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
				loadingMore_corp = false;
//				ex_list_corp.addHeaderView(header_corp);
			}
//			tv_corp = new TextView(getActivity());
//			tv_corp.setText("No data found.\nPlease refine your search.");
//			tv_corp.setTextSize(25);
//			tv_corp.setGravity(Gravity.CENTER);
//			if (result.equals("true") && list_header_corp.size() == 0) {
//				if (ex_list_corp.removeFooterView(tv_corp) != true) {
//					ex_list_corp.addFooterView(tv_corp);
////					ex_list_corp.removeHeaderView(header_corp);
//				} else {
//					ex_list_corp.removeFooterView(tv_corp);
//					tv_corp.setVisibility(View.GONE);
//					ex_list_corp.refreshDrawableState();
//					// ex_list.setAdapter(ExpAdapter);
//				}
//			}
			
			ExpAdapter_corp.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);

		}

	}

	public void accessWebService_corp() {
		SoapAccessTask_corp task_corp = new SoapAccessTask_corp();
		loadingMore_corp = true;
		// passes values for the urls strin`g array
		task_corp.execute(new String[] { "USD", "LKR" });
	}

	public void onBackPressed() {
		SoapAccessTask_corp lmib_corp = new SoapAccessTask_corp();
		CheckInternet_corporate check_corp = new CheckInternet_corporate();
		if (lmib_corp.getStatus() == AsyncTask.Status.RUNNING || check_corp.getStatus()==AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib_corp.cancel(true);
			check_corp.cancel(true);
			p_bar.setVisibility(View.GONE);

		} else if (lmib_corp.getStatus() == AsyncTask.Status.FINISHED || check_corp.getStatus()==AsyncTask.Status.RUNNING) {
			lmib_corp.cancel(true);
			check_corp.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			page_corp=1;
			getActivity().finish();
		}
	}
	
	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
	        @Override public void run() {
	        	swipeRefreshLayout.setRefreshing(false);
	        	ConnectionDetector cd = new ConnectionDetector(getActivity()); 
				if (cd.isNetworkAvailable(getActivity())) {
	        		list_header_corp.clear();
	        		page_corp=1;
	    			initializeStart_corp();
//	    			ex_list_corp.removeFooterView(tv_corp);
	    			tv_internet_status.setVisibility(View.GONE);
	    		} else {
	    			list_header_corp.clear();
	    			ExpAdapter_corp.notifyDataSetChanged();
	    			tv_internet_status.setVisibility(View.VISIBLE);
	    		}
	        }
	    }, 5000);
	}

	protected void initializeStart_corp() {
//		String s_city_corp = "", s_cat_corp = "null", s_search_corp = "null", fro_m_corp = "null", t_o_corp = "null";
//		Bundle b_corp = getActivity().getIntent().getExtras();
//		if (b_corp!=null)
//		s_city_corp = b_corp.getString("city");
//		s_city_corp = new database_method().get_data(getActivity(), "select id from city where city_name='" + s_city_corp + "'");
		page_corp=1;
		url_corp = new url().search_list +"&city_id=" + s_city_corp+"&corporate=0" + "&page="+page_corp;
		//Log.i("log", "OnStart_p url: "+url_corp);
		t_date_corp = null;
		ad_id_corp = 0;
		checkInternet_corporate();
		ExpAdapter_corp.notifyDataSetChanged();
	}
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		list_header_corp.clear();
		ExpAdapter_corp.notifyDataSetChanged();
//		list_header=null;
		visit_again_corp=true;
		page_corp=1;
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
//		list_header.clear();
//		ExpAdapter.notifyDataSetChanged();
//		start=false;
//		visit_again=false;
		//Log.i("log", "onPause");
	}
	
	public void checkInternet_corporate() {
		CheckInternet_corporate check = new CheckInternet_corporate();
		check.execute();
	}

	class CheckInternet_corporate extends AsyncTask<String, Void, String> {

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
//					list_header.clear();
//					list_header.clear();
				if (scrollDone_corp) {
					accessWebService_corp();
					scrollDone_corp = false;
				} else if (visit_again_corp){
						ad_id_corp = 0;
						list_header_corp.clear();
		    			initializeStart_corp();
		    			visit_again_corp=false;
		    			//Log.i("log", "onCreateView inside");
					} else {
						list_header_corp.clear();
						accessWebService_corp();
						//Log.i("log", "onCreateView outside");
					}
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