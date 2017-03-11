package custom.list;

import java.util.ArrayList;

import com.example.search.car.pools.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import array.list.comp_array_list;

public class custom_company_list extends ArrayAdapter<comp_array_list> {

	private final Activity context;
	private ArrayList<comp_array_list> ad_item;

	public custom_company_list(Context con, ArrayList<comp_array_list> ad_item) {
		super(con, R.layout.custom_company_list, ad_item);
		this.context = (Activity) con;
		this.ad_item = ad_item;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.custom_company_list, null, true);
		RelativeLayout l = (RelativeLayout) rowView.findViewById(R.id.l1_top_company_list);
		if (position == 0) {
			l.setVisibility(View.VISIBLE);
		} else {
			l.setVisibility(View.GONE);
		}
		l.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		LinearLayout ll_main = (LinearLayout) rowView.findViewById(R.id.ll_main);
		LinearLayout l1 = (LinearLayout) rowView.findViewById(R.id.ll_company_image);
		LinearLayout l2 = (LinearLayout) rowView.findViewById(R.id.ll_company_details);
		if (!(position % 2 == 0)) {
			ll_main.removeAllViews();
			ll_main.addView(l2);
			ll_main.addView(l1);
		}
		TextView name = (TextView) rowView.findViewById(R.id.tv_company_name);
		TextView address = (TextView) rowView.findViewById(R.id.tv_address);
		
		name.setText(getCapitalWords(ad_item.get(position).getCompany_name()));
		address.setText(getCapitalWords(ad_item.get(position).getCompany_add()));
		
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Heavy.otf");
		name.setTypeface(tf);
		address.setTypeface(tf);
		TextView t1 = (TextView)rowView.findViewById(R.id.tv_add_company);
//		TextView t2 = (TextView)rowView.findViewById(R.id.tv_company);
		t1.setTypeface(tf);
//		t2.setTypeface(tf);
		return rowView;
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
}