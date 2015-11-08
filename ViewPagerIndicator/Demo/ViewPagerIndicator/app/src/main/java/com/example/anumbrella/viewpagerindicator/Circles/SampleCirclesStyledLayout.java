package com.example.anumbrella.viewpagerindicator.Circles;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.CirclePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15-11-8.
 * <p/>
 * Circles指示器通过xml指定其属性
 */
public class SampleCirclesStyledLayout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_circles_layout);
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        mIndicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
