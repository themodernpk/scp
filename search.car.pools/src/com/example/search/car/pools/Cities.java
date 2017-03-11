package com.example.search.car.pools;

import remak.pager.post_main;
import custom.list.custom_list;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

@SuppressLint("NewApi")
public class Cities extends Fragment implements OnRefreshListener {

	Intent i;
	final String name[] = { "Delhi/NCR", "Bengaluru", "Kolkata", "Mumbai", "Pune", "Ahmedabad" };

	private SwipeRefreshLayout swipeRefreshLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.cities, container, false);
		ProgressBar p_bar = (ProgressBar) (rootView).findViewById(R.id.progressBar1);
		p_bar.setVisibility(View.GONE);
		TextView tv_internet_status = (TextView) rootView.findViewById(R.id.tv_internet_status);
		tv_internet_status.setVisibility(View.GONE);
		
//		getActivity().getActionBar().setTitle("Cities");
		getActivity().findViewById(R.id.ib_navigation_search).setVisibility(View.VISIBLE);
		// swipe layout to refresh
		swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
		swipeRefreshLayout.setOnRefreshListener(this);
		swipeRefreshLayout.setColorSchemeResources(R.color.swipe_color, android.R.color.holo_green_light,
				android.R.color.holo_orange_light, android.R.color.holo_red_light);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
//		getActivity().getActionBar().setTitle("Cities");
		ListView l1 = (ListView) getActivity().findViewById(R.id.list_cities);
		String arr[] = { "D E L H I", "B E N G A L U R U", "K O L K A T A", "M U M B A I", "P U N E",
				"A H M E D A B A D" };
		custom_list adapter = new custom_list(getActivity(), arr);
		l1.setAdapter(adapter);
		l1.setDivider(null);
		l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent i = new Intent(getActivity(), post_main.class);
				i.putExtra("city", name[position]);
				i.putExtra("frag_id", 1);
				startActivity(i);
			}
		});
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(false);
			}
		}, 2500);
	}
}
