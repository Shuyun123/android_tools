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
 * Titles指示器，默认指示器为三角形
 */
public class SampleTitlesTriangle extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_titles);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        TitlePagerIndicator indicator = (TitlePagerIndicator) findViewById(R.id.indicator);
        indicator.setFooterIndicatorStyle(TitlePagerIndicator.IndicatorStyle.Triangle);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
    }
}
