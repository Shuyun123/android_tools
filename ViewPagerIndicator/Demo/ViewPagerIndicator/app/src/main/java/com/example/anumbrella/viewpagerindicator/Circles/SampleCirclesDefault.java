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
 * circles指示器默认样式
 */
public class SampleCirclesDefault extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_circles);
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        mIndicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
