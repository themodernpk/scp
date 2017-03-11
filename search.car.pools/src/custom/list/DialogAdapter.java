package custom.list;

import com.example.search.car.pools.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import post.details.post_details_main;

public class DialogAdapter extends BaseAdapter{

	private Context context;
	private String[] arr;
	public DialogAdapter(Activity activity, String[] arr) {
		// TODO Auto-generated constructor stub
		this.context = activity;
		this.arr = arr;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arr.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = ( (Activity) context).getLayoutInflater();
		View rowView = inflater.inflate(R.layout.dialog_item, null, true);
		TextView t = (TextView)rowView.findViewById(R.id.tv_item);
		ImageView i = (ImageView)rowView.findViewById(R.id.iv_item);
		t.setText(arr[pos]);
		SVG svg = SVGParser.getSVGFromResource(context.getResources(), R.raw.arrow_right_green);
		i.setImageDrawable(svg.createPictureDrawable());
		
		View v = (View)rowView.findViewById(R.id.view_green_top);
		if (pos==0)
			v.setVisibility(View.VISIBLE);
		else
			v.setVisibility(View.GONE);
		
		View v1 = (View)rowView.findViewById(R.id.view_bottom);
		TextView t2 = (TextView)rowView.findViewById(R.id.textView1);
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		if (pos==arr.length-1){
			v1.setVisibility(View.GONE);
			t2.setVisibility(View.GONE);
		}
		return rowView;
	}

}
