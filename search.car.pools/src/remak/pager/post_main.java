package remak.pager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.example.search.car.pools.R;
import com.example.search.car.pools.create_activity;
import com.example.search.car.pools.search_result;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.welcome;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;
import dash.board.dashboard_main;
import tab.pager.TabsPagerAdapter;

public class post_main extends FragmentActivity implements ActionBar.TabListener, OnClickListener {

	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private ActionBar actionBar;
	SharedPreferences sharedpreferences;
	MenuItem bedMenuItem;
	SharedPreferences task;
	// Tab titles
	private String[] tabs = { "All", "Provider", "Seeker", "Corporate", "Companies" };
	public static final String MyPREFERENCES = "MyPrefs";
	SharedPreferences.Editor editor;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rem_main_remake_pager);
		task = getSharedPreferences("user", MODE_PRIVATE);
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			String s_city = extras.getString("city");
			sharedpreferences = this.getPreferences(0);
			Editor editor = sharedpreferences.edit();
			editor.putString("city", s_city);
			editor.commit();

			getActionBar().setDisplayHomeAsUpEnabled(false);
			getActionBar().setHomeButtonEnabled(false);
			getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0087ca")));
			// Drawable d = ContextCompat.getDrawable(post_main.this,
			// R.drawable.logooo);
			// getActionBar().setIcon(d);
			getActionBar().setTitle("");
			// SpannableString s = new SpannableString("Cities - " + s_city);
			// s.setSpan(new TypefaceSpan(post_main.this,
			// "AvenirLTStd_Book.otf"), 0, s.length(),
			// Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			// getActionBar().setTitle(s);
		}

		// Initialization
		viewPager = (ViewPager) findViewById(R.id.pager);
		// actionBar = getActionBar();
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar = getActionBar();
		actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#0087ca")));
		// Adding Tabs

		for (String tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name).setTabListener(this));
		}
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		/**
		 * on swiping the viewpager make respective tab selected
		 */
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
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

		d1 = ContextCompat.getDrawable(post_main.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(post_main.this, R.drawable.touch_blue_back_color);
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
//		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
//	             ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME|ActionBar.DISPLAY_SHOW_TITLE);
		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		disableCollapsibleTabs();
	}
	
	private void disableCollapsibleTabs() {
		try {
            if (actionBar instanceof ActionBar) {
                //ICS and forward
                try {
                    Field actionBarField = actionBar.getClass().getDeclaredField("mActionBar");
                    actionBarField.setAccessible(true);
                    actionBar = (ActionBar) actionBarField.get(actionBar);
                } catch (Exception e) {
                    Log.e("", "Error while disabling actionbar collapsible tabs", e);
                }
            }
            Field actionViewField = actionBar.getClass().getDeclaredField("mTabScrollView");
            actionViewField.setAccessible(true);
            Object mTabScrollView =  actionViewField.get(actionBar);
 
            Method setAllowCollapse = mTabScrollView.getClass().getDeclaredMethod("setAllowCollapse", boolean.class);
            setAllowCollapse.setAccessible(true);
            setAllowCollapse.invoke(mTabScrollView, false);
 
        } catch (Exception e) {
            Log.d("", "Error while disabling actionbar collapsible tabs", e);
        }
    }
	
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// MenuInflater inflater = getMenuInflater();
		// inflater.inflate(R.menu.main, (Menu) menu);
		// bedMenuItem = (MenuItem) menu.findItem(R.id.menu_login);
		//
		// if (task.getString("user_id", null) != null) {
		// ((MenuItem) bedMenuItem).setTitle("Logout");
		// } else {
		// ((MenuItem) bedMenuItem).setTitle("Login/Register");
		// }

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
	public boolean onPrepareOptionsMenu(Menu menu) {
		// menu.findItem(R.id.menu_add_new_list).setVisible(!(task.getString("user_id",
		// null) == null));
		// menu.findItem(R.id.menu_dashboard).setVisible(!(task.getString("user_id",
		// null) == null));
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		Intent i;
		// Handle action buttons
		// switch (item.getItemId()) {
		// case R.id.menu_add_new_list:
		// if (task.getString("user_id", null) != null) {
		// i = new Intent(getBaseContext(), create_activity.class);
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
		// if (((MenuItem) bedMenuItem).getTitle().equals("Logout")) {
		// SharedPreferences.Editor editor = getSharedPreferences("user",
		// MODE_PRIVATE).edit();
		// editor.clear();
		// editor.commit();
		// ((MenuItem) bedMenuItem).setTitle("Login/Register");
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
		//
		// finish();
		//
		// return super.onOptionsItemSelected(item);
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
		Typeface tf = Typeface.createFromAsset(post_main.this.getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		b_search.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		promptsView.show();
	}

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(post_main.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(post_main.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(post_main.this.getAssets(), "AvenirLTStd_Book.otf");
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
			String[] search_for = { "Seeker", "Provider", "Both" };
			dialog(" Search For", search_for, sp_search_for);
		} else if (v.equals(close)) {
			promptsView.dismiss();
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(post_main.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(post_main.this, search_result.class);
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
			Intent i = new Intent(post_main.this, welcome.class);
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

			final PopupMenu popup = new PopupMenu(post_main.this, v);
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
							Toast.makeText(post_main.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(post_main.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(post_main.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_login:
						if (((MenuItem) bedMenuItem).getTitle().equals("Logout")) {
							SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
							editor.clear();
							editor.commit();
							((MenuItem) bedMenuItem).setTitle("Login/Register");
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

}
