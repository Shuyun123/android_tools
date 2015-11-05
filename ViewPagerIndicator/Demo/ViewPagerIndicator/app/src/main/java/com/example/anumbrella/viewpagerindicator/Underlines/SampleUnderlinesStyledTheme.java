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
 * 通过主题(theme)来设置指示器的属性
 */
public class SampleUnderlinesStyledTheme extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStae) {
        super.onCreate(savedInstanceStae);
        //通过theme方法来设置样式的属性
        setContentView(R.layout.sample_underlines);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());
        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(adapter);

        mIndicator = (UnderlinePagerIndicator) findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
    }

}
