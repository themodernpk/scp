package com.map;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.example.search.car.pools.R;
import com.example.search.car.pools.Search;
import com.example.search.car.pools.create_activity;
import com.example.search.car.pools.search_result;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.welcome;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import custom.list.DialogAdapter;
import dash.board.dashboard_main;

public class MyMap extends Activity implements OnClickListener {
	final int RQS_GooglePlayServices = 1;
	private GoogleMap myMap;
	double src_lat, src_long, dest_lat, dest_long;
	double source_lat_local, source_long_local, destination_lat_local, destination_long_local;
	MarkerOptions markerOptions;
	String city = "";
	String from_latlng, to_latlng;
	String from, to;
	String from_intent, to_intent;
	LatLng srcLatLng;
	MenuItem bedMenuItem;
	SharedPreferences task;

	// ConnectionDetector cd;
	// search dialog widgets
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	Dialog promptsView;
	RelativeLayout close;

	boolean finalize = false;

	private ProgressDialog progressDialog;

	// custom Navigation
	public ImageButton ib_back, ib_handle, ib_logo, ib_search, ib_menu;
	// final PopupMenu popup = null;
	LinearLayout l_back, l_handle, l_logo, l_nav_search, l_menu;
	Drawable d1, d2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		task = getSharedPreferences("user", MODE_PRIVATE);

		// getActionBar().setDisplayHomeAsUpEnabled(true);
		// getActionBar().setHomeButtonEnabled(true);
		// getActionBar().setBackgroundDrawable(new
		// ColorDrawable(Color.parseColor("#0087ca")));
		// Drawable d = ContextCompat.getDrawable(MyMap.this,
		// R.drawable.logooo);
		// getActionBar().setIcon(d);
		// getActionBar().setTitle("");
		Bundle b = getIntent().getExtras();
		from_intent = b.getString("from");
		to_intent = b.getString("to");
		int city_index = b.getInt("city_index");
		switch (city_index) {
		case 1:
			if (from_intent.toLowerCase().contentEquals("dwarka") || from_intent.toLowerCase().contentEquals("palam") 
					|| to_intent.toLowerCase().contentEquals("dwarka") || to_intent.toLowerCase().contentEquals("palam")) {
				city = ",Delhi";
			} else {
				city = "";
			}
			break;

		case 2:
			city = "Mumbai";
			break;

		case 3:
			city = "Bengaluru";
			break;

		case 4:
			city = "Pune";
			break;

		case 5:
			city = "Kolkata";
			break;

		case 6:
			city = "Ahmedabad";
			break;
		}
		

		from = from_intent + city;// + "," + city;
		to = to_intent;// + "," + city;
		from = from.replaceAll(" ", "%20");
		to = to.replaceAll(" ", "%20");

		FragmentManager myFragmentManager = getFragmentManager();
		MapFragment myMapFragment = (MapFragment) myFragmentManager.findFragmentById(R.id.map);
		myMap = myMapFragment.getMap();

		srcLatLng = new LatLng(src_lat, src_long);

		// Enabling MyLocation in Google Map
		myMap.setMyLocationEnabled(true);
		myMap.getUiSettings().setZoomControlsEnabled(true);
		myMap.getUiSettings().setCompassEnabled(true);
		myMap.getUiSettings().setMyLocationButtonEnabled(true);
		myMap.getUiSettings().setAllGesturesEnabled(true);
		myMap.setTrafficEnabled(true);
		// myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(srcLatLng,12));
		markerOptions = new MarkerOptions();

		// Polyline line = myMap.addPolyline(new
		// PolylineOptions().add(srcLatLng,
		// destLatLng).width(5).color(Color.RED));

		// new GeoCoding(from).execute();
		// cd = new ConnectionDetector(MyMap.this);
		// if (cd.isConnectingToInternet())
		// new GetLatLong().execute();
		// else
		// Toast.makeText(MyMap.this, "No Internet Connection!",
		// Toast.LENGTH_SHORT).show();
		checkInternet();

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

		d1 = ContextCompat.getDrawable(MyMap.this, R.drawable.touch_ripple_back_color);
		d2 = ContextCompat.getDrawable(MyMap.this, R.drawable.touch_blue_back_color);
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
//		ib_back.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
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

		// popup = new PopupMenu(welcome.this, ib_menu);
//		ib_menu.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		ib_search.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});

		getActionBar().setCustomView(mCustomView);
		getActionBar().setDisplayShowCustomEnabled(true);

	}

	class GetLatLong extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MyMap.this);
			progressDialog.setMessage("Fetching route, Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.setCancelable(true);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			getLatLongFromAddress(from);
			getLatLongToAddress(to);
			return null;
		}

		public void getLatLongFromAddress(String youraddress) {
			String uri = "http://maps.googleapis.com/maps/api/geocode/json?address=" + youraddress + "&sensor=false";
			HttpGet httpGet = new HttpGet(uri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			StringBuilder stringBuilder = new StringBuilder();

			try {
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			} catch (ClientProtocolException e) {
				//e.printStackTrace();
			} catch (IOException e) {
				//e.printStackTrace();
			}

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject = new JSONObject(stringBuilder.toString());

				src_long = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lng");

				src_lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lat");

				//Log.d("log", "from latitude: " + src_lat);
				//Log.d("log", "from longitude: " + src_long);
			} catch (JSONException e) {
				//e.printStackTrace();
			}
		}

		public void getLatLongToAddress(String youraddress) {
			String uri = "http://maps.google.com/maps/api/geocode/json?address=" + youraddress + "&sensor=false";
			HttpGet httpGet = new HttpGet(uri);
			HttpClient client = new DefaultHttpClient();
			HttpResponse response;
			StringBuilder stringBuilder = new StringBuilder();

			try {
				response = client.execute(httpGet);
				HttpEntity entity = response.getEntity();
				InputStream stream = entity.getContent();
				int b;
				while ((b = stream.read()) != -1) {
					stringBuilder.append((char) b);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}

			JSONObject jsonObject = new JSONObject();
			try {
				jsonObject = new JSONObject(stringBuilder.toString());

				dest_long = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lng");

				dest_lat = ((JSONArray) jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
						.getJSONObject("location").getDouble("lat");

				//Log.d("log", "to latitude: " + dest_lat);
				//Log.d("log", "to longitude: " + dest_long);
			} catch (Exception e) {
				//e.printStackTrace();
			}

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			connectAsyncTask _connectAsyncTask = new connectAsyncTask();
			// if (cd.isConnectingToInternet())
			// _connectAsyncTask.execute();
			// else
			// Toast.makeText(MyMap.this, "No Internet Connection!",
			// Toast.LENGTH_SHORT).show();
			checkInternet();
			finalize = true;
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
		// if (bedMenuItem != null) {
		// if (task.getString("user_id", null) != null) {
		// bedMenuItem.setTitle("Logout");
		// } else {
		// bedMenuItem.setTitle("Login/Register");
		// }
		// }

		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

		if (resultCode == ConnectionResult.SUCCESS) {
			// Toast.makeText(getApplicationContext(),
			// "isGooglePlayServicesAvailable SUCCESS",
			// Toast.LENGTH_LONG).show();
		} else {
			GooglePlayServicesUtil.getErrorDialog(resultCode, this, RQS_GooglePlayServices);
		}

	}

	private class connectAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			fetchData();
			// getLatLongFromAddress(from);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (doc != null) {
				NodeList _nodelist = doc.getElementsByTagName("status");
				Node node1 = _nodelist.item(0);
				String _status1 = node1.getChildNodes().item(0).getNodeValue();
				if (_status1.equalsIgnoreCase("OK")) {
					NodeList _nodelist_path = doc.getElementsByTagName("overview_polyline");
					Node node_path = _nodelist_path.item(0);
					Element _status_path = (Element) node_path;
					NodeList _nodelist_destination_path = _status_path.getElementsByTagName("points");
					Node _nodelist_dest = _nodelist_destination_path.item(0);
					String _path = _nodelist_dest.getChildNodes().item(0).getNodeValue();
					List<LatLng> directionPoint = decodePoly(_path);

					PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.RED);
					for (int i = 0; i < directionPoint.size(); i++) {
						rectLine.add(directionPoint.get(i));
					}
					// Adding route on the map
					myMap.addPolyline(rectLine);
					// markerOptions.position(new LatLng(src_lat, src_long));
					// markerOptions.draggable(true);
					// myMap.addMarker(markerOptions);
					LatLng destLatLng = new LatLng(dest_lat, dest_long);

					LatLng srcLatLng = new LatLng(src_lat, src_long);
					myMap.addMarker(new MarkerOptions().position(srcLatLng)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_a))
							.title(getCapitalWords(from_intent)));

					myMap.addMarker(new MarkerOptions().position(destLatLng)
							.icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_b))
							.title(getCapitalWords(to_intent)));

					myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destLatLng, 10));
				} else {
					showAlert("Unable to find the route");
				}

			} else {
				showAlert("Unable to find the route");
			}

			progressDialog.dismiss();
			// Toast.makeText(MyMap.this, "Source Lat: "+src_lat+"\nSource Long:
			// "+src_long+
			// "\nDes Lat: "+dest_lat+"\nDest Long: "+dest_long,
			// Toast.LENGTH_LONG).show();

		}

	}

	private String getCapitalWords(String sentence) {
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

	Document doc = null;

	private void fetchData() {
		StringBuilder urlString = new StringBuilder();
		urlString.append("http://maps.google.com/maps/api/directions/xml?origin=");
		urlString.append(src_lat);
		urlString.append(",");
		urlString.append(src_long);
		urlString.append("&destination=");// to
		urlString.append(dest_lat);
		urlString.append(",");
		urlString.append(dest_long);
		urlString.append("&sensor=false&mode=driving");
		//Log.d("url", "::" + urlString.toString());
		HttpURLConnection urlConnection = null;
		URL url = null;
		try {
			url = new URL(urlString.toString());
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.connect();
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = (Document) db.parse(urlConnection.getInputStream());// Util.XMLfromString(response);
		} catch (Exception e) {
			//e.printStackTrace();
		}
	}

	private void showAlert(String message) {
		AlertDialog.Builder alert = new AlertDialog.Builder(MyMap.this);
		alert.setTitle("Error");
		alert.setCancelable(false);
		alert.setMessage(message);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MyMap.this.finish();
			}
		});
		alert.show();
	}

	private ArrayList<LatLng> decodePoly(String encoded) {
		ArrayList<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;
		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;
			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng position = new LatLng((double) lat / 1E5, (double) lng / 1E5);
			poly.add(position);
		}
		return poly;
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
	public boolean onOptionsItemSelected(MenuItem item) {
		// The action bar home/up action should open or close the drawer.
		// ActionBarDrawerToggle will take care of this.

		Intent i;
		// Handle action buttons
		// switch (item.getItemId()) {
		// case R.id.menu_add_new_list:
		// if (task.getString("user_id", null) != null) {
		// i = new Intent(getBaseContext(), MyMap.class);
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
		Typeface tf = Typeface.createFromAsset(MyMap.this.getAssets(), "AvenirLTStd_Book.otf");
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
			String[] search_for = { "Seeker", "Provider", "Both" };
			dialog(" Search For", search_for, sp_search_for);
		} else if (v.equals(close)) {
			promptsView.dismiss();
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(MyMap.this, "Firstly Select the City", Toast.LENGTH_LONG).show();
			} else {
				Intent i = new Intent(MyMap.this, search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_url", " ");
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
			Intent i = new Intent(MyMap.this, welcome.class);
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

			final PopupMenu popup = new PopupMenu(MyMap.this, v);
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
							i = new Intent(getBaseContext(), MyMap.class);
							startActivity(i);
						} else {
							Toast.makeText(MyMap.this, "Please Login first", Toast.LENGTH_LONG).show();
						}
						return true;
					case R.id.menu_dashboard:
						if (task.getString("user_id", null) != null) {
							i = new Intent(MyMap.this, dashboard_main.class);
							i.putExtra("edit", "12344");
							startActivity(i);
						} else {
							Toast.makeText(MyMap.this, "Please Login first", Toast.LENGTH_LONG).show();
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

	public void dialog(String name, final String[] arr, final TextView tv) {

		final Dialog dialog = new Dialog(MyMap.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(MyMap.this, arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(MyMap.this.getAssets(), "AvenirLTStd_Book.otf");
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
			int i = 0;
			if (result.contentEquals("true")) {
				if (result.contentEquals("true") && (finalize == true)) {
					connectAsyncTask _connectAsyncTask = new connectAsyncTask();
					_connectAsyncTask.execute();
					finalize = false;
					i++;
				} else {
					// Toast.makeText(MyMap.this, "No Internet Connection",
					// Toast.LENGTH_SHORT).show();
					// MyMap.this.finish();
					// i++;
				}
				if (result.contentEquals("true") && (i == 0)) {
					new GetLatLong().execute();
				} else {
					// Toast.makeText(MyMap.this, "No Internet Connection",
					// Toast.LENGTH_SHORT).show();
					// MyMap.this.finish();
				}
			} else {
				Toast.makeText(MyMap.this, "Server Not Reachable", Toast.LENGTH_SHORT).show();
				MyMap.this.finish();
			}
			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		connectAsyncTask lmib = new connectAsyncTask();
		GetLatLong lmib1 = new GetLatLong();

		if (lmib.getStatus() == AsyncTask.Status.RUNNING || lmib.getStatus() == AsyncTask.Status.FINISHED
				|| lmib1.getStatus() == AsyncTask.Status.RUNNING || lmib1.getStatus() == AsyncTask.Status.FINISHED) {
			// My AsyncTask is currently doing work in doInBackground()
			lmib.cancel(true);
			progressDialog.dismiss();
		}
		finish();
	}

}