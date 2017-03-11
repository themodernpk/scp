package dash.board;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.example.search.car.pools.DataBaseHelper;
import com.example.search.car.pools.R;
import com.example.search.car.pools.search_result;
import com.example.search.car.pools.user_login;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.nirhart.parallaxscroll.views.ParallaxExpandableListView;
import com.squareup.picasso.Picasso;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import array.list.Child;
import array.list.Group;
import custom.list.ExpandListSearchAdapter.MD5Util;
import post.details.post_details_main;

public class my_listing extends Fragment implements OnRefreshListener {
	private ExpandListAdapter ExpAdapter;
	private ArrayList<Group> ExpListItems;
	private ParallaxExpandableListView ExpandList;
	private SQLiteDatabase database;
	DataBaseHelper dataBaseHelper;
	ArrayList<Group> list = new ArrayList<Group>();
	SharedPreferences task;

	// timer for updating listings every 5 secs
	Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();

	private SwipeRefreshLayout swipeRefreshLayout;
	TextView tv_empty, tv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.dashboard_item, container, false);
		ExpandList = (ParallaxExpandableListView) rootView.findViewById(R.id.exp_list);

		// for cancelling notification
		NotificationManager nm = (NotificationManager) getActivity()
				.getSystemService(getActivity().NOTIFICATION_SERVICE);
		nm.cancel(1234567890);

		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		// tv_empty.setText("No List
		// Items");//rootView.findViewById(R.id.tv_empty);
		// tv_empty.setTextSize(20);
		//
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);

		ExpListItems = SetStandardGroups();
		tv = new TextView(getActivity());
		if (ExpListItems.size() == 0) {
			tv.setText("No data found.\nPlease add a listing.");
			tv.setTextSize(25);
			tv.setGravity(Gravity.CENTER);
			ExpandList.addFooterView(tv);
		} else {
			ExpandList.removeFooterView(tv);
		}
		ExpAdapter = new ExpandListAdapter(getActivity(), ExpListItems);
		ExpandList.setAdapter(ExpAdapter);
		ExpandList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition,
					long id) {
				// dialog_first(list.get(groupPosition).getItems().get(childPosition).getAd_id(),
				// list.get(groupPosition).getItems().get(childPosition).getMessage(),
				// list.get(groupPosition).getItems().get(childPosition).getResponse_id());
				return true;
			}
		});
	}

	public ArrayList<Group> SetStandardGroups() {

		list.clear();

		dataBaseHelper = new DataBaseHelper(getActivity());

		database = dataBaseHelper.getReadableDatabase();
		Cursor c = database.rawQuery("select title, id, date from ad where user_id='" + task.getString("user_id", null)
				+ "' and enable='1' ORDER BY local_id DESC", null);

		try {
			if (c != null) {
				while (c.moveToNext()) {
					ArrayList<Child> ch_list1 = new ArrayList<Child>();

					Cursor c1 = database.rawQuery(
							"select sender_name,sender_phone,sender_email,date,msg ,local_response_id, ad_id from response where ad_id='"
									+ c.getInt(1) + "' and (receiver_id='" + task.getString("user_id", null)
									+ "' or sender_id='" + task.getString("user_id", null)
									+ "') and visibility='1' ORDER BY local_response_id DESC",
							null);

					while (c1.moveToNext()) {
						ch_list1.add(new Child(c1.getString(0), c1.getString(1), c1.getString(2), c1.getString(3),
								c1.getString(4), c.getInt(1), c1.getString(5), c1.getString(6)));
					}
					list.add(new Group(c.getString(0), c.getString(2), ch_list1));
				}
			} else if (c == null) {
				// return null;
			}

		} catch (Exception e) {

		} finally {
			c.close();
			database.close();
		}

		return list;
	}

	/*
	 * public void dialog_first(final String ad_id, final String title, final
	 * String response_id) { String arr[] = { "Reply", "Delete" }; final Dialog
	 * dialog = new Dialog(getActivity()); dialog.setTitle(title);
	 * dialog.setContentView(R.layout.dialog); final ListView list = (ListView)
	 * dialog.findViewById(R.id.list_cities);
	 * 
	 * ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
	 * android.R.layout.simple_list_item_1, android.R.id.text1, arr);
	 * list.setAdapter(adapter);
	 * list.setBackgroundColor(Color.parseColor("#919191"));
	 * list.setOnItemClickListener(new OnItemClickListener() {
	 * 
	 * @Override public void onItemClick(AdapterView<?> parent, View view, int
	 * position, long id) { if (position == 0) { dialog_reply(title,
	 * response_id);
	 * 
	 * }
	 * 
	 * if (position == 1) { dataBaseHelper.delete_response(response_id);
	 * SetStandardGroups(); ExpAdapter.notifyDataSetChanged(); }
	 * 
	 * dialog.hide(); } }); dialog.show(); }
	 */

	public void dialog_reply(String title, String response_id) {
		final String data[] = new String[15];
		final String timeStamp = "Draft";// new SimpleDateFormat("yyyy-MM-dd",
											// Locale.getDefault()).format(new
											// Date());
		final Dialog dialog = new Dialog(getActivity());
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.setContentView(R.layout.reply_dialog);
		final TextView t = (TextView) dialog.findViewById(R.id.tv_1_send_msg);
		t.setText(title);
		final EditText et_msg = (EditText) dialog.findViewById(R.id.et_your_dialog_msg);
		Button b_send_email = (Button) dialog.findViewById(R.id.b_send_email);
		// Button b_send_sms = (Button) dialog.findViewById(R.id.b_send_sms);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "AvenirLTStd_Book.otf");
		t.setTypeface(tf);
		et_msg.setTypeface(tf);
		b_send_email.setTypeface(tf);
		// b_send_sms.setTypeface(tf);
		final RelativeLayout l_close = (RelativeLayout) dialog.findViewById(R.id.l_close);
		l_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dataBaseHelper = new DataBaseHelper(getActivity());

		database = dataBaseHelper.getWritableDatabase();
		Cursor c = database
				.rawQuery("select ad_id,ad_title,ad_post_date,sender_id,date from response where local_response_id='"
						+ response_id + "'", null);

		try {
			while (c.moveToNext()) {
				data[0] = "";
				data[1] = c.getString(0);
				data[2] = c.getString(1);
				data[3] = c.getString(2);
				data[4] = task.getString("user_id", null);
				data[5] = task.getString("name", null);
				data[6] = task.getString("email", null);
				data[7] = task.getString("phone", null);
				data[8] = c.getString(3);
				data[9] = et_msg.getText().toString();
				data[10] = "0";
				data[11] = timeStamp;
				data[12] = "1";
				data[13] = "0";
				data[14] = "0";

			}

		} catch (Exception e) {
		}
		b_send_email.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// do your work here
				if (!et_msg.getText().toString().contentEquals("")) {
					data[9] = et_msg.getText().toString();
					data[9] = data[9].replaceAll("'", "''");
					dataBaseHelper.insert_response(data);
					SetStandardGroups();
					ExpAdapter.notifyDataSetChanged();
					Toast.makeText(getActivity(), "Message sent", Toast.LENGTH_LONG).show();
					dialog.dismiss();
				} else {
					Toast.makeText(getActivity(), "Write your message", Toast.LENGTH_LONG).show();
				}
			}
		});
		// b_send_sms.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // do your work here
		// data[9] = et_msg.getText().toString();
		// data[9] = data[9].replaceAll("'", "''");
		// data[10] = "1";
		// dataBaseHelper.insert_response(data);
		// SetStandardGroups();
		// ExpAdapter.notifyDataSetChanged();
		// Toast.makeText(getActivity(), "Msg sent", Toast.LENGTH_LONG).show();
		//
		// dialog.dismiss();
		//
		// }
		// });
		database.close();
		dialog.show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startTimer();
	}

	public void startTimer() {
		// set a new Timer
		timer = new Timer();
		// initialize the TimerTask's job
		initializeTimerTask();
		// schedule the timer, after the first 5000ms the TimerTask will run
		// every 10000ms
		timer.schedule(timerTask, 10000, 10000); //
	}

	public void initializeTimerTask() {

		timerTask = new TimerTask() {
			public void run() {

				// use a handler to run a toast that shows the current timestamp
				handler.post(new Runnable() {
					public void run() {
						// get the current timeStamp
						SetStandardGroups();
						ExpAdapter.notifyDataSetChanged();
						if (ExpListItems.size() != 0) {
							ExpandList.removeFooterView(tv);
						}
					}
				});
			}
		};
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				SetStandardGroups();
				if (ExpListItems.size() != 0)
					ExpandList.removeFooterView(tv);
				ExpAdapter.notifyDataSetChanged();
			}
		}, 5000);
	}

	public class ExpandListAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<Group> groups;
		Typeface tf;

		public ExpandListAdapter(Context context, ArrayList<Group> groups) {
			this.context = context;
			this.groups = groups;
			tf = Typeface.createFromAsset(this.context.getAssets(), "AvenirLTStd_Book.otf");
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			ArrayList<Child> chList = groups.get(groupPosition).getItems();
			return chList.get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {

			final Child child = (Child) getChild(groupPosition, childPosition);
			if (convertView == null) {
				LayoutInflater infalInflater = (LayoutInflater) context
						.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				convertView = infalInflater.inflate(R.layout.my_listings_messages, null);
			}
			TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
			TextView tv_date1 = (TextView) convertView.findViewById(R.id.tv_date1234);
			TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
			TextView tv_email = (TextView) convertView.findViewById(R.id.tv_email);
			TextView tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);

			ImageView iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
			SVG svg_tick = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_tick);
			iv_tick.setImageDrawable(svg_tick.createPictureDrawable());
			
			ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_watch);
			SVG svg_clock = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_clock);
			iv_clock.setImageDrawable(svg_clock.createPictureDrawable());
			
			LinearLayout reply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
			// LinearLayout delete = (LinearLayout)
			// convertView.findViewById(R.id.ll_delete);
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv_user_image_my_messages);
			ImageView iv_above = (ImageView) convertView.findViewById(R.id.iv_user_image_my_messages_gravatar);
			SVG svg1 = SVGParser.getSVGFromResource(getActivity().getResources(), R.raw.circle_gravatar);
			iv_above.setImageDrawable(svg1.createPictureDrawable());

			LinearLayout ll_tick = (LinearLayout)convertView.findViewById(R.id.ll_tick);
			LinearLayout ll_watch = (LinearLayout)convertView.findViewById(R.id.ll_watch);
			
			tv_name.setText(child.getFrom());

			tv_phone.setText(child.getPhone());
			tv_email.setText(child.getEmail());
			tv_msg.setText(child.getMessage());

			String temp_date = child.getDate();
			if (temp_date.contentEquals("Draft")) {
				tv_date.setVisibility(View.GONE);
				iv_tick.setVisibility(View.GONE);
				iv_clock.setVisibility(View.VISIBLE);
				ll_tick.setVisibility(View.GONE);
				ll_watch.setVisibility(View.VISIBLE);
			} else if (!child.getDate().equals("")) {
				String[] date = child.getDate().split("-");
				String t_date = date[2] + " "
						+ (new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1]).substring(0, 3) + " "
						+ date[0];
				tv_date.setText(t_date);
				tv_date.setVisibility(View.VISIBLE);
				iv_tick.setVisibility(View.VISIBLE);
				iv_clock.setVisibility(View.GONE);
				ll_tick.setVisibility(View.VISIBLE);
				ll_watch.setVisibility(View.GONE);
			} else {
				tv_date.setVisibility(View.GONE);
			}

			tv_name.setTypeface(tf);
			tv_date.setTypeface(tf);
			tv_date1.setTypeface(tf);
			tv_phone.setTypeface(tf);
			tv_email.setTypeface(tf);
			tv_email.setTypeface(tf);
			tv_msg.setTypeface(tf);

			// int category = Integer.parseInt(child.getCat_id());
			// String default_image_url;
			// if (category==1)
			// default_image_url =
			// "http://www.searchcarpools.com/images/seeker-male.gif";
			// else
			// default_image_url =
			// "http://www.searchcarpools.com/images/provider-male.gif";
			String hash = MD5Util.md5Hex(child.getEmail().toLowerCase().trim());
			String gravatarUrl = "http://www.gravatar.com/avatar/" + hash
					+ "?d=http://s15.postimg.org/q6j7rf3kn/4jsqob0.png&s=150";

			Picasso.with(context).load(gravatarUrl).into(iv);

			reply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog_reply(child.getFrom(), child.getResponse_id());
				}
			});

			// delete.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// dataBaseHelper.delete_response(child.getResponse_id());
			// SetStandardGroups();
			// ExpAdapter.notifyDataSetChanged();
			//
			// }
			// });

			// iv.setImageResource(child.getImage());

			// tv.setText(child.getName().toString()+"::"+child.getTag());
			// tv.setTag(child.getTag());
			// TODO Auto-generated method stub
			return convertView;

		}

		@Override
		public int getChildrenCount(int groupPosition) {
			ArrayList<Child> chList = groups.get(groupPosition).getItems();

			return chList.size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groups.get(groupPosition);
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
			Group group = (Group) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.group_item, null);
			}
			
			TextView tv = (TextView) convertView.findViewById(R.id.group_name);
			TextView tv_date = (TextView) convertView.findViewById(R.id.group_date);
			TextView tv1 = (TextView) convertView.findViewById(R.id.group_date1);
			tv.setText(group.getName());
			
			ImageView iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
			SVG svg_tick = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_tick);
			iv_tick.setImageDrawable(svg_tick.createPictureDrawable());
			
			ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_watch);
			SVG svg_clock = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_clock);
			iv_clock.setImageDrawable(svg_clock.createPictureDrawable());
			
			String temp_date = group.getDate();
			if (temp_date.contentEquals("Draft")) {
				tv_date.setVisibility(View.GONE);
				iv_tick.setVisibility(View.GONE);
				iv_clock.setVisibility(View.VISIBLE);
			} else {
				String[] date = group.getDate().split("-");
				String t_date = date[2] + " "
						+ (new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1]).substring(0, 3) + " "
						+ date[0];
				tv_date.setText(t_date);
				iv_clock.setVisibility(View.GONE);
				tv_date.setVisibility(View.VISIBLE);
				iv_tick.setVisibility(View.VISIBLE);
				/*
				 * if (group.getDate().equals("")) {
				 * tv_date.setVisibility(View.GONE); } else {
				 * tv_date.setText(t_date); }
				 */
			}
			
			ImageView header = (ImageView) convertView.findViewById(R.id.iv_my_listings_header);
			RelativeLayout rl_header = (RelativeLayout) convertView.findViewById(R.id.rl_group);

			if (!(groupPosition % 2 == 0))
				rl_header.setBackgroundColor(Color.parseColor("#f4f4f4"));
			else
				rl_header.setBackgroundColor(Color.parseColor("#ececec"));

			if (isExpanded) {
				header.setBackgroundResource(R.drawable.up_arrow_green);
				rl_header.setBackgroundColor(Color.parseColor("#00ca98"));
				tv.setTextColor(Color.WHITE);
				tv_date.setTextColor(Color.WHITE);
				tv1.setTextColor(Color.WHITE);
			} else {
				header.setBackgroundResource(R.drawable.down_arrow_grey);
				tv.setTextColor(Color.parseColor("#313131"));
				tv_date.setTextColor(Color.parseColor("#6aa9d4"));
				tv1.setTextColor(Color.parseColor("#979799"));
			}
			tv.setTypeface(tf);
			tv_date.setTypeface(tf);
			tv1.setTypeface(tf);

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

	}

}