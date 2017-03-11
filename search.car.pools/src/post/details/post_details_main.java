package post.details;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.search.car.pools.DataBaseHelper;
import com.example.search.car.pools.EmailAndPhoneChecker;
import com.example.search.car.pools.R;
import com.example.search.car.pools.create_activity;
//import com.example.search.car.pools.map_activity;
import com.example.search.car.pools.search_result;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.welcome;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.map.MyMap;
import com.squareup.picasso.Picasso;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;
import custom.list.ExpandListSearchAdapter.MD5Util;
import dash.board.dashboard_main;
import data.service.ServiceHandler;
import data.service.url;

public class post_details_main extends FragmentActivity implements OnClickListener, OnTouchListener {
	TextView tv_by, tv_profession, tv_profession_below, tv_gender, tv_phone, tv_from, tv_to, tv_category, tv_route,
			tv_seats, tv_departure, tv_returning, tv_description, tv_company_name, tv_address;

	TextView tv_profession_below1, tv_from1, tv_to1, tv_route1, tv_description1, tv_company_name1, tv_address1;

	// RelativeLayout b_send_email, b_map;// rl_bottom;// b_route, ;
	// //b_send_sms
	String url = "", post_id = null, receiver_id, user_email, category;
	RelativeLayout l_company_name, l_address;// rl_phone_details;
	RelativeLayout l_phone, ll_bottom_call_button;
	String data[] = { "-----------------", ":-----------------", ":-----------------", ":-----------------",
			":-----------------", ":-----------------", ":-----------------", ":-----------------",
			":-----------------", ":-----------------", ":-----------------", ":-----------------",
			":-----------------", ":-----------------", ":-----------------", ":-----------------",
			":-----------------", ":-----------------", ":---", ":---", ":---", ":---", ":---" };
	MenuItem bedMenuItem;
	DataBaseHelper dbHelper;
	Dialog map;
	SharedPreferences task;

	ImageView user_image;
	// LinearLayout b;

	// search dialog widgets
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	RelativeLayout close;
	String city_i, ad_title, url_sms = "";
	String sms_body;

	String phone_no_for_unregistered_users;

	boolean sms_is_sent = false; // check for checking sms sent or not
	String city;

	private ProgressDialog p_bar;

	LinearLayout l_button_sms, l_button_map;
	// FloatingActionButton fabButton;
	RelativeLayout fabButton;
	RelativeLayout scroll_l;
	ImageView iv_map, iv_msg;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.details_new);
		task = getSharedPreferences("user", MODE_PRIVATE);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			post_id = extras.getString("post_id");
			user_email = extras.getString("user_email");
			category = extras.getString("category");
		}
		dbHelper = new DataBaseHelper(this);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		// Drawable d = ContextCompat.getDrawable(post_details_main.this,
		// R.drawable.logo12_copy);
		// getActionBar().setIcon(d);

		// Drawable d = ContextCompat.getDrawable(post_details_main.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");
		ll_bottom_call_button = (RelativeLayout) findViewById(R.id.ll_bottom_call_button);
		ll_bottom_call_button.setOnClickListener(this);

//		rl_phone_details = (RelativeLayout) findViewById(R.id.l_phone_details);
//		rl_phone_details.setOnClickListener(this);

		l_company_name = (RelativeLayout) findViewById(R.id.l2);
		l_address = (RelativeLayout) findViewById(R.id.l3);
		l_company_name.setVisibility(View.GONE);
		l_address.setVisibility(View.GONE);
		// rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);
		// b = (LinearLayout) findViewById(R.id.ll_bottom_button);
		url = new url().add_details + "&id=" + post_id;
		tv_by = (TextView) findViewById(R.id.tv_by);
		tv_profession = (TextView) findViewById(R.id.tv_profession);
		tv_profession_below = (TextView) findViewById(R.id.tv_profession_below);
		tv_gender = (TextView) findViewById(R.id.tv_gender);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_from = (TextView) findViewById(R.id.tv_from);
		tv_to = (TextView) findViewById(R.id.tv_to);
		tv_category = (TextView) findViewById(R.id.tv_category);
		tv_route = (TextView) findViewById(R.id.tv_route);
		tv_seats = (TextView) findViewById(R.id.tv_seats);
		tv_departure = (TextView) findViewById(R.id.tv_departure);
		tv_returning = (TextView) findViewById(R.id.tv_returning);
		tv_description = (TextView) findViewById(R.id.tv_description);
		tv_company_name = (TextView) findViewById(R.id.tv_company_name);
		tv_address = (TextView) findViewById(R.id.tv_address);

		// right side textviews
		tv_profession_below1 = (TextView) findViewById(R.id.tv_profession_below1);
		tv_from1 = (TextView) findViewById(R.id.tv_from1);
		tv_to1 = (TextView) findViewById(R.id.tv_to1);
		tv_route1 = (TextView) findViewById(R.id.tv_route1);
		tv_description1 = (TextView) findViewById(R.id.tv_description1);
		tv_company_name1 = (TextView) findViewById(R.id.tv_company_name1);
		tv_address1 = (TextView) findViewById(R.id.tv_address1);

		// b_send_email = (RelativeLayout) findViewById(R.id.b_send_email);
		// b_send_sms = (RelativeLayout) findViewById(R.id.b_send_sms);
		// b_route = (RelativeLayout) findViewById(R.id.b_route);
		// b_map = (RelativeLayout) findViewById(R.id.b_map);
		user_image = (ImageView) findViewById(R.id.iv_user_image_details);

		ImageView iv_above = (ImageView) findViewById(R.id.iv_user_image_details_gravatar);
		SVG svg1 = SVGParser.getSVGFromResource(post_details_main.this.getResources(),
				R.raw.circle_gravatar_post_details);
		iv_above.setImageDrawable(svg1.createPictureDrawable());

		ImageView iv_map = (ImageView) findViewById(R.id.iv_button_map);
		SVG svg_map = SVGParser.getSVGFromResource(post_details_main.this.getResources(), R.raw.map);
		iv_map.setImageDrawable(svg_map.createPictureDrawable());

		ImageView iv_msg = (ImageView) findViewById(R.id.iv_button_email);
		SVG svg_msg = SVGParser.getSVGFromResource(post_details_main.this.getResources(), R.raw.mail_icon);
		iv_msg.setImageDrawable(svg_msg.createPictureDrawable());

		ImageView iv_call = (ImageView) findViewById(R.id.iv_button_call);
		SVG svg_call = SVGParser.getSVGFromResource(getResources(), R.raw.phone_call);
		iv_call.setImageDrawable(svg_call.createPictureDrawable());

		ImageView iv_plus = (ImageView) findViewById(R.id.iv_button_plus);
		SVG svg_plus = SVGParser.getSVGFromResource(getResources(), R.raw.plus_icon);
		iv_plus.setImageDrawable(svg_plus.createPictureDrawable());
		
		tv_profession_below.setVisibility(View.GONE); tv_gender.setVisibility(View.GONE); tv_phone.setVisibility(View.GONE);
		tv_from.setVisibility(View.GONE); tv_to.setVisibility(View.GONE); tv_category.setVisibility(View.GONE); 
		tv_route.setVisibility(View.GONE); tv_seats.setVisibility(View.GONE);tv_departure.setVisibility(View.GONE);
		tv_returning.setVisibility(View.GONE);tv_description.setVisibility(View.GONE);tv_company_name.setVisibility(View.GONE);
		tv_address.setVisibility(View.GONE);tv_profession_below1.setVisibility(View.GONE);tv_from1.setVisibility(View.GONE);
		tv_to1.setVisibility(View.GONE);tv_route1.setVisibility(View.GONE);tv_description1.setVisibility(View.GONE);
		tv_company_name1.setVisibility(View.GONE);tv_address1.setVisibility(View.GONE);
		
		// b_send_email.setOnClickListener(this);
		// b_send_sms.setOnClickListener(this);
		// b_route.setOnClickListener(this);
		// b_map.setOnClickListener(this);
		// b.setOnClickListener(this);
		// ConnectionDetector cd = new
		// ConnectionDetector(getApplicationContext());
		// if (cd.isConnectingToInternet()) {
		// accessWebService();
		// } else {
		// Toast.makeText(getApplication(), "No Internet Connection",
		// Toast.LENGTH_LONG).show();
		// }
		checkInternet();
		// fabButton = new
		// FloatingActionButton.Builder(this).withDrawable(getResources().getDrawable(R.drawable.add_1))
		// .withButtonColor(Color.parseColor("#1abc9c")).withGravity(Gravity.BOTTOM
		// | Gravity.RIGHT)
		// .withMargins(0, 0, 15, 15).create();
		fabButton = (RelativeLayout) findViewById(R.id.ll_bottom_float_button);
		fabButton.setOnClickListener(post_details_main.this);
		// l_button = (LinearLayout)findViewById(R.id.ll_bottom_button);
		l_button_sms = (LinearLayout) findViewById(R.id.ll_bottom_button_sms);
		l_button_map = (LinearLayout) findViewById(R.id.ll_bottom_button_map);

		// l_button.setOnClickListener(post_details_main.this);
		l_button_map.setOnClickListener(post_details_main.this);
		l_button_sms.setOnClickListener(post_details_main.this);
		l_button_map.setVisibility(View.GONE);
		l_button_sms.setVisibility(View.GONE);

		scroll_l = (RelativeLayout) findViewById(R.id.scroll_l);
		scroll_l.setOnClickListener(post_details_main.this);
		scroll_l.setOnTouchListener(post_details_main.this);

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

		d1 = ContextCompat.getDrawable(post_details_main.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(post_details_main.this, R.drawable.touch_blue_back_color);
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

		l_handle.setVisibility(View.GONE);
		// l_back.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// popup = new PopupMenu(welcome.this, ib_menu);
		// l_menu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// l_nav_search.setOnClickListener(new OnClickListener() {
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
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v.equals(scroll_l)) {
			l_button_map.setVisibility(View.GONE);
			l_button_sms.setVisibility(View.GONE);
			fabButton.setBackgroundResource(R.drawable.floatbutton_green);
			fabButton.setRotation(90);
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		if (v.equals(l_button_sms)) {
			if (data[0].equals("-----------------")) {
				Toast.makeText(this, "Data not found", Toast.LENGTH_LONG).show();
			} else if (task.getString("user_id", null) == null) {
				// Intent smsIntent = new Intent(Intent.ACTION_VIEW);
				// smsIntent.setType("vnd.android-dir/mms-sms");
				// smsIntent.putExtra("address",
				// phone_no_for_unregistered_users);
				// smsIntent.putExtra("sms_body","\n--\nThis message has been
				// send against your listing url: "+"url");
				// startActivity(smsIntent);

				if (city_i.contentEquals("1"))
					city = "delhi";
				else if (city_i.contentEquals("2"))
					city = "mumbai";
				else if (city_i.contentEquals("3"))
					city = "bengaluru";
				else if (city_i.contentEquals("4"))
					city = "pune";
				else if (city_i.contentEquals("5"))
					city = "kolkata";
				else if (city_i.contentEquals("6"))
					city = "ahmedabad";
				// Log.d("log", "City: " + city + " City Index: " + city_i);
				url_sms = new url().url_short + "http://www.searchcarpools.com/view/details/" + city_i + "/" + city
						+ "/" + post_id + "/" + data[0].toLowerCase().trim();
				sms_is_sent = true;
				new CheckInternet().execute();
				l_button_map.setVisibility(View.GONE);
				l_button_sms.setVisibility(View.GONE);
				fabButton.setBackgroundResource(R.drawable.floatbutton_green);
				fabButton.setRotation(90);
				// dialog("Send Sms", 2);
			} else {
				dialog("Send Email", 1);
				l_button_map.setVisibility(View.GONE);
				l_button_sms.setVisibility(View.GONE);
				fabButton.setBackgroundResource(R.drawable.floatbutton_green);
				fabButton.setRotation(90);
			}
		}
		if (v.equals(scroll_l)) {
			l_button_map.setVisibility(View.GONE);
			l_button_sms.setVisibility(View.GONE);
		}
		// if (v.equals(b_send_sms)) {
		// if (data[0].equals("-----------------")) {
		// Toast.makeText(this, "Data not found", Toast.LENGTH_LONG).show();
		// } else {
		// dialog("Send Sms", 2);
		// }
		// }
		
		if (v.equals(tv_phone) || v.equals(ll_bottom_call_button)) {
			if (!tv_phone.getText().toString().contentEquals("")) {
				Intent i = new Intent(Intent.ACTION_DIAL);
				i.setData(Uri.parse("tel:" + data[4]));
				startActivity(i);
			}
		}
		if (v.equals(l_button_map)) {
			if (data[0].equals("-----------------")) {
				Toast.makeText(this, "Data not found", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(post_details_main.this, MyMap.class);
				i.putExtra("from", data[5]);
				i.putExtra("to", data[6]);
				i.putExtra("city_index", Integer.parseInt(city_i));
				startActivity(i);
				l_button_map.setVisibility(View.GONE);
				l_button_sms.setVisibility(View.GONE);
				fabButton.setBackgroundResource(R.drawable.floatbutton_green);
				fabButton.setRotation(90);
			}
		}
		if (v.equals(fabButton)) {
			if (l_button_map.getVisibility() == View.VISIBLE || l_button_sms.getVisibility() == View.VISIBLE) {
				fabButton.setBackgroundResource(R.drawable.floatbutton_green);
				fabButton.setRotation(90);
				l_button_map.animate().translationY(0).alpha(0.0f).setDuration(300)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								l_button_map.setVisibility(View.GONE);
							}
						});
				l_button_sms.animate().translationY(0).alpha(0.0f).setDuration(300)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationEnd(Animator animation) {
								super.onAnimationEnd(animation);
								l_button_sms.setVisibility(View.GONE);
							}
						});
				// l_button_map.setVisibility(View.GONE);
				// l_button_sms.setVisibility(View.GONE);
			} else {
				fabButton.setBackgroundResource(R.drawable.floatbutton_red);
				fabButton.setRotation(45);
				l_button_map.animate().translationX(0).alpha(1.0f).setDuration(300)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationStart(Animator animation) {
								super.onAnimationStart(animation);
								l_button_map.setVisibility(View.VISIBLE);
							}
						});

				l_button_sms.animate().translationX(0).alpha(1.0f).setDuration(300)
						.setListener(new AnimatorListenerAdapter() {
							@Override
							public void onAnimationStart(Animator animation) {
								super.onAnimationStart(animation);
								l_button_sms.setVisibility(View.VISIBLE);
							}
						});
				// l_button_map.setVisibility(View.VISIBLE);
				// l_button_sms.setVisibility(View.VISIBLE);
			}
		}
		// if (v.equals(b_route)) {
		// Intent i = new Intent(post_details_main.this, MyMap.class);
		// i.putExtra("from", data[5]);
		// i.putExtra("to", data[6]);
		// i.putExtra("city_index", Integer.parseInt(city_i));
		// startActivity(i);
		// }
		// if (v.equals(b)) {
		// rl_bottom.setVisibility(View.VISIBLE);
		// final ScrollView scroll = (ScrollView) findViewById(R.id.scroll);
		// scroll.smoothScrollTo(0, rl_bottom.getTop());
		// }

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
				Toast.makeText(post_details_main.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(post_details_main.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_id", 0);
				startActivity(i);
				promptsView.dismiss();
				finish();
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
			Intent i = new Intent(post_details_main.this, welcome.class);
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

			final PopupMenu popup = new PopupMenu(post_details_main.this, v);
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
					switch (item.getItemId()) {
					case R.id.menu_add_new_list:
						if (task.getString("user_id", null) != null) {
							i = new Intent(getBaseContext(), create_activity.class);

							startActivity(i);
						} else {
							Toast.makeText(post_details_main.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(post_details_main.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(post_details_main.this, "Please Login first", Toast.LENGTH_LONG).show();
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
		}

	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			// p_bar.setMessage("Connecting Server taking time.\nPlease
			// wait...");
			// p_bar.show();
		}

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
					JSONObject obj = new JSONObject(jsonStr);
					String status = obj.getString("status");
					// Log.i("log", "status: " + status);
					if (status.contentEquals("success")) {
						JSONArray taskarray = obj.getJSONArray("data");
						// Log.i("log", "JSON Object Data: " + obj);
						for (int i = 0; i < taskarray.length(); i++) {
							JSONObject jsonObject = taskarray.getJSONObject(i);
							receiver_id = jsonObject.getString("user_id").toString();
							city_i = jsonObject.getString("city").toString();
							data[0] = jsonObject.getString("title").toString();
							data[1] = jsonObject.getString("name").toString();
							data[2] = jsonObject.getString("profession").toString();
							data[3] = jsonObject.getString("gender").toString();
							data[4] = jsonObject.getString("phone").toString();
							data[5] = jsonObject.getString("from").toString();
							data[6] = jsonObject.getString("to").toString();
							data[7] = jsonObject.getString("cat_name").toString();
							data[8] = jsonObject.getString("route").toString();
							data[9] = jsonObject.getString("seats").toString();
							data[10] = jsonObject.getString("departure_time").toString();
							data[11] = jsonObject.getString("return_time").toString();
							data[12] = jsonObject.getString("desc").toString();
							data[13] = jsonObject.getString("company_name").toString();
							data[14] = jsonObject.getString("company_address").toString();
							data[15] = jsonObject.getString("date").toString();
							data[16] = jsonObject.getString("city");
							phone_no_for_unregistered_users = jsonObject.getString("phone").toString();

							webResponse = "false";
						}
						// Log.d("log", "Company Name: " + data[13] + " Company
						// Add: " + data[14] + " Phone: " +
						// phone_no_for_unregistered_users);

					}

				} catch (Exception e) {
					//e.printStackTrace();
				}
			} else {
				// Log.e("ServiceHandler", "Couldn't get any data from the
				// url");
			}

			return webResponse;
		}

		protected void onPostExecute(String result) {
			String default_image_url;
			if (category.contentEquals("1"))
				default_image_url = "http://s17.postimg.org/kxbsajban/seeker_male.png";
			else
				default_image_url = "http://s17.postimg.org/mjwei0xyn/provider_male.png";
			String hash = MD5Util.md5Hex(user_email.toLowerCase().trim());

			String gender = data[3];
			// int gender = Integer.parseInt(gender_1);

			if (category.contentEquals("1") && gender.contentEquals("0"))
				default_image_url = "http://s17.postimg.org/kxbsajban/seeker_male.png";
			else if (category.contentEquals("1") && gender.contentEquals("1")) {
				default_image_url = "http://s17.postimg.org/qitm0umlr/seeker_female.png";
			} else if (category.contentEquals("2") && gender.contentEquals("0"))
				default_image_url = "http://s17.postimg.org/mjwei0xyn/provider_male.png";
			else if (category.contentEquals("2") && gender.contentEquals("1"))
				default_image_url = "http://s17.postimg.org/sm418imen/provider_female.png";
			else
				default_image_url = "http://s24.postimg.org/rtj6c6abl/no_gender.png";

			String gravatarUrl = "http://www.gravatar.com/avatar/" + hash + "?d=" + default_image_url + "&s=200";
			Picasso.with(post_details_main.this).load(gravatarUrl).into(user_image);
			// new getGravatarImage(user_image, gravatarUrl).execute();
			set_data();
			p_bar.dismiss();
		}
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();

		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	public void set_data() {
		if (data[14].equals("null") || data[13].contentEquals("") || data[14].contentEquals("")
				|| data[13].contentEquals("null")) {
			l_company_name.setVisibility(View.GONE);
			l_address.setVisibility(View.GONE);

		} else {
			l_company_name.setVisibility(View.VISIBLE);
			l_address.setVisibility(View.VISIBLE);
		}

		tv_profession_below.setVisibility(View.VISIBLE); tv_gender.setVisibility(View.VISIBLE); tv_phone.setVisibility(View.VISIBLE);
		tv_from.setVisibility(View.VISIBLE); tv_to.setVisibility(View.VISIBLE); tv_category.setVisibility(View.VISIBLE); 
		tv_route.setVisibility(View.VISIBLE); tv_seats.setVisibility(View.VISIBLE);tv_departure.setVisibility(View.VISIBLE);
		tv_returning.setVisibility(View.VISIBLE);tv_description.setVisibility(View.VISIBLE);tv_company_name.setVisibility(View.VISIBLE);
		tv_address.setVisibility(View.VISIBLE);tv_profession_below1.setVisibility(View.VISIBLE);tv_from1.setVisibility(View.VISIBLE);
		tv_to1.setVisibility(View.VISIBLE);tv_route1.setVisibility(View.VISIBLE);tv_description1.setVisibility(View.VISIBLE);
		tv_company_name1.setVisibility(View.VISIBLE);tv_address1.setVisibility(View.VISIBLE);
		
		tv_by.setText(getCapitalWords(data[1]));
		tv_profession.setText(getCapitalWords(data[2]));
		
		if (!data[2].contentEquals("")) {
			tv_profession_below.setText(getCapitalWords(data[2]));
			tv_profession_below1.setText(getCapitalWords(data[2]));
		} else {
			tv_profession_below.setText("NA");
			tv_profession_below1.setText("NA");
		}

		if (data[3].contentEquals("0"))
			tv_gender.setText("Male");
		else if (data[3].contentEquals("1"))
			tv_gender.setText("Female");
		else
			tv_gender.setText("NA");

		if (!data[4].contentEquals("")) {
			tv_phone.setText(data[4]);
		} else {
			tv_phone.setText("NA");
		}

		if (!data[5].contentEquals("")) {
			tv_from.setText(getCapitalWords(data[5]));
			tv_from1.setText(getCapitalWords(data[5]));
		} else {
			tv_from.setText("NA");
			tv_from1.setText("NA");
		}

		if (!data[6].contentEquals("")) {
			tv_to.setText(getCapitalWords(data[6]));
			tv_to1.setText(getCapitalWords(data[6]));
		} else {
			tv_to.setText("NA");
			tv_to1.setText("NA");
		}

		if (!data[7].contentEquals("")) {
			tv_category.setText(data[7]);
		} else {
			tv_category.setText("NA");
		}

		if (data[8] != null && data[8].length() > 2) {
			tv_route.setText(data[8].substring(0, 1).toUpperCase() + data[8].substring(1));
			tv_route1.setText(data[8].substring(0, 1).toUpperCase() + data[8].substring(1));
		} else {
			tv_route.setText(data[8]);
			tv_route1.setText(data[8]);
		}

		if (!data[9].contentEquals("")) {
			tv_seats.setText(data[9]);
		} else {
			tv_seats.setText("NA");
		}

		if (!data[10].contentEquals("")) {
			tv_departure.setText(data[10].toUpperCase());
		} else {
			tv_departure.setText("NA");
		}

		if (!data[11].contentEquals("")) {
			tv_returning.setText(data[11].toUpperCase());
		} else {
			tv_returning.setText("NA");
		}

		if (data[12] != null && data[12].length() > 2) {
			tv_description.setText(data[12].substring(0, 1).toUpperCase() + data[12].substring(1));
			tv_description1.setText(data[12].substring(0, 1).toUpperCase() + data[12].substring(1));
		} else {
			tv_description.setText("NA");
			tv_description1.setText("NA");
		}

		if (!data[13].contentEquals("")) {
			tv_company_name.setText(getCapitalWords(data[13]));
			tv_company_name1.setText(getCapitalWords(data[13]));
		} else {
			tv_company_name.setText("NA");
			tv_company_name1.setText("NA");
		}

		if (!data[14].contentEquals("") || !data[14].equals("null")) {
			tv_address.setText(getCapitalWords(data[14]));
			tv_address1.setText(getCapitalWords(data[14]));
		} else {
			tv_address.setText("NA");
			tv_address1.setText("NA");
		}
		
		if (tv_address.getText().toString().contentEquals(":-----------------")){
			tv_address.setText("NA");
			tv_address1.setText("NA");
		}
		
		if (tv_profession_below1.getText().length() > 10)
			tv_profession_below1.setVisibility(View.GONE);
		else 
			tv_profession_below.setVisibility(View.GONE);
		if (tv_from1.getText().length() > 10 )
			tv_from1.setVisibility(View.GONE);
		else 
			tv_from.setVisibility(View.GONE);
		if (tv_to1.getText().length() > 10 )
			tv_to1.setVisibility(View.GONE);
		else
			tv_to.setVisibility(View.GONE);
		if (tv_route1.getText().length() > 10 )
			tv_route1.setVisibility(View.GONE);
		else
			tv_route.setVisibility(View.GONE);
		if (tv_description1.getText().length() > 10)
			tv_description1.setVisibility(View.GONE);
		else
			tv_description.setVisibility(View.GONE);
		if (tv_company_name1.getText().length() > 10)
			tv_company_name1.setVisibility(View.GONE);
		else 
			tv_company_name.setVisibility(View.GONE);
		if (tv_address1.getText().length() > 10)
			tv_address1.setVisibility(View.GONE);
		else
			tv_address.setVisibility(View.GONE);
		

		Typeface tf = Typeface.createFromAsset(post_details_main.this.getAssets(), "AvenirLTStd_Book.otf");
		tv_profession.setTypeface(tf);
		tv_profession_below.setTypeface(tf);
		tv_gender.setTypeface(tf);
		tv_phone.setTypeface(tf);
		tv_from.setTypeface(tf);
		tv_to.setTypeface(tf);
		tv_category.setTypeface(tf);
		tv_route.setTypeface(tf);
		tv_seats.setTypeface(tf);
		tv_departure.setTypeface(tf);
		tv_returning.setTypeface(tf);
		tv_description.setTypeface(tf);
		tv_company_name.setTypeface(tf);
		tv_address.setTypeface(tf);

		tv_profession_below1.setTypeface(tf);
		tv_from1.setTypeface(tf);
		tv_to1.setTypeface(tf);
		tv_route1.setTypeface(tf);
		tv_description1.setTypeface(tf);
		tv_company_name1.setTypeface(tf);
		tv_address1.setTypeface(tf);

		Typeface tf1 = Typeface.createFromAsset(post_details_main.this.getAssets(), "AvenirLTStd-Heavy.otf");
		tv_by.setTypeface(tf1);
		TextView tv = (TextView) findViewById(R.id.tvmy_userName);
		TextView tv1 = (TextView) findViewById(R.id.tvmy_com_add);
		TextView tv2 = (TextView) findViewById(R.id.tvmy_userProfession);
		TextView tv3 = (TextView) findViewById(R.id.tvmy_usergender);
		TextView tv4 = (TextView) findViewById(R.id.tvmy_userphone);
		TextView tv5 = (TextView) findViewById(R.id.tvmy_userFrom);
		TextView tv6 = (TextView) findViewById(R.id.tvmy_userTo);
		TextView tv7 = (TextView) findViewById(R.id.tvmy_usercategory);
		TextView tv8 = (TextView) findViewById(R.id.tvmy_userDescription);
		TextView tv9 = (TextView) findViewById(R.id.tvmy_userDeparture);
		TextView tv10 = (TextView) findViewById(R.id.tvmy_userReturning);
		TextView tv11 = (TextView) findViewById(R.id.tvmy_userSeats);
		TextView tv12 = (TextView) findViewById(R.id.tvmy_userRoute);
		tv.setTypeface(tf1);
		tv1.setTypeface(tf1);
		tv2.setTypeface(tf1);
		tv3.setTypeface(tf1);
		tv4.setTypeface(tf1);
		tv5.setTypeface(tf1);
		tv6.setTypeface(tf1);
		tv7.setTypeface(tf1);
		tv8.setTypeface(tf1);
		tv9.setTypeface(tf1);
		tv10.setTypeface(tf1);
		tv11.setTypeface(tf1);
		tv12.setTypeface(tf1);

		TextView t = (TextView) findViewById(R.id.tv_header_item);
		t.setTypeface(tf1);
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

	public class getSMSURL extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler sh = new ServiceHandler();
			String url1 = url_sms.replace(" ", "-");
			// Log.d("log", "url_sms> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			// Log.d("log", "sms_body >" + jsonStr);
			if (jsonStr != null) {
				try {
					JSONObject jsonObject = new JSONObject(jsonStr);

					sms_body = jsonObject.getString("id").toString();
					// Log.d("log", "doInBackgraound short_url >" + sms_body);
					// Log.d("log", "City: " + city_i + " Phone: " + data[4] + "
					// Title" + data[0] + " City Data: " + data[16]);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			} else {
				// Log.e("ServiceHandler", "Couldn't get any data from the
				// url");
			}
			return sms_body;
		}

		protected void onPostExecute(String result) {
			sms_body = result;
			p_bar.dismiss();
			// Log.d("log", "OnPostExecute short_url >" + sms_body);
			sendSMS();
		}

	}

	public void dialog(String name, int method) {
		final String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
		Typeface tf = Typeface.createFromAsset(post_details_main.this.getAssets(), "AvenirLTStd_Book.otf");
		final SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		if (method == 1 || method == 2) {
			dialog.setContentView(R.layout.send_msg);
			RelativeLayout l1_name = (RelativeLayout) dialog.findViewById(R.id.l1_your_name);
			RelativeLayout l1_email = (RelativeLayout) dialog.findViewById(R.id.l1_your_email);
			final RelativeLayout l1_phone = (RelativeLayout) dialog.findViewById(R.id.l1_your_phone);
			final EditText your_name = (EditText) dialog.findViewById(R.id.et_your_name);
			final EditText your_email = (EditText) dialog.findViewById(R.id.et_your_email);
			final EditText your_phone = (EditText) dialog.findViewById(R.id.et_your_phone);
			final EditText your_msg = (EditText) dialog.findViewById(R.id.et_your_sms);
			final RelativeLayout b_send_msg = (RelativeLayout) dialog.findViewById(R.id.b_send_msg);
			final RelativeLayout b_back_msg = (RelativeLayout) dialog.findViewById(R.id.b_back_msg);
			final TextView t1 = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
			final TextView t2 = (TextView) dialog.findViewById(R.id.tv_2_send_msg);
			final TextView t3 = (TextView) dialog.findViewById(R.id.tv_back_send_msg);
			final TextView t4 = (TextView) dialog.findViewById(R.id.tv_send_send_msg);
			your_name.setTypeface(tf);
			your_email.setTypeface(tf);
			your_phone.setTypeface(tf);
			your_msg.setTypeface(tf);
			t1.setTypeface(tf);
			t2.setTypeface(tf);
			t3.setTypeface(tf);
			t4.setTypeface(tf);
			final ImageView iv1 = (ImageView) dialog.findViewById(R.id.iv_send_msg);
			final RelativeLayout close = (RelativeLayout) dialog.findViewById(R.id.l_close);

			close.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			b_back_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			if (name.equals("Send Email")) {
				l1_phone.setVisibility(View.GONE);
				iv1.setImageResource(R.drawable.heading_mail_icon);
				t2.setText("Message");
				t4.setText("SEND MESSAGE");
			} else {
				iv1.setImageResource(R.drawable.heading_phone_icon);
				t2.setText("SMS");
				t4.setText("SEND SMS");
			}
			if (task.getString("name", null) != null) {
				l1_name.setVisibility(View.GONE);
				l1_email.setVisibility(View.GONE);
				l1_phone.setVisibility(View.GONE);
			}

			b_send_msg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// do your work here
					String name = null, email = null, phone = null, msg;

					if (task.getString("name", null) != null) {
						String sender_id = task.getString("user_id", null);
						if (your_msg.getText().toString().length() != 0) {
							name = task.getString("name", null);
							email = task.getString("email", null);
							phone = task.getString("phone", null);
							if (name.toUpperCase().equals("SEND EMAIL")) {
								String[] data_ar = { "", post_id, data[0].replaceAll("'", "''"),
										data[15].replaceAll("'", "''"), sender_id, name.replaceAll("'", "''"),
										email.replaceAll("'", "''"), phone.replaceAll("'", "''"), receiver_id,
										your_msg.getText().toString().replaceAll("'", "''"), "0", timeStamp, "0", "0",
										"0" };

								dbHelper.insert_response(data_ar);
							} else {
								String[] data_ar = { "", post_id, data[0].replaceAll("'", "''"),
										data[15].replaceAll("'", "''"), sender_id, name.replaceAll("'", "''"),
										email.replaceAll("'", "''"), phone.replaceAll("'", "''"), receiver_id,
										your_msg.getText().toString().replaceAll("'", "''"), "0", timeStamp, "1", "0",
										"0" };

								dbHelper.insert_response(data_ar);
							}

							Toast.makeText(getApplication(), "Message Sent", Toast.LENGTH_LONG).show();
							dialog.dismiss();
						} else {
							Toast.makeText(getApplication(), "Write your message", Toast.LENGTH_LONG).show();
						}
					} else {
						if (your_name.getText().toString().length() < 1) {
							Toast.makeText(getApplication(), "Please fill your name", Toast.LENGTH_LONG).show();
						} else if (!(new EmailAndPhoneChecker().validEmail(your_email.getText().toString()))) {
							Toast.makeText(getApplication(), "Please write a valid email address", Toast.LENGTH_LONG)
									.show();
						} else if (l1_phone.getVisibility() == View.VISIBLE
								&& (your_phone.getText().toString().length() < 9
										|| your_phone.getText().toString().length() > 14)) {
							Toast.makeText(getApplication(), "Please write a valid Contact No.", Toast.LENGTH_LONG)
									.show();
						} else if (your_msg.getText().toString().length() < 1) {
							Toast.makeText(getApplication(), "Please don't leave message blank.", Toast.LENGTH_LONG)
									.show();
						} else {
							name = your_name.getText().toString();
							email = your_email.getText().toString();
							phone = your_phone.getText().toString();
							if (name.toUpperCase().equals("SEND EMAIL")) {
								String[] data_arr = { "", post_id, data[0].replaceAll("'", "''"),
										data[15].replaceAll("'", "''"), "0", name.replaceAll("'", "''"),
										email.replaceAll("'", "''"), "", receiver_id,
										your_msg.getText().toString().replaceAll("'", "''"), "0", timeStamp, "0", "0",
										"0" };
								dbHelper.insert_response(data_arr);
							} else {
								String[] data_arr = { "", post_id, data[0].replaceAll("'", "''"), data[15], "0",
										name.replaceAll("'", "''"), email.replaceAll("'", "''"),
										phone.replaceAll("'", "''"), receiver_id,
										your_msg.getText().toString().replaceAll("'", "''"), "0", timeStamp, "1", "0",
										"0" };

								dbHelper.insert_response(data_arr);
							}
							Toast.makeText(getApplication(), "Message sent", Toast.LENGTH_LONG).show();
							dialog.dismiss();
						}
					}
				}
			});
			if (name.toUpperCase().equals("EMAIL")) {
				l1_phone.setVisibility(View.GONE);
			}
		}
		dialog.show();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		// bedMenuItem = menu.findItem(R.id.menu_login);
		// SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
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

		// Intent i;
		// Handle action buttons
		// switch (item.getItemId()) {
		// case R.id.menu_add_new_list:
		// if (task.getString("user_id", null) != null) {
		// i = new Intent(getBaseContext(), create_activity.class);
		//
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
		// finish();
		//
		// return super.onOptionsItemSelected(item);
		//
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
		Typeface tf = Typeface.createFromAsset(post_details_main.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(post_details_main.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(post_details_main.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(post_details_main.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				dialog.dismiss();
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

	private void sendSMS() {
		ad_title = data[0].replace(" ", "-");
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least
																	// KitKat
		{
			String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need
																						// to
																						// change
																						// the
																						// build
																						// to
																						// API
																						// 19

			Intent sendIntent = new Intent(Intent.ACTION_SEND);
			sendIntent.setType("text/plain");
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"Hi!\nI'm interested in your ad and look forward to hearing from you.\nThis message is sent for your ad "
							+ sms_body + " at www.searchcarpools.com");
			// Log.i("log", "Send SMS Method1: short_url >" + sms_body);
			sendIntent.putExtra("address", data[4]);

			if (defaultSmsPackageName != null)// Can be null in case that there
												// is no default, then the user
												// would be able to choose
			// any app that support this intent.
			{
				sendIntent.setPackage(defaultSmsPackageName);
			}
			startActivity(sendIntent);

		} else // For early versions, do what worked for you before.
		{
			Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
			smsIntent.setType("vnd.android-dir/mms-sms");
			smsIntent.putExtra("address", data[4]);
			smsIntent.putExtra("sms_body",
					"Hi!\nI'm interested in your ad and look forward to hearing from you.\nThis message is sent for your ad "
							+ sms_body + " at www.searchcarpools.com");
			// Log.d("log", "Send SMS Method2: short_url >" + sms_body);
			startActivity(smsIntent);
		}
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			p_bar = new ProgressDialog(post_details_main.this);
			p_bar.setMessage("Loading. Please wait...");
			p_bar.show();
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

			int i = 0;
			if (result.contentEquals("true")) {
				if (result.contentEquals("true") && sms_is_sent == true) {
					new getSMSURL().execute();
					sms_is_sent = false;
					i++;
				} else {
					// Toast.makeText(post_details_main.this, "No Internet
					// Connnection", Toast.LENGTH_SHORT).show();
					// i++;
				}
				if (result.contentEquals("true") && (i == 0)) {
					accessWebService();
				} else {
					// Toast.makeText(post_details_main.this, "No Internet
					// Connnection", Toast.LENGTH_SHORT).show();
				}
			} else {
				p_bar.dismiss();
				Toast.makeText(post_details_main.this, "Server Not Reachable", Toast.LENGTH_SHORT).show();
				post_details_main.this.finish();
			}
			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}
