package remak.pager;

import java.net.URLEncoder;

import com.example.search.car.pools.ConnectionDetector;
import com.example.search.car.pools.EmailAndPhoneChecker;
import com.example.search.car.pools.R;
import com.example.search.car.pools.company_list;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.welcome;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import custom.list.DialogAdapter;
import custom.list.database_method;
import data.service.ServiceHandler;
import data.service.url;
import post.details.post_details_main;

public class AddCompany extends Activity implements OnClickListener {

	EditText et_c_name, et_c_address, et_name, et_official_email, et_contact_number, et_designation;
	TextView sp_city, t1;
	Button b_register;
	String url = "";
	RelativeLayout rl;
	ImageView i1, i01, i2, i3, i4, i5, i6, i7, i8, i02, i05, i06, i07, i08;
	// ConnectionDetector cd;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.permote_carpolling);
		// cd = new ConnectionDetector(AddCompany.this);
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().setHomeButtonEnabled(true);
//		Drawable d = ContextCompat.getDrawable(AddCompany.this, R.drawable.logooo);
//		getActionBar().setIcon(d);
//		getActionBar().setTitle("");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// prevent
																							// keyboard
																							// from
																							// popping
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

		Typeface tf = Typeface.createFromAsset(AddCompany.this.getAssets(), "AvenirLTStd_Book.otf");
		t1.setTypeface(tf);
		et_c_name.setTypeface(tf);
		et_c_address.setTypeface(tf);
		et_name.setTypeface(tf);
		et_official_email.setTypeface(tf);
		et_contact_number.setTypeface(tf);
		et_designation.setTypeface(tf);
		sp_city.setTypeface(tf);
		b_register.setTypeface(tf);

		Bundle b = getIntent().getExtras();
		String comp_name = b.getString("company_name");
		et_c_name.setText(comp_name);
		// Resources r = getResources();
		// int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
		// 35, r.getDisplayMetrics());
		//
		// final FloatLabelLayout fl =
		// (FloatLabelLayout)rootView.findViewById(R.id.fl1);
		// final RelativeLayout.LayoutParams params =
		// (RelativeLayout.LayoutParams)fl.getLayoutParams();
		// params.setMargins(35, 5, 20, 0); //substitute parameters for left,
		// top, right, bottom
		//
		// final RelativeLayout.LayoutParams params1 =
		// (RelativeLayout.LayoutParams)fl.getLayoutParams();
		// params.setMargins(px, 20, 20, 0);

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
				
				d1 = ContextCompat.getDrawable(AddCompany.this, R.drawable.touch_ripple_back_color);
				d2 = ContextCompat.getDrawable(AddCompany.this, R.drawable.touch_blue_back_color);
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
				ib_back.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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
				});
				l_handle.setVisibility(View.GONE);
				l_menu.setVisibility(View.GONE);
				// popup = new PopupMenu(welcome.this, ib_menu);
				
				l_nav_search.setVisibility(View.GONE);
				ib_logo.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
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
						Intent i = new Intent(AddCompany.this, welcome.class);
						startActivity(i);
					}
				});
				getActionBar().setCustomView(mCustomView);
				getActionBar().setDisplayShowCustomEnabled(true);
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(sp_city) || v.equals(rl)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		}
		if (v.equals(b_register)) {
			if (et_c_name.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Company Name", Toast.LENGTH_LONG).show();
				et_c_name.setError("Required");
			}
			else if (et_c_address.getText().toString().length() == 0){
//				Toast.makeText(company_list.this, "Please Enter Company Address", Toast.LENGTH_LONG).show();
				et_c_address.setError("Required");
			}
			else if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(AddCompany.this, "Please Select City", Toast.LENGTH_LONG).show();
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
			}else {
				String s_city = new database_method().get_data(AddCompany.this,
						"select id from city where city_name='" + sp_city.getText().toString() + "'");
				try {
					url = new url().create_company + "&comp_name="
							+ URLEncoder.encode(et_c_name.getText().toString(), "UTF-8") + "&comp_add="
							+ URLEncoder.encode(et_c_address.getText().toString(), "UTF-8") + "&city_id=" + s_city
							+ "&c_name=" + URLEncoder.encode(et_name.getText().toString(), "UTF-8") + "&c_email="
							+ URLEncoder.encode(et_official_email.getText().toString(), "UTF-8") + "&c_phone="
							+ et_contact_number.getText().toString() + "&c_designation="
							+ URLEncoder.encode(et_designation.getText().toString(), "UTF-8");
					SharedPreferences task = AddCompany.this.getSharedPreferences("user", AddCompany.this.MODE_PRIVATE);
					// if (task.getString("user_id", null) != null) {
					// ConnectionDetector cd = new
					// ConnectionDetector(AddCompany.this);
					// if (cd.isConnectingToInternet()) {
					// accessWebService();
					// } else {
					// Toast.makeText(AddCompany.this, "No Internet Connection",
					// Toast.LENGTH_LONG).show();
					// }
					checkInternet();
				} catch (Exception e) {
					//e.printStackTrace();
				}
				// } else {
				// Toast.makeText(AddCompany.this, "Please Login first",
				// Toast.LENGTH_LONG).show();
				// }
			}
		}
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(AddCompany.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(AddCompany.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(AddCompany.this.getAssets(), "AvenirLTStd_Book.otf");
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

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(AddCompany.this, null, "Adding ! please Wait...");

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
				Toast.makeText(AddCompany.this, result, Toast.LENGTH_LONG).show();
				AddCompany.this.finish();
			} else {
				Toast.makeText(AddCompany.this, "Error Processing request, please try again", Toast.LENGTH_LONG).show();

			}
		}
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();

		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
//		switch (item.getItemId()) {
//		case android.R.id.home:
//			finish();
//			return super.onOptionsItemSelected(item);
//		}
		return super.onOptionsItemSelected(item);
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

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
			if (result.contentEquals("true")) {
				accessWebService();
			} else {
				Toast.makeText(AddCompany.this, "Server Not Reachable", Toast.LENGTH_LONG).show();
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}
