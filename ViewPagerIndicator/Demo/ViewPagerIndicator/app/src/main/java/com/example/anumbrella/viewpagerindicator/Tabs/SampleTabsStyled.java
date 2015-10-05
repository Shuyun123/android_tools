package com.example.anumbrella.viewpagerindicator.Tabs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.TabPagerIndicator;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragment;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/10/5.
 * <p/>
 * 自定义样式的tabs选项卡(通过更改主题改变样式)
 */
public class SampleTabsStyled extends FragmentActivity {


    /**
     * tabs选项卡的内容
     */
    private final String[] CONTENT = new String[]{"新闻", "科技", "头条", "历史", "军事", "娱乐","手机"};


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
    class TabsAdapter extends FragmentPagerAdapter {

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
    }


}
