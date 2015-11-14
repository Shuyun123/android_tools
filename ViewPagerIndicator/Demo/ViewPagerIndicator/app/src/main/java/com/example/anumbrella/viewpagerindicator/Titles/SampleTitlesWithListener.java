package com.example.anumbrella.viewpagerindicator.Titles;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.anumbrella.viewpager.TitlePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/11/14.
 * <p/>
 * Titles指示器,调用接口设置滑动监听事件
 */
public class SampleTitlesWithListener extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_titles);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (TitlePagerIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        mIndicator.setViewPager(mPager);

        mIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Toast.makeText(SampleTitlesWithListener.this, "你当前滑动到的页面是：" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
