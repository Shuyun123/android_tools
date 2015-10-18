package com.example.anumbrella.viewpagerindicator.Icons;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.ViewParent;

import com.example.anumbrella.viewpager.PagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * Icons默认样式
 */
public class SampleIconsDefault extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_icons);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);
        mIndicator = (PagerIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

    }


}
