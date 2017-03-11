package dash.board;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import data.service.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.search.car.pools.ConnectionDetector;
import com.example.search.car.pools.DataBaseHelper;
import com.example.search.car.pools.R;
import com.example.search.car.pools.user_login;
import com.example.search.car.pools.dashboard.getGravatarImage;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.squareup.picasso.Picasso;

import custom.list.DialogAdapter;
import custom.list.database_method;
import custom.list.ExpandListSearchAdapter.MD5Util;

public class my_account extends Fragment implements OnClickListener {
	TextView tv_username, tv_sec_user_gender, tv_sec_user_city, tv_sec_user_year;
	EditText et_user_password, et_user_name, et_user_Email, et_user_phone, et_user_place, et_user_profession;
	TextView t1, t2, t3, t4, t5, t6, t7, t8, t9;
	Button b_user_update;
	private SQLiteDatabase database;
	DataBaseHelper dataBaseHelper;
	SharedPreferences task;
	String url = "";
	SharedPreferences.Editor editor;
	ImageView user_image;
	// ConnectionDetector cd;
	
	private ProgressDialog p_bar;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.my_account, container, false);
		// cd = new ConnectionDetector(getActivity());
		// tv_username = (TextView) rootView.findViewById(R.id.tv_user_name);
		tv_sec_user_gender = (TextView) rootView.findViewById(R.id.tv_sec_user_gender);
		tv_sec_user_city = (TextView) rootView.findViewById(R.id.tv_sec_user_city);
		tv_sec_user_year = (TextView) rootView.findViewById(R.id.tv_sec_user_year);
		tv_sec_user_gender.setOnClickListener(this);
		tv_sec_user_city.setOnClickListener(this);
		tv_sec_user_year.setOnClickListener(this);
		et_user_password = (EditText) rootView.findViewById(R.id.et_user_password);
		et_user_name = (EditText) rootView.findViewById(R.id.et_user_name);
		et_user_Email = (EditText) rootView.findViewById(R.id.et_user_Email);
		et_user_phone = (EditText) rootView.findViewById(R.id.et_user_phone);
		et_user_place = (EditText) rootView.findViewById(R.id.et_user_place);
		et_user_profession = (EditText) rootView.findViewById(R.id.et_user_profession);
		b_user_update = (Button) rootView.findViewById(R.id.b_user_update);
		b_user_update.setOnClickListener(this);
		user_image = (ImageView) rootView.findViewById(R.id.iv_user_image_my_account);
		ImageView iv_above = (ImageView) rootView.findViewById(R.id.iv_user_image_my_account_gravatar);
		SVG svg1 = SVGParser.getSVGFromResource(getActivity().getResources(), R.raw.circle_gravatar);
		iv_above.setImageDrawable(svg1.createPictureDrawable());

		t1 = (TextView) rootView.findViewById(R.id.t1);
		t2 = (TextView) rootView.findViewById(R.id.t2);
		t3 = (TextView) rootView.findViewById(R.id.t3);
		t4 = (TextView) rootView.findViewById(R.id.t4);
		t5 = (TextView) rootView.findViewById(R.id.t5);
		t6 = (TextView) rootView.findViewById(R.id.t6);
		t7 = (TextView) rootView.findViewById(R.id.t7);
		t8 = (TextView) rootView.findViewById(R.id.t8);
		t9 = (TextView) rootView.findViewById(R.id.t9);

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd_Book.otf");
		t1.setTypeface(tf);
		t2.setTypeface(tf);
		t3.setTypeface(tf);
		t4.setTypeface(tf);
		t5.setTypeface(tf);
		t6.setTypeface(tf);
		t7.setTypeface(tf);
		t8.setTypeface(tf);
		t9.setTypeface(tf);
		tv_sec_user_gender.setTypeface(tf);
		tv_sec_user_city.setTypeface(tf);
		tv_sec_user_year.setTypeface(tf);
		et_user_password.setTypeface(tf);
		et_user_name.setTypeface(tf);
		et_user_Email.setTypeface(tf);
		et_user_phone.setTypeface(tf);
		et_user_place.setTypeface(tf);
		et_user_profession.setTypeface(tf);
		b_user_update.setTypeface(tf);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
//		getActivity().getActionBar().setHomeButtonEnabled(true);
		task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
		editor = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE).edit();
		dataBaseHelper = new DataBaseHelper(getActivity());
		set_data();
	}

	@Override
	public void onClick(View v) {

		if (v.equals(tv_sec_user_city)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, tv_sec_user_city);
		}
		if (v.equals(tv_sec_user_gender)) {
			String[] gender = { "Male", "Female" };
			dialog("Gender", gender, tv_sec_user_gender);
		}
		if (v.equals(tv_sec_user_year)) {
			String[] year = new String[49];
			for (int i = 0; i <= 48; i++) {
				year[i] = String.valueOf(1996 - i);
			}
			dialog("Brith Year", year, tv_sec_user_year);
		}
		if (v.equals(b_user_update)) {
			if (et_user_password.getText().length() < 1 || et_user_name.getText().length() < 1
					|| et_user_Email.getText().length() < 1 || et_user_phone.getText().length() < 1
					|| et_user_place.getText().length() < 1 || et_user_profession.getText().length() < 1) {
				Toast.makeText(getActivity(), "Fill all the fields", Toast.LENGTH_LONG).show();
			} else {
				String gender = tv_sec_user_gender.getText().toString();
				if (gender.toUpperCase().equals("MALE")) {
					gender = "0";
				} else if (gender.toUpperCase().equals("FEMALE")) {
					gender = "1";
				}
				String s_city = new database_method().get_data(getActivity(),
						"select id from city where city_name='" + tv_sec_user_city.getText().toString() + "'");
				try {
				url = new url().update_user + "&user_id=" + task.getString("user_id", null) + "&username="
						+ URLEncoder.encode(et_user_Email.getText().toString(), "UTF-8")  
						+ "&password=" + URLEncoder.encode(et_user_password.getText().toString(), "UTF-8") 
						+ "&name=" + URLEncoder.encode(et_user_name.getText().toString(), "UTF-8") 
						+ "&gender=" + gender + "&phone="
						+ et_user_phone.getText().toString() + "&dob=" + tv_sec_user_year.getText().toString()
						+ "&profession=" + URLEncoder.encode(et_user_profession.getText().toString(), "UTF-8") + "&state=" + s_city + "&place="
						+ URLEncoder.encode(et_user_place.getText().toString(), "UTF-8") ;
				//Log.d("log", "User Update url: " + url);
				checkInternet();
				} catch (Exception e){
					//e.printStackTrace();
				}
				// if (cd.isConnectingToInternet()) {
				// accessWebService();
				// } else {
				// Toast.makeText(getActivity(), "No Internet Connection",
				// Toast.LENGTH_LONG).show();
				// }
			}
		}

	}

	public void dialog(String name, final String[] arr, final TextView tv) {
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog);
		final ListView list = (ListView) dialog.findViewById(R.id.list_cities);
		DialogAdapter adapter = new DialogAdapter(getActivity(), arr);
		list.setAdapter(adapter);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText("Select " + name);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		// ViewGroup.LayoutParams params = list.getLayoutParams();
		// if (tv.equals(tv_sec_user_gender)){
		// params.height=150;
		// list.setLayoutParams(params);
		// list.requestLayout();
		// }
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);

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

	public void set_data() {

		dataBaseHelper = new DataBaseHelper(getActivity());
		database = dataBaseHelper.getWritableDatabase();
		Cursor c = database.rawQuery(
				"select username,password,name,email,phone,place,profession,gender,state,dob from user where user_id='"
						+ task.getString("user_id", null) + "'",
				null);
		while (c.moveToNext()) {
			// tv_username.setText(c.getString(0));
			et_user_password.setText("");
			et_user_name.setText(c.getString(2));
			et_user_Email.setText(c.getString(3));
			et_user_phone.setText(c.getString(4));
			et_user_place.setText(c.getString(5));
			et_user_profession.setText(c.getString(6));

			if (c.getString(7).contentEquals("0"))
				tv_sec_user_gender.setText("Male");
			else if (c.getString(7).contentEquals("1"))
				tv_sec_user_gender.setText("Female");
			else
				tv_sec_user_gender.setText("");

			String s_city = new database_method().get_data(getActivity(),
					"select city_name from city where id='" + c.getString(8) + "'");
			if (s_city.contentEquals("null") || s_city==null)
				tv_sec_user_city.setText("");
			else 
				tv_sec_user_city.setText(s_city);
			
			if (c.getString(9).contentEquals("0000"))
				tv_sec_user_year.setText("");
			else 
				tv_sec_user_year.setText(c.getString(9));
		}
		database.close();

		String hash = MD5Util.md5Hex(et_user_Email.getText().toString().toLowerCase().trim());
		String gravatarUrl = "http://www.gravatar.com/avatar/" + hash
				+ "?d=http://s15.postimg.org/q6j7rf3kn/4jsqob0.png" + "&s=150";
		Picasso.with(getActivity()).load(gravatarUrl).into(user_image);
		// if (cd.isConnectingToInternet())
		// new getGravatarImage(user_image, gravatarUrl).execute();
	}

	/*
	 * public class getGravatarImage extends AsyncTask<Void, Void, Void>{
	 * 
	 * 
	 * ImageView image; String gravatarUrl; Bitmap bmp; public
	 * getGravatarImage(ImageView image, String gravatarUrl) { // TODO
	 * Auto-generated constructor stub this.image = image; this.gravatarUrl =
	 * gravatarUrl; }
	 * 
	 * @Override protected Void doInBackground(Void... params) { // TODO
	 * Auto-generated method stub try { URL url = new URL(gravatarUrl);
	 * HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	 * connection.setDoInput(true); connection.connect(); InputStream input =
	 * connection.getInputStream(); bmp = BitmapFactory.decodeStream(input); }
	 * catch (IOException e) { e.printStackTrace(); } return null; }
	 * 
	 * @Override protected void onPostExecute(Void result) { // TODO
	 * Auto-generated method stub super.onPostExecute(result);
	 * RoundedBitmapDrawable dr =
	 * RoundedBitmapDrawableFactory.create(getActivity().getResources(), bmp);
	 * dr.setCornerRadius(Math.max(bmp.getWidth(), bmp.getHeight()) / 2.0f);
	 * image.setImageDrawable(dr); }
	 * 
	 * }
	 */

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p_bar.setCancelable(true);
		}
		
		@Override
		protected String doInBackground(String... urls) {
			//Log.d("log", "doinbackground started");
			String webResponse = "Error. Please try again.";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url.replace(" ", "%20");
			//Log.d("log", "url update> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("log", "Response update> " + jsonStr);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					String status = jsonObj.getString("status");
					//Log.d("log", "status: " + status);
					if (status.contentEquals("success")) {
						JSONObject obj = jsonObj.getJSONObject("data");
						// for (int i = 0; i < taskarray.length(); i++) {
						if (obj != null) {
							String data[] = { obj.getString("username"), "Pass1", obj.getString("name"),
									obj.getString("email"), obj.getString("gender"), obj.getString("phone"),
									obj.getString("dob"), obj.getString("profession"), obj.getString("state"),
									obj.getString("place"), obj.getString("date"), obj.getString("lastlogin"),
									obj.getString("ad_notification") };

							dataBaseHelper.update_user(data);
							editor.putString("user_id", obj.getString("user_id"));
							editor.putString("name", obj.getString("name"));
							editor.putString("email", obj.getString("email"));
							editor.putString("phone", obj.getString("phone"));
							editor.commit();
							webResponse = "Updated Successfully";
						}
					}
				} catch (Exception e) {
					//e.printStackTrace();
					webResponse = "Error Occured. Please try again.";
				}
			}
			// else {
			// Log.e("ServiceHandler", "Couldn't get any data from the url");
			// }
			//Log.d("log", "webresponse" + webResponse);
			//Log.d("log", "doinbackground finished");
			return webResponse;
		}

		protected void onPostExecute(String result) {

			p_bar.dismiss();
			//Log.d("log", "onPostExecute Main");
			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
//			et_user_password.setText("");
		}
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();

		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	public void checkInternet() {
		CheckInternet check = new CheckInternet();
		check.execute();
	}

	class CheckInternet extends AsyncTask<String, Void, String> {
		

		@Override
		protected void onPreExecute() {
			p_bar = ProgressDialog.show(getActivity(), null, "Updating Information!\nPlease wait...");
			p_bar.setCancelable(true);
			//Log.d("log", "onPreExecute");
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			// Log.i("log", "Network Status during doInBackGround: " +
			// netWorking);
			//Log.d("log", "doinbackground started");
			String deviceVersion = Build.VERSION.RELEASE;
			if (deviceVersion.contentEquals("4.3")) {
//				Log.d("log", "Jelly Bean");
				NetworkInfo localNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity"))
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
				//Log.d("log", "onPostexecute");
				accessWebService();
			} else {
				Toast.makeText(getActivity(), "Server Not Reachable", Toast.LENGTH_SHORT).show();
			}

			// Log.i("log", "Network Status after onpostexecute: " +
			// netWorking);

		}
	}

}
