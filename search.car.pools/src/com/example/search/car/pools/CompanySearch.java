package com.example.search.car.pools;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.search.car.pools.company_list.custom_company_list;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import array.list.comp_array_list;
import data.service.ServiceHandler;
import data.service.url;

public class CompanySearch extends Activity {

	ListView list;
	int ad_id = 0;
	ArrayList<comp_array_list> company_item;

	String url;
	boolean loadingMore = false;

	String s_city = "";
	custom_company_list adapter;

	ProgressBar p_bar;
	private SwipeRefreshLayout swipeRefreshLayout;

	int page = 1;
	boolean scrollDone_company = false;

	TextView tv_internet_status;// t1;
	EditText et_comp_name;
	Button b_search;
	String url_search_company = "";
	String city_id = "";

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

		Drawable d = ContextCompat.getDrawable(CompanySearch.this, R.drawable.logooo);
		getActionBar().setIcon(d);
		getActionBar().setTitle("");
		Bundle b = getIntent().getExtras();
		if (b != null) {
			city_id = b.getString("city_id");
		}
		// swipe layout to refresh
		// swipeRefreshLayout = (SwipeRefreshLayout)
		// findViewById(R.id.swipe_refresh_layout);
		// swipeRefreshLayout.setOnRefreshListener(this);
		// swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color,
		// android.R.color.holo_green_light,
		// android.R.color.holo_orange_light, android.R.color.holo_red_light);

		list.setDividerHeight(2);
		// checkInternet();
		
		adapter = new custom_company_list(CompanySearch.this, company_item);
		list.setAdapter(adapter);
		
		tv_internet_status = (TextView) findViewById(R.id.tv_internet_status);
		tv_internet_status.setVisibility(View.GONE);

		b_search = (Button) findViewById(R.id.b_find_company);
		b_search.setVisibility(View.VISIBLE);
		url_search_company = new url().search_company;
		et_comp_name = (EditText) findViewById(R.id.et_company_name_search);
		b_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.equals(b_search)) {
					url_search_company += "&city_id=" + city_id + "&comp_name=" + et_comp_name.getText().toString()
							+ "&page=" + page;
					//Log.i("log", "url on click: " + url_search_company);
					// Log.d("log", "url on click: " + url_search_company);
					// Log.v("log", "url on click: " + url_search_company);
					// Log.e("log", "url on click: " + url_search_company);
					Toast.makeText(CompanySearch.this, et_comp_name.getText().toString(), Toast.LENGTH_SHORT).show();
					// checkInternet();
					accessWebService();
				}
			}
		});

	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();
		loadingMore = true;
		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			p_bar.setVisibility(View.VISIBLE);

		}

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
					for (int i = 0; i < taskarray.length(); i++) {

						final JSONObject jsonObject = taskarray.getJSONObject(i);
						company_item.add(new comp_array_list(1, jsonObject.getString("company_name"),
								jsonObject.getString("company_add")));
//						ad_id = jsonObject.getInt("company_id");
						webResponse = "false";
						
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
				loadingMore = false;
			}
			if (result.equals("true") && company_item.size() == 0) {
				// ConnectionDetector cd = new
				// ConnectionDetector(getApplicationContext());
				// if (cd.isConnectingToInternet()) {
				// accessWebService();
				// } else {
				// Toast.makeText(getApplication(), "No Internet Connection",
				// Toast.LENGTH_LONG).show();
				//
				// }

			}
			adapter.notifyDataSetChanged();
			p_bar.setVisibility(View.GONE);

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
}
