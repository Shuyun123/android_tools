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
 * Circles指示器，设置指示器填充显示页面的圆圈快速切换
 */
public class SampleCirclesSnap extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_circles_layout);
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        CirclePagerIndicator indicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        indicator.setSnap(true);
    }
}
