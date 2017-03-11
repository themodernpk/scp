package com.example.search.car.pools;

import dash.board.dashboard_main;
import post.details.post_details_main;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.ExpandListSearchAdapter.MD5Util;
import custom.list.ExpandListSearchAdapter.getGravatarImage;

@SuppressLint("NewApi")
public class dashboard extends Fragment implements OnClickListener, OnScrollChangedListener {
	Button b_dashboard;
	TextView tv_gender, tv_name, tv_email, tv_phone, tv_birth, tv_profession, tv_place, tv_city;
	private SQLiteDatabase database;
	ImageView image;
	SharedPreferences task;
	ScrollView scroll;
	float mActionBarHeight;
	
	// timer for updating listings every 5 secs
		Timer timer;
		TimerTask timerTask;
		final Handler handler = new Handler();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.dashboard, container, false);
		task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
		b_dashboard = (Button) rootView.findViewById(R.id.b_dashboard);
		b_dashboard.setOnClickListener(this);
		tv_name = (TextView) rootView.findViewById(R.id.tv_name);
		tv_email = (TextView) rootView.findViewById(R.id.tv_email);
		tv_phone = (TextView) rootView.findViewById(R.id.tv_phone);
		tv_birth = (TextView) rootView.findViewById(R.id.tv_birth);
		tv_profession = (TextView) rootView.findViewById(R.id.tv_profession);
		tv_place = (TextView) rootView.findViewById(R.id.tv_place);
		tv_city = (TextView) rootView.findViewById(R.id.tv_city_my_profile);
		tv_gender = (TextView) rootView.findViewById(R.id.tv_sec_user_gender);
		image = (ImageView)rootView.findViewById(R.id.iv_user_image_my_profile);
		ImageView iv_above = (ImageView)rootView.findViewById(R.id.iv_user_image_my_profile_gravatar);
		SVG svg1 = SVGParser.getSVGFromResource(getActivity().getResources(), R.raw.circle_gravatar_dashboard);
		iv_above.setImageDrawable(svg1.createPictureDrawable());
		
//		getActivity().getActionBar().setTitle("My Profile");
		
		// set typeface
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd_Book.otf");
		tv_name.setTypeface(tf);
		tv_profession.setTypeface(tf);
		tv_email.setTypeface(tf);
		tv_phone.setTypeface(tf);
		tv_birth.setTypeface(tf);
		tv_profession.setTypeface(tf);
		tv_place.setTypeface(tf);
		tv_city.setTypeface(tf);
		Typeface tf1 = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd-Heavy.otf");
		TextView tv1 = (TextView) rootView.findViewById(R.id.tvmy_userGender);
		TextView tv2 = (TextView) rootView.findViewById(R.id.tvmy_userPhone);
		TextView tv3 = (TextView) rootView.findViewById(R.id.tvmy_userCity);
		TextView tv4 = (TextView) rootView.findViewById(R.id.tvmy_userPlace);
		TextView tv5 = (TextView) rootView.findViewById(R.id.tvmy_userDOB);
		TextView tv6 = (TextView) rootView.findViewById(R.id.tvmy_userProfession);
		tv1.setTypeface(tf1);
		tv2.setTypeface(tf1);
		tv3.setTypeface(tf1);
		tv4.setTypeface(tf1);
		tv5.setTypeface(tf1);
		tv6.setTypeface(tf1);
		b_dashboard.setTypeface(tf1);
		
		final TypedArray mstyled = getActivity().getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize });
		mActionBarHeight = mstyled.getDimension(0, 0);
		mstyled.recycle();
		
		scroll = (ScrollView)rootView.findViewById(R.id.scroll_my_profile);
		scroll.getViewTreeObserver().addOnScrollChangedListener(this);
		
		if (task.getString("user_id", null) == null) {
			Toast.makeText(getActivity(), "Please login first", Toast.LENGTH_LONG).show();
			startTimer();
		} else {
			set_data();
		}
		
		return rootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		set_data();
		startTimer();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(b_dashboard)) {
			if (task.getString("user_id", null) != null) {
				Intent i = new Intent(getActivity(), dashboard_main.class);
				i.putExtra("edit", "1");
				startActivity(i);
			} else {
				Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_LONG).show();
			}
		}

	}

	public void set_data() {
		int i = 0;
		DataBaseHelper helper = new DataBaseHelper(getActivity());
		database = helper.getReadableDatabase();
		if (task.getString("user_id", null) != null) {
			
//			Bitmap bm = getRoundedShape(decodeFile(getActivity(), R.drawable.provider), 100);
//			image.setImageBitmap(bm);
			
			Cursor c = database
					.rawQuery("select username,name,email,phone,dob,profession,place,state, gender from user where user_id='"
							+ task.getString("user_id", null) + "'", null);
			try {
				while (c.moveToNext()) {
					tv_name.setText(getCapitalWords(c.getString(1)));
					tv_email.setText(c.getString(2));
					tv_phone.setText(c.getString(3));
					
					if (c.getString(4).contentEquals("0000"))
						tv_birth.setText("");
					else 
						tv_birth.setText(c.getString(4));
					
					if (c.getString(5) != null && c.getString(5).length() > 2)
						tv_profession.setText(c.getString(5).substring(0, 1).toUpperCase() + c.getString(5).substring(1));
					else
						tv_profession.setText(c.getString(5).toUpperCase());
					
					if (c.getString(6) != null && c.getString(6).length() > 2)
						tv_place.setText(c.getString(6).substring(0, 1).toUpperCase() + c.getString(6).substring(1));
					else
						tv_place.setText(c.getString(6).toUpperCase());
					
					if (c.getString(8).contentEquals("0"))
						tv_gender.setText("Male");
					else if (c.getString(8).contentEquals("1"))
						tv_gender.setText("Female");
					else 
						tv_gender.setText("");
					
					String state = c.getString(7);
					DataBaseHelper h = new DataBaseHelper(getActivity());
					SQLiteDatabase db = h.getReadableDatabase();
					Cursor c1 = db.rawQuery("select city_name from city where id='" + state +"'", null);
					while (c1.moveToNext()) {
						tv_city.setText(c1.getString(0));
					}
					db.close();
					
					String hash = MD5Util.md5Hex(c.getString(2).toLowerCase().trim());
					String gravatarUrl = "http://www.gravatar.com/avatar/" + hash  + "?d=http://s15.postimg.org/q6j7rf3kn/4jsqob0.png";
					
					ConnectionDetector cd = new ConnectionDetector(getActivity());
					if (cd.isConnectingToInternet())
						Picasso.with(getActivity()).load(gravatarUrl).into(image);
//						new getGravatarImage(image, gravatarUrl).execute();
					
					
				}
				database.close();
			} catch (Exception e) {
			}
		}
	}

public class getGravatarImage extends AsyncTask<Void, Void, Void>{

		
		ImageView image;
		String gravatarUrl;
		Bitmap bmp;
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
			RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getActivity().getResources(), bmp);
			dr.setCornerRadius(Math.max(bmp.getWidth(), bmp.getHeight()) / 2.0f);
			image.setImageDrawable(dr);
		}
		
	}
	
	
	private CharSequence getCapitalWords(String sentence) {
		String[] words = null;
		if (sentence != null && sentence.length() > 2) {
			try{
			words = sentence.split(" ");
			for (int i = 0; i < words.length; i++) {
				words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
			}
			} catch(Exception e){
				//e.printStackTrace();
			}
			return TextUtils.join(" ", words);
		} else
			return sentence;
	}

	@Override
	public void onScrollChanged() {
		// TODO Auto-generated method stub
		/*float mfloat = scroll.getScrollY();
	    if (mfloat >= mActionBarHeight && getActivity().getActionBar().isShowing()) {
	    	getActivity().getActionBar().hide();
	    } else if ( mfloat==0 && !getActivity().getActionBar().isShowing()) {
	    	getActivity().getActionBar().show();
	    }*/
	}
	
	public void startTimer() {
		// set a new Timer
		timer = new Timer();
		// initialize the TimerTask's job
		initializeTimerTask();
		// schedule the timer, after the first 5000ms the TimerTask will run
		// every 10000ms
		timer.schedule(timerTask, 2000, 2000); //
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {

				// use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						// get the current timeStamp
						if (task.getString("user_id", null) == null) {
							tv_name.setText(":------------------");
							tv_email.setText(":------------------");
							tv_phone.setText(":------------------");
							tv_birth.setText(":------------------");
							tv_profession.setText(":------------------");
							tv_place.setText(":------------------");
							tv_gender.setText(":------------------");
							tv_city.setText(":------------------");
							image.setImageDrawable(null);
						}
					}
				});
			}
		};
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		//Log.i("log", "OnPause Called");
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		//Log.i("log", "OnDestroy Called");
	}
}
