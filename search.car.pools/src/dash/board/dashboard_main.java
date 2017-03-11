package dash.board;

import tab.pager.PagerAdapter;
import tab.pager.dashboard_pager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabWidget;
import custom.list.DialogAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import com.example.search.car.pools.R;
import com.example.search.car.pools.create_activity;
import com.example.search.car.pools.search_result;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.welcome;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

public class dashboard_main extends FragmentActivity
		implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, OnClickListener {

	private ViewPager viewPager;
	private dashboard_pager mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "My Listing", "My Messages", "My Account" };
	MenuItem bedMenuItem;
	SharedPreferences task;

	// search dialog widgets
	EditText et_from, et_to;
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

	private TabHost mTabHost;
	private ViewPager mViewPager;
	private HashMap<String, TabInfo> mapTabInfo = new HashMap<String, dashboard_main.TabInfo>();
	private PagerAdapter mPagerAdapter;

	/**
	 * Maintains extrinsic info of a tab's construct
	 */
	private class TabInfo {
		private String tag;
		private Class<?> clss;
		private Bundle args;
		private Fragment fragment;

		TabInfo(String tag, Class<?> clazz, Bundle args) {
			this.tag = tag;
			this.clss = clazz;
			this.args = args;
		}

	}

	/**
	 * A simple factory that returns dummy views to the Tabhost
	 */
	class TabFactory implements TabContentFactory {

		private final Context mContext;

		/**
		 * @param context
		 */
		public TabFactory(Context context) {
			mContext = context;
		}

		/**
		 * (non-Javadoc)
		 * 
		 * @see android.widget.TabHost.TabContentFactory#createTabContent(java.lang.String)
		 */
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rem_main);
		task = getSharedPreferences("user", MODE_PRIVATE);
		this.initialiseTabHost(savedInstanceState);
		this.intialiseViewPager();

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
		
		d1 = ContextCompat.getDrawable(dashboard_main.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(dashboard_main.this, R.drawable.touch_blue_back_color);
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
			
//		ib_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		l_handle.setVisibility(View.GONE);

//		ib_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		ib_logo.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		ib_menu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		// Initilization
		// viewPager = (ViewPager) findViewById(R.id.pager);
		// actionBar = getActionBar();
		// mAdapter = new dashboard_pager(getSupportFragmentManager());
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// Drawable d = ContextCompat.getDrawable(dashboard_main.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");

		// viewPager.setAdapter(mAdapter);
		//
		// actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		// // Adding Tabs
		// for (String tab_name : tabs) {
		// actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		// }
		// actionBar.setStackedBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		// /**
		// * on swiping the viewpager make respective tab selected
		// */
		// viewPager.setOnPageChangeListener(new
		// ViewPager.OnPageChangeListener() {
		//
		// @Override
		// public void onPageSelected(int position) {
		// // on changing the page
		// // make respected tab selected
		// actionBar.setSelectedNavigationItem(position);
		//// actionBar.setTitle(tabs[position]);
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		// }
		// });

		Bundle b = getIntent().getExtras();
		String s = b.getString("edit");
		if (s.contentEquals("3")) {
			int frag = R.layout.my_account;
			mViewPager.setCurrentItem(frag);
			//Log.i("log", "From" + s);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, (Menu) menu);
		// bedMenuItem = (MenuItem) menu.findItem(R.id.menu_login);
		// SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
		// if (task.getString("user_id", null) != null) {
		// ((MenuItem) bedMenuItem).setTitle("Logout");
		// } else {
		// ((MenuItem) bedMenuItem).setTitle("Login/Register");
		// }
		// menu.findItem(R.id.menu_add_new_list).setTitle("Home");
		// menu.findItem(R.id.menu_dashboard).setTitle("Add New List");
		// menu.findItem(R.id.menu_notification).setVisible(false);
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
		// ((MenuItem) bedMenuItem).setTitle("Logout");
		// } else {
		// ((MenuItem) bedMenuItem).setTitle("Login/Register");
		// }
		// }
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		Intent i;
		// Handle action buttons
//		switch (item.getItemId()) {
//		case R.id.menu_add_new_list:
//
//			i = new Intent(getBaseContext(), welcome.class);
//			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(i);
//			return true;
//		case R.id.menu_dashboard:
//			if (task.getString("user_id", null) != null) {
//				i = new Intent(this, create_activity.class);
//				startActivity(i);
//			} else {
//				Toast.makeText(this, "Please Login first", Toast.LENGTH_LONG).show();
//			}
//			return true;
//		case R.id.menu_login:
//			if (((MenuItem) bedMenuItem).getTitle().equals("Logout")) {
//				SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
//				editor.clear();
//				editor.commit();
//				((MenuItem) bedMenuItem).setTitle("Login/Register");
//
//				Intent i_user = new Intent(getBaseContext(), user_login.class);
//				startActivity(i_user);
//				finish();
//			}
//
//			return true;
//
//		// case R.id.menu_search:
//		// showSearchDialog();
//		// return true;
//
//		case android.R.id.home:
//
//			finish();
//
//			return super.onOptionsItemSelected(item);
//		default:
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
		Typeface tf = Typeface.createFromAsset(dashboard_main.this.getAssets(), "AvenirLTStd_Book.otf");
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
		if ((v.equals(l_1) || (v.equals(sp_city)))) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		} else if ((v.equals(l_2) || (v.equals(sp_category)))) {
			String[] category = { "All", "Carpool", "Cab", "Rideshare" };
			dialog("Category", category, sp_category);
		} else if ((v.equals(l_3) || (v.equals(sp_search_for)))) {
			String[] search_for = { "Search For", "Seeker", "Provider", "Both" };
			dialog(" Search For", search_for, sp_search_for);
		} else if (v.equals(close)) {
			promptsView.dismiss();
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(dashboard_main.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(dashboard_main.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_url", " ");
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
			Intent i = new Intent(dashboard_main.this, welcome.class);
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
			
			final PopupMenu popup = new PopupMenu(dashboard_main.this, v);
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

						i = new Intent(getBaseContext(), create_activity.class);
//						i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(i);
						return true;
					case R.id.menu_dashboard:
//						if (task.getString("user_id", null) != null) {
//							i = new Intent(dashboard_main.this, create_activity.class);
//							startActivity(i);
//						} else {
//							Toast.makeText(dashboard_main.this, "Please Login first", Toast.LENGTH_LONG).show();
//						}
						return true;
					case R.id.menu_login:
						if (((MenuItem) bedMenuItem).getTitle().equals("Logout")) {
							SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							((MenuItem) bedMenuItem).setTitle("Login/Register");

							Intent i_user = new Intent(getBaseContext(), user_login.class);
							startActivity(i_user);
							finish();
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

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(dashboard_main.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(dashboard_main.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(dashboard_main.this.getAssets(), "AvenirLTStd_Book.otf");
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

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onSaveInstanceState(android.os.Bundle)
	 */
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("tab", mTabHost.getCurrentTabTag()); // save the tab
																// selected
		super.onSaveInstanceState(outState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onRestoreInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			this.mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab")); // set
																					// the
																					// tab
																					// as
																					// per
																					// the
																					// saved
																					// state
		}
		super.onRestoreInstanceState(savedInstanceState);
	}

	/**
	 * Initialise ViewPager
	 */
	private void intialiseViewPager() {

		List<Fragment> fragments = new Vector<Fragment>();
		fragments.add(Fragment.instantiate(this, my_listing.class.getName()));
		fragments.add(Fragment.instantiate(this, my_messages.class.getName()));
		fragments.add(Fragment.instantiate(this, my_account.class.getName()));
		this.mPagerAdapter = new PagerAdapter(super.getSupportFragmentManager(), fragments);
		//
		this.mViewPager = (ViewPager) super.findViewById(R.id.tabviewpager);
		this.mViewPager.setAdapter(this.mPagerAdapter);
		this.mViewPager.setOnPageChangeListener(this);
	}

	/**
	 * Initialise the Tab Host
	 */
	private void initialiseTabHost(Bundle args) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		TabInfo tabInfo = null;
		dashboard_main.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab1").setIndicator("My Listings"),
				(tabInfo = new TabInfo("Tab1", my_listing.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		dashboard_main.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab2").setIndicator("My Messages"),
				(tabInfo = new TabInfo("Tab2", my_messages.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		dashboard_main.AddTab(this, this.mTabHost, this.mTabHost.newTabSpec("Tab3").setIndicator("My Account"),
				(tabInfo = new TabInfo("Tab3", my_account.class, args)));
		this.mapTabInfo.put(tabInfo.tag, tabInfo);
		// Default to first tab
		// this.onTabChanged("Tab1");
		//
		TabWidget widget = mTabHost.getTabWidget();
		for (int i = 0; i < widget.getChildCount(); i++) {
			View v = widget.getChildAt(i);

			v.setBackgroundResource(R.drawable.tab_indicator_ab_stylingactionbar);
		}
		mTabHost.setOnTabChangedListener(this);
	}

	/**
	 * Add Tab content to the Tabhost
	 * 
	 * @param activity
	 * @param tabHost
	 * @param tabSpec
	 * @param clss
	 * @param args
	 */
	private static void AddTab(dashboard_main activity, TabHost tabHost, TabHost.TabSpec tabSpec, TabInfo tabInfo) {
		// Attach a Tab view factory to the spec
		tabSpec.setContent(activity.new TabFactory(activity));
		tabHost.addTab(tabSpec);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	public void onTabChanged(String tag) {
		// TabInfo newTab = this.mapTabInfo.get(tag);
		int pos = this.mTabHost.getCurrentTab();
		this.mViewPager.setCurrentItem(pos);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled
	 * (int, float, int)
	 */
	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected
	 * (int)
	 */
	@Override
	public void onPageSelected(int position) {
		// TODO Auto-generated method stub
		this.mTabHost.setCurrentTab(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.view.ViewPager.OnPageChangeListener#
	 * onPageScrollStateChanged(int)
	 */
	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub

	}
}
