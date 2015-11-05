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
 * Underlines指示器通过代码设置样式属性
 */
public class SampleUnderlinesStyledMethods extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        setContentView(R.layout.sample_underlines);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        UnderlinePagerIndicator indicator = (UnderlinePagerIndicator) findViewById(R.id.indicator);
        //通过代码设置样式属性
        indicator.setViewPager(mPager);
        indicator.setSelectedColor(0xFFCC0000);
        indicator.setBackgroundColor(0xFFCCCCCC);
        indicator.setFadeDelay(1000);
        indicator.setFadeLength(1000);
        mIndicator = indicator;
    }
}
