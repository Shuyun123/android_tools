package com.example.anumbrella.viewpagerindicator.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.anumbrella.viewpager.IconPagerAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * Fragment的适配器
 */
public class TestFragmentAdapter extends FragmentPagerAdapter implements IconPagerAdapter {


    /**
     * ViewPager页面显示的内容
     */
    private static final String[] CONTENT = new String[]{"这个", "是一", "图片", "指示器"};

    /**
     * tabs选项的图片
     */
    private static final int[] ICONS = new int[]{
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };


    private int mCount = ICONS.length;


    public TestFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new TestFragment().newInstance(CONTENT[position % CONTENT.length]);
    }

    @Override
    public int getIconResId(int index) {
        return ICONS[index % ICONS.length];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TestFragmentAdapter.CONTENT[position % CONTENT.length];
    }

    @Override
    public int getCount() {
        return mCount;
    }

    public void setCount(int count) {
        if (count > 0 && count < 10) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
