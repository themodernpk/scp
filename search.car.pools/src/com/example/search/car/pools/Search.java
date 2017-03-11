package com.example.search.car.pools;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;

@SuppressLint("NewApi")
public class Search extends Fragment implements OnClickListener {
	EditText et_from, et_to;
	TextView sp_city, sp_category, sp_search_for;
	Button b_search;
	LinearLayout l_1, l_2, l_3;
	ImageView i1,i2,i3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.search, container, false);
		l_1 = (LinearLayout) rootView.findViewById(R.id.dgdf);
		l_2 = (LinearLayout) rootView.findViewById(R.id.ddf);
		l_3 = (LinearLayout) rootView.findViewById(R.id.gdf);

		// getActivity().getActionBar().setTitle("Search");
		getActivity().findViewById(R.id.ib_navigation_search).setVisibility(View.GONE);
		
		et_from = (EditText) rootView.findViewById(R.id.et_search_from);
		et_to = (EditText) rootView.findViewById(R.id.et_search_to);
		sp_city = (TextView) rootView.findViewById(R.id.sp_sec_city);
		sp_category = (TextView) rootView.findViewById(R.id.sp_category);
		sp_search_for = (TextView) rootView.findViewById(R.id.sp_search_for);
		b_search = (Button) rootView.findViewById(R.id.b_search);

		i1 = (ImageView) rootView.findViewById(R.id.iv_city);
		i2 = (ImageView) rootView.findViewById(R.id.iv_cat);
		i3 = (ImageView) rootView.findViewById(R.id.iv_search_for);
		
		b_search.setOnClickListener(this);
		sp_city.setOnClickListener(this);
		sp_category.setOnClickListener(this);
		sp_search_for.setOnClickListener(this);

		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd_Book.otf");
		et_from.setTypeface(tf);
		et_to.setTypeface(tf);
		sp_city.setTypeface(tf);
		sp_category.setTypeface(tf);
		sp_search_for.setTypeface(tf);
		b_search.setTypeface(tf);
		
		i1.setOnClickListener(this);
		i2.setOnClickListener(this);
		i3.setOnClickListener(this);
		l_1.setOnClickListener(this);
		l_2.setOnClickListener(this);
		l_3.setOnClickListener(this);
		TextView t = (TextView)rootView.findViewById(R.id.tv_looking_for);
		View v = (View)rootView.findViewById(R.id.view_search);
		t.setTypeface(tf);
		t.setVisibility(View.VISIBLE);
		v.setVisibility(View.VISIBLE);
		l_1.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
//				v.setBackgroundColor(Color.parseColor("#ededed"));
//				v.setBackgroundColor(Color.WHITE);
				
				return true;
			}
		});

		/*final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.dialog_logo);
		final ImageView iv_out = (ImageView) dialog.findViewById(R.id.iv_logo_out);
		final ImageView iv_center = (ImageView) dialog.findViewById(R.id.iv_logo_center);
		SVG svg_out = SVGParser.getSVGFromResource(getResources(), R.raw.logo_s);
		SVG svg_center = SVGParser.getSVGFromResource(getResources(), R.raw.logo_cross);
		iv_out.setImageDrawable(svg_out.createPictureDrawable());
		iv_center.setImageDrawable(svg_center.createPictureDrawable());
		final Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.anim_logo);
		iv_center.startAnimation(animation);
		dialog.show();*/
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
//		menu.findItem(R.id.menu_search).setVisible(false);
		super.onPrepareOptionsMenu(menu);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(l_1) || v.equals(sp_city) || v.equals(i1)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		} else if (v.equals(l_2) || v.equals(sp_category) || v.equals(i2)) {
			String[] category = { "All", "Carpool", "Cab", "Rideshare" };
			dialog("Category", category, sp_category);
		} else if (v.equals(l_3) || v.equals(sp_search_for) || v.equals(i3)) {
			String[] search_for = { "Seeker", "Provider", "Both" };
			dialog(" Search For", search_for, sp_search_for);
		} else if (v.equals(b_search)) {
			if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(getActivity(), "First Select the City", Toast.LENGTH_LONG).show();
//				sp_city.setError("Required");
			} else {
				Intent i = new Intent(getActivity(), search_result.class);
				i.putExtra("city", sp_city.getText().toString());
				i.putExtra("category", sp_category.getText().toString());
				i.putExtra("search_for", sp_search_for.getText().toString());
				i.putExtra("from", et_from.getText().toString());
				i.putExtra("to", et_to.getText().toString());
				i.putExtra("frag_id", 1);
				i.putExtra("company_id", 0);
				startActivity(i);
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
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				tv.setText(arr[position]);
				if (tv.equals(sp_city)) {
					et_from.setText("");
					et_to.setText("");
				}
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
}
