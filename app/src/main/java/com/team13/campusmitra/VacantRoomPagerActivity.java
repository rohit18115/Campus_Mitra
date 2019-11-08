package com.team13.campusmitra;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.team13.campusmitra.adaptors.VacantRoomTabAdaptor;
import com.team13.campusmitra.fragments.BookingListFragment;
import com.team13.campusmitra.fragments.VacantRoomFragment;

public class VacantRoomPagerActivity extends FragmentActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private VacantRoomTabAdaptor adaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacant_room_pager);
        viewPager = findViewById(R.id.vacant_room_viewPager);
        tabLayout = findViewById(R.id.vacant_room_tab);
        adaptor = new VacantRoomTabAdaptor(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        VacantRoomFragment vacant = new VacantRoomFragment();
        vacant.setContext(this,this);
        adaptor.addPage(vacant,"Vacant Room");
        BookingListFragment fragment = new BookingListFragment();

        adaptor.addPage(fragment,"Booked Rooms");

        viewPager.setAdapter(adaptor);
        tabLayout.setupWithViewPager(viewPager);

    }
}
