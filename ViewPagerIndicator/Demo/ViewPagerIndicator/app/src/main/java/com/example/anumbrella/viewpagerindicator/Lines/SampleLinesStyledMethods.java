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
 * Lines指示器通过java定义方法制定属性
 */
public class SampleLinesStyledMethods extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_lines);

        adapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager) findViewById(R.id.pager);

        mPager.setAdapter(adapter);

        LinePagerIndicator indicator = (LinePagerIndicator) findViewById(R.id.indicator);

        mIndicator = indicator;
        indicator.setViewPager(mPager);


        //通过预留的方法设置属性
        final float density = getResources().getDisplayMetrics().density;
        indicator.setSelectedColor(0x88FF0000);
        indicator.setUnselectedColor(0xFF888888);
        indicator.setStrokeWidth(density * 4);
        indicator.setLineWidth(density * 30);

    }


}
