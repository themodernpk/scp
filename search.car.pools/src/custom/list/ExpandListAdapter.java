package custom.list;

import java.util.ArrayList;

import com.example.search.car.pools.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import array.list.Child;
import array.list.Group;

public class ExpandListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<Group> groups;

	public ExpandListAdapter(Context context, ArrayList<Group> groups) {
		this.context = context;
		this.groups = groups;
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

		Child child = (Child) getChild(groupPosition, childPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.my_listings_messages, null);
		}
		TextView tv_name = (TextView) convertView.findViewById(R.id.tv_name);
		TextView tv_date = (TextView) convertView.findViewById(R.id.tv_date);
		TextView tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
		TextView tv_email = (TextView) convertView.findViewById(R.id.tv_email);
		TextView tv_msg = (TextView) convertView.findViewById(R.id.tv_msg);

		LinearLayout reply = (LinearLayout) convertView.findViewById(R.id.ll_reply);
		LinearLayout delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
		
		tv_name.setText(child.getFrom());
		tv_date.setText(child.getDate());
		tv_phone.setText(child.getPhone());
		tv_email.setText(child.getEmail());
		tv_msg.setText(child.getMessage());
		
		reply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});

		delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		
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
		tv.setText(group.getName());
		if (group.getDate().equals("")) {
			tv_date.setVisibility(View.GONE);
		} else {
			tv_date.setText(group.getDate());
		}
		ImageView header = (ImageView)convertView.findViewById(R.id.iv_my_listings_header);
		RelativeLayout rl_header = (RelativeLayout)convertView.findViewById(R.id.rl_group);
		if (isExpanded)
			header.setBackgroundResource(R.drawable.up_arrow_green);
		else
			header.setBackgroundResource(R.drawable.down_arrow_grey);
		
		if (!(groupPosition%2==0))
				rl_header.setBackgroundColor(Color.parseColor("#f4f4f4"));
		else
			rl_header.setBackgroundColor(Color.parseColor("#ececec"));
		// TODO Auto-generated method stub
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
