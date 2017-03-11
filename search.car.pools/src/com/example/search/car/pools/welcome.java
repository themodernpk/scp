/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.search.car.pools;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.devin.clm.coustomviews.MyCirclure;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.animation.ValueAnimatorCompat;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

public class welcome extends Activity implements OnClickListener {
	private DrawerLayout mDrawerLayout;
	private RelativeLayout mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	Boolean isInternetPresent = false;
	String userid = "";
	MenuItem bedMenuItem;
	int backButtonCount = 0;
	private SQLiteDatabase database;
	// Connection detector class
	SharedPreferences task;
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;
	String layout = "", page = "";

	String rem_data = "";
	int frag_id = 0;

	// search dialog widgets
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	RelativeLayout close;

	boolean canExit;
	String frag_tag;

	// for notification
	// private NotificationReceiver nReceiver;
	Menu menu;

	// SLIDING MENU OPTIONS
	RelativeLayout rlSearch, rlCities, rlDashboard, rlLogin; //rlEditProfile,
	LinearLayout rlProfile, rlProfile_login;
	ImageView iv_search, iv_cities, iv_dashboard, iv_login; //iv_edit_profile, 
	SVG svg_search, svg_cities, svg_dashboard, svg_edit_profile, svg_login, svg_logout;
	RelativeLayout l_search, l_cities, l_dashboard, l_edit_profile;
	TextView login, profile_login, tv_profile_name, tv_profile_email;
	MyCirclure avatar;

	Typeface tf;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			frag_id = bundle.getInt("frag_id");
		}
		tf = Typeface.createFromAsset(welcome.this.getAssets(), "AvenirLTStd_Book.otf");
		// for notification
		// nReceiver = new NotificationReceiver();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("com.kpbird.nlsexample.NOTIFICATION_LISTENER_EXAMPLE");
		// registerReceiver(nReceiver, filter);

		task = getSharedPreferences("user", MODE_PRIVATE);
		SharedPreferences sharedpreferences = this.getPreferences(0);
		Editor editor = sharedpreferences.edit();
		editor.putString("username", userid);

		editor.commit();
		initMenu();

		// mTitle = mDrawerTitle = "Search Carpool";
		mPlanetTitles = getResources().getStringArray(R.array.menu_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (RelativeLayout) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer
		// opens
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// mDrawerList.setAdapter(new ArrayAdapter<String>(this,
		// R.layout.drawer_list_item, mPlanetTitles));
		mDrawerList.setOnClickListener(this);

		// enable ActionBar app icon to behave as action to toggle nav drawer

		// getActionBar().setTitle("");
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		// Drawable d = ContextCompat.getDrawable(welcome.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);

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
		
		d1 = ContextCompat.getDrawable(welcome.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(welcome.this, R.drawable.touch_blue_back_color);
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
		
		// l_handle.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if (event.getAction() == MotionEvent.ACTION_DOWN){
		// v.setBackgroundResource(R.drawable.touch_ripple_back_color);
		// } else if (event.getAction() == MotionEvent.ACTION_UP){
		// v.setBackgroundResource(R.drawable.touch_blue_back_color);
		// }
		// return true;
		// }
		// });
		// ib_handle.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// final int DELAY = 200;
		// ColorDrawable f = new ColorDrawable(Color.parseColor("#0087ca"));
		// ColorDrawable f1 = new ColorDrawable(Color.parseColor("#3398ca"));
		// Drawable d1 =
		// ContextCompat.getDrawable(welcome.this,R.drawable.touch_ripple_back_color);
		// Drawable d2 =
		// ContextCompat.getDrawable(welcome.this,R.drawable.touch_blue_back_color);
		// AnimationDrawable a = new AnimationDrawable();
		// a.addFrame(f1, DELAY);
		// a.addFrame(f, DELAY);
		// a.setOneShot(true);
		// l_handle.setBackground(a);
		// a.start();
		// if (mDrawerLayout.isDrawerOpen(GravityCompat.START) &&
		// event.getAction() == MotionEvent.ACTION_UP) {
		// mDrawerLayout.closeDrawer(mDrawerList);
		// } else {
		// mDrawerLayout.openDrawer(mDrawerList);
		// }
		//
		// return true;
		// }
		// });

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
		
		// int colorFrom = Color.parseColor("#0087ca");
		// int colorTo = Color.parseColor("#3398ca");
		// ValueAnimator colorAnimation = ValueAnimator.ofObject(new
		// ArgbEvaluator(), colorFrom,colorTo);
		// colorAnimation.addUpdateListener(new AnimatorUpdateListener() {
		//
		// @Override
		// public void onAnimationUpdate(ValueAnimator animator) {
		// // TODO Auto-generated method stub
		// l_handle.setBackgroundColor((Integer) animator.getAnimatedValue());
		// }
		// });

//		l_handle.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
		l_back.setVisibility(View.GONE);
		// popup = new PopupMenu(welcome.this, ib_menu);
//		l_menu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		l_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
				mDrawerLayout, /* DrawerLayout object */
				R.drawable.ic_drawerxx, /*
										 * nav drawer image to replace 'Up'
										 * caret
										 */
				R.string.drawer_open, /*
										 * "open drawer" description for
										 * accessibility
										 */
				R.string.drawer_close /*
										 * "close drawer" description for
										 * accessibility
										 */
		) {
			public void onDrawerClosed(View view) {
				// getActionBar().setTitle("Search Carpool");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}

			public void onDrawerOpened(View drawerView) {
				// getActionBar().setTitle("Search Carpool");
				invalidateOptionsMenu(); // creates call to
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			selectItem(frag_id);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, menu);
		// this.menu = menu;
		//
		// bedMenuItem = popup.getMenu().findItem(R.id.menu_login);
		// SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
		// if (task.getString("user_id", null) != null) {
		// bedMenuItem.setTitle("Logout");
		// login.setText("Logout");
		// iv_login.setImageDrawable(svg_logout.createPictureDrawable());
		// rlProfile.setVisibility(View.VISIBLE);
		// rlProfile_login.setVisibility(View.GONE);
		// rlDashboard.setVisibility(View.VISIBLE);
		// rlEditProfile.setVisibility(View.VISIBLE);
		// set_data();
		// } else {
		// bedMenuItem.setTitle("Login/Register");
		// login.setText("Login");
		// iv_login.setImageDrawable(svg_login.createPictureDrawable());
		// rlDashboard.setVisibility(View.GONE);
		// rlEditProfile.setVisibility(View.GONE);
		// rlProfile.setVisibility(View.GONE);
		// rlProfile_login.setVisibility(View.VISIBLE);
		// }

		return super.onCreateOptionsMenu(menu);
	}

	/* Called whenever we call invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.menu_add_new_list).setVisible(!(task.getString("user_id",
		// null) == null));
		// menu.findItem(R.id.menu_dashboard).setVisible(!(task.getString("user_id",
		// null) == null));
		return super.onPrepareOptionsMenu(menu);
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
		if (task.getString("user_id", null) != null) {
			// bedMenuItem.setTitle("Logout");
			login.setText("Logout");
			iv_login.setImageDrawable(svg_logout.createPictureDrawable());
			rlProfile.setVisibility(View.VISIBLE);
			rlProfile_login.setVisibility(View.GONE);
			rlDashboard.setVisibility(View.VISIBLE);
//			rlEditProfile.setVisibility(View.VISIBLE);
			set_data();
		} else {
			// bedMenuItem.setTitle("Login/Register");
			login.setText("Login");
			rlDashboard.setVisibility(View.GONE);
//			rlEditProfile.setVisibility(View.GONE);
			iv_login.setImageDrawable(svg_login.createPictureDrawable());
			rlProfile.setVisibility(View.GONE);
			rlProfile_login.setVisibility(View.VISIBLE);

		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		Intent i;
		// Handle action buttons
		switch (item.getItemId()) {
		case R.id.menu_add_new_list:
			if (task.getString("user_id", null) != null) {
				i = new Intent(welcome.this, create_activity.class);
				startActivity(i);
			} else {
				Toast.makeText(welcome.this, "Login first", Toast.LENGTH_SHORT).show();
			}

			return true;
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
			// if (bedMenuItem.getTitle().equals("Logout")) {
			// SharedPreferences.Editor editor = getSharedPreferences("user",
			// MODE_PRIVATE).edit();
			// editor.clear();
			// editor.commit();
			// //bedMenuItem.setTitle("Login/Register");
			// login.setText("Login");
			// rlDashboard.setVisibility(View.GONE);
			// rlEditProfile.setVisibility(View.GONE);
			//
			// if (layout.contentEquals("Dashboard") ||
			// frag_tag.contentEquals("Dashboard")){
			// // highlight search menu on slider during on resume
			// // change fragment to search
			// FragmentManager fm = getFragmentManager();
			// FragmentTransaction fragmentTransaction = fm.beginTransaction();
			// fragmentTransaction.replace(R.id.content_frame, new Search());
			// fragmentTransaction.commit();
			// svg_search =
			// SVGParser.getSVGFromResource(welcome.this.getResources(),
			// R.raw.search1);
			// iv_search.setImageDrawable(svg_search.createPictureDrawable());
			// rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
			// l_search.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));
			// }
			//
			// iv_login.setImageDrawable(svg_login.createPictureDrawable());
			// rlProfile.setVisibility(View.GONE);
			// rlProfile_login.setVisibility(View.VISIBLE);
			// if (frag_id == 6) {
			// Intent i_user = new Intent(getBaseContext(), user_login.class);
			// i_user.putExtra("frag_id", frag_id);
			// startActivity(i_user);
			// }
			// } else {
			// i = new Intent(getBaseContext(), user_login.class);
			// i.putExtra("frag_id", frag_id);
			// startActivity(i);
			// }
			return true;
		// case R.id.menu_search:
		// showSearchDialog();
		// return true;

		// case R.id.menu_notification:
		//
		// return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements OnClickListener {
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view, int
		// position, long id) {
		// selectItem(position);
		// frag_id = position;
		// }

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Fragment fragment = null;
			if (v.equals(rlCities)) {
				layout = "Cities";
				fragment = new Cities();
				frag_tag = "Cities";
				svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city1);
				iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
				rlCities.setBackgroundColor(Color.parseColor("#00ca98"));
			} else if (v.equals(rlDashboard)) {
				if (task.getString("user_id", null) != null) {
//					layout = "Dashboard";
//					fragment = new dashboard();
//					frag_tag = "Dashboard";
//					// getActionBar().setTitle("My Profile");
//					svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard1);
//					iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
//					rlDashboard.setBackgroundColor(Color.parseColor("#00ca98"));
					Intent i = new Intent(welcome.this, dashboard_main.class);
					i.putExtra("edit", "1");
					startActivity(i);
				} else {
					Toast.makeText(welcome.this, "Please Login First", Toast.LENGTH_SHORT).show();
				}
			} else if (v.equals(rlSearch)) {
				layout = "Search";
				frag_tag = "Search";
				fragment = new Search();
				svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search1);
				iv_search.setImageDrawable(svg_search.createPictureDrawable());
				rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
			} else {
				layout = "Dashboard";
				fragment = new dashboard();
				frag_tag = "Dashboard";
				// getActionBar().setTitle("My Profile");
			}

			FragmentManager fm = getFragmentManager();
			FragmentTransaction fragmentTransaction = fm.beginTransaction();
			fragmentTransaction.replace(R.id.content_frame, fragment, frag_tag);
			fragmentTransaction.commit();
		}
	}

	private void selectItem(int position) {
		setTitle(mPlanetTitles[position]);

		// update the main content by replacing fragments
		Fragment fragment = null;
		if (mPlanetTitles[position].equals("Cities")) {
			layout = "Cities";
			fragment = new Cities();
			frag_tag = "Cities";

			// } else if (mPlanetTitles[position].equals("Promote Carpooling"))
			// {
			// layout = "Promote Carpooling";
			// fragment = new Promote_Carpooling();
			// frag_tag = "Promote Carpooling";
			// } else if (mPlanetTitles[position].equals("Create Listing")) {
			// layout = "Create Listing";
			// fragment = new Create_Listing();
			// frag_id = 3;
			// frag_tag = "Create Listing";
			// } else if (mPlanetTitles[position].equals("About Us")) {
			// layout = "About Us";
			// fragment = new About_Us();
			//
			// } else if (mPlanetTitles[position].equals("Blog")) {
			// layout = "Blog";
			// fragment = new blog();

		} else if (mPlanetTitles[position].equals("Dashboard")) {
			layout = "Dashboard";
			fragment = new dashboard();
			frag_tag = "Dashboard";
			// getActionBar().setTitle("My Profile");
		} else {
			layout = "Search";
			frag_tag = "Search";
			fragment = new Search();
		}

		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment, frag_tag);
		fragmentTransaction.commit();
		/*
		 * Bundle args = new Bundle();
		 * args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		 * fragment.setArguments(args);
		 * 
		 * FragmentManager fragmentManager = getFragmentManager();
		 * fragmentManager.beginTransaction().replace(R.id.content_frame,
		 * fragment).commit();
		 */
		// update selected item and title, then close the drawer
		// mDrawerList.setItemChecked(position, true);

		mDrawerLayout.closeDrawer(mDrawerList);

	}

	private void initMenu() {

		iv_search = (ImageView) findViewById(R.id.iv_search);
		iv_cities = (ImageView) findViewById(R.id.iv_cities);
		iv_dashboard = (ImageView) findViewById(R.id.iv_dashboard);
//		iv_edit_profile = (ImageView) findViewById(R.id.iv_edit_profile);
		iv_login = (ImageView) findViewById(R.id.iv_login);
		login = (TextView) findViewById(R.id.tv_login);
		profile_login = (TextView) findViewById(R.id.tv_profile_login);
		tv_profile_name = (TextView) findViewById(R.id.tv_profile_name);
		tv_profile_email = (TextView) findViewById(R.id.tv_profile_email);
		avatar = (MyCirclure) findViewById(R.id.avatar);

		svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search1);
		svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city);
		svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard);
		svg_edit_profile = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.edit);
		svg_login = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.login_side_menu);
		svg_logout = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.logout_side_menu);

		iv_search.setImageDrawable(svg_search.createPictureDrawable());
		iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
		iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
//		iv_edit_profile.setImageDrawable(svg_edit_profile.createPictureDrawable());

		rlProfile = (LinearLayout) findViewById(R.id.rlProfile);
		rlProfile_login = (LinearLayout) findViewById(R.id.rlProfile_login);
		rlSearch = (RelativeLayout) findViewById(R.id.rlSearch);
		rlCities = (RelativeLayout) findViewById(R.id.rlCities);
		rlDashboard = (RelativeLayout) findViewById(R.id.rlDashboard);
//		rlEditProfile = (RelativeLayout) findViewById(R.id.rlEditProfile);
		rlLogin = (RelativeLayout) findViewById(R.id.rlLogin);

		l_search = (RelativeLayout) findViewById(R.id.l_search);
		l_cities = (RelativeLayout) findViewById(R.id.l_cities);
		l_dashboard = (RelativeLayout) findViewById(R.id.l_dashboard);
//		l_edit_profile = (RelativeLayout) findViewById(R.id.l_Edit_Profile);

		if (task.getString("user_id", null) != null) {
			iv_login.setImageDrawable(svg_logout.createPictureDrawable());
			rlProfile.setVisibility(View.VISIBLE);
			rlProfile_login.setVisibility(View.GONE);
			rlDashboard.setVisibility(View.GONE);
//			rlEditProfile.setVisibility(View.GONE);
			set_data();
		} else {
			iv_login.setImageDrawable(svg_login.createPictureDrawable());
			rlProfile.setVisibility(View.GONE);
			rlProfile_login.setVisibility(View.VISIBLE);
		}

		rlLogin.setOnClickListener(this);
		rlProfile.setOnClickListener(this);
		rlProfile_login.setOnClickListener(this);
		rlSearch.setOnClickListener(this);
		rlCities.setOnClickListener(this);
		rlDashboard.setOnClickListener(this);
		login.setOnClickListener(this);
		profile_login.setOnClickListener(this);
//		rlEditProfile.setOnClickListener(this);
		rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
		l_search.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		// getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
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
		Typeface tf = Typeface.createFromAsset(welcome.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
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
		} else if (v.equals(rlCities)) {
			layout = "Cities";
			fragment = new Cities();
			frag_tag = "Cities";
			set_fragment(fragment);
			mDrawerLayout.closeDrawer(mDrawerList);
			// getActionBar().setTitle("Cities");
			svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city1);
			iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
			rlCities.setBackgroundColor(Color.parseColor("#00ca98"));
			l_cities.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));

			svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard);
			iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
			rlDashboard.setBackgroundColor(Color.parseColor("#2C3E50"));
			l_dashboard.setBackground(getResources().getDrawable(R.drawable.search_blue));
			svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search);
			iv_search.setImageDrawable(svg_search.createPictureDrawable());
			rlSearch.setBackgroundColor(Color.parseColor("#2C3E50"));
			l_search.setBackground(getResources().getDrawable(R.drawable.search_blue));
		} else if (v.equals(rlDashboard)) {
			if (task.getString("user_id", null) != null) {
//				layout = "Dashboard";
//				fragment = new dashboard();
//				frag_tag = "Dashboard";
//				// getActionBar().setTitle("My Profile");
//				set_fragment(fragment);
//				mDrawerLayout.closeDrawer(mDrawerList);
//				svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard1);
//				iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
//				rlDashboard.setBackgroundColor(Color.parseColor("#00ca98"));
//				l_dashboard.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));
//
//				svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city);
//				iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
//				rlCities.setBackgroundColor(Color.parseColor("#2C3E50"));
//				l_cities.setBackground(getResources().getDrawable(R.drawable.search_blue));
//				svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search);
//				iv_search.setImageDrawable(svg_search.createPictureDrawable());
//				rlSearch.setBackgroundColor(Color.parseColor("#2C3E50"));
//				l_search.setBackground(getResources().getDrawable(R.drawable.search_blue));
				Intent i = new Intent(welcome.this, dashboard_main.class);
				i.putExtra("edit", "1");
				startActivity(i);
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				Toast.makeText(welcome.this, "Please Login First", Toast.LENGTH_SHORT).show();
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		} else if (v.equals(rlSearch)) {
			layout = "Search";
			frag_tag = "Search";
			fragment = new Search();
			set_fragment(fragment);
			mDrawerLayout.closeDrawer(mDrawerList);
			// getActionBar().setTitle("Search");

			svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search1);
			iv_search.setImageDrawable(svg_search.createPictureDrawable());
			rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
			l_search.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));

			svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard);
			iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
			rlDashboard.setBackgroundColor(Color.parseColor("#2C3E50"));
			l_dashboard.setBackground(getResources().getDrawable(R.drawable.search_blue));
			svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city);
			iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
			rlCities.setBackgroundColor(Color.parseColor("#2C3E50"));
			l_cities.setBackground(getResources().getDrawable(R.drawable.search_blue));
		} else if (v.equals(rlProfile_login) || v.equals(profile_login)) {
			if (task.getString("user_id", null) == null) {
				Intent i = new Intent(getBaseContext(), user_login.class);
				i.putExtra("frag_id", frag_id);
				startActivity(i);
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		} 
//		else if (v.equals(rlEditProfile)) {
//			if (task.getString("user_id", null) != null) {
//				Intent i = new Intent(welcome.this, dashboard_main.class);
//				i.putExtra("edit", "3");
//				startActivity(i);
//			} else {
//				Toast.makeText(welcome.this, "Please Login First", Toast.LENGTH_SHORT).show();
//			}
//			mDrawerLayout.closeDrawer(mDrawerList);
//		} 
		else if (v.equals(login) || v.equals(rlLogin)) {
			if (task.getString("user_id", null) != null && login.getText().toString().contentEquals("Logout")) {
				SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
				editor.clear();
				editor.commit();
				login.setText("Login");
				rlDashboard.setVisibility(View.GONE);
//				rlEditProfile.setVisibility(View.GONE);

				if (layout.contentEquals("Dashboard") || frag_tag.contentEquals("Dashboard")) {
					// highlight search menu on slider during on resume
					// change fragment to search
					FragmentManager fm = getFragmentManager();
					FragmentTransaction fragmentTransaction = fm.beginTransaction();
					fragmentTransaction.replace(R.id.content_frame, new Search());
					fragmentTransaction.commit();
					svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search1);
					iv_search.setImageDrawable(svg_search.createPictureDrawable());
					rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
					l_search.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));
				}

				iv_login.setImageDrawable(svg_login.createPictureDrawable());
				rlProfile.setVisibility(View.GONE);
				rlProfile_login.setVisibility(View.VISIBLE);
				if (frag_id == 6) {
					Intent i_user = new Intent(getBaseContext(), user_login.class);
					i_user.putExtra("frag_id", frag_id);
					startActivity(i_user);
				}
			} else {
				Intent i = new Intent(getBaseContext(), user_login.class);
				i.putExtra("frag_id", frag_id);
				startActivity(i);
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(welcome.this, "First Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(welcome.this, search_result.class);
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
		} else if (v.equals(l_nav_search) || v.equals(ib_search)){
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
		} else if (v.equals(l_handle) || v.equals(ib_handle)){
			final int DELAY = 200;
			// ColorDrawable f = new
			// ColorDrawable(Color.parseColor("#0087ca"));
			// ColorDrawable f1 = new
			// ColorDrawable(Color.parseColor("#3398ca"));

			AnimationDrawable a = new AnimationDrawable();
			a.addFrame(d1, DELAY);
			a.addFrame(d2, DELAY);
			a.setOneShot(true);
			l_handle.setBackground(a);
			a.start();
			if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
		} else if (v.equals(l_menu) || v.equals(ib_menu)){
			final int DELAY = 200;
			// for light background
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

			final PopupMenu popup = new PopupMenu(welcome.this, v);
			popup.getMenuInflater().inflate(R.menu.main, popup.getMenu());

			bedMenuItem = popup.getMenu().findItem(R.id.menu_login);
			final SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
			popup.getMenu().findItem(R.id.menu_add_new_list).setVisible(!(task.getString("user_id", null) == null));
			popup.getMenu().findItem(R.id.menu_dashboard).setVisible(!(task.getString("user_id", null) == null));

			if (task.getString("user_id", null) != null) {
				bedMenuItem.setTitle("Logout");
				login.setText("Logout");
				iv_login.setImageDrawable(svg_logout.createPictureDrawable());
				rlProfile.setVisibility(View.VISIBLE);
				rlProfile_login.setVisibility(View.GONE);
				rlDashboard.setVisibility(View.VISIBLE);
//				rlEditProfile.setVisibility(View.VISIBLE);
				set_data();
			} else {
				bedMenuItem.setTitle("Login/Register");
				login.setText("Login");
				iv_login.setImageDrawable(svg_login.createPictureDrawable());
				rlDashboard.setVisibility(View.GONE);
//				rlEditProfile.setVisibility(View.GONE);
				rlProfile.setVisibility(View.GONE);
				rlProfile_login.setVisibility(View.VISIBLE);
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
					switch (id) {
					case R.id.menu_add_new_list:
						if (task.getString("user_id", null) != null) {
							i = new Intent(welcome.this, create_activity.class);
							startActivity(i);
						} else {
							Toast.makeText(welcome.this, "Login first", Toast.LENGTH_SHORT).show();
						}

						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(welcome.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(welcome.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_login:
						if (bedMenuItem.getTitle().equals("Logout")) {
							SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							// bedMenuItem.setTitle("Login/Register");
							login.setText("Login");
							rlDashboard.setVisibility(View.GONE);
//							rlEditProfile.setVisibility(View.GONE);

							if (layout.contentEquals("Dashboard") || frag_tag.contentEquals("Dashboard")) {
								// highlight search menu on slider during on
								// resume
								// change fragment to search
								FragmentManager fm = getFragmentManager();
								FragmentTransaction fragmentTransaction = fm.beginTransaction();
								fragmentTransaction.replace(R.id.content_frame, new Search());
								fragmentTransaction.commit();
								SVG svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(),
										R.raw.search1);
								iv_search.setImageDrawable(svg_search.createPictureDrawable());
								rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
								l_search.setBackground(
										getResources().getDrawable(R.drawable.white_circle_side_menu));
							}

							iv_login.setImageDrawable(svg_login.createPictureDrawable());
							rlProfile.setVisibility(View.GONE);
							rlProfile_login.setVisibility(View.VISIBLE);
							if (frag_id == 6) {
								Intent i_user = new Intent(getBaseContext(), user_login.class);
								i_user.putExtra("frag_id", frag_id);
								startActivity(i_user);
							}
						} else {
							i = new Intent(getBaseContext(), user_login.class);
							i.putExtra("frag_id", frag_id);
							startActivity(i);
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

	private void set_fragment(Fragment fragment) {
		// TODO Auto-generated method stub
		FragmentManager fm = getFragmentManager();
		FragmentTransaction fragmentTransaction = fm.beginTransaction();
		fragmentTransaction.replace(R.id.content_frame, fragment, frag_tag);
		fragmentTransaction.commit();
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(welcome.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(welcome.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(welcome.this.getAssets(), "AvenirLTStd_Book.otf");
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

	public Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			switch (msg.what) {
			case 1:
				canExit = false;
				break;
			default:
				break;
			}
		}
	};

	public void onBackPressed() {
		if (canExit) {
			super.onBackPressed();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			// Fragment fm = getFragmentManager().findFragmentByTag("Search");
			if (frag_tag != "Search") {
				svg_search = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.search1);
				iv_search.setImageDrawable(svg_search.createPictureDrawable());
				rlSearch.setBackgroundColor(Color.parseColor("#00ca98"));
				l_search.setBackground(getResources().getDrawable(R.drawable.white_circle_side_menu));

				svg_dashboard = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.dashboard);
				iv_dashboard.setImageDrawable(svg_dashboard.createPictureDrawable());
				rlDashboard.setBackgroundColor(Color.parseColor("#2C3E50"));
				l_dashboard.setBackground(getResources().getDrawable(R.drawable.search_blue));
				svg_cities = SVGParser.getSVGFromResource(welcome.this.getResources(), R.raw.city);
				iv_cities.setImageDrawable(svg_cities.createPictureDrawable());
				rlCities.setBackgroundColor(Color.parseColor("#2C3E50"));
				l_cities.setBackground(getResources().getDrawable(R.drawable.search_blue));

				FragmentManager fm = getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
				fragmentTransaction.replace(R.id.content_frame, new Search());
				fragmentTransaction.commit();
				frag_tag = "Search";
			} else {
				canExit = true;
				Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
			}

		}
		mHandler.sendEmptyMessageDelayed(1,
				2000/* time interval to next press in milli second */);// if not
																		// pressed
																		// within
																		// 2
																		// seconds
																		// then
																		// will
																		// be
																		// setted(canExit)
																		// as
																		// false
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// unregisterReceiver(nReceiver);
	}

	// class NotificationReceiver extends BroadcastReceiver {
	//
	// @Override
	// public void onReceive(Context context, Intent intent) {
	// Log.i("log", "Notification Received:\n" +
	// intent.getStringExtra("notification_event"));
	// TextView tv;
	// RelativeLayout badgeLayout;
	// if (intent.getStringExtra("notification_event")
	// .contentEquals("onNotificationPosted :com.example.search.car.pools")) {
	// Toast.makeText(welcome.this, "Notification Received:\n" +
	// intent.getStringExtra("notification_event"),
	// Toast.LENGTH_SHORT).show();
	// Log.i("log", "Notification Received:\n" +
	// intent.getStringExtra("notification_event"));
	// badgeLayout = (RelativeLayout)
	// menu.findItem(R.id.menu_notification).getActionView();
	// tv = (TextView)
	// badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
	// tv.setBackground(getResources().getDrawable(R.drawable.green_circle));
	// } else {
	// // badgeLayout = (RelativeLayout)
	// // menu.findItem(R.id.menu_notification).getActionView();
	// // tv = (TextView)
	// // badgeLayout.findViewById(R.id.actionbar_notifcation_textview);
	// //
	// tv.setBackground(getResources().getDrawable(R.drawable.blue_circle_small));
	// }
	// }
	// }

	public void set_data() {
		int i = 0;
		DataBaseHelper helper = new DataBaseHelper(welcome.this);
		database = helper.getReadableDatabase();
		if (task.getString("user_id", null) != null) {

			// Bitmap bm = getRoundedShape(decodeFile(getActivity(),
			// R.drawable.provider), 100);
			// image.setImageBitmap(bm);

			Cursor c = database.rawQuery(
					"select name,email from user where user_id='" + task.getString("user_id", null) + "'", null);
			try {
				while (c.moveToNext()) {
					tv_profile_name.setText(getCapitalWords(c.getString(0)));
					tv_profile_email.setText(c.getString(1));
					tv_profile_name.setTypeface(tf);
					tv_profile_email.setTypeface(tf);

					String gravatarUrl = "http://www.gravatar.com/avatar/"
							+ MD5Util.md5Hex(c.getString(1).toLowerCase().trim())
							+ "?d=http://s15.postimg.org/q6j7rf3kn/4jsqob0.png&s=150";
					// Picasso.with(welcome.this).load(gravatarUrl).into(avatar);
					// avatar.invalidate();
					checkInternet(gravatarUrl);
				}
				database.close();
			} catch (Exception e) {
			}
		}
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
			return sentence.toUpperCase().toString();
	}

	public void checkInternet(String url) {
		new CheckInternet(url).execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {

		String gravatarUrl;

		public CheckInternet(String url) {
			// TODO Auto-generated constructor stub
			this.gravatarUrl = url;
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Log.i("log", "Network Status during doInBackGround: " +
			// netWorking);
			String bool = "false";
			try {
				Process p1 = java.lang.Runtime.getRuntime().exec("/system/bin/ping -c 1 www.google.com");
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

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			// Log.i("log", "Network Status onpostexecute: " + netWorking);
			if (result.contentEquals("true")) {
				new getGravatarImage(avatar, gravatarUrl).execute();
			} else {
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

	public class getGravatarImage extends AsyncTask<Void, Void, Void> {
		Bitmap bmp;
		ImageView image = null;
		String gravatarUrl = null;

		public getGravatarImage(ImageView image, String gravatarUrl) {
			// TODO Auto-generated constructor stub
			this.image = image;
			this.gravatarUrl = gravatarUrl;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(gravatarUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				bmp = BitmapFactory.decodeStream(input);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			avatar.setBorderColor(Color.WHITE);
			avatar.setBorderWidth(5);
			avatar.setImageBitmap(bmp);
		}
	}

}