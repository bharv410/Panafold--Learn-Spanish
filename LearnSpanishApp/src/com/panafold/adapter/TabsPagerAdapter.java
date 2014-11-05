package com.panafold.adapter;

import com.panafold.main.ChangeWordFragment;
import com.panafold.main.CurrentWordFragment;
import com.panafold.main.PhraseFragment;
import com.panafold.main.WebsiteFragment;

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
			return new ChangeWordFragment();
		case 1:
			return new CurrentWordFragment();
		case 2:
			return new PhraseFragment();
		case 3:
			return new WebsiteFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
