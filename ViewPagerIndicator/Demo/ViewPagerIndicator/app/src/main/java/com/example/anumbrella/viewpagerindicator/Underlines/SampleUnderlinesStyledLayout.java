package com.example.anumbrella.viewpagerindicator.Underlines;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.UnderlinePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15-11-5.
 * <p/>
 * Underlines指示器通过xml布局设置属性
 */
public class SampleUnderlinesStyledLayout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_underlines_layout);


        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        mIndicator = (UnderlinePagerIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }
}
