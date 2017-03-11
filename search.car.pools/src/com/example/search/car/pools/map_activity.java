//package com.example.search.car.pools;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import org.json.JSONObject;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.CameraPosition;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//import com.google.android.maps.GeoPoint;
//
//import dash.board.dashboard_main;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.support.v4.app.FragmentActivity;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//public class map_activity extends FragmentActivity {
//	GoogleMap googleMap;
//	Double lat1, lang1, lat2, lang2;
//	String source = null, destination = null;
//	MenuItem bedMenuItem;
//	SharedPreferences task;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.map);
//		Bundle bundle = getIntent().getExtras();
//		task = getSharedPreferences("user", MODE_PRIVATE);
//
//		if (bundle != null) {
//			source = bundle.getString("source");
//			destination = bundle.getString("destination");
//
//		}
//		initilizeMap();
//		getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActionBar().setHomeButtonEnabled(true);
//		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0087ca")));
//
//		if (source != null) {
//			lat1 = (double) (getLocationFromAddress(source).getLatitudeE6() / 1000000.0);
//			lang1 = (double) (getLocationFromAddress(source).getLongitudeE6() / 1000000.0);
//		}
//		if (destination != null) {
//			lat2 = (double) (getLocationFromAddress(destination).getLatitudeE6() / 1000000.0);
//			lang2 = (double) (getLocationFromAddress(destination).getLongitudeE6() / 1000000.0);
//		}
//
//		if (lat1 != null && lang1 != null && lat2 != null && lang2 != null) {
//			draw_destination();
//		}
//	}
//
//	public void show_map() {
//
//		// Changing map type
//		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//		googleMap.setMyLocationEnabled(true);
//
//		// Enable / Disable zooming controls
//		googleMap.getUiSettings().setZoomControlsEnabled(true);
//
//		// Enable / Disable my location button
//		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//		googleMap.setTrafficEnabled(true);
//
//		// Enable / Disable Compass icon
//		googleMap.getUiSettings().setCompassEnabled(true);
//
//		// Enable / Disable Rotate gesture
//		googleMap.getUiSettings().setRotateGesturesEnabled(true);
//
//		// Enable / Disable zooming functionality
//		googleMap.getUiSettings().setZoomGesturesEnabled(true);
//
//	}
//
//	private void initilizeMap() {
//		if (googleMap == null) {
//			googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//
//			// check if map is created successfully or not
//			if (googleMap == null) {
//				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
//			}
//		}
//		if (googleMap != null) {
//			show_map();
//		}
//	}
//
//	public GeoPoint getLocationFromAddress(String strAddress) {
//
//		Geocoder coder = new Geocoder(this);
//		List<Address> address;
//		GeoPoint p1 = null;
//
//		try {
//			address = coder.getFromLocationName(strAddress, 5);
//			if (address == null) {
//				return null;
//			}
//			Address location = address.get(0);
//			location.getLatitude();
//			location.getLongitude();
//
//			p1 = new GeoPoint((int) (location.getLatitude() * 1E6), (int) (location.getLongitude() * 1E6));
//		} catch (Exception e) {
//
//		}
//		return p1;
//	}
//
//	public void move_camera() {
//
//		float zoom = googleMap.getCameraPosition().zoom;
//
//		CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(lat2, lang2)).zoom(zoom)
//
//				.bearing(14).build();
//
//		googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//
//	}
//
//	public void draw_destination() {
//
//		MarkerOptions marker = new MarkerOptions().position(new LatLng(lat2, lang2)).title("End point ")
//				.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher));
//		googleMap.addMarker(marker);
//		// markerPoints.add(new LatLng(Double.parseDouble(des_lat),
//		// Double.parseDouble(des_lan)));
//
//		LatLng origin = new LatLng(lat1, lang1);
//		LatLng dest = new LatLng(lat2, lang2);
//
//		// Getting URL to the Google Directions API
//		String url = getDirectionsUrl(origin, dest);
//
//		DownloadTask downloadTask = new DownloadTask();
//
//		// Start downloading json data from Google Directions API
//		downloadTask.execute(url);
//
//	}
//
//	private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//		// Origin of route
//		String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//		// Destination of route
//		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//		// Sensor enabled
//		String sensor = "sensor=false";
//
//		// Waypoints
//		String waypoints = "";
//
//		waypoints = "waypoints=";
//		waypoints = lat1 + "," + lang1 + "|" + lat2 + "," + lang2 + "|";
//
//		// Building the parameters to the web service
//		String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + waypoints;
//
//		// Output format
//		String output = "json";
//
//		// Building the url to the web service
//		String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;
//		Log.d("json api url=", url);
//		return url;
//	}
//
//	private class DownloadTask extends AsyncTask<String, Void, String> {
//
//		// Downloading data in non-ui thread
//		@Override
//		protected String doInBackground(String... url) {
//
//			// For storing data from web service
//			String data = "";
//
//			try {
//				// Fetching the data from web service
//				data = downloadUrl(url[0]);
//			} catch (Exception e) {
//				Log.d("Background Task", e.toString());
//			}
//			return data;
//		}
//
//		// Executes in UI thread, after the execution of
//		// doInBackground()
//		@Override
//		protected void onPostExecute(String result) {
//			super.onPostExecute(result);
//
//			ParserTask parserTask = new ParserTask();
//
//			// Invokes the thread for parsing the JSON data
//			parserTask.execute(result);
//
//		}
//	}
//
//	private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//		// Parsing the data in non-ui thread
//		@Override
//		protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//			JSONObject jObject;
//			List<List<HashMap<String, String>>> routes = null;
//
//			try {
//				jObject = new JSONObject(jsonData[0]);
//				DirectionsJSONParser parser = new DirectionsJSONParser();
//
//				// Starts parsing data
//				routes = parser.parse(jObject);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return routes;
//		}
//
//		// Executes in UI thread, after the parsing process
//		@Override
//		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//
//			ArrayList<LatLng> points = null;
//			PolylineOptions lineOptions = null;
//
//			// Traversing through all the routes
//			for (int i = 0; i < result.size(); i++) {
//				points = new ArrayList<LatLng>();
//				lineOptions = new PolylineOptions();
//
//				// Fetching i-th route
//				List<HashMap<String, String>> path = result.get(i);
//
//				// Fetching all the points in i-th route
//				for (int j = 0; j < path.size(); j++) {
//					HashMap<String, String> point = path.get(j);
//
//					double lat = Double.parseDouble(point.get("lat"));
//					double lng = Double.parseDouble(point.get("lng"));
//					LatLng position = new LatLng(lat, lng);
//
//					points.add(position);
//				}
//
//				// Adding all the points in the route to LineOptions
//				lineOptions.addAll(points);
//				lineOptions.width(4);
//				lineOptions.color(Color.BLUE).zIndex(5);
//			}
//
//			// Drawing polyline in the Google Map for the i-th route
//			googleMap.addPolyline(lineOptions);
//			move_camera();
//		}
//	}
//
//	private String downloadUrl(String strUrl) throws IOException {
//		String data = "";
//		InputStream iStream = null;
//		HttpURLConnection urlConnection = null;
//		try {
//			URL url = new URL(strUrl);
//
//			// Creating an http connection to communicate with url
//			urlConnection = (HttpURLConnection) url.openConnection();
//
//			// Connecting to url
//			urlConnection.connect();
//
//			// Reading data from url
//			iStream = urlConnection.getInputStream();
//
//			BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//			StringBuffer sb = new StringBuffer();
//
//			String line = "";
//			while ((line = br.readLine()) != null) {
//				sb.append(line);
//			}
//
//			data = sb.toString();
//
//			br.close();
//
//		} catch (Exception e) {
//			Log.d("Exception while downloading url", e.toString());
//		} finally {
//			iStream.close();
//			urlConnection.disconnect();
//		}
//		return data;
//	}
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.main, menu);
//		bedMenuItem = menu.findItem(R.id.menu_login);
//		SharedPreferences task = getSharedPreferences("user", MODE_PRIVATE);
//		if (task.getString("user_id", null) != null) {
//			bedMenuItem.setTitle("Logout");
//		} else {
//			bedMenuItem.setTitle("Login/Register");
//		}
//
//		return super.onCreateOptionsMenu(menu);
//	}
//
//	/* Called whenever we call invalidateOptionsMenu() */
//
//	@Override
//	protected void onResume() {
//		// TODO Auto-generated method stub
//		super.onResume();
//
//		if (bedMenuItem != null) {
//			if (task.getString("user_id", null) != null) {
//				bedMenuItem.setTitle("Logout");
//			} else {
//				bedMenuItem.setTitle("Login/Register");
//			}
//		}
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// The action bar home/up action should open or close the drawer.
//		// ActionBarDrawerToggle will take care of this.
//
//		Intent i;
//		// Handle action buttons
//		switch (item.getItemId()) {
//		case R.id.menu_add_new_list:
//			if (task.getString("user_id", null) != null) {
//				i = new Intent(getBaseContext(), create_activity.class);
//
//				startActivity(i);
//			} else {
//				Toast.makeText(this, "Pls Login first", Toast.LENGTH_LONG).show();
//			}
//			return true;
//		case R.id.menu_dashboard:
//			if (task.getString("user_id", null) != null) {
//				i = new Intent(this, dashboard_main.class);
//				startActivity(i);
//			} else {
//				Toast.makeText(this, "Pls Login first", Toast.LENGTH_LONG).show();
//			}
//			return true;
//		case R.id.menu_login:
//			if (bedMenuItem.getTitle().equals("Logout")) {
//				SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
//				editor.clear();
//				editor.commit();
//				bedMenuItem.setTitle("Login/Registration");
//
//				Intent i_user = new Intent(getBaseContext(), user_login.class);
//
//				startActivity(i_user);
//			}
//
//			return true;
//
//		case android.R.id.home:
//
//			finish();
//
//			return super.onOptionsItemSelected(item);
//		default:
//			return super.onOptionsItemSelected(item);
//		}
//	}
//
//}
