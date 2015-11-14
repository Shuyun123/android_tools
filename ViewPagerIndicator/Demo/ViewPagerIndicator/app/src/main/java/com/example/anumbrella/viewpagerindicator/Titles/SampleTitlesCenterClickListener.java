package com.example.anumbrella.viewpagerindicator.Titles;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.example.anumbrella.viewpager.TitlePagerIndicator;
import com.example.anumbrella.viewpagerindicator.BaseActivity;
import com.example.anumbrella.viewpagerindicator.Fragment.TestFragmentAdapter;
import com.example.anumbrella.viewpagerindicator.R;

/**
 * Created by anumbrella on 15/11/14.
 * <p/>
 * Titles指示器带回调监听事件
 */
public class SampleTitlesCenterClickListener extends BaseActivity implements TitlePagerIndicator.OnCenteredItem {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_titles);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);
        TitlePagerIndicator indicator = (TitlePagerIndicator) findViewById(R.id.indicator);
        indicator.setOnCenterItemClickListener(this);
        mPager.setAdapter(adapter);
        indicator.setViewPager(mPager);
    }


    /**
     * 回调方法
     *
     * @param position 点击的item的序列索引
     */
    @Override
    public void onCenteredClickItem(int position) {
        Toast.makeText(SampleTitlesCenterClickListener.this, "你点击了中间的标题", Toast.LENGTH_SHORT).show();

    }
}
