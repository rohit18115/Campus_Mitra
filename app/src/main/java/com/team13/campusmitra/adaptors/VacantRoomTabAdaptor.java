package com.team13.campusmitra.adaptors;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class VacantRoomTabAdaptor extends FragmentStatePagerAdapter {
    private ArrayList<Fragment> pages = new ArrayList<>();
    private ArrayList<String> titles= new ArrayList<>();
    public VacantRoomTabAdaptor(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void addPage(Fragment fragment,String title){
        pages.add(fragment);
        titles.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        return pages.get(position);

    }

    @Override
    public int getCount() {
        return pages.size();
    }
}
