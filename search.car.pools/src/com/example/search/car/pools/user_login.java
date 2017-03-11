package com.example.search.car.pools;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.search.car.pools.search_result.CheckInternet;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;
import dash.board.dashboard_main;
import data.service.ServiceHandler;
import data.service.url;
import post.details.post_details_main;

public class user_login extends Activity implements OnClickListener {
	TextView tv_title, tv_new_user, tv_sec_city, tv_sec_brith;
	EditText et_user_name, et_user_email, et_user_password, et_user_phone, et_user_place, et_user_profession;
	RelativeLayout l1_name;
	LinearLayout l1_login;
	Button b_register;
	String url;
	private SQLiteDatabase database;
	DataBaseHelper dataBaseHelper;
	SharedPreferences.Editor editor;
	int frag_id;
	String activity_name;
	MenuItem bedMenuItem;
	RadioGroup rg;
	RelativeLayout r1, r2, r3;
	ImageView i1, i2;

	// search dialog widgets
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for, t1, t2, t3;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	ImageView logo, mail, pass;
	RelativeLayout close;
	static int gender = 5;

	private ProgressDialog p_bar;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	// forgot password
	TextView tv_forgot;
	boolean forgot_pass = false;
	String url_forgot = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			frag_id = bundle.getInt("frag_id");

		}
		editor = getSharedPreferences("user", MODE_PRIVATE).edit();
		dataBaseHelper = new DataBaseHelper(this);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		// Drawable d = ContextCompat.getDrawable(user_login.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// prevent
																							// keyboard
																							// from
																							// popping
		et_user_name = (EditText) findViewById(R.id.et_user_name);
		et_user_email = (EditText) findViewById(R.id.et_user_email);
		et_user_password = (EditText) findViewById(R.id.et_user_password);
		et_user_phone = (EditText) findViewById(R.id.et_user_phone);
		et_user_place = (EditText) findViewById(R.id.et_user_place);
		et_user_profession = (EditText) findViewById(R.id.et_user_profession);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_new_user = (TextView) findViewById(R.id.tv_new_user);
		tv_sec_city = (TextView) findViewById(R.id.tv_sec_city);
		tv_sec_brith = (TextView) findViewById(R.id.tv_sec_brith);
		rg = (RadioGroup) findViewById(R.id.rg_gender);
		l1_name = (RelativeLayout) findViewById(R.id.L1_name);
		l1_login = (LinearLayout) findViewById(R.id.l1_login);
		b_register = (Button) findViewById(R.id.b_user_register);
		b_register.setOnClickListener(this);
		b_register.setText("LOGIN");
		tv_new_user.setText("REGISTER");
		tv_title.setText("REGISTER NOW!");

		t1 = (TextView) findViewById(R.id.tv_1_login);
		t2 = (TextView) findViewById(R.id.tv_2_login);
		t3 = (TextView) findViewById(R.id.tv_3_login);

		tv_forgot = (TextView) findViewById(R.id.tv_forgot_pass);
		tv_forgot.setOnClickListener(this);

		tv_new_user.setOnClickListener(this);
		tv_sec_city.setOnClickListener(this);
		tv_sec_brith.setOnClickListener(this);
		rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.radio0)
					gender = 0;
				else if (checkedId == R.id.radio1)
					gender = 1;
			}
		});

		r1 = (RelativeLayout) findViewById(R.id.rl1);
		r2 = (RelativeLayout) findViewById(R.id.rl2);
		r3 = (RelativeLayout) findViewById(R.id.rl3);
		r1.setOnClickListener(this);
		r2.setOnClickListener(this);
		r3.setOnClickListener(this);

		i1 = (ImageView) findViewById(R.id.iv_1);
		i2 = (ImageView) findViewById(R.id.iv_2);
		i1.setOnClickListener(this);
		i2.setOnClickListener(this);

		logo = (ImageView) findViewById(R.id.iv_company_logo);
		SVG svg1 = SVGParser.getSVGFromResource(user_login.this.getResources(), R.raw.logo);
		logo.setImageDrawable(svg1.createPictureDrawable());

		ImageView iv2 = (ImageView) findViewById(R.id.iv_3_login);
		SVG svg = SVGParser.getSVGFromResource(user_login.this.getResources(), R.raw.car);
		iv2.setImageDrawable(svg.createPictureDrawable());

		mail = (ImageView) findViewById(R.id.iv_email_login);
		SVG svg2 = SVGParser.getSVGFromResource(user_login.this.getResources(), R.raw.mail_icon);
		mail.setImageDrawable(svg2.createPictureDrawable());

		pass = (ImageView) findViewById(R.id.iv_pass_login);
		SVG svg3 = SVGParser.getSVGFromResource(user_login.this.getResources(), R.raw.password_key);
		pass.setImageDrawable(svg3.createPictureDrawable());
		Typeface tf = Typeface.createFromAsset(user_login.this.getAssets(), "AvenirLTStd_Book.otf");
		tv_title.setTypeface(tf);
		tv_new_user.setTypeface(tf);
		tv_sec_city.setTypeface(tf);
		tv_sec_brith.setTypeface(tf);
		et_user_name.setTypeface(tf);
		et_user_email.setTypeface(tf);
		et_user_password.setTypeface(tf);
		et_user_phone.setTypeface(tf);
		et_user_place.setTypeface(tf);
		et_user_profession.setTypeface(tf);
		b_register.setTypeface(tf);

		// Login should be shown first
		l1_login.setVisibility(View.GONE);
		l1_name.setVisibility(View.GONE);
		tv_new_user.setText("REGISTER");
		b_register.setText("LOGIN");
		// getActionBar().setTitle("Login");
		logo.setVisibility(View.VISIBLE);
		tv_title.setVisibility(View.GONE);
		mail.setVisibility(View.VISIBLE);
		pass.setVisibility(View.VISIBLE);

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

		d1 = ContextCompat.getDrawable(user_login.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(user_login.this, R.drawable.touch_blue_back_color);
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
		l_menu.setVisibility(View.GONE);
		// popup = new PopupMenu(welcome.this, ib_menu);

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
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(tv_new_user)) {
			if (tv_new_user.getText().toString().equals("LOGIN")) {
				l1_login.setVisibility(View.GONE);
				l1_name.setVisibility(View.GONE);
				tv_new_user.setText("REGISTER");
				b_register.setText("LOGIN");
				// getActionBar().setTitle("Login");
				logo.setVisibility(View.VISIBLE);
				tv_title.setVisibility(View.GONE);
				mail.setVisibility(View.VISIBLE);
				pass.setVisibility(View.VISIBLE);
				et_user_email.setText("");
				et_user_password.setText("");
				tv_forgot.setVisibility(View.VISIBLE);
			} else {
				l1_login.setVisibility(View.VISIBLE);
				l1_name.setVisibility(View.VISIBLE);
				tv_new_user.setText("LOGIN");
				tv_title.setVisibility(View.VISIBLE);
				tv_title.setText("REGISTER NOW!");
				// getActionBar().setTitle("Register");
				b_register.setText("REGISTER");
				logo.setVisibility(View.GONE);
				mail.setVisibility(View.GONE);
				pass.setVisibility(View.GONE);
				tv_forgot.setVisibility(View.GONE);
			}
		}
		if (v.equals(tv_sec_city) || v.equals(r1) || v.equals(i1)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, tv_sec_city);
		}
		if (v.equals(tv_sec_brith) || v.equals(r2) || v.equals(i2)) {
			String[] year = new String[49];
			for (int i = 0; i <= 48; i++) {
				year[i] = String.valueOf(1996 - i);
			}
			dialog("Brith Year", year, tv_sec_brith);
		}
		if (v.equals(b_register)) {
			if (b_register.getText().toString().toUpperCase().equals("REGISTER")) {
				if (et_user_name.getText().toString().contentEquals(""))
					Toast.makeText(user_login.this, "Please enter your name", Toast.LENGTH_SHORT).show();
				else if (!(new EmailAndPhoneChecker().validEmail(et_user_email.getText().toString())))
					Toast.makeText(user_login.this, "Please a valid Email Address", Toast.LENGTH_SHORT).show();
				else if (et_user_password.getText().toString().contentEquals(""))
					Toast.makeText(user_login.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
				else if (et_user_phone.getText().toString().length() < 9
						|| et_user_phone.getText().toString().length() > 14)
					Toast.makeText(user_login.this, "Please enter a valid Phone No.", Toast.LENGTH_SHORT).show();
				else if (tv_sec_city.getText().toString().contentEquals("Select City"))
					Toast.makeText(user_login.this, "Please select your city", Toast.LENGTH_SHORT).show();
				else if (et_user_place.getText().toString().contentEquals(""))
					Toast.makeText(user_login.this, "Please enter your place", Toast.LENGTH_SHORT).show();
				else if (et_user_profession.getText().toString().contentEquals(""))
					Toast.makeText(user_login.this, "Please enter your profession", Toast.LENGTH_SHORT).show();
				else if (tv_sec_brith.getText().toString().contentEquals("Select Year of Birth"))
					Toast.makeText(user_login.this, "Please Select Year of Birth", Toast.LENGTH_SHORT).show();
				else if (gender == 5)
					Toast.makeText(user_login.this, "Please select your gender", Toast.LENGTH_SHORT).show();
				else {
					String state = "";
					DataBaseHelper h = new DataBaseHelper(user_login.this);
					SQLiteDatabase db = h.getReadableDatabase();
					Cursor c1 = db.rawQuery(
							"select id from city where city_name='" + tv_sec_city.getText().toString() + "'", null);
					while (c1.moveToNext()) {
						state = c1.getString(0);
					}
					db.close();
					// ConnectionDetector cd = new
					// ConnectionDetector(getApplicationContext());
					// if (cd.isConnectingToInternet()) {
					try {
						url = new url().create_new_user + "&username="
								+ URLEncoder.encode(et_user_email.getText().toString(), "UTF-8") + "&password="
								+ URLEncoder.encode(et_user_password.getText().toString(), "UTF-8") + "&name="
								+ URLEncoder.encode(et_user_name.getText().toString().trim(), "UTF-8") + "&email="
								+ URLEncoder.encode(et_user_email.getText().toString().trim(), "UTF-8") + "&gender="
								+ gender + "&phone=" + et_user_phone.getText().toString() + "&year="
								+ tv_sec_brith.getText().toString() + "&pro="
								+ URLEncoder.encode(et_user_profession.getText().toString(), "UTF-8") + "&city=" + state
								+ "&place=" + URLEncoder.encode(et_user_place.getText().toString(), "UTF-8");
						tv_new_user.setEnabled(false);
						b_register.setEnabled(false);
						checkInternet();
					} catch (Exception e) {
						//e.printStackTrace();
					}
					// } else {
					// Toast.makeText(getApplication(), "No Internet
					// Connection", Toast.LENGTH_LONG).show();
					// }
				}

			} else {

				if (!(new EmailAndPhoneChecker().validEmail(et_user_email.getText().toString())))
					Toast.makeText(user_login.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
				else if (et_user_password.getText().toString().contentEquals(""))
					Toast.makeText(user_login.this, "Password cannot be blank", Toast.LENGTH_SHORT).show();
				else {
					try {
						url = new url().user_login + "&login_username="
								+ URLEncoder.encode(et_user_email.getText().toString(), "UTF-8") + "&login_pass="
								+ URLEncoder.encode(et_user_password.getText().toString(), "UTF-8");
						// logincase(et_user_email.getText().toString(),
						// et_user_password.getText().toString());
						// accessWebService();
						// Toast.makeText(user_login.this, "Password:'"+
						// et_user_password.getText().toString()+"'",
						// Toast.LENGTH_SHORT).show();

						//Log.d("log", "url after encloding" + url);
						tv_new_user.setEnabled(false);
						b_register.setEnabled(false);
						checkInternet();
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			}

		}
		if (v.equals(tv_forgot)) {
			dialog_reply();
		}
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
				Toast.makeText(user_login.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(user_login.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_id", 0);

				startActivity(i);
				promptsView.dismiss();
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
			Intent i = new Intent(user_login.this, welcome.class);
			startActivity(i);
		}

	}

	public void dialog(String name, final String[] arr, final TextView tv) {
		final Dialog dialog = new Dialog(user_login.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(user_login.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(user_login.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				if (tv.equals(tv_sec_city)) {
					ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tv_sec_city.getLayoutParams();
					mlp.setMargins(0, 0, 0, 0);
					t1.setVisibility(View.VISIBLE);
				} else {
					ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) tv_sec_brith.getLayoutParams();
					mlp.setMargins(0, 0, 0, 0);
					t2.setVisibility(View.VISIBLE);
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		// bedMenuItem = menu.findItem(R.id.menu_login);
		// // menu.findItem(R.id.menu_notification).setVisible(false);
		// menu.findItem(R.id.menu_add_new_list).setVisible(false);
		// menu.findItem(R.id.menu_dashboard).setVisible(false);
		// menu.findItem(R.id.menu_login).setVisible(false);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// int id = item.getItemId();
		//
		// if (id == android.R.id.home) {
		// onBackPressed();
		// }
		// if (id == R.id.menu_search) {
		// showSearchDialog();
		// }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (frag_id == 6) {
			Intent intent = new Intent(getApplicationContext(), welcome.class);
			intent.putExtra("frag_id", frag_id);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p_bar.setCancelable(true);
		}

		@SuppressWarnings("unused")
		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "Error. Please try again";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url.replace(" ", "%20");
			//Log.d("log", "Login url: " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("log", "> " + jsonStr);
			//Log.d("log", "> " + "First time: " + webResponse);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					String status = jsonObj.getString("status");
					//Log.d("log", "status: " + status);
					if (status.contentEquals("success")) {
						JSONArray taskarray = jsonObj.getJSONArray("data");
						for (int i = 0; i < taskarray.length(); i++) {
							JSONObject obj = taskarray.getJSONObject(i);

							// check if user exist then update or else insert
							// user

							DataBaseHelper helper = new DataBaseHelper(user_login.this);
							SQLiteDatabase database = helper.getReadableDatabase();
							Cursor c = database.rawQuery(
									"select user_id from user where user_id='" + obj.getString("user_id") + "'", null);

							if (c != null && c.moveToFirst()) {
								String data_[] = { obj.getString("username"), "Pass123", obj.getString("name"),
										obj.getString("email"), obj.getString("gender"), obj.getString("phone"),
										obj.getString("dob"), obj.getString("profession"), obj.getString("state"),
										obj.getString("place"), obj.getString("date"), obj.getString("lastlogin"),
										obj.getString("ad_notification") };
								dataBaseHelper.update_user(data_);
							} else {
								String data[] = { obj.getString("user_id"), obj.getString("username"), "Pass123",
										obj.getString("name"), obj.getString("email"), obj.getString("gender"),
										obj.getString("phone"), obj.getString("dob"), obj.getString("profession"),
										obj.getString("state"), obj.getString("place"), obj.getString("date"),
										obj.getString("lastlogin"), obj.getString("ad_notification") };
								dataBaseHelper.insert_user(data);
							}
							c.close();
							database.close();

							editor.putString("user_id", obj.getString("user_id"));
							editor.putString("name", obj.getString("name"));
							editor.putString("email", obj.getString("email"));
							editor.putString("phone", obj.getString("phone"));
							editor.commit();
							webResponse = b_register.getText().toString() + " Successfully";
							//Log.d("log", "> " + "second time: " + webResponse);
						}
					} else if (status.contentEquals("failed")) {
						if (jsonStr.contains("Incorrect credentials")) {
							webResponse = "Incorrect credentials";
							//Log.d("log", "> " + "Incorrect: " + webResponse);
						} else if (jsonStr.contains("This email is already registered")) {
							webResponse = "This email is already registered";
							//Log.d("log", "> " + "already exist: " + webResponse);
						}
					}

				} catch (Exception e) {
					//e.printStackTrace();
					// webResponse = "error=" + e;
				}
			} else {
				//Log.e("ServiceHandler", "Couldn't get any data from the url");
			}
			//Log.d("log", "> " + "after do in background: " + webResponse);
			return webResponse;
		}

		protected void onPostExecute(String result) {

			p_bar.dismiss();
			//Log.d("log", "> " + "Result: " + result);
			if (result.contains("Successfully")) {
				finish();
			}
			tv_new_user.setEnabled(true);
			b_register.setEnabled(true);
			Toast.makeText(getApplication(), result, Toast.LENGTH_SHORT).show();
		}
	}

	public void logincase(String username, String password) {
		int i = 0;
		/*
		 * DataBaseHelper helper = new DataBaseHelper(this); database =
		 * helper.getWritableDatabase(); Cursor c = database.rawQuery(
		 * "select user_id,name,email,phone from user where username='" +
		 * username + "' and password='" + password + "'", null);
		 * 
		 * try { while (c.moveToNext()) { editor.putString("user_id",
		 * c.getString(0)); editor.putString("name", c.getString(1));
		 * editor.putString("email", c.getString(2)); editor.putString("phone",
		 * c.getString(3)); editor.commit(); i++; } database.close(); } catch
		 * (Exception e) { }
		 */
		checkInternet();
		/*
		 * if (i == 0) {
		 * 
		 * 
		 * } else { Toast.makeText(this, "Login Successfully",
		 * Toast.LENGTH_LONG).show(); finish(); }
		 */
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();

		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
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
		Typeface tf = Typeface.createFromAsset(user_login.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			p_bar = ProgressDialog.show(user_login.this, null, "Loading ! please Wait...");
			p_bar.setCancelable(true);
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

			if (result.contentEquals("true") && forgot_pass == false) {
				accessWebService();
			} else if (result.contentEquals("true") && forgot_pass == true) {
				new Forgot_Pass().execute();
			} else {
				Toast.makeText(user_login.this, "Server not reachable", Toast.LENGTH_SHORT).show();
				tv_new_user.setEnabled(true);
				b_register.setEnabled(true);
				p_bar.dismiss();
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

	public void dialog_reply() {
		final Dialog dialog = new Dialog(user_login.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.forgot_password);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Recover Password");
		final EditText et_email = (EditText) dialog.findViewById(R.id.et_email_forgot_pass);
		Button b_send_email = (Button) dialog.findViewById(R.id.b_send_email);
		// Button b_send_sms = (Button) dialog.findViewById(R.id.b_send_sms);
		Typeface tf = Typeface.createFromAsset(user_login.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		et_email.setTypeface(tf);
		b_send_email.setTypeface(tf);
		// b_send_sms.setTypeface(tf);
		final RelativeLayout l_close = (RelativeLayout) dialog.findViewById(R.id.l_close);
		l_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		b_send_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// do your work here
				if (!(new EmailAndPhoneChecker().validEmail(et_email.getText().toString()))) {
					Toast.makeText(user_login.this, "Please enter a valid Email Address", Toast.LENGTH_SHORT).show();
				} else {
					try {
						url_forgot = new url().forgot_pass + "&email_id="
								+ URLEncoder.encode(et_email.getText().toString(), "UTF-8");
						forgot_pass = true;
						checkInternet();
						dialog.dismiss();
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}
			}
		});
		dialog.show();
	}

	class Forgot_Pass extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String webResponse = "Error. Please try again";
			ServiceHandler sh = new ServiceHandler();
			String jsonStr = sh.makeServiceCall(url_forgot, ServiceHandler.GET);
			//Log.d("log", "> " + jsonStr);
			//Log.d("log", "> " + "First time: " + webResponse);
			if (jsonStr.contentEquals("ok")) {
				webResponse = "ok";
			} else {
				webResponse = "error";
			}
			return webResponse;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			p_bar.dismiss();
			if (result.contentEquals("ok")) {
				Toast.makeText(user_login.this, "Account details are sent to the email.", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(user_login.this, "Email not found.", Toast.LENGTH_LONG).show();
			}
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
}
