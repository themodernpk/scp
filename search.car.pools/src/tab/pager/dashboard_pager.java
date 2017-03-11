package tab.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import dash.board.my_account;
import dash.board.my_listing;
import dash.board.my_messages;

public class dashboard_pager extends FragmentPagerAdapter {

	public dashboard_pager(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new my_listing();
		case 1:
			// Games fragment activity
			return new my_messages();
		case 2:
			// Games fragment activity
			return new my_account();
			
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 3;
	}

}
