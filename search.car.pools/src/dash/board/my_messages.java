package dash.board;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import array.list.Child;
import array.list.Group;
import array.list.MyMessagesChild;
import array.list.My_Message_Group;
import custom.list.ExpandListSearchAdapter.MD5Util;
import com.example.search.car.pools.DataBaseHelper;
import com.example.search.car.pools.R;
import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;
import com.squareup.picasso.Picasso;

public class my_messages extends Fragment implements OnRefreshListener {

	// private ExpandableListView ExpandList;
	// ArrayList<ExpandableListItem> ch_list1 = new
	// ArrayList<ExpandableListItem>();
	ArrayList<My_Message_Group> groups = new ArrayList<My_Message_Group>();
	private SQLiteDatabase database_My_Messages;
	DataBaseHelper dataBaseHelper_My_Messages;
	SharedPreferences task;
	Button b_send_email;// b_send_sms;

	ArrayList<My_Message_Group> ExpMessages;

	// ListView as per new layout
	ExpandableListView lv;
	// Adapter Items
	// TextView name, message, phone, email, msg_date;
	// ImageView user_image, msg_open, msg_indicator;
	// LinearLayout reply, delete, upper, bottom;
	// CustomArrayAdapter adapter_list;
	MyMessagesAdapter adapter_list;
	private int CELL_DEFAULT_HEIGHT;// = 155; // for MOTO E: 123, this should be
									// fixed in xml part and not here

	// timer for updating listings every 5 secs
	Timer timer;
	TimerTask timerTask;
	final Handler handler = new Handler();

	private SwipeRefreshLayout swipeRefreshLayout;

	TextView tv_msg;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_expanding_cells, container, false);
		lv = (ExpandableListView) rootView.findViewById(R.id.main_list_view);

		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		dataBaseHelper_My_Messages = new DataBaseHelper(getActivity());

		task = getActivity().getSharedPreferences("user", getActivity().MODE_PRIVATE);
		// dataBaseHelper_My_Messages = new DataBaseHelper(getActivity());
		// adapter = new MyMessagesListAdapter(getActivity(),
		// SetStandardGroups());

		// adding footer
		// View v =
		// getActivity().getLayoutInflater().inflate(R.layout.my_message_footer,
		// null);
		// lv.addFooterView(v);

		ExpMessages = SetStandardGroups();

		tv_msg = new TextView(getActivity());
		if (ExpMessages.size() == 0) {
			tv_msg.setText("No Messages.");
			tv_msg.setTextSize(25);
			tv_msg.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			lv.addFooterView(tv_msg);
		} else {
			lv.removeFooterView(tv_msg);
		}

		adapter_list = new MyMessagesAdapter(getActivity(), ExpMessages);
		lv.setAdapter(adapter_list);
		lv.setDividerHeight(0);
		lv.setGroupIndicator(null);

		/*
		 * Display display =
		 * getActivity().getWindowManager().getDefaultDisplay(); DisplayMetrics
		 * metrics = new DisplayMetrics(); display.getMetrics(metrics);
		 * 
		 * if (metrics.density == 0.75) CELL_DEFAULT_HEIGHT = 63; else if
		 * (metrics.density == 1.0) CELL_DEFAULT_HEIGHT = 100; else if
		 * (metrics.density == 1.5) CELL_DEFAULT_HEIGHT = 120; else if
		 * (metrics.density == 2.0) CELL_DEFAULT_HEIGHT = 160; else if
		 * (metrics.density == 2.5) CELL_DEFAULT_HEIGHT = 175; else if
		 * (metrics.density == 3.0) CELL_DEFAULT_HEIGHT = 235;
		 */

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	public class MyMessagesAdapter extends BaseExpandableListAdapter {

		private Context context;
		private ArrayList<My_Message_Group> groups;
		Typeface tf;

		MyMessagesChild object;

		public MyMessagesAdapter(Context context, ArrayList<My_Message_Group> groups) {
			this.context = context;
			this.groups = groups;
			tf = Typeface.createFromAsset(this.context.getAssets(), "AvenirLTStd_Book.otf");
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			ArrayList<MyMessagesChild> chList = groups.get(groupPosition).getChild();
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

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.my_message_child, null);

			final MyMessagesChild object = (MyMessagesChild) getChild(groupPosition, childPosition);
			TextView message = (TextView) convertView.findViewById(R.id.tv_message_my_messages);
			TextView msg_date = (TextView) convertView.findViewById(R.id.tv_date_my_messages);
			LinearLayout reply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
			LinearLayout delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
			// LinearLayout upper = (LinearLayout)
			// convertView.findViewById(R.id.ll_upper);
			LinearLayout bottom = (LinearLayout) convertView.findViewById(R.id.ll_bottom);

			ImageView iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
			SVG svg_tick = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_tick);
			iv_tick.setImageDrawable(svg_tick.createPictureDrawable());
			
			ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_watch);
			SVG svg_clock = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_clock);
			iv_clock.setImageDrawable(svg_clock.createPictureDrawable());
			
			LinearLayout ll_tick = (LinearLayout)convertView.findViewById(R.id.ll_tick);
			LinearLayout ll_watch = (LinearLayout)convertView.findViewById(R.id.ll_watch);
			TextView tv_date1 = (TextView) convertView.findViewById(R.id.tv_date1234);
			message.setText(object.getMessage());

			String temp_date = object.getDate();
			if (temp_date.contentEquals("Draft")) {
				msg_date.setVisibility(View.GONE);
				iv_tick.setVisibility(View.GONE);
				iv_clock.setVisibility(View.VISIBLE);
				ll_tick.setVisibility(View.GONE);
				ll_watch.setVisibility(View.VISIBLE);
			} else if (object.getDate().equals("")) {
				String[] date = object.getDate().split("-");
				String t_date = date[2] + " "
						+ (new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1]).substring(0, 3) + " "
						+ date[0];
				msg_date.setText(t_date);
				msg_date.setVisibility(View.VISIBLE);
				iv_tick.setVisibility(View.VISIBLE);
				iv_clock.setVisibility(View.GONE);
				ll_tick.setVisibility(View.VISIBLE);
				ll_watch.setVisibility(View.GONE);
			} 
			else if (!temp_date.contentEquals("Draft")){
				ll_tick.setVisibility(View.VISIBLE);
				ll_watch.setVisibility(View.GONE);
			}

			message.setTypeface(tf);
			msg_date.setTypeface(tf);
			tv_date1.setTypeface(tf);
			reply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog_reply(object.getName(), object.getResponse_id());
				}
			});

			delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dataBaseHelper_My_Messages.delete_response(object.getResponse_id());
					SetStandardGroups();
					adapter_list.notifyDataSetChanged();

				}
			});

			return convertView;

		}

		@Override
		public int getChildrenCount(int groupPosition) {
			ArrayList<MyMessagesChild> chList = groups.get(groupPosition).getChild();

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
			My_Message_Group object = (My_Message_Group) getGroup(groupPosition);
			if (convertView == null) {
				LayoutInflater inf = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.my_message_group, null);
			}
			TextView msg_upper = (TextView) convertView.findViewById(R.id.tv_message_upper_my_messages);
			TextView date_upper = (TextView) convertView.findViewById(R.id.tv_date_upper_my_messages);
			TextView phone_upper = (TextView) convertView.findViewById(R.id.tv_phone_my_messages);
			TextView tv_date1 = (TextView) convertView.findViewById(R.id.tv_date1234);
			
			ImageView toggle = (ImageView) convertView.findViewById(R.id.toggle_details);
			ImageView msg_indicator = (ImageView) convertView.findViewById(R.id.iv_message_indicator_my_messages);
			msg_indicator.setImageResource(R.drawable.blue_outline);
			TextView name = (TextView) convertView.findViewById(R.id.tv_username_my_messages);
			TextView email = (TextView) convertView.findViewById(R.id.tv_email_my_messages);
			TextView phone = (TextView) convertView.findViewById(R.id.tv_phone_my_messages);

			ImageView user_image = (ImageView) convertView.findViewById(R.id.iv_user_image_my_messages);
			ImageView iv_above = (ImageView) convertView.findViewById(R.id.iv_user_image_my_messages_gravatar);
			SVG svg1 = SVGParser.getSVGFromResource(getActivity().getResources(), R.raw.circle_gravatar);
			iv_above.setImageDrawable(svg1.createPictureDrawable());

			ImageView iv_tick = (ImageView) convertView.findViewById(R.id.iv_tick);
			SVG svg_tick = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_tick);
			iv_tick.setImageDrawable(svg_tick.createPictureDrawable());
			
			ImageView iv_clock = (ImageView) convertView.findViewById(R.id.iv_watch);
			SVG svg_clock = SVGParser.getSVGFromResource(context.getResources(), R.raw.chat_clock);
			iv_clock.setImageDrawable(svg_clock.createPictureDrawable());
			
			LinearLayout ll_tick = (LinearLayout)convertView.findViewById(R.id.ll_tick);
			LinearLayout ll_watch = (LinearLayout)convertView.findViewById(R.id.ll_watch);
			// int category = Integer.parseInt(object.getCat_id());
			// String default_image_url;
			// if (category==1)
			// default_image_url =
			// "http://www.searchcarpools.com/images/seeker-male.gif";
			// else
			// default_image_url =
			// "http://www.searchcarpools.com/images/provider-male.gif";
			String hash = MD5Util.md5Hex(object.getEmail().toLowerCase().trim());
			String gravatarUrl = "http://www.gravatar.com/avatar/" + hash
					+ "?d=http://s15.postimg.org/q6j7rf3kn/4jsqob0.png&s=150";

			Picasso.with(context).load(gravatarUrl).into(user_image);

			TextView email_upper = (TextView) convertView.findViewById(R.id.tv_email_my_messages);
			
			
			phone_upper.setText(object.getPhone());
			msg_upper.setText(object.getMsg());

			name.setText(object.getName());
			email.setText(object.getEmail());
			phone.setText(object.getPhone());

			
			
			String temp_date = object.getDate();
			if (temp_date.contentEquals("Draft")) {
				date_upper.setVisibility(View.GONE);
				iv_tick.setVisibility(View.GONE);
				iv_clock.setVisibility(View.VISIBLE);
				ll_tick.setVisibility(View.GONE);
				ll_watch.setVisibility(View.VISIBLE);
			} else if (!object.getDate().equals("")) {
				String[] date = object.getDate().split("-");
				String t_date = date[2] + " "
						+ (new DateFormatSymbols().getMonths()[Integer.parseInt(date[1]) - 1]).substring(0, 3) + " "
						+ date[0];
				date_upper.setText(t_date);
				date_upper.setVisibility(View.VISIBLE);
				iv_tick.setVisibility(View.VISIBLE);
				iv_clock.setVisibility(View.GONE);
				ll_tick.setVisibility(View.VISIBLE);
				ll_watch.setVisibility(View.GONE);
			} else {
				date_upper.setVisibility(View.GONE);
			}

			if (isExpanded) {
				date_upper.setVisibility(View.GONE);
				msg_upper.setVisibility(View.GONE);
				phone_upper.setVisibility(View.VISIBLE);
				email_upper.setVisibility(View.VISIBLE);
				msg_indicator.setImageResource(R.drawable.green_outline);
				toggle.setBackgroundResource(R.drawable.minus);
				ll_tick.setVisibility(View.GONE);
				ll_watch.setVisibility(View.GONE);
			} else {
				date_upper.setVisibility(View.VISIBLE);
				msg_upper.setVisibility(View.VISIBLE);
				phone_upper.setVisibility(View.GONE);
				email_upper.setVisibility(View.GONE);
				toggle.setBackgroundResource(R.drawable.add);
				msg_indicator.setImageResource(R.drawable.blue_outline);
			}
			
			name.setTypeface(tf);
			email.setTypeface(tf);
			phone.setTypeface(tf);
			phone_upper.setTypeface(tf);
			msg_upper.setTypeface(tf);
			date_upper.setTypeface(tf);
			tv_date1.setTypeface(tf);
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

	public ArrayList<My_Message_Group> SetStandardGroups() {
		// Adding adapter
		// ch_list1.clear();
		groups.clear();

		dataBaseHelper_My_Messages = new DataBaseHelper(getActivity());

		database_My_Messages = dataBaseHelper_My_Messages.getReadableDatabase();
		Cursor c = database_My_Messages.rawQuery(
				"select sender_name,sender_phone,sender_email,date,msg,ad_id,local_response_id, ad_id from response where receiver_id='"
						+ task.getString("user_id", null) + "' and visibility='1' ORDER BY local_response_id DESC",
				null); // and sync='1'

		try {
			while (c.moveToNext()) {
				ArrayList<MyMessagesChild> ch_list1 = new ArrayList<MyMessagesChild>();
				ch_list1.add(new MyMessagesChild(c.getString(3), c.getString(4), c.getString(6), c.getString(0)));
				// Log.i("log", c.getString(0) + c.getString(1) + c.getString(2)
				// + c.getString(3) + c.getString(4)
				// + c.getString(5) + c.getString(6));
				groups.add(new My_Message_Group(c.getString(0), c.getString(1), c.getString(2), c.getString(5),
						c.getString(6), c.getString(4), c.getString(3), c.getString(7), ch_list1));
			}

		} catch (Exception e) {
		}
		database_My_Messages.close();
		return groups;
	}

	public void dialog_reply(String title, final String response_id) {
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

		dataBaseHelper_My_Messages = new DataBaseHelper(getActivity());

		database_My_Messages = dataBaseHelper_My_Messages.getWritableDatabase();
		Cursor c = database_My_Messages.rawQuery(
				"select ad_id,ad_title,ad_post_date,receiver_id,date, sender_id from response where local_response_id='"
						+ response_id + "'",
				null);

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
				data[8] = c.getString(5);
				// data[9] = et_msg.getText().toString();
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
					String s = et_msg.getText().toString();
					data[9] = s.replaceAll("'", "''");
					dataBaseHelper_My_Messages.insert_response(data);
					SetStandardGroups();
					adapter_list.notifyDataSetChanged();

					// Toast.makeText(getActivity(), "Message Sent\nLocal
					// response
					// id:"+ response_id+ "\nReceiver ID:" +data[8],
					// Toast.LENGTH_LONG).show();
					Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
					//Log.i("log", "Msg Sent" + "\nMsg:" + data[9] + "\nReceiver ID:" + data[8]);
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
		// String s = et_msg.getText().toString();
		// data[9] = s.replaceAll("'", "''");
		// data[10] = "1";
		// dataBaseHelper_My_Messages.insert_response(data);
		// SetStandardGroups();
		// adapter_list.notifyDataSetChanged();
		// Toast.makeText(getActivity(), "SMS sent", Toast.LENGTH_LONG).show();
		//
		// dialog.dismiss();
		//
		// }
		// });
		database_My_Messages.close();
		dialog.show();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startTimer();
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
						adapter_list.notifyDataSetChanged();
						if (ExpMessages.size() != 0) {
							lv.removeFooterView(tv_msg);
						}
					}
				});
			}
		};
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
				SetStandardGroups();
				if (ExpMessages.size() != 0) {
					lv.removeFooterView(tv_msg);
				}
				adapter_list.notifyDataSetChanged();
			}
		}, 5000);
	}

}