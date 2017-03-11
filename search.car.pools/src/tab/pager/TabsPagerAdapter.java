package tab.pager;

import remak.pager.add_company;
import remak.pager.all;
import remak.pager.companies;
import remak.pager.corporate;
import remak.pager.provider;
import remak.pager.seeker;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new all();
		case 1:
			// Games fragment activity
			return new provider();
		case 2:
			// Games fragment activity
			return new seeker();
		case 3:
			// Games fragment activity
			return new corporate();
		case 4:
			// Games fragment activity
			return new companies();
		
//		case 5:
//			// Games fragment activity
//			return new add_company();
		
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 5;
	}

}
