package com.example.search.car.pools;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;
import custom.list.database_method;
import dash.board.dashboard_main;

public class create_activity extends Activity implements OnClickListener {
	RadioGroup rg_carpool, rg_carpool_type;
	RelativeLayout r1, r2, r3, r4, r5, r6;
	TextView tv_sec_city, tv_sec_company, tv_sec_category, tv_sec_seats, tv_sec_departure, tv_sec_return, tv1, tv2, tv3,
			tv4, tv5, tv6, tv7, tv8;
	EditText et_title, et_description, et_form, et_to, et_route;
	Button b_create_list;
	String s_carpool = "Seeker", s_carpool_type = "0", company_id = "0";
	LinearLayout l1_company;
	SharedPreferences task;

	ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11, iv12, iv13;
	ImageView iv02, iv04, iv05, iv011, iv012, iv013;
	MenuItem bedMenuItem;

	// search dialog widgets
	EditText et_from, et_to_dialog;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	RelativeLayout close;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_list);
		task = getSharedPreferences("user", MODE_PRIVATE);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// Drawable d = ContextCompat.getDrawable(create_activity.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// prevent
																							// keyboard
																							// from
																							// popping
		tv_sec_city = (TextView) findViewById(R.id.tv_c_sec_city);
		tv_sec_company = (TextView) findViewById(R.id.tv_sec_company);
		tv_sec_category = (TextView) findViewById(R.id.tv_sec_category);
		tv_sec_seats = (TextView) findViewById(R.id.tv_sec_seats);
		tv_sec_departure = (TextView) findViewById(R.id.tv_sec_departure);
		tv_sec_return = (TextView) findViewById(R.id.tv_sec_return);
		et_title = (EditText) findViewById(R.id.et_title);
		et_description = (EditText) findViewById(R.id.et_description);
		et_form = (EditText) findViewById(R.id.et_from);
		et_to = (EditText) findViewById(R.id.et_to);
		et_route = (EditText) findViewById(R.id.et_route);
		rg_carpool = (RadioGroup) findViewById(R.id.rg_carpool);
		rg_carpool_type = (RadioGroup) findViewById(R.id.rg_carpool_type);
		b_create_list = (Button) findViewById(R.id.b_company_register);
		l1_company = (LinearLayout) findViewById(R.id.l1_select_company);
		b_create_list.setOnClickListener(this);
		tv_sec_city.setOnClickListener(this);
		tv_sec_company.setOnClickListener(this);
		tv_sec_seats.setOnClickListener(this);
		tv_sec_departure.setOnClickListener(this);
		tv_sec_return.setOnClickListener(this);
		tv_sec_category.setOnClickListener(this);
		l1_company.setVisibility(View.GONE);

		Typeface tf = Typeface.createFromAsset(create_activity.this.getAssets(), "AvenirLTStd-Heavy.otf");
		tv1 = (TextView) findViewById(R.id.tv_1_create_list);
		tv2 = (TextView) findViewById(R.id.tv_2_create_list);
		tv3 = (TextView) findViewById(R.id.tv_3_create_list);
		tv4 = (TextView) findViewById(R.id.tv_4_create_list);
		tv5 = (TextView) findViewById(R.id.tv_5_create_list);
		tv6 = (TextView) findViewById(R.id.tv_6_create_list);
		tv7 = (TextView) findViewById(R.id.tv_7_create_list);
		tv8 = (TextView) findViewById(R.id.tv_8_create_list);

		iv1 = (ImageView) findViewById(R.id.iv_1);
		iv2 = (ImageView) findViewById(R.id.iv_2);
		iv3 = (ImageView) findViewById(R.id.iv_3);
		iv4 = (ImageView) findViewById(R.id.iv_4);
		iv5 = (ImageView) findViewById(R.id.iv_5);
		iv6 = (ImageView) findViewById(R.id.iv_6);
		iv7 = (ImageView) findViewById(R.id.iv_7);
		iv8 = (ImageView) findViewById(R.id.iv_8);
		iv9 = (ImageView) findViewById(R.id.iv_9);
		iv10 = (ImageView) findViewById(R.id.iv_10);
		iv11 = (ImageView) findViewById(R.id.iv_11);
		iv12 = (ImageView) findViewById(R.id.iv_12);
		iv13 = (ImageView) findViewById(R.id.iv_13);

		iv02 = (ImageView) findViewById(R.id.iv_02);
		iv04 = (ImageView) findViewById(R.id.iv_04);
		iv05 = (ImageView) findViewById(R.id.iv_05);
		iv011 = (ImageView) findViewById(R.id.iv_011);
		iv012 = (ImageView) findViewById(R.id.iv_012);
		iv013 = (ImageView) findViewById(R.id.iv_013);
		RadioButton rb1 = (RadioButton) findViewById(R.id.radio0);
		RadioButton rb2 = (RadioButton) findViewById(R.id.radio1);
		RadioButton rb3 = (RadioButton) findViewById(R.id.radio3);
		RadioButton rb4 = (RadioButton) findViewById(R.id.radio4);
		r1 = (RelativeLayout) findViewById(R.id.l_city_create_list);
		r2 = (RelativeLayout) findViewById(R.id.l_company_create_list);
		r3 = (RelativeLayout) findViewById(R.id.l_category_create_list);
		r4 = (RelativeLayout) findViewById(R.id.l_seats_create_list);
		r5 = (RelativeLayout) findViewById(R.id.l_departure_create_list);
		r6 = (RelativeLayout) findViewById(R.id.l_return_create_list);
		r1.setOnClickListener(this);
		r2.setOnClickListener(this);
		r3.setOnClickListener(this);
		r4.setOnClickListener(this);
		r5.setOnClickListener(this);
		r6.setOnClickListener(this);

		ImageView iv_A = (ImageView) findViewById(R.id.iv_8_A);
		SVG svg_a = SVGParser.getSVGFromResource(getResources(), R.raw.location_a);
		iv_A.setImageDrawable(svg_a.createPictureDrawable());

		ImageView iv_B = (ImageView) findViewById(R.id.iv_9_B);
		SVG svg_b = SVGParser.getSVGFromResource(getResources(), R.raw.location_b);
		iv_B.setImageDrawable(svg_b.createPictureDrawable());

		rg_carpool.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio0) {
					s_carpool = "Seeker";
				} else if (checkedId == R.id.radio1) {
					s_carpool = "Provider";
				}
			}
		});
		rg_carpool_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio3) {
					s_carpool_type = "0";
					l1_company.setVisibility(View.GONE);
					tv_sec_company.setText("Select Company");
					company_id = "0";
				} else if (checkedId == R.id.radio4) {
					s_carpool_type = "1";
					l1_company.setVisibility(View.VISIBLE);
				}
			}
		});

		et_title.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv6.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv6.setImageResource(R.drawable.grey_circle);

			}
		});

		et_description.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv7.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv7.setImageResource(R.drawable.grey_circle);

			}
		});

		et_form.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv8.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv8.setImageResource(R.drawable.grey_circle);

			}
		});

		et_to.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv9.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv9.setImageResource(R.drawable.grey_circle);

			}
		});

		et_route.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv10.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv10.setImageResource(R.drawable.grey_circle);

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

		SVG svg_back = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_back);
		SVG svg_handle = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_menu);
		SVG svg_logo = SVGParser.getSVGFromResource(getResources(), R.raw.logo_splash);
		SVG svg_search = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_search);
		SVG svg_menu = SVGParser.getSVGFromResource(getResources(), R.raw.actionbar_triple_dot);

		l_back = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_back);
		l_handle = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_handle);
		l_logo = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_logo);
		l_nav_search = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_search);
		l_menu = (LinearLayout) mCustomView.findViewById(R.id.l_navigation_menu);

		ib_back.setImageDrawable(svg_back.createPictureDrawable());
		ib_handle.setImageDrawable(svg_handle.createPictureDrawable());
		ib_logo.setImageDrawable(svg_logo.createPictureDrawable());
		ib_search.setImageDrawable(svg_search.createPictureDrawable());
		ib_menu.setImageDrawable(svg_menu.createPictureDrawable());
		d1 = ContextCompat.getDrawable(create_activity.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(create_activity.this, R.drawable.touch_blue_back_color);
		l_back.setBackground(d2);
		l_handle.setBackground(d2);
		l_logo.setBackground(d2);
		l_nav_search.setBackground(d2);
		l_menu.setBackground(d2);

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
		// popup = new PopupMenu(welcome.this, ib_menu);
		// ib_menu.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed here it is 2
		String message = data.getStringExtra("company_name");
		if (resultCode == 2) {
			company_id = data.getStringExtra("company_id");
			tv_sec_company.setText(message);
			iv4.setVisibility(View.VISIBLE);
			iv04.setVisibility(View.INVISIBLE);
			tv4.setVisibility(View.VISIBLE);
		} else if (resultCode == 0) {
			company_id = data.getStringExtra("company_id");
			tv_sec_company.setText(message);
			iv4.setVisibility(View.INVISIBLE);
			iv04.setVisibility(View.VISIBLE);
			tv4.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(tv_sec_company) || v.equals(r2)) {
			if (tv_sec_city.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
				Toast.makeText(this, "First Select City", Toast.LENGTH_LONG).show();
			} else {
				String city = new database_method().get_data(this,
						"select id from city where city_name='" + tv_sec_city.getText().toString() + "'");
				Intent intent = new Intent(create_activity.this, company_list.class);
				intent.putExtra("city_id", city);
				// startActivity(intent);
				startActivityForResult(intent, 2);
			}
		}
		if (v.equals(tv_sec_city) || v.equals(r1)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, tv_sec_city);
		}
		if (v.equals(tv_sec_category) || v.equals(r3)) {
			String[] category = { "Carpool", "Cab", "Rideshare" };
			dialog("Category", category, tv_sec_category);

		}
		if (v.equals(tv_sec_departure) || v.equals(r5)) {
			String[] departure = new String[23];
			for (int i = 0; i <= 11; i++) {
				departure[i] = String.valueOf(i + " - " + (i + 1) + " am");
			}
			for (int i = 1; i <= 11; i++) {
				departure[i + 11] = String.valueOf(i + " - " + (i + 1) + " pm");
			}
			dialog("Departure", departure, tv_sec_departure);

		}
		if (v.equals(tv_sec_return) || v.equals(r6)) {
			String[] departure = new String[23];
			for (int i = 0; i <= 11; i++) {
				departure[i] = String.valueOf(i + " - " + (i + 1) + " am");
			}
			for (int i = 1; i <= 11; i++) {
				departure[i + 11] = String.valueOf(i + " - " + (i + 1) + " pm");
			}
			dialog("Return", departure, tv_sec_return);

		}
		if (v.equals(tv_sec_seats) || v.equals(r4)) {
			String[] seats = new String[10];
			for (int i = 0; i <= 9; i++) {
				seats[i] = String.valueOf(i + 1);
			}
			dialog("Seats", seats, tv_sec_seats);
		}
		if (v.equals(b_create_list)) {
			create_list();
			// Toast.makeText(getActivity(), et_title.getText().toString(),
			// Toast.LENGTH_LONG).show();
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
				Toast.makeText(create_activity.this, "First Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(create_activity.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_url", " ");
				startActivity(i);
				promptsView.dismiss();
				create_activity.this.finish();
			}
		} else if (v.equals(l_nav_search) || v.equals(ib_search)) {
			final int DELAY = 200;
			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_nav_search.setBackground(a);
			a.start();
			showSearchDialog();
		} else if (v.equals(l_back) || v.equals(ib_back)) {
			final int DELAY = 200;
			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_back.setBackground(a);
			a.start();
			finish();
		} else if (v.equals(l_logo) || v.equals(ib_logo)) {
			final int DELAY = 200;
			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_logo.setBackground(a);
			a.start();
			Intent i = new Intent(create_activity.this, welcome.class);
			startActivity(i);
		} else if (v.equals(l_menu) || v.equals(ib_menu)) {
			final int DELAY = 200;
			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_menu.setBackground(a);
			a.start();
			final PopupMenu popup = new PopupMenu(create_activity.this, v);
			popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

			bedMenuItem = popup.getMenu().findItem(R.id.menu_login).setTitle("Logout");
			final SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);

			popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					// TODO Auto-generated method stub
					int id = item.getItemId();
					Intent i;
					switch (id) {
					case R.id.menu_add_new_list:
						// nothing happens here
						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(create_activity.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(create_activity.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_login:
						if (bedMenuItem.getTitle().equals("Logout")) {
							SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							create_activity.this.finish();
							// bedMenuItem.setTitle("Login/Register");

							// Intent i_user = new Intent(getBaseContext(),
							// user_login.class);
							//
							// startActivity(i_user);
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

	public void create_list() {
		if (tv_sec_city.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(this, "Select City", Toast.LENGTH_LONG).show();
		} else if (tv_sec_company.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")
				&& s_carpool_type.equals("1")) {
			Toast.makeText(this, "Select Company", Toast.LENGTH_LONG).show();
		} else if (tv_sec_category.getText().toString().toUpperCase().equals("SELECT CATEGORY")) {
			Toast.makeText(this, "Select category first", Toast.LENGTH_LONG).show();
		} else if (et_title.getText().toString().length() < 1) {
			et_title.setError("Title cannot be blank");
			Toast.makeText(this, "Title Cannot contain special charaters", Toast.LENGTH_LONG).show();
		} else if (et_description.getText().toString().length() < 1) {
			et_description.setError("Description cannot be blank");
			Toast.makeText(this, "Enter Description", Toast.LENGTH_LONG).show();
		} else if (et_form.getText().toString().length() < 1) {
			et_form.setError("From/ Source cannot be blank");
			Toast.makeText(this, "Enter From/ Source", Toast.LENGTH_LONG).show();
		} else if (et_to.getText().toString().length() < 1) {
			et_to.setError("To/ Destination cannot be blank");
			Toast.makeText(this, "Enter To/ Destination", Toast.LENGTH_LONG).show();
		} else if (et_route.getText().toString().length() < 1) {
			et_route.setError("Route cannot be blank");
			Toast.makeText(this, "Enter Route", Toast.LENGTH_LONG).show();
		} else if (tv_sec_seats.getText().toString().toUpperCase().equals("SELECT SEATS")) {
			Toast.makeText(this, "Select Seats first", Toast.LENGTH_LONG).show();
		} else if (tv_sec_departure.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(this, "Select Departure first", Toast.LENGTH_LONG).show();
		} else if (tv_sec_return.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(this, "Select Return first", Toast.LENGTH_LONG).show();
		} else {

			if (task.getString("user_id", null) != null) {
				try {
					String type = new database_method().get_data(this,
							"select type_id from type where type_name='" + s_carpool + "'");
					String city = new database_method().get_data(this,
							"select id from city where city_name='" + tv_sec_city.getText().toString() + "'");
					String category = new database_method().get_data(this,
							"select cat_id from category where cat_name='" + tv_sec_category.getText().toString()
									+ "'");
					String timeStamp = "Draft";// new
												// SimpleDateFormat("yyyy-MM-dd",
												// Locale.getDefault()).format(new
												// Date());
					// id,user_id,type,city,company_id,fro_m,t_o,route,seats,departure_time,return_trip,return_time,title,desc,date,hits,enable,sync
					DataBaseHelper db = new DataBaseHelper(this);
					String data[] = { "00", task.getString("user_id", null), type, category, city, company_id,
							et_form.getText().toString().replaceAll("'", "''"), et_to.getText().toString().replaceAll("'", "''"), 
							et_route.getText().toString().replaceAll("'", "''"),
							tv_sec_seats.getText().toString(), tv_sec_departure.getText().toString(), "0",
							tv_sec_return.getText().toString(), et_title.getText().toString().replaceAll("'", "''"),
							et_description.getText().toString().replaceAll("'", "''"), timeStamp, "0", "1", "0" };

					db.insert_ad(data);
					Toast.makeText(this, "List Created", Toast.LENGTH_LONG).show();
					set_blank();
					Intent i = new Intent(create_activity.this, dashboard_main.class);
					i.putExtra("edit", "0");
					startActivity(i);
					finish();
				} catch (Exception e) {
					//e.printStackTrace();
					Toast.makeText(this, "Something went wrong.\nPlease try again.", Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(this, "Please Login First", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(create_activity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(create_activity.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(create_activity.this.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				if (tv.equals(tv_sec_city)) {
					iv2.setVisibility(View.VISIBLE);
					iv02.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_category)) {
					iv5.setVisibility(View.VISIBLE);
					iv05.setVisibility(View.INVISIBLE);
					tv5.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_seats)) {
					iv11.setVisibility(View.VISIBLE);
					iv011.setVisibility(View.INVISIBLE);
					tv6.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_departure)) {
					iv12.setVisibility(View.VISIBLE);
					iv012.setVisibility(View.INVISIBLE);
					tv7.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_return)) {
					iv13.setVisibility(View.VISIBLE);
					iv013.setVisibility(View.INVISIBLE);
					tv8.setVisibility(View.VISIBLE);
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
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		// bedMenuItem = menu.findItem(R.id.menu_login);
		// SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
		// if (task.getString("user_id", null) != null) {
		// bedMenuItem.setTitle("Logout");
		// } else {
		// bedMenuItem.setTitle("Login/Register");
		// }
		// menu.findItem(R.id.menu_add_new_list).setTitle("Home");
		// menu.findItem(R.id.menu_add_new_list).setVisible(false);
		return super.onCreateOptionsMenu(menu);
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

		Intent i;
		// Handle action buttons
		switch (item.getItemId()) {
		// case R.id.menu_add_new_list:
		//
		// i = new Intent(getBaseContext(), welcome.class);
		// i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(i);
		// return true;
		case R.id.menu_dashboard:
			if (task.getString("user_id", null) != null) {
				i = new Intent(this, dashboard_main.class);
				i.putExtra("edit", "12344");
				startActivity(i);
			} else {
				Toast.makeText(this, "Please Login first", Toast.LENGTH_LONG).show();
			}
			return true;
		case R.id.menu_login:
			if (bedMenuItem.getTitle().equals("Logout")) {
				SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
				editor.clear();
				editor.commit();
				create_activity.this.finish();
				// bedMenuItem.setTitle("Login/Register");

				// Intent i_user = new Intent(getBaseContext(),
				// user_login.class);
				//
				// startActivity(i_user);
			}

			return true;

		// case R.id.menu_search:
		// showSearchDialog();
		// return true;

		case android.R.id.home:

			finish();

			return super.onOptionsItemSelected(item);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void set_blank() {
		tv_sec_city.setText("Select City");
		tv_sec_company.setText("Select Company");
		tv_sec_category.setText("Select Category");
		tv_sec_seats.setText("Select Seats");

		tv_sec_departure.setText("Select Departure");
		tv_sec_return.setText("Select Return");
		et_title.setText("");
		et_description.setText("");
		et_form.setText("");
		et_to.setText("");
		et_route.setText("");
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
		et_to_dialog = (EditText) promptsView.findViewById(R.id.et_search_to);
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
		Typeface tf = Typeface.createFromAsset(create_activity.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}
}
