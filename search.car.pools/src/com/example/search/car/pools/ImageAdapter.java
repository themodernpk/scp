package com.example.search.car.pools;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private static final int[] ids = { R.raw.slide_1_mid_image, R.raw.slide_2_mid_image, R.raw.slide_3_mid_image, R.raw.slide_4_mid_image,
			R.raw.slide_5_mid_image};
	String[] str = {"LARGEST CARPOOL COMMUNITY", "ROUTE TRACKING VIA GOOGLE MAP" ,"SEND FREE SMS TO CARPOOLERS", "FREE CORPORATE CARPOOLING",
	"83,287 CARPOOLS & 28,536 CARPOOLERS"};

	public ImageAdapter(Context context) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}

	@Override
	public int getCount() {
		return ids.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.splash_layout, null);
		}
		SVG svg2 = SVGParser.getSVGFromResource(context.getResources(), ids[position]);
		((ImageView) convertView.findViewById(R.id.slide_1)).setImageDrawable(svg2.createPictureDrawable());
		((TextView) convertView.findViewById(R.id.text_1)).setText(str[position]);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd-Heavy.otf");
		((TextView) convertView.findViewById(R.id.text_1)).setTypeface(tf);
		return convertView;
	}

}