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
 * Circles指示器通过方法指定其属性
 */
public class SampleCirclesStyledMethods extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_circles);
        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        CirclePagerIndicator indicator = (CirclePagerIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setRadius(10 * density);
        indicator.setPageColor(0x880000FF);
        indicator.setFillColor(0xFF888888);
        indicator.setStrokeColor(0xFF000000);
        indicator.setStrokeWidth(2 * density);
        mIndicator = indicator;
    }
}
