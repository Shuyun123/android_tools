package com.example.anumbrella.viewpagerindicator.Lines;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.example.anumbrella.viewpager.LinePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/10/31.
 * <p/>
 * Lines指示器样式通过xml定义属性
 */
public class SampleLinesStyledLayout extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_lines_layout);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(adapter);

        mIndicator = (LinePagerIndicator) findViewById(R.id.indicator);

        mIndicator.setViewPager(mPager);
    }

}
