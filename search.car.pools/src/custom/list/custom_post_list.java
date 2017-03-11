package custom.list;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import array.list.Child;
import array.list.Group;
import array.list.ad_array_list;

import com.example.search.car.pools.R;

public class custom_post_list extends ArrayAdapter<ad_array_list> {

	private final Activity context;
	private ArrayList<ad_array_list> ad_item;

	public custom_post_list(Context con, ArrayList<ad_array_list> ad_item) {
		super(con, R.layout.custom_post_list_item_new, ad_item);
		this.context = (Activity) con;
		this.ad_item = ad_item;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView = inflater.inflate(R.layout.custom_post_list_item_new, null, true);
		TextView tv_name = (TextView) rowView.findViewById(R.id.tv_user_name);
		TextView tv_profession = (TextView) rowView.findViewById(R.id.tv_user_profession);
		TextView tv_date = (TextView) rowView.findViewById(R.id.tv_date);
		TextView tv_from = (TextView) rowView.findViewById(R.id.tv_user_from);
		TextView tv_to = (TextView) rowView.findViewById(R.id.tv_user_to);
		TextView tv_desc = (TextView) rowView.findViewById(R.id.tv_user_listing_description);
		ImageView image = (ImageView) rowView.findViewById(R.id.iv_user_image);

		tv_name.setText(ad_item.get(position).getUser_name());
		tv_profession.setText("/ " + ad_item.get(position).getUser_profession());
		String[] date = (ad_item.get(position).getPost()).split("-");
		tv_date.setText(date[2]);
		tv_from.setText(ad_item.get(position).getFrom());
		tv_to.setText("- " + ad_item.get(position).getTo());
		tv_desc.setText(ad_item.get(position).getDesc());
//		image.setImageBitmap(getRoundedShape(decodeFile(context, R.drawable.provider), 80));
		
		return rowView;
	}

	public static Bitmap decodeFile(Context context, int resId) {
		try {
			// decode image size
			// mContext=context;
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeResource(context.getResources(), resId, o);
			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 200;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeResource(context.getResources(), resId, o2);
		} catch (Exception e) {
		}
		return null;
	}

	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage, int width) {
		// TODO Auto-generated method stub
		int targetWidth = width;
		int targetHeight = width;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2, ((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2), Path.Direction.CCW);
		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight()),
				new Rect(0, 0, targetWidth, targetHeight), null);
		return targetBitmap;
	}

}