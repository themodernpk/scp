package data.service;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import dash.board.dashboard_main;

import com.example.search.car.pools.DataBaseHelper;
import com.example.search.car.pools.MainActivity;
import com.example.search.car.pools.R;
import com.example.search.car.pools.welcome;

public class Receiver extends BroadcastReceiver {
	DataBaseHelper helper;
	private SQLiteDatabase database;

	Context con;
	String mobile, id;
	SharedPreferences task;

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// For our recurring task, we'll just display a message
		con = arg0;
		helper = new DataBaseHelper(arg0);
		task = con.getSharedPreferences("user", con.MODE_PRIVATE);
		this.accessWebService();
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {
		// private ProgressDialog progress = null;
		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "";
			ServiceHandler sh = new ServiceHandler();
			database = helper.getWritableDatabase();
			if (task.getString("user_id", null) != null) {

				// ======================fetch ad data===================
				{
					Cursor c = database.rawQuery("select id from ad where sync='1' order by id desc limit 1", null);
					String id = "0";
					try {
						while (c.moveToNext()) {
							id = c.getString(0);
						}
						int j = 0;
						while (j != 1) {
							String url = new url().fetch_ad_data + "&user_id=" + task.getString("user_id", null)
									+ "&ad_id=" + id;
							String url1 = url.replace(" ", "%20");
							String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);

							//Log.d("log", "url= " + url1);
							//Log.d("log", "Response: " + jsonStr);
							if (jsonStr != null) {
								JSONObject jsonObj = new JSONObject(jsonStr);
								JSONArray taskarray = jsonObj.getJSONArray("data");
								//Log.d("arraylength=", String.valueOf(taskarray.length()));
								// Iterate the jsonArray and print the info of
								// JSONObjects
								if (taskarray.length() < 20) {
									j = 1;
								}
								for (int i = 0; i < taskarray.length(); i++) {

									final JSONObject jsonObject = taskarray.getJSONObject(i);
									// =============================check
									// id====================

									id = jsonObject.getString("id");

									String data[] = { jsonObject.getString("id"), jsonObject.getString("user_id"),
											jsonObject.getString("type"), jsonObject.getString("cat_id"),
											jsonObject.getString("city"), jsonObject.getString("company_id"),
											jsonObject.getString("from").replaceAll("'", "''"), jsonObject.getString("to").replaceAll("'", "''"),
											jsonObject.getString("route").replaceAll("'", "''"), jsonObject.getString("seats"),
											jsonObject.getString("departure_time"), jsonObject.getString("return_trip"),
											jsonObject.getString("return_time"), jsonObject.getString("title").replaceAll("'", "''"),
											jsonObject.getString("desc").replaceAll("'", "''"), jsonObject.getString("date"),
											jsonObject.getString("hits"), jsonObject.getString("enable"), "1" };

									helper.insert_ad(data);
									// =========================================================

								}
							}

						}
					} catch (Exception e) {
						//Log.d("error=", e.toString());
					}

				}
				// =========================fetch response
				// data=============================
				{
					// Cursor c = database.rawQuery("select response_id from
					// response where sync='1' and (sender_id='" +
					// task.getString("user_id", null) + "' or receiver_id='"
					// + task.getString("user_id", null) + "') order by
					// response_id desc limit 1 ", null);

					Cursor c = database.rawQuery(
							"select response_id from response where sync='1' order by response_id desc limit 1", null);

					String id = "0";
					try {
						while (c.moveToNext()) {
							id = c.getString(0);
						}
						int j = 0;
						while (j != 1) {
							String url = new url().fetch_response + "&user_id=" + task.getString("user_id", null)
									+ "&id=" + id;
							String url1 = url.replace(" ", "%20");
							String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);

							//Log.d("log", "response url" + url1);
							//Log.d("log", "Response: " + jsonStr);
							if (jsonStr != null) {
								JSONObject jsonObj = new JSONObject(jsonStr);
								JSONArray taskarray = jsonObj.getJSONArray("data");
								//Log.d("arraylength=", String.valueOf(taskarray.length()));
								// Iterate the jsonArray and print the info of
								// JSONObjects
								if (taskarray.length() < 20) {
									j = 1;
								}
								for (int i = 0; i < taskarray.length(); i++) {
									final JSONObject jsonObject = taskarray.getJSONObject(i);
									final JSONObject obj_response = jsonObject.getJSONObject("response");
									final JSONObject obj_ad = jsonObject.getJSONObject("ad");
									final JSONObject obj_user = jsonObject.getJSONObject("user");
									// response_id,ad_id,ad_title,ad_post_date,sender_id,sender_name,sender_email,sender_phone,receiver_id,msg,sms,date,visibility,read,sync
									id = obj_response.getString("response_id");
									
									String msg = obj_response.getString("msg").replaceAll("'", "''");
									
									String data[] = { obj_response.getString("response_id"), obj_ad.getString("ad_id"),
											obj_ad.getString("ad_title"), obj_ad.getString("ad_post_date"),

											obj_response.getString("sender_id"), obj_user.getString("sender_name").replaceAll("'", "''"),
											obj_user.getString("sender_email").replaceAll("'", "''"), obj_user.getString("sender_phone"),
											obj_response.getString("receiver_id"), msg,
											obj_response.getString("sms"), obj_response.getString("date"),
											obj_response.getString("visibility"), obj_response.getString("read"), "1" };

									helper.insert_response(data);
									//Log.i("log", "New Message Inserted" + "\nMsg:" + obj_response.getString("msg")+ "\nReceiver ID:" + obj_response.getString("receiver_id"));
									if (!(obj_response.getString("sender_id").contentEquals(task.getString("user_id", null))))
										showNotification(obj_user.getString("sender_name"), obj_response.getString("msg"));
									
									// =========================================================
								}
							}

						}
					} catch (Exception e) {
						//Log.d("error=", e.toString());
					}

				}

				// =========================upload ad===========================

				{
					Cursor c = database.rawQuery(
							"select local_id,id,user_id,type,cat_id,city,company_id,fro_m,t_o,route,seats,departure_time,return_trip,return_time,title,desc,date,hits,enable,sync from ad where sync='0' and user_id='"
									+ task.getString("user_id", null) + "'",
							null);

					try {
						while (c.moveToNext()) {
							String url = new url().create_add + "&user_id=" + c.getString(2) + "&type=" + c.getString(3)
									+ "&cat_id=" + c.getString(4) + "&city=" + c.getString(5) + "&company_id="
									+ c.getString(6) + "&from=" + URLEncoder.encode(c.getString(7) , "UTF-8")+ "&to=" + URLEncoder.encode(c.getString(8) , "UTF-8") + "&route="
									+ URLEncoder.encode(c.getString(9) , "UTF-8") + "&seats=" + c.getString(10) + "&departure_time="
									+ c.getString(11) + "&return_trip=" + c.getString(12) + "&return_time="
									+ c.getString(13) + "&title=" + URLEncoder.encode(c.getString(14) , "UTF-8") + "&desc=" + URLEncoder.encode(c.getString(15) , "UTF-8")
									+ "&date=" + c.getString(16) + "&hits=" + c.getString(17) + "&enable="
									+ c.getString(18);
							String url1 = url.replace(" ", "%20");
							String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
							//Log.d("log", "Create Ad new url: " + url);
							//Log.d("log", "Create Ad new response: " + jsonStr);
							if (jsonStr != null) {
								JSONObject jsonObj = new JSONObject(jsonStr);
								String ad_id = jsonObj.getString("ad_id");
								String ad_date = jsonObj.getString("ad_date");
								helper.update_ad(c.getString(0), ad_id, ad_date);
							}
						}

					} catch (Exception e) {
						//e.printStackTrace();
					}
				}

				// =========================upload
				// response===========================
				{
					Cursor c = database.rawQuery(
							"select local_response_id,response_id,local_ad_id,ad_id,sender_id,receiver_id,msg,sms,visibility,read,sender_phone,sender_email from response where sync='0'",
							null);// and receiver_id='"
					// + task.getString("user_id", null) + "'", //sync='0' and
					// null);

					// String r_id;
					try {
						while (c.moveToNext()) {
							// if (c.getString(1)=="")
							// r_id="0";
							// else
							// r_id=c.getString(1);
							String url = new url().response + "&ad_id=" + c.getString(3) + "&sender_id="
									+ c.getString(4) + "&receiver_id=" + c.getString(5) + "&msg=" + URLEncoder.encode(c.getString(6) , "UTF-8")
									+ "&sms=" + c.getString(7) + "&visibility=" + c.getString(8) + "&read="
									+ c.getString(9) + "&email=" + URLEncoder.encode(c.getString(11) , "UTF-8") + "&response_id=0";// +r_id;
							String url1 = url.replace(" ", "%20");
							String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
							//Log.d("url=", url1);
							//Log.d("Response: ", "> " + jsonStr);
							if (jsonStr != null && !jsonStr.contentEquals("updated")) {
								JSONObject jsonObj = new JSONObject(jsonStr);
								String id = jsonObj.getString("response_id");
								String date = jsonObj.getString("response_date");
								helper.update_response(c.getString(0), id, date);
								//Log.i("log", "inserted");
								// } else {
								// Log.i("log", "Updated");
							}
						}
					} catch (Exception e) {
						//e.printStackTrace();
					}
				}

				// =========================upload response deleted
				// items===========================
				{
					Cursor c = database.rawQuery(
							"select local_response_id,response_id,local_ad_id,ad_id,sender_id,receiver_id,msg,sms,visibility,read,sender_phone,sender_email from response where visibility = 0 and receiver_id='"
									+ task.getString("user_id", null) + "'",
							null);

					// String r_id="";
					try {
						while (c.moveToNext()) {
							// if (c.getString(1)=="")
							// r_id="0";
							// else
							// r_id=c.getString(1);
							String url = new url().response + "&ad_id=" + c.getString(3) + "&sender_id="
									+ c.getString(4) + "&receiver_id=" + c.getString(5) + "&msg=" + URLEncoder.encode(c.getString(6) , "UTF-8")
									+ "&sms=" + c.getString(7) + "&visibility=" + c.getString(8) + "&read="
									+ c.getString(9) + "&phone=" + c.getString(10) + "&email=" + URLEncoder.encode(c.getString(11) , "UTF-8")
									+ "&response_id=" + c.getString(1);
							String url1 = url.replace(" ", "%20");
							String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
							//Log.d("log", "url for deleted response=" + url1);
							//Log.d("log", "Response: " + jsonStr);
							if (jsonStr != null && jsonStr.contentEquals("updated")) {
								// JSONObject jsonObj = new JSONObject(jsonStr);
								String id = c.getString(1);
								helper.update_response_from_server(c.getString(0), id);
								//Log.i("log", "Updated");
							}
						}
					} catch (Exception e) {
					}
				}

				// =========================fetch updated data from server=============================

				String url = new url().update_local_db_with_server + "&user_id=" + task.getString("user_id", null);
				String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
				//Log.d("log", "update url: " + url);
				//Log.d("log", "Response: " + jsonStr);
				try {
					if (jsonStr != null) {
						JSONObject jsonObj = new JSONObject(jsonStr);
						JSONArray array_response = jsonObj.getJSONArray("response");
						if (array_response.length() != 0) {
							String[] data_response = new String[array_response.length()];
							for (int i = 0; i < array_response.length(); i++) {
								data_response[i] = array_response.getString(i);
								//Log.d("log", "Array response data: " + data_response[i]);
								Cursor c = database.rawQuery(
										"select response_id from response where response_id = '"+data_response[i] +"'", null);
								if (c!=null && c.moveToNext()){
									// set this id visibility = 0
									helper.update_response(data_response[i]);
								}
								c.close();
							}
							
						}
						
						JSONArray array_ad = jsonObj.getJSONArray("ad");
						if (array_ad.length() != 0) {
							String[] data_ad = new String[array_ad.length()];
							for (int i = 0; i < array_ad.length(); i++) {
								data_ad[i] = array_ad.getString(i);
								//Log.d("log", "Array Ad data: " + data_ad[i]);
								Cursor c = database.rawQuery(
										"select id from ad where id = '"+data_ad[i] +"'", null);
								if (c!=null && c.moveToNext()){
									// set this id visibility = 0
									helper.update_ad(data_ad[i]);
								}
								c.close();
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace()
				}
			}
			database.close();
			return webResponse;
		}

		protected void onPostExecute(String result) {
			// Toast.makeText(con, "service", Toast.LENGTH_LONG).show();

		}
	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();
		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}

	private void showNotification(String title, String text) {
		NotificationManager notificationManager = (NotificationManager) con.getSystemService(con.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.logo1, "New Message", System.currentTimeMillis());
		Intent notificationIntent = new Intent(con, welcome.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(con, 0, notificationIntent, 0);
		notification.setLatestEventInfo(con, title, text, pendingIntent);
		notification.defaults = Notification.DEFAULT_ALL;
		notificationManager.notify(1234567890, notification);
	}

}
