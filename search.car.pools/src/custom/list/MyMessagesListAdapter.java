//package custom.list;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Locale;
//
//import com.example.search.car.pools.DataBaseHelper;
//import com.example.search.car.pools.R;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//import array.list.Child;
//
//public class MyMessagesListAdapter extends ArrayAdapter<Child>{
//
//	private final Activity context;
//	private ArrayList<Child> child;
//	Button b_send_email, b_send_sms;
//	private SQLiteDatabase database;
//	DataBaseHelper dataBaseHelper;
//	SharedPreferences task;
//	
//	public MyMessagesListAdapter(Context context, ArrayList<Child> ch_list1, SharedPreferences task) {
//		super(context, R.layout.my_messages_item, ch_list1);
//		this.context = (Activity) context;
//		this.child = ch_list1;
//	}
//
//	@Override
//	public View getView(final int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		LayoutInflater inflater = context.getLayoutInflater();
//		View rowView = inflater.inflate(R.layout.my_messages_item, null, true);
//		
//		TextView name = (TextView)rowView.findViewById(R.id.tv_username_my_messages);
//		TextView email = (TextView)rowView.findViewById(R.id.tv_email_my_messages);
//		TextView phone = (TextView)rowView.findViewById(R.id.tv_phone_my_messages);
//		TextView message = (TextView)rowView.findViewById(R.id.tv_message_my_messages);
//		TextView msg_date = (TextView)rowView.findViewById(R.id.tv_date_my_messages);
//		TextView msg_date_upper = (TextView)rowView.findViewById(R.id.tv_date_upper_my_messages);
//		ImageView user_image  = (ImageView)rowView.findViewById(R.id.iv_user_image_my_messages);
//		ImageView msg_indicator = (ImageView)rowView.findViewById(R.id.iv_message_indicator_my_messages);
//		ImageView msg_open = (ImageView)rowView.findViewById(R.id.iv_add_minus_my_messages);
//		LinearLayout reply = (LinearLayout)rowView.findViewById(R.id.ll_reply);
//		LinearLayout delete = (LinearLayout)rowView.findViewById(R.id.ll_delete);
//		
//		name.setText(child.get(position).getFrom());
//		email.setText(child.get(position).getEmail());
//		phone.setText(child.get(position).getPhone());
//		message.setText(child.get(position).getMessage());
//		msg_date.setText(child.get(position).getDate());
//		msg_date_upper.setText(child.get(position).getDate());
//		reply.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dialog_reply(child.get(position).getFrom(), child.get(position).getResponse_id());
//			}
//		});
//		
//		delete.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				dataBaseHelper.delete_response(child.get(position).getResponse_id());
//			}
//		});
//		return rowView;
//	}
//	
//	public void dialog_reply(String title, String response_id) {
//		final String data[] = new String[15];
//		final String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//		String arr[] = { "Reply", "View Listing", "Delete", "MArk as Read" };
//		final Dialog dialog = new Dialog(context);
//		dialog.setTitle(title);
//		dialog.setContentView(R.layout.reply_dialog);
//		final EditText et_msg = (EditText) dialog.findViewById(R.id.et_your_dialog_msg);
//		b_send_email = (Button) dialog.findViewById(R.id.b_send_email);
//		b_send_sms = (Button) dialog.findViewById(R.id.b_send_sms);
//
//		dataBaseHelper = new DataBaseHelper(context);
//
//		database = dataBaseHelper.getWritableDatabase();
//		Cursor c = database
//				.rawQuery("select ad_id,ad_title,ad_post_date,receiver_id,date from response where local_response_id='"
//						+ response_id + "'", null);
//
//		try {
//			while (c.moveToNext()) {
//				data[0] = "";
//				data[1] = c.getString(0);
//				data[2] = c.getString(1);
//				data[3] = c.getString(2);
//				data[4] = task.getString("user_id", null);
//				data[5] = task.getString("name", null);
//				data[6] = task.getString("email", null);
//				data[7] = task.getString("phone", null);
//				data[8] = c.getString(3);
//				data[9] = et_msg.getText().toString();
//				data[10] = "0";
//				data[11] = timeStamp;
//				data[12] = "1";
//				data[13] = "0";
//				data[14] = "0";
//
//			}
//			database.close();
//		} catch (Exception e) {
//		}
//		b_send_email.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// do your work here
//				data[9] = et_msg.getText().toString();
//				dataBaseHelper.insert_response(data);
////				SetStandardGroups();
////				ExpAdapter.notifyDataSetChanged();
//				Toast.makeText(context, "Msg sent", Toast.LENGTH_LONG).show();
//
//				dialog.dismiss();
//
//			}
//		});
//		b_send_sms.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// do your work here
//				data[9] = et_msg.getText().toString();
//				data[10] = "1";
//				dataBaseHelper.insert_response(data);
////				SetStandardGroups();
////				ExpAdapter.notifyDataSetChanged();
//				Toast.makeText(context, "Msg sent", Toast.LENGTH_LONG).show();
//
//				dialog.dismiss();
//
//			}
//		});
//		dialog.show();
//	}
//}
