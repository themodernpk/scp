package com.example.search.car.pools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import custom.list.DialogAdapter;
import custom.list.database_method;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import data.service.*;

@SuppressLint("NewApi")
public class Create_Listing extends Fragment implements OnClickListener {
	RadioGroup rg_carpool, rg_carpool_type;
	RelativeLayout r1, r2, r3, r4, r5, r6;
	TextView tv_sec_city, tv_sec_company, tv_sec_category, tv_sec_seats, tv_sec_departure, tv_sec_return, tv1, tv2, tv3,
			tv4, tv5, tv6, tv7, tv8;
	EditText et_title, et_description, et_form, et_to, et_route;
	Button b_create_list;
	String s_carpool = "Seeker", s_carpool_type = "0", company_id = "0";
	LinearLayout l1_company;
	SharedPreferences task;

	ImageView iv1, iv2, iv3, iv4, iv5, iv6, iv7, iv8, iv9, iv10, iv11, iv12, iv13;
	ImageView iv02, iv04, iv05, iv011, iv012, iv013;
	LinearLayout.LayoutParams params, paramsIV;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.create_list, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);// prevent keyboard from popping
		task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd-Heavy.otf");
		tv1 = (TextView) rootView.findViewById(R.id.tv_1_create_list);
		tv2 = (TextView) rootView.findViewById(R.id.tv_2_create_list);
		tv3 = (TextView) rootView.findViewById(R.id.tv_3_create_list);
		tv4 = (TextView) rootView.findViewById(R.id.tv_4_create_list);
		tv5 = (TextView) rootView.findViewById(R.id.tv_5_create_list);
		tv6 = (TextView) rootView.findViewById(R.id.tv_6_create_list);
		tv7 = (TextView) rootView.findViewById(R.id.tv_7_create_list);
		tv8 = (TextView) rootView.findViewById(R.id.tv_8_create_list);

		iv1 = (ImageView) rootView.findViewById(R.id.iv_1);
		iv2 = (ImageView) rootView.findViewById(R.id.iv_2);
		iv3 = (ImageView) rootView.findViewById(R.id.iv_3);
		iv4 = (ImageView) rootView.findViewById(R.id.iv_4);
		iv5 = (ImageView) rootView.findViewById(R.id.iv_5);
		iv6 = (ImageView) rootView.findViewById(R.id.iv_6);
		iv7 = (ImageView) rootView.findViewById(R.id.iv_7);
		iv8 = (ImageView) rootView.findViewById(R.id.iv_8);
		iv9 = (ImageView) rootView.findViewById(R.id.iv_9);
		iv10 = (ImageView) rootView.findViewById(R.id.iv_10);
		iv11 = (ImageView) rootView.findViewById(R.id.iv_11);
		iv12 = (ImageView) rootView.findViewById(R.id.iv_12);
		iv13 = (ImageView) rootView.findViewById(R.id.iv_13);

		iv02 = (ImageView) rootView.findViewById(R.id.iv_02);
		iv04 = (ImageView) rootView.findViewById(R.id.iv_04);
		iv05 = (ImageView) rootView.findViewById(R.id.iv_05);
		iv011 = (ImageView) rootView.findViewById(R.id.iv_011);
		iv012 = (ImageView) rootView.findViewById(R.id.iv_012);
		iv013 = (ImageView) rootView.findViewById(R.id.iv_013);
		
		b_create_list = (Button) rootView.findViewById(R.id.b_company_register);

		tv_sec_city = (TextView) rootView.findViewById(R.id.tv_c_sec_city);
		tv_sec_company = (TextView) rootView.findViewById(R.id.tv_sec_company);
		tv_sec_category = (TextView) rootView.findViewById(R.id.tv_sec_category);
		tv_sec_seats = (TextView) rootView.findViewById(R.id.tv_sec_seats);
		tv_sec_departure = (TextView) rootView.findViewById(R.id.tv_sec_departure);
		tv_sec_return = (TextView) rootView.findViewById(R.id.tv_sec_return);
		RadioButton rb1 = (RadioButton) rootView.findViewById(R.id.radio0);
		RadioButton rb2 = (RadioButton) rootView.findViewById(R.id.radio1);
		RadioButton rb3 = (RadioButton) rootView.findViewById(R.id.radio3);
		RadioButton rb4 = (RadioButton) rootView.findViewById(R.id.radio4);
		TextView t1 = (TextView) rootView.findViewById(R.id.tv_sec_city_1);

		et_title = (EditText) rootView.findViewById(R.id.et_title);
		et_description = (EditText) rootView.findViewById(R.id.et_description);
		et_form = (EditText) rootView.findViewById(R.id.et_from);
		et_to = (EditText) rootView.findViewById(R.id.et_to);
		et_route = (EditText) rootView.findViewById(R.id.et_route);

		et_title.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv6.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv6.setImageResource(R.drawable.grey_circle);
			}
		});

		et_description.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv7.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv7.setImageResource(R.drawable.grey_circle);
			}
		});

		et_form.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv8.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv8.setImageResource(R.drawable.grey_circle);
			}
		});

		et_to.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv9.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv9.setImageResource(R.drawable.grey_circle);
			}
		});

		et_route.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				iv10.setImageResource(R.drawable.blue_circle_small);
				if (s.length() < 1)
					iv10.setImageResource(R.drawable.grey_circle);
			}
		});

		rg_carpool = (RadioGroup) rootView.findViewById(R.id.rg_carpool);
		rg_carpool_type = (RadioGroup) rootView.findViewById(R.id.rg_carpool_type);

		l1_company = (LinearLayout) rootView.findViewById(R.id.l1_select_company);
		r1 = (RelativeLayout) rootView.findViewById(R.id.l_city_create_list);
		r2 = (RelativeLayout) rootView.findViewById(R.id.l_company_create_list);
		r3 = (RelativeLayout) rootView.findViewById(R.id.l_category_create_list);
		r4 = (RelativeLayout) rootView.findViewById(R.id.l_seats_create_list);
		r5 = (RelativeLayout) rootView.findViewById(R.id.l_departure_create_list);
		r6 = (RelativeLayout) rootView.findViewById(R.id.l_return_create_list);
		r1.setOnClickListener(this);
		r2.setOnClickListener(this);
		r3.setOnClickListener(this);
		r4.setOnClickListener(this);
		r5.setOnClickListener(this);
		r6.setOnClickListener(this);
		b_create_list.setOnClickListener(this);
		tv_sec_city.setOnClickListener(this);
		tv_sec_company.setOnClickListener(this);
		tv_sec_seats.setOnClickListener(this);
		tv_sec_departure.setOnClickListener(this);
		tv_sec_return.setOnClickListener(this);
		tv_sec_category.setOnClickListener(this);
		l1_company.setVisibility(View.GONE);

		tv1.setTypeface(tf);
		tv2.setTypeface(tf);
		tv3.setTypeface(tf);
		tv4.setTypeface(tf);
		tv5.setTypeface(tf);
		tv6.setTypeface(tf);
		tv7.setTypeface(tf);
		tv8.setTypeface(tf);
		tv_sec_city.setTypeface(tf);
		tv_sec_company.setTypeface(tf);
		tv_sec_seats.setTypeface(tf);
		tv_sec_category.setTypeface(tf);
		tv_sec_departure.setTypeface(tf);
		tv_sec_return.setTypeface(tf);
		b_create_list.setTypeface(tf);
		t1.setTypeface(tf);
		rb1.setTypeface(tf);
		rb2.setTypeface(tf);
		rb3.setTypeface(tf);
		rb4.setTypeface(tf);
		et_description.setTypeface(tf);
		et_form.setTypeface(tf);
		et_route.setTypeface(tf);
		et_title.setTypeface(tf);
		et_to.setTypeface(tf);

		rg_carpool.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio0) {
					s_carpool = "Seeker";
				} else if (checkedId == R.id.radio1) {
					s_carpool = "Provider";
				}
			}
		});
		rg_carpool_type.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.radio3) {
					s_carpool_type = "0";
					l1_company.setVisibility(View.GONE);
					tv_sec_company.setText("Select Company");
					company_id = "0";
				} else if (checkedId == R.id.radio4) {
					s_carpool_type = "1";
					l1_company.setVisibility(View.VISIBLE);
				}
			}
		});
		return rootView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// check if the request code is same as what is passed here it is 2
		String message = data.getStringExtra("company_name");
		if (requestCode == 2) {
			company_id = data.getStringExtra("company_id");
			tv_sec_company.setText(message);
		} else {
			company_id = data.getStringExtra("company_id");
			tv_sec_company.setText(message);
			iv4.setVisibility(View.VISIBLE);
			iv04.setVisibility(View.INVISIBLE);
			tv4.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(tv_sec_company) || v.equals(r2)) {
			Intent intent = new Intent(getActivity(), company_list.class);
			startActivityForResult(intent, 2);

		}
		if (v.equals(tv_sec_city) || v.equals(r1)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, tv_sec_city);
		}
		if (v.equals(tv_sec_category) || v.equals(r3)) {
			String[] category = { "Carpool", "Cab", "Rideshare" };
			dialog("Category", category, tv_sec_category);

		}
		if (v.equals(tv_sec_departure) || v.equals(r5)) {
			String[] departure = new String[23];
			for (int i = 0; i <= 11; i++) {
				departure[i] = String.valueOf(i + " - " + (i + 1) + " am");
			}
			for (int i = 1; i <= 11; i++) {
				departure[i + 11] = String.valueOf(i + " - " + (i + 1) + " pm");
			}
			dialog("Departure", departure, tv_sec_departure);

		}
		if (v.equals(tv_sec_return) || v.equals(r6)) {
			String[] departure = new String[23];
			for (int i = 0; i <= 11; i++) {
				departure[i] = String.valueOf(i + " - " + (i + 1) + " am");
			}
			for (int i = 1; i <= 11; i++) {
				departure[i + 11] = String.valueOf(i + " - " + (i + 1) + " pm");
			}
			dialog("Return", departure, tv_sec_return);

		}
		if (v.equals(tv_sec_seats) || v.equals(r4)) {
			String[] seats = new String[10];
			for (int i = 0; i <= 9; i++) {
				seats[i] = String.valueOf(i + 1);
			}
			dialog("Seats", seats, tv_sec_seats);
		}
		if (v.equals(b_create_list)) {
			create_list();
			// Toast.makeText(getActivity(), et_title.getText().toString(),
			// Toast.LENGTH_LONG).show();
		}
	}

	public void create_list() {
		if (tv_sec_city.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(getActivity(), "Select City", Toast.LENGTH_LONG).show();
		} else if (tv_sec_company.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")
				&& s_carpool_type.equals("1")) {
			Toast.makeText(getActivity(), "Select Company", Toast.LENGTH_LONG).show();
		} else if (tv_sec_category.getText().toString().toUpperCase().equals("SELECT CATEGORY")) {

			Toast.makeText(getActivity(), "Select category first", Toast.LENGTH_LONG).show();
		} else if (et_title.getText().toString().length() < 1) {
			Toast.makeText(getActivity(), "Enter Title", Toast.LENGTH_LONG).show();
		} else if (et_description.getText().toString().length() < 1) {
			Toast.makeText(getActivity(), "Enter Description", Toast.LENGTH_LONG).show();
		} else if (et_form.getText().toString().length() < 1) {
			Toast.makeText(getActivity(), "Enter From/ Source", Toast.LENGTH_LONG).show();
		} else if (et_to.getText().toString().length() < 1) {
			Toast.makeText(getActivity(), "Enter To/ Destination", Toast.LENGTH_LONG).show();
		} else if (et_route.getText().toString().length() < 1) {
			Toast.makeText(getActivity(), "Enter Route", Toast.LENGTH_LONG).show();
		} else if (tv_sec_seats.getText().toString().toUpperCase().equals("SELECT SEATS")) {
			Toast.makeText(getActivity(), "Select Seats first", Toast.LENGTH_LONG).show();
		} else if (tv_sec_departure.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(getActivity(), "Select Departure first", Toast.LENGTH_LONG).show();
		} else if (tv_sec_return.getText().toString().toUpperCase().substring(0, 6).equals("SELECT")) {
			Toast.makeText(getActivity(), "Select Return first", Toast.LENGTH_LONG).show();
		} else {

			if (task.getString("user_id", null) != null) {
				try {
					String type = new database_method().get_data(getActivity(),
							"select type_id from type where type_name='" + s_carpool + "'");
					String city = new database_method().get_data(getActivity(),
							"select id from city where city_name='" + tv_sec_city.getText().toString() + "'");
					String category = new database_method().get_data(getActivity(),
							"select cat_id from category where cat_name='" + tv_sec_category.getText().toString()
									+ "'");
					String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
					// id,user_id,type,city,company_id,fro_m,t_o,route,seats,departure_time,return_trip,return_time,title,desc,date,hits,enable,sync
					DataBaseHelper db = new DataBaseHelper(getActivity());
					String data[] = { "", task.getString("user_id", null), type, category, city, company_id,
							et_form.getText().toString(), et_to.getText().toString(), et_route.getText().toString(),
							tv_sec_seats.getText().toString(), tv_sec_departure.getText().toString(), "0",
							tv_sec_return.getText().toString(), et_title.getText().toString(),
							et_description.getText().toString(), timeStamp, "0", "1", "0" };

					db.insert_ad(data);
					Toast.makeText(getActivity(), "List Created", Toast.LENGTH_LONG).show();
					set_blank();
				} catch (Exception e) {
					Toast.makeText(getActivity(), "Error=" + e, Toast.LENGTH_LONG).show();
				}
			} else {
				Toast.makeText(getActivity(), "Please Login First", Toast.LENGTH_LONG).show();
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
				if (tv.equals(tv_sec_city)) {
					iv2.setVisibility(View.VISIBLE);
					iv02.setVisibility(View.INVISIBLE);
					tv2.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_category)) {
					iv5.setVisibility(View.VISIBLE);
					iv05.setVisibility(View.INVISIBLE);
					tv5.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_seats)) {
					iv11.setVisibility(View.VISIBLE);
					iv011.setVisibility(View.INVISIBLE);
					tv6.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_departure)) {
					iv12.setVisibility(View.VISIBLE);
					iv012.setVisibility(View.INVISIBLE);
					tv7.setVisibility(View.VISIBLE);
				} else if (tv.equals(tv_sec_return)) {
					iv13.setVisibility(View.VISIBLE);
					iv013.setVisibility(View.INVISIBLE);
					tv8.setVisibility(View.VISIBLE);
				}
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

	public void set_blank() {
		tv_sec_city.setText("Select City");
		tv_sec_company.setText("Select Company");
		tv_sec_category.setText("Select Category");
		tv_sec_seats.setText("Select Seats");

		tv_sec_departure.setText("Select Departure");
		tv_sec_return.setText("Select Return");
		et_title.setText("");
		et_description.setText("");
		et_form.setText("");
		et_to.setText("");
		et_route.setText("");
	}
	
	public void onBackPressed()
	{
	    
	}
	
}
