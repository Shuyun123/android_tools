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
 * Underlines指示器，指示器滑动后不会消失
 */
public class SampleUnderlinesNoFade extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_underlines);


        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        UnderlinePagerIndicator indicator = (UnderlinePagerIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        //设置指示器不会消失
        indicator.setFades(false);
        mIndicator = indicator;
    }
}
