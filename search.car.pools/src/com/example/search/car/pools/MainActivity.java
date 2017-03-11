package com.example.search.car.pools;

import java.io.IOException;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;
import org.taptwo.android.widget.ViewFlow.ViewSwitchListener;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.SQLException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import data.service.internetServiceStatus;

public class MainActivity extends Activity implements OnClickListener {
	RelativeLayout rel_main, skip, next;
	DataBaseHelper myDbHelper;
	// Button b;
	private ViewFlow viewflow;
	View view;
	TextView tv_skip, tv_next, tv_continue;
	LinearLayout l_bottom, l_continue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rel_main = (RelativeLayout) findViewById(R.id.rel_main);
		rel_main.setOnClickListener(this);
		view = (View) findViewById(R.id.viewlight);

		skip = (RelativeLayout) findViewById(R.id.rl_skip);
		next = (RelativeLayout) findViewById(R.id.rl_next);
		skip.setOnClickListener(this);
		next.setOnClickListener(this);

		l_bottom = (LinearLayout) findViewById(R.id.l_button_bottom);
		l_continue = (LinearLayout) findViewById(R.id.l_button);
		l_bottom.setOnClickListener(this);
		l_continue.setOnClickListener(this);

		Typeface tf = Typeface.createFromAsset(MainActivity.this.getAssets(), "AvenirLTStd-Heavy.otf");
		tv_next = (TextView) findViewById(R.id.tv_next);
		tv_skip = (TextView) findViewById(R.id.tv_skip);
		tv_skip.setTypeface(tf);
		tv_next.setTypeface(tf);
		tv_continue = (TextView) findViewById(R.id.tv_continue);
		tv_continue.setTypeface(tf);

		ImageView iv = (ImageView)findViewById(R.id.tv_logo);
		SVG svg = SVGParser.getSVGFromResource(MainActivity.this.getResources(), R.raw.logo_splash);
		iv.setImageDrawable(svg.createPictureDrawable());
		
		// b = (Button)findViewById(R.id.button1);
		viewflow = (ViewFlow) findViewById(R.id.viewFlow);
		viewflow.setAdapter(new ImageAdapter(this), 0);
		CircleFlowIndicator indic = (CircleFlowIndicator) findViewById(R.id.viewflowindic);
		viewflow.setFlowIndicator(indic);
		viewflow.setOnViewSwitchListener(new ViewSwitchListener() {

			@Override
			public void onSwitched(View v, int position) {
				// TODO Auto-generated method stub
				if (viewflow.getSelectedItemPosition() == 4) {
					l_bottom.setVisibility(View.INVISIBLE);
					l_continue.setVisibility(View.VISIBLE);
				} 
			}
		});
		myDbHelper = new DataBaseHelper(this);

		try {

			myDbHelper.createDataBase();

		} catch (IOException ioe) {

			throw new Error("Unable to create database");

		}

		try {

			myDbHelper.openDataBase();

		} catch (SQLException sqle) {

			throw sqle;

		}
		Intent i0 = new Intent();
		i0.setAction("data.service.internetServiceStatus");
		if (isMyServiceRunning(internetServiceStatus.class) == false) {
			startService(i0);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		viewflow.onConfigurationChanged(newConfig);
	}

	@Override
	public void onClick(View v) {
		if (v.equals(rel_main) || v.equals(skip)) {
			Intent i = new Intent(getBaseContext(), welcome.class);
			startActivity(i);
			finish();
		}
		if (v.equals(next)) {
			int i = viewflow.getSelectedItemPosition();
			if (i == 3) {
				l_bottom.setVisibility(View.INVISIBLE);
				l_continue.setVisibility(View.VISIBLE);
			} 
				viewflow.setAdapter(new ImageAdapter(MainActivity.this), i + 1);
		}
		if (v.equals(l_continue)) {
			Intent i = new Intent(getBaseContext(), welcome.class);
			startActivity(i);
			finish();
		}

	}

	private boolean isMyServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}