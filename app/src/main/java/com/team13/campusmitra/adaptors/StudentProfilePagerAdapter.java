package com.team13.campusmitra.adaptors;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.team13.campusmitra.fragments.BasicProfileFragment;
import com.team13.campusmitra.fragments.StudentProfileFragment;

public class StudentProfilePagerAdapter extends FragmentStatePagerAdapter {

    int mNoOfTabs;
    private Context context;

    private String[] tabTitles = new String[]{"Basic Info","Advanced Info"};

    public StudentProfilePagerAdapter(@NonNull FragmentManager fm, int mNoOfTabs, Context context) {
        super(fm);
        this.mNoOfTabs = mNoOfTabs;
        this.context = context;
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
                return new BasicProfileFragment(context);
            case 1:
                return new StudentProfileFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
