package com.example.anumbrella.viewpagerindicator.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.IconPagerAdapter;
import com.example.anumbrella.viewpager.TabPagerIndicator;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragment;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/10/5.
 * <p/>
 * tabs选项卡,自定义带图片
 */
public class SampleTabsWithIcons extends FragmentActivity {
    /**
     * tabs选项卡的内容
     */
    private static final String[] CONTENT = new String[]{"新闻", "科技", "头条", "历史"};


    /**
     * tabs选项卡带的图片
     */
    private static final int[] ICONS = new int[]{
            R.drawable.perm_group_calendar,
            R.drawable.perm_group_camera,
            R.drawable.perm_group_device_alarms,
            R.drawable.perm_group_location,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_tabs);
        FragmentPagerAdapter adapter = new TabsAdapter(getSupportFragmentManager());

        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        //设置适配器
        pager.setAdapter(adapter);
        TabPagerIndicator indicator = (TabPagerIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(pager);

    }


    /**
     * 创建Tabs的适配器
     */
    class TabsAdapter extends FragmentPagerAdapter implements IconPagerAdapter{

        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return TestFragment.newInstance(CONTENT[position % CONTENT.length] + "页面");
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }


        /**
         * 设定tabs上面的title
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length];
        }


        @Override
        public int getIconResId(int index) {
            return ICONS[index];
        }


    }

}
