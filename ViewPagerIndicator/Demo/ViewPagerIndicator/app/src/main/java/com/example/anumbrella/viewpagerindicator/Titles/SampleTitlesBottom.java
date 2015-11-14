package com.example.anumbrella.viewpagerindicator.Titles;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.TitlePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/11/14.
 * <p/>
 * Titles指示器布局方位在屏幕的下方
 */
public class SampleTitlesBottom extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_titles_bottom);
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (TitlePagerIndicator) findViewById(R.id.indicator);
        mPager.setAdapter(adapter);
        mIndicator.setViewPager(mPager);
    }

}
