package com.team13.campusmitra.adaptors;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.team13.campusmitra.fragments.BasicProfileFragment;
import com.team13.campusmitra.fragments.FacultyProfileFragment;

public class FacultyProfilePagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;

    private String[] tabTitles = new String[]{"Basic Info","Advanced Info"};

    public FacultyProfilePagerAdapter(@NonNull FragmentManager fm, int mNoOfTabs) {
        super(fm);
        this.mNoOfTabs = mNoOfTabs;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new BasicProfileFragment();
            case 1:
                return new FacultyProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
