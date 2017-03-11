package com.example.search.car.pools;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

public class MainAnimation extends Activity {
	
	private static String DB_PATH = "/data/data/com.example.search.car.pools/databases/";
    private static String DB_NAME ="searchca_carpools";
	
	
	int progress = 0;
	Handler h = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_animation);

		ImageView iv_main_anim_logo = (ImageView) findViewById(R.id.main_anim_logo);
		SVG svg1 = SVGParser.getSVGFromResource(getResources(), R.raw.logo_splash);
		iv_main_anim_logo.setImageDrawable(svg1.createPictureDrawable());

		ImageView iv_main_anim_background = (ImageView) findViewById(R.id.main_anim_background);
		SVG svg2 = SVGParser.getSVGFromResource(getResources(), R.raw.bg_image);
		iv_main_anim_background.setImageDrawable(svg2.createPictureDrawable());

		final ImageView iv_main_anim_carpooling = (ImageView) findViewById(R.id.main_anim_carpooling);
		final SVG svg3 = SVGParser.getSVGFromResource(getResources(), R.raw.carpooling);

		final ImageView iv_main_anim_happy = (ImageView) findViewById(R.id.main_anim_happy);
		final SVG svg4 = SVGParser.getSVGFromResource(getResources(), R.raw.happy);
		iv_main_anim_happy.setImageDrawable(svg4.createPictureDrawable());
		iv_main_anim_carpooling.setImageDrawable(svg3.createPictureDrawable());
		iv_main_anim_happy.setVisibility(View.INVISIBLE);
		iv_main_anim_carpooling.setVisibility(View.INVISIBLE);

		ImageView iv_main_anim_left_cloud = (ImageView) findViewById(R.id.main_anim_left_cloud);
		SVG svg5 = SVGParser.getSVGFromResource(getResources(), R.raw.cloud_left);
		iv_main_anim_left_cloud.setImageDrawable(svg5.createPictureDrawable());

		ImageView iv_main_anim_right_cloud = (ImageView) findViewById(R.id.main_anim_right_cloud);
		SVG svg6 = SVGParser.getSVGFromResource(getResources(), R.raw.cloud_right);
		iv_main_anim_right_cloud.setImageDrawable(svg6.createPictureDrawable());

		ImageView iv_main_anim_car = (ImageView) findViewById(R.id.main_anim_car);
		SVG svg7 = SVGParser.getSVGFromResource(getResources(), R.raw.car_main_animation);
		iv_main_anim_car.setImageDrawable(svg7.createPictureDrawable());

//		ImageView iv_main_anim_buddy_left = (ImageView) findViewById(R.id.main_anim_logo_buddy_left);
//		SVG svg8 = SVGParser.getSVGFromResource(getResources(), R.raw.logo_left_buddy);
//		iv_main_anim_buddy_left.setImageDrawable(svg8.createPictureDrawable());
//
//		ImageView iv_main_anim_buddy_mid = (ImageView) findViewById(R.id.main_anim_logo_buddy_mid);
//		SVG svg9 = SVGParser.getSVGFromResource(getResources(), R.raw.logo_mid_buddy);
//		iv_main_anim_buddy_mid.setImageDrawable(svg9.createPictureDrawable());
//
//		ImageView iv_main_anim_buddy_right = (ImageView) findViewById(R.id.main_anim_logo_buddy_right);
//		SVG svg10 = SVGParser.getSVGFromResource(getResources(), R.raw.logo_right_buddy);
//		iv_main_anim_buddy_right.setImageDrawable(svg10.createPictureDrawable());

		TranslateAnimation animation = new TranslateAnimation(-50.0f, 20.0f, 0.0f, 0.0f);
		animation.setDuration(3000);
		animation.setRepeatCount(ValueAnimator.INFINITE);
		animation.setRepeatMode(2);
		animation.setFillAfter(true);
		iv_main_anim_left_cloud.startAnimation(animation);

		TranslateAnimation animation1 = new TranslateAnimation(-50.0f, 20.0f, 0.0f, 0.0f);
		animation1.setDuration(5000);
		animation1.setRepeatCount(ValueAnimator.INFINITE);
		animation1.setRepeatMode(2);
		animation1.setFillAfter(true);
		iv_main_anim_right_cloud.startAnimation(animation1);

		TranslateAnimation anim_car = new TranslateAnimation(0.0f, 275.0f, 0.0f, 0.0f);
		anim_car.setDuration(2000);
		anim_car.setFillAfter(true);
		// iv_main_anim_car.startAnimation(anim_car);

//		TranslateAnimation anim_buddy_left = new TranslateAnimation(0.0f, 0.0f, 0.0f, 10.0f);
//		anim_buddy_left.setDuration(600);
//		anim_buddy_left.setRepeatCount(ValueAnimator.INFINITE);
//		anim_buddy_left.setFillAfter(true);
//		iv_main_anim_buddy_left.startAnimation(anim_buddy_left);
//
//		TranslateAnimation anim_buddy_mid = new TranslateAnimation(0.0f, 0.0f, 0.0f, 15.0f);
//		anim_buddy_mid.setDuration(800);
//		anim_buddy_mid.setRepeatCount(ValueAnimator.INFINITE);
//		anim_buddy_mid.setFillAfter(true);
//		iv_main_anim_buddy_mid.startAnimation(anim_buddy_mid);
//
//		TranslateAnimation anim_buddy_right = new TranslateAnimation(0.0f, 0.0f, 0.0f, 10.0f);
//		anim_buddy_right.setDuration(600);
//		anim_buddy_right.setRepeatCount(ValueAnimator.INFINITE);
//		anim_buddy_right.setFillAfter(true);
//		iv_main_anim_buddy_right.startAnimation(anim_buddy_right);

		final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
		iv_main_anim_car.animate().translationX(275.0f).setDuration(2000).setListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationEnd(Animator animation) {
				super.onAnimationEnd(animation);
				iv_main_anim_happy.setImageDrawable(svg4.createPictureDrawable());
				iv_main_anim_carpooling.setImageDrawable(svg3.createPictureDrawable());
				new Thread(new Runnable() {

					@Override
					public void run() {
						for (int i = 0; i < 2; i++) {
							progress += 5;
							h.post(new Runnable() {

								@Override
								public void run() {
									// TODO Auto-generated method stub
									iv_main_anim_happy.startAnimation(animationFadeIn);
									iv_main_anim_carpooling.startAnimation(animationFadeIn);
									if (progress == 10) {
										boolean dbExist = checkDataBase();
									    if(dbExist){
									    //do nothing - database already exist
									    	Intent intent = new Intent(MainAnimation.this, welcome.class);
											startActivity(intent);
											MainAnimation.this.finish();
									    }else{
									    	Intent intent = new Intent(MainAnimation.this, MainActivity.class);
											startActivity(intent);
											MainAnimation.this.finish();
									    }
									}
								}
							});
							try {
								Thread.sleep(1200);
							} catch (InterruptedException e) {
								// TODO: handle exception
							}
						}

					}
				}).start();
			}
		});

		// Animation 1
		/*
		 * TranslateAnimation animation = new TranslateAnimation(0.0f, 400.0f,
		 * 0.0f, 0.0f); animation.setDuration(5000);
		 * animation.setRepeatCount(5); animation.setRepeatMode(2);
		 * animation.setFillAfter(true); //
		 * img_animation.startAnimation(animation); //
		 * img_animation_tyre1.startAnimation(animation); //
		 * img_animation_tyre2.startAnimation(animation);
		 * 
		 * ////Animation 2 AnimationSet animationSet = new AnimationSet(true);
		 * 
		 * TranslateAnimation a = new TranslateAnimation(
		 * Animation.ABSOLUTE,200, Animation.ABSOLUTE,200,
		 * Animation.ABSOLUTE,200, Animation.ABSOLUTE,200); a.setDuration(1000);
		 * 
		 * RotateAnimation r = new RotateAnimation(0f, -360f,37,200);
		 * r.setStartOffset(1000); r.setDuration(5000);
		 * 
		 * animationSet.addAnimation(a); animationSet.addAnimation(r);
		 * animationSet.addAnimation(animation); //
		 * img_animation_tyre.startAnimation(animationSet);
		 * 
		 * //Animation 3 Animation shake = AnimationUtils.loadAnimation(this,
		 * R.anim.shake); // img_animation.startAnimation(shake);
		 * 
		 * 
		 * // new current animation ImageView wheel =
		 * (ImageView)findViewById(R.id.wheel); AnimatorSet wheelSet =
		 * (AnimatorSet) AnimatorInflater.loadAnimator(this,
		 * R.animator.wheel_spin); wheelSet.setTarget(wheel); wheelSet.start();
		 * 
		 * //get the sun view ImageView sun = (ImageView)findViewById(R.id.sun);
		 * //load the sun movement animation AnimatorSet sunSet = (AnimatorSet)
		 * AnimatorInflater.loadAnimator(this, R.animator.sun_swing); //set the
		 * view as target sunSet.setTarget(sun); //start the animation
		 * sunSet.start();
		 * 
		 * ValueAnimator skyAnim = ObjectAnimator.ofInt
		 * (findViewById(R.id.car_layout), "backgroundColor", Color.rgb(0x66,
		 * 0xcc, 0xff), Color.rgb(0x00, 0x66, 0x99)); skyAnim.setDuration(3000);
		 * skyAnim.setRepeatCount(ValueAnimator.INFINITE);
		 * skyAnim.setRepeatMode(ValueAnimator.REVERSE);
		 * 
		 * skyAnim.setEvaluator(new ArgbEvaluator()); skyAnim.start();
		 * 
		 * ObjectAnimator cloudAnim =
		 * ObjectAnimator.ofFloat(findViewById(R.id.cloud1), "x", -350);
		 * cloudAnim.setDuration(3000);
		 * cloudAnim.setRepeatCount(ValueAnimator.INFINITE);
		 * cloudAnim.setRepeatMode(ValueAnimator.REVERSE); cloudAnim.start();
		 * 
		 * ObjectAnimator cloudAnim2 =
		 * ObjectAnimator.ofFloat(findViewById(R.id.cloud2), "x", -300);
		 * cloudAnim2.setDuration(3000);
		 * cloudAnim2.setRepeatCount(ValueAnimator.INFINITE);
		 * cloudAnim2.setRepeatMode(ValueAnimator.REVERSE); cloudAnim2.start();
		 */
	}

	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;
		try {
			String myPath = DB_PATH + DB_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
		} catch (SQLiteException e) {
			// database does't exist yet.
		}
		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

}
