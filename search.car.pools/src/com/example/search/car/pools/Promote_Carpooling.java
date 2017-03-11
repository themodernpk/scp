package com.example.search.car.pools;

import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import custom.list.DialogAdapter;
import custom.list.database_method;
import data.service.ServiceHandler;
import data.service.url;

@SuppressLint("NewApi")
public class Promote_Carpooling extends Fragment implements OnClickListener {
	EditText et_c_name, et_c_address, et_name, et_official_email, et_contact_number, et_designation;
	TextView sp_city,t1;
	Button b_register;
	String url = "";
	RelativeLayout rl;
	ImageView i1, i01, i2, i3, i4, i5, i6, i7, i8, i02,i05,i06,i07,i08;
	ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.permote_carpolling, container, false);
		cd = new ConnectionDetector(getActivity());

		et_c_name = (EditText) rootView.findViewById(R.id.et_company_name);
		et_c_address = (EditText) rootView.findViewById(R.id.et_company_address);
		et_name = (EditText) rootView.findViewById(R.id.et_your_name);
		et_official_email = (EditText) rootView.findViewById(R.id.et_offical_email);
		et_contact_number = (EditText) rootView.findViewById(R.id.et_contact_number);
		et_designation = (EditText) rootView.findViewById(R.id.et_designation);
		sp_city = (TextView) rootView.findViewById(R.id.sp_sec_city);
		b_register = (Button) rootView.findViewById(R.id.b_company_register);
		b_register.setOnClickListener(this);
		sp_city.setOnClickListener(this);

		i1 = (ImageView) rootView.findViewById(R.id.iv_1);
		i01 = (ImageView) rootView.findViewById(R.id.iv_01);
		i02 = (ImageView) rootView.findViewById(R.id.iv_02);
		i05 = (ImageView) rootView.findViewById(R.id.iv_05);
		i06 = (ImageView) rootView.findViewById(R.id.iv_06);
		i07 = (ImageView) rootView.findViewById(R.id.iv_07);
		i08 = (ImageView) rootView.findViewById(R.id.iv_08);
		
		i2 = (ImageView) rootView.findViewById(R.id.iv_2);
		i3 = (ImageView) rootView.findViewById(R.id.iv_3);
		i4 = (ImageView) rootView.findViewById(R.id.iv_4);
		i5 = (ImageView) rootView.findViewById(R.id.iv_5);
		i6 = (ImageView) rootView.findViewById(R.id.iv_6);
		i7 = (ImageView) rootView.findViewById(R.id.iv_7);
		i8 = (ImageView) rootView.findViewById(R.id.iv_8);
		t1 = (TextView)rootView.findViewById(R.id.tv_2_create_list);
		rl = (RelativeLayout)rootView.findViewById(R.id.l_city_create_list);
		rl.setOnClickListener(this);
		
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd-Heavy.otf");
		t1.setTypeface(tf);
		et_c_name.setTypeface(tf); et_c_address.setTypeface(tf); et_name.setTypeface(tf); et_official_email.setTypeface(tf); 
		et_contact_number.setTypeface(tf); et_designation.setTypeface(tf);
		sp_city.setTypeface(tf);
		b_register.setTypeface(tf);
		
//		Resources r = getResources();
//		int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
//		
//		final FloatLabelLayout fl = (FloatLabelLayout)rootView.findViewById(R.id.fl1);
//		final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)fl.getLayoutParams();
//		params.setMargins(35, 5, 20, 0); //substitute parameters for left, top, right, bottom
//		
//		final RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)fl.getLayoutParams();
//		params.setMargins(px, 20, 20, 0);
		
		
		
		et_c_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i1.setVisibility(View.INVISIBLE);
				i01.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i1.setVisibility(View.VISIBLE);
					i01.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_c_address.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i2.setVisibility(View.INVISIBLE);
				i02.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i2.setVisibility(View.VISIBLE);
					i02.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i5.setVisibility(View.INVISIBLE);
				i05.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i5.setVisibility(View.VISIBLE);
					i05.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_official_email.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i6.setVisibility(View.INVISIBLE);
				i06.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i6.setVisibility(View.VISIBLE);
					i06.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_contact_number.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i7.setVisibility(View.INVISIBLE);
				i07.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i7.setVisibility(View.VISIBLE);
					i07.setVisibility(View.INVISIBLE);
				}
			}
		});
		et_designation.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				i8.setVisibility(View.INVISIBLE);
				i08.setVisibility(View.VISIBLE);
				if (s.length() < 1){
					i8.setVisibility(View.VISIBLE);
					i08.setVisibility(View.INVISIBLE);
				}
			}
		});

		return rootView;
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
					i3.setVisibility(View.VISIBLE);
					i3.setImageResource(R.drawable.blue_circle_small);
					i4.setVisibility(View.GONE);
					t1.setVisibility(View.VISIBLE);
				} else {
					
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(sp_city) || v.equals(rl)) {
			String[] city = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };
			dialog("City", city, sp_city);
		}
		if (v.equals(b_register)) {
			if (et_c_name.getText().toString().length() == 0 )
				Toast.makeText(getActivity(), "Please Enter Company Name", Toast.LENGTH_LONG).show();
			else if (et_c_address.getText().toString().length() == 0 )
				Toast.makeText(getActivity(), "Please Enter Company Address", Toast.LENGTH_LONG).show();
			else if (sp_city.getText().toString().toUpperCase().equals("SELECT CITY")) {
				Toast.makeText(getActivity(), "Please Select City", Toast.LENGTH_LONG).show();
			}else if (et_name.getText().toString().length() == 0 )
				Toast.makeText(getActivity(), "Please Enter Your Name", Toast.LENGTH_LONG).show();
			else if (!(new EmailAndPhoneChecker().validEmail(et_official_email.getText().toString())))
				Toast.makeText(getActivity(), "Please a valid Email Address", Toast.LENGTH_SHORT).show();
			else if (et_contact_number.getText().toString().length()<8 || et_contact_number.getText().toString().length()>14)
				Toast.makeText(getActivity(), "Please Enter a valid Contact No.", Toast.LENGTH_LONG).show();
			else if (et_designation.getText().toString().length() == 0 )
				Toast.makeText(getActivity(), "Please Enter Your Designation", Toast.LENGTH_LONG).show();
			 else {
				String s_city = new database_method().get_data(getActivity(),
						"select id from city where city_name='" + sp_city.getText().toString() + "'");
				url = new url().create_company + "&company_name=" + et_c_name.getText().toString() + "&company_add="
						+ et_c_address.getText().toString() + "&city_id=" + s_city + "&c_name="
						+ et_name.getText().toString() + "&c_email=" + et_official_email.getText().toString()
						+ "&c_phone=" + et_contact_number.getText().toString() + "&c_designation="
						+ et_designation.getText().toString();
				SharedPreferences task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
				if (task.getString("user_id", null) != null) {
					ConnectionDetector cd = new ConnectionDetector(getActivity());
					if (cd.isConnectingToInternet()) {
						accessWebService();
					} else {
						Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
					}

				} else {
					Toast.makeText(getActivity(), "Please Login first", Toast.LENGTH_LONG).show();
				}
			}
		}
	}

	private class SoapAccessTask extends AsyncTask<String, Void, String> {

		private ProgressDialog progress = null;

		@Override
		protected void onPreExecute() {
			progress = ProgressDialog.show(getActivity(), null, "Loading ! please Wait...");

		}

		@Override
		protected String doInBackground(String... urls) {
			String webResponse = "true";
			ServiceHandler sh = new ServiceHandler();
			String url1 = url.replace(" ", "%20");
			//Log.d("url: ", "> " + url1);
			String jsonStr = sh.makeServiceCall(url1, ServiceHandler.GET);
			//Log.d("Response: ", "> " + jsonStr);
			if (jsonStr != null) {
				webResponse = jsonStr;
			} else {
				//Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return webResponse;
		}

		protected void onPostExecute(String result) {
			progress.dismiss();
			Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
			if (result.toUpperCase().equals("COMPANY CREATED SUCCESSFULLY")) {
				et_c_name.setText("");
				et_c_address.setText("");
				et_name.setText("");
				et_official_email.setText("");
				et_contact_number.setText("");
				et_designation.setText("");
				sp_city.setText("Select City");
			}
		}

	}

	public void accessWebService() {
		SoapAccessTask task = new SoapAccessTask();

		// passes values for the urls strin`g array
		task.execute(new String[] { "USD", "LKR" });
	}
	
}
