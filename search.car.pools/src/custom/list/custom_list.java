package custom.list;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.search.car.pools.R;

public class custom_list extends ArrayAdapter<String> {

	int img_arr[] = { R.drawable.delhi, R.drawable.mumbai, R.drawable.kolkata, R.drawable.pune, R.drawable.bengaluru,
			R.drawable.ahmedabad };
	String name[];
	private final Activity context;

	public custom_list(Context con, String arr[]) {
		super(con, R.layout.custom_cities_item, arr);
		this.context = (Activity) con;
		this.name = arr;

	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.custom_cities_item, null, true);
		TextView tv_name = (TextView) rowView.findViewById(R.id.tv_custom_cities_item);
		ImageView img = (ImageView) rowView.findViewById(R.id.iv_city_custom_cities_item);
		tv_name.setText(name[position]);
		img.setBackgroundResource(img_arr[position]);
		TextView text = (TextView) rowView.findViewById(R.id.tv_custom_cities_item);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd_Book.otf");
		text.setTypeface(tf);
		LinearLayout ll = (LinearLayout) rowView.findViewById(R.id.l1_cities_custom_cities_item);

		if (position == 0) {
			ll.setBackgroundColor(Color.parseColor("#121c30"));
			ll.setAlpha(0.9f);
		}
		if (position == 1) {
			ll.setBackgroundColor(Color.parseColor("#082d34"));
			ll.setAlpha(0.9f);
		}
		if (position == 2) {
			ll.setBackgroundColor(Color.parseColor("#1b243a"));
			ll.setAlpha(0.92f);
		}
		if (position == 3) {
			ll.setBackgroundColor(Color.parseColor("#2b182d"));
			ll.setAlpha(0.92f);
		}
		if (position == 4) {
			ll.setBackgroundColor(Color.parseColor("#2b1b1b"));
			ll.setAlpha(0.92f);
		}
		if (position == 5) {
			ll.setBackgroundColor(Color.parseColor("#2f2d20"));
			ll.setAlpha(0.92f);
		}
		return rowView;
	}

}