package remak.pager;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.search.car.pools.ConnectionDetector;
import com.example.search.car.pools.R;
import com.example.search.car.pools.search_result;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import array.list.comp_array_list;
import custom.list.database_method;
import data.service.ServiceHandler;
import data.service.url;
import remak.pager.corporate.CheckInternet_corporate;

public class companies extends Fragment implements OnRefreshListener {
	ListView list_comp;
	int ad_id_comp = 0;
	ArrayList<comp_array_list> company_item;

	String url_comp;
	boolean loadingMore_comp = false;

	String s_city_comp = "";
	custom_company_list adapter_comp;
	private SQLiteDatabase database_comp;
	ProgressBar p_bar;
	TextView tv_internet_status;

	// ConnectionDetector cd;
	private SwipeRefreshLayout swipeRefreshLayout;
	boolean visit_again_comp = false, addCompanyVisited = false, scrollDone_company = false;

	int page_comp = 1;

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		url_comp = new url().search_company_list + "&city_id=" + s_city_comp + "&page=" + page_comp;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.cities, container, false);
		// cd = new ConnectionDetector(getActivity());
		tv_internet_status = (TextView) rootView.findViewById(R.id.tv_internet_status);
		tv_internet_status.setVisibility(View.GONE);
		list_comp = (ListView) rootView.findViewById(R.id.list_cities);
		p_bar = (ProgressBar) rootView.findViewById(R.id.progressBar1);
		company_item = new ArrayList<comp_array_list>();
		//SharedPreferences sharedpreferences = getActivity().getPreferences(0);
		Bundle b_comp = getActivity().getIntent().getExtras();
		if (b_comp!=null)
		s_city_comp = b_comp.getString("city");
		s_city_comp = new database_method().get_data(getActivity(),
				"select id from city where city_name='" + s_city_comp + "'");

		url_comp = new url().search_company_list + "&city_id=" + s_city_comp + "&page=" + page_comp;

		//Log.i("log", "onCreateViewCompanies: " + url_comp);

		list_comp.setDivider(null);

		checkInternet_comp();
		adapter_comp = new custom_company_list(getActivity(), company_item);
		list_comp.setAdapter(adapter_comp);
		// swipe layout to refresh
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);

		list_comp.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int company_id = company_item.get(position).getCompany_id();
				String city="";
				Bundle b_comp = getActivity().getIntent().getExtras();
				if (b_comp!=null)
					city = b_comp.getString("city");
				Intent i = new Intent(getActivity(), search_result.class);
				i.putExtra("city", city);
				i.putExtra("category", "");
				i.putExtra("search_for", "");
				i.putExtra("from", "");
				i.putExtra("to", "");
				i.putExtra("frag_id", "");
				i.putExtra("company_id", company_id);
				startActivity(i);
			}
		});
		list_comp.setOnScrollListener(new OnScrollListener() {
			// useless here, skip!
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				int threshold = 1;
				int count = list_comp.getCount();

				if (scrollState == SCROLL_STATE_IDLE) {
					if ((list_comp.getLastVisiblePosition() >= count - threshold) && loadingMore_comp == false) {
						// url_comp = new url().search_company_list + "id=" +
						// ad_id_comp + "&city=" + s_city_comp
						// + "&keyword=null";
						scrollDone_company = true;
						page_comp += 1;
						int i_comp = url_comp.lastIndexOf("&");
						if (i_comp != -1) {
							url_comp = url_comp.substring(0, i_comp); 
						}
						url_comp += "&page=" + page_comp;
						//Log.i("log", "On scroll Url" + url_comp);
						//Log.i("log", "Page: " + page_comp);
						loadingMore_comp = true;
						checkInternet_comp();
					}
				}
			}

			// dumdumdum
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// what is the bottom iten that is visible
				// int lastInScreen = firstVisibleItem + visibleItemCount;
				// // is the bottom item visible & not loading more already ?
				// Load
				// // more !
				//
				// if ((lastInScreen == totalItemCount) && loadingMore_comp ==
				// false) {
				// if (company_item.size() == 0) {
				// ad_id_comp = 0;
				// }
				// url_comp = new url().search_company_list + "id=" + ad_id_comp
				// + "&city=" + s_city_comp
				// + "&keyword=null";
				//
				//// if (cd.isConnectingToInternet()) {
				//// accessWebService_comp();
				//// tv_internet_status.setVisibility(View.GONE);
				//// } else {
				//// tv_internet_status.setVisibility(View.VISIBLE);
				//// }
				// checkInternet_comp();
				// }
			}
		});
		return rootView;
	}

	private class SoapAccessTask_comp extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url_comp.replace(" ", "%20");
			//Log.d("url: ", "> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("Response: ", "> " + jsonStr);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					String status = jsonObj.getString("status");
					//Log.i("log", "status Companies: " + status);

					JSONArray taskarray = jsonObj.getJSONArray("data");
					for (int i = 0; i < taskarray.length(); i++) {

						final JSONObject jsonObject = taskarray.getJSONObject(i);
						company_item.add(new comp_array_list(jsonObject.getInt("company_id"),
								jsonObject.getString("company_name"), jsonObject.getString("company_add")));
						ad_id_comp = jsonObject.getInt("company_id");
						webResponse = "false";
						//Log.i("log", "json data Companies: " + status);
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
				loadingMore_comp = false;
			}
			// if (result.equals("true") && company_item.size() == 0) {
			//// accessWebService_comp();
			// tv_internet_status.setVisibility(View.GONE);
			// } else {
			// tv_internet_status.setVisibility(View.VISIBLE);
			// }

			adapter_comp.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);

		}

	}

	public void accessWebService_comp() {
		SoapAccessTask_comp task_comp = new SoapAccessTask_comp();
		loadingMore_comp = true;
		// passes values for the urls strin`g array
		task_comp.execute(new String[] { "USD", "LKR" });
	}

	public void onBackPressed() {
		SoapAccessTask_comp lmib_comp = new SoapAccessTask_comp();
		CheckInternet_comp check_comp = new CheckInternet_comp();
		if (lmib_comp.getStatus() == AsyncTask.Status.RUNNING || check_comp.getStatus()==AsyncTask.Status.RUNNING) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib_comp.cancel(true);
			check_comp.cancel(true);
			p_bar.setVisibility(View.GONE);

		} else if (lmib_comp.getStatus() == AsyncTask.Status.FINISHED || check_comp.getStatus()==AsyncTask.Status.RUNNING) {
			lmib_comp.cancel(true);
			check_comp.cancel(true);
			p_bar.setVisibility(View.GONE);
			// My AsyncTask is done and onPostExecute was called
		} else {
			page_comp=1;
			getActivity().finish();
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
			if (position == 0) {
				l.setVisibility(View.VISIBLE);
			} else {
				l.setVisibility(View.GONE);
			}
			l.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(getActivity(), AddCompany.class);
					i.putExtra("company_name", "");
					startActivity(i);
					addCompanyVisited = true;
				}
			});

			ImageView iv = (ImageView) rowView.findViewById(R.id.img_company_list_svg);
			// Bitmap bmp = BitmapFactory.decodeResource(getResources(),
			// R.drawable.corporate_1);
			// RoundedBitmapDrawable dr =
			// RoundedBitmapDrawableFactory.create(context.getResources(), bmp);
			// dr.setCornerRadius(Math.max(bmp.getWidth(), bmp.getHeight()) /
			// 2.0f);
			// iv.setImageDrawable(dr);

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

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				ConnectionDetector cd = new ConnectionDetector(getActivity());
				if (cd.isNetworkAvailable(getActivity())) {
					ad_id_comp = 0;
					company_item.clear();
					page_comp=1;
					initializeStart();
					tv_internet_status.setVisibility(View.GONE);
				} else {
					company_item.clear();
					adapter_comp.notifyDataSetChanged();
					tv_internet_status.setVisibility(View.VISIBLE);
				}
			}
		}, 5000);
	}

	public void initializeStart() {
//		Bundle b_comp = getActivity().getIntent().getExtras();
//		if (b_comp!=null)
//		s_city_comp = b_comp.getString("city");
//		s_city_comp = new database_method().get_data(getActivity(),
//				"select id from city where city_name='" + s_city_comp + "'");
		page_comp=1;
		url_comp = new url().search_company_list + "&city_id=" + s_city_comp + "&page=" + page_comp;
		//Log.i("log", "onCreateViewCompanies: " + url_comp);
		ad_id_comp = 0;
		checkInternet_comp();
		adapter_comp.notifyDataSetChanged();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		company_item.clear();
		adapter_comp.notifyDataSetChanged();
		// list_header=null;
		visit_again_comp = true;
		page_comp=1;
		//Log.i("log", "onDestroyViewCompanies");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (addCompanyVisited) {
			company_item.clear();
			accessWebService_comp();
			adapter_comp.notifyDataSetChanged();
			addCompanyVisited = false;
		}
		//Log.i("log", "onResumeCompanies");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// list_header.clear();
		// ExpAdapter.notifyDataSetChanged();
		// start=false;
		// visit_again=false;
		//Log.i("log", "onPauseCompanies");
	}

	public void checkInternet_comp() {
		CheckInternet_comp check = new CheckInternet_comp();
		check.execute();
	}

	class CheckInternet_comp extends AsyncTask<String, Void, String> {

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
				if (scrollDone_company) {
					accessWebService_comp();
					scrollDone_company = false;
				} else if (visit_again_comp) {
					ad_id_comp = 0;
					company_item.clear();
					initializeStart();
					visit_again_comp = false;
					// Log.i("log", "onCreateView inside");
				} else {
					company_item.clear();
					accessWebService_comp();
					// Log.i("log", "onCreateView outside");
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