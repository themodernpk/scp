package custom.list;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;

import com.example.search.car.pools.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import array.list.Header;
import array.list.ad_array_list;

public class ExpandListSearchAdapter extends BaseExpandableListAdapter {

	public Bitmap bmp = null;
	private Context context;
	private ArrayList<Header> groups;

	public ExpandListSearchAdapter(Context context, ArrayList<Header> groups) {
		this.context = context;
		this.groups = groups;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		ArrayList<ad_array_list> chList = new ArrayList<ad_array_list>();
		try {
			chList = groups.get(groupPosition).getItems();
			return chList.get(childPosition);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.custom_post_list_item_new, null);
		}
		if (getGroupCount() != 0) {

			ad_array_list child = (ad_array_list) getChild(groupPosition, childPosition);

			View viewLastLine = (View) convertView.findViewById(R.id.view2);
			if (getChildrenCount(0) != 0) {
				if (childPosition == getChildrenCount(groupPosition) - 1) {
					viewLastLine.setVisibility(View.INVISIBLE);
				} else {
					viewLastLine.setVisibility(View.VISIBLE);
				}
			}

			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_user_name);
			TextView tv_profession = (TextView) convertView.findViewById(R.id.tv_user_profession);
			TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			TextView tv_month_below = (TextView) convertView.findViewById(R.id.tv_month_below);
			TextView tv_from = (TextView) convertView.findViewById(R.id.tv_user_from);
			TextView tv_to = (TextView) convertView.findViewById(R.id.tv_user_to);
			TextView tv_from_lower = (TextView) convertView.findViewById(R.id.tv_user_from_lower);
			TextView tv_to_lower = (TextView) convertView.findViewById(R.id.tv_user_to_lower);
			TextView tv_desc = (TextView) convertView.findViewById(R.id.tv_user_listing_description);
			ImageView image = (ImageView) convertView.findViewById(R.id.iv_user_image);
			LinearLayout l_upper = (LinearLayout)convertView.findViewById(R.id.l4_from_to_upper);
			LinearLayout l_lower = (LinearLayout)convertView.findViewById(R.id.l4_from_to_lower);
			try {
			ImageView iv_above = (ImageView) convertView.findViewById(R.id.iv_user_image_gravatar);
			SVG svg1 = SVGParser.getSVGFromResource(context.getResources(), R.raw.circle_gravatar);
			iv_above.setImageDrawable(svg1.createPictureDrawable());

			ImageView iv_A1 = (ImageView) convertView.findViewById(R.id.iv_A);
			
			ImageView iv_B1 = (ImageView) convertView.findViewById(R.id.iv_B);
			

			tv_name.setText(getCapitalWords(child.getUser_name()));
			if (child.getUser_profession().contentEquals("") || child.getUser_profession() == null)
				tv_profession.setText("");
			else
				tv_profession.setText("/ " + child.getUser_profession().toUpperCase());
			String[] date = (child.getPost()).split("-");
			String str = date[2];
			tv_date.setText(str);
			tv_from.setText(getCapitalWords(child.getFrom()));
			tv_to.setText(getCapitalWords(child.getTo()));
			tv_from_lower.setText(getCapitalWords(child.getFrom()));
			tv_to_lower.setText(getCapitalWords(child.getTo()));
			
			ImageView iv_A = (ImageView) convertView.findViewById(R.id.iv_from);
			SVG svg_a = SVGParser.getSVGFromResource(context.getResources(), R.raw.location_a);
			iv_A.setImageDrawable(svg_a.createPictureDrawable());

			ImageView iv_B = (ImageView) convertView.findViewById(R.id.iv_to);
			SVG svg_b = SVGParser.getSVGFromResource(context.getResources(), R.raw.location_b);
			iv_B.setImageDrawable(svg_b.createPictureDrawable());
			
			iv_A1.setImageDrawable(svg_a.createPictureDrawable());
			iv_B1.setImageDrawable(svg_b.createPictureDrawable());
			
			if (child.getFrom().length() > 10 || child.getTo().length() >10){
				l_upper.setVisibility(View.GONE);
				l_lower.setVisibility(View.VISIBLE);
			} else {
				l_upper.setVisibility(View.VISIBLE);
				l_lower.setVisibility(View.GONE);
			}
			String month_below = new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1];
			tv_month_below.setText(month_below.substring(0, 3));
			
			
			String desc = child.getDesc();
			if (desc != null && desc.length() > 2)
				tv_desc.setText(child.getDesc().substring(0, 1).toUpperCase() + child.getDesc().substring(1));

			Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd_Book.otf");
			tv_name.setTypeface(tf);
			tv_profession.setTypeface(tf);
			tv_date.setTypeface(tf);
			tv_from.setTypeface(tf);
			tv_to.setTypeface(tf);
			tv_desc.setTypeface(tf);
			tv_month_below.setTypeface(tf);

			String gender = child.getGender();
			// int gender = Integer.parseInt(gender_1);

			int category = Integer.parseInt(child.getType());
			String default_image_url = "";

			if (category == 1 && gender.contentEquals("0"))
				default_image_url = "http://s17.postimg.org/kxbsajban/seeker_male.png";
			else if (category == 1 && gender.contentEquals("1")) {
				default_image_url = "http://s17.postimg.org/qitm0umlr/seeker_female.png";
			} else if (category == 2 && gender.contentEquals("0"))
				default_image_url = "http://s17.postimg.org/mjwei0xyn/provider_male.png";
			else if (category == 2 && gender.contentEquals("1"))
				default_image_url = "http://s17.postimg.org/sm418imen/provider_female.png";
			else
				default_image_url = "http://s24.postimg.org/rtj6c6abl/no_gender.png";

			String hash = MD5Util.md5Hex(child.getEmail().toLowerCase().trim());
			String gravatarUrl = "http://www.gravatar.com/avatar/" + hash + "?d=" + default_image_url +"&s=150";

			Picasso.with(context).load(gravatarUrl).into(image);
			} catch (Exception e){
				//e.printStackTrace();
			}

			// new getGravatarImage(image, gravatarUrl).execute();
			// RoundedBitmapDrawable dr =
			// RoundedBitmapDrawableFactory.create(context.getResources(), bmp);
			// dr.setCornerRadius(Math.max(bmp.getWidth(), bmp.getHeight()) /
			// 2.0f);
			// image.setImageDrawable(dr);
		}
		 
		return convertView;
	}

	private CharSequence getCapitalWords(String sentence) {
		String[] words = null;
		if (sentence != null && sentence.length() > 2) {
			try {
				words = sentence.split(" ");
				for (int i = 0; i < words.length; i++) {
					if (words[i].length() != 0)
						words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
			return TextUtils.join(" ", words);
		} else
			return sentence;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		try {
		ArrayList<ad_array_list> chList = groups.get(groupPosition).getItems();
		return chList.size();
		} catch (Exception e){
			//e.printStackTrace();
		}
		return 0;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
//		return groups.get(groupPosition);
		Header header = null;//) getGroup(groupPosition);
		try {
//			header = groups.get(groupPosition);
			return groups.get(groupPosition);
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Header header = (Header) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = inf.inflate(R.layout.header_item, null);
		}
		final LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll_above_ex_list);
		if (getGroupCount() > 0 && groupPosition == 0) {
			ll.setVisibility(View.VISIBLE);
		} else {
			ll.setVisibility(View.GONE);
		}
		TextView tv = (TextView) convertView.findViewById(R.id.tv_header_item);
		tv.setText(header.getdate().replaceAll(".(?!$)", "$0 "));
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "AvenirLTStd_Book.otf");
		tv.setTypeface(tf);
		ExpandableListView eLV = (ExpandableListView) parent;
		eLV.expandGroup(groupPosition);
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	public static class MD5Util {
		public static String hex(byte[] array) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}
			return sb.toString();
		}

		public static String md5Hex(String message) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				return hex(md.digest(message.getBytes("CP1252")));
			} catch (NoSuchAlgorithmException e) {
			} catch (UnsupportedEncodingException e) {
			}
			return null;
		}
	}

	public class getGravatarImage extends AsyncTask<Void, Void, Void> {

		ImageView image;
		String gravatarUrl;

		public getGravatarImage(ImageView image, String gravatarUrl) {
			// TODO Auto-generated constructor stub
			this.image = image;
			this.gravatarUrl = gravatarUrl;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				URL url = new URL(gravatarUrl);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				bmp = BitmapFactory.decodeStream(input);
			} catch (IOException e) {
				//e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}

	}
}
