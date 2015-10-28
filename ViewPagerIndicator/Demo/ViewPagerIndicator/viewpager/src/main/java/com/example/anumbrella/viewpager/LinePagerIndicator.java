/*
 * Copyright (C) 2012 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.anumbrella.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by anumbrella on 15/10/20.
 * <p/>
 * 线条指示器
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * LinePagerIndicator绘制下划线为每个页面，当选中该页面时线条颜色会相应进行改变
 */
public class LinePagerIndicator extends View implements PagerIndicator {


    /**
     * 设置指示器的宽度
     */
    private float mLineWidth;


    /**
     * 设置指示器间的间隔距离
     */
    private float mGapWidth;


    /**
     * 指示器是否在屏幕的中央显示
     */
    private boolean mCentered;


    /**
     * 设置ViewPager页面滑动改变的监听类
     */
    private ViewPager.OnPageChangeListener pageChangeListener;


    /**
     * 设置画笔保真
     */
    private final Paint mPaintUnselected = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 设置画笔保真
     */
    private final Paint mPaintselected = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 移动至少要滑动的距离
     */
    private int mTouchSlop;

    private ViewPager mViewPager;

    /**
     * 当前页面索引
     */
    private int mCurrentPage;


    public LinePagerIndicator(Context context) {
        this(context, null);
    }

    public LinePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiLinePagerIndicatorStyle);
    }


    public LinePagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //获取资源对象
        final Resources res = getResources();

        //加载默认设置属性
        //选中当前的指示器的颜色
        final int defaultSelectedColor = res.getColor(R.color.default_line_indicator_selected_color);
        //没有选中的指示器的颜色
        final int defaultUnselectedColor = res.getColor(R.color.default_line_indicator_unselected_color);
        //线指示器的宽度
        final float defalutLineWidth = res.getDimension(R.dimen.default_line_indicator_line_width);
        //线指示器之间的间隔距离
        final float defalutGapWidth = res.getDimension(R.dimen.default_line_indicator_gap_widh);
        //设置指示器的高度
        final float defalutStrokeWidth = res.getDimension(R.dimen.default_line_indicator_stroke_width);
        //指示器是否在屏幕中央显示
        final boolean defaultCentered = res.getBoolean(R.bool.default_line_indicator_centered);
        //加载xml中设置的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.LinePagerIndicator, defStyle, 0);

        mCentered = array.getBoolean(R.styleable.LinePagerIndicator_centered, defaultCentered);
        mLineWidth = array.getDimension(R.styleable.LinePagerIndicator_lineWidth, defalutLineWidth);
        mGapWidth = array.getDimension(R.styleable.LinePagerIndicator_gapWidth, defalutGapWidth);
        //设置指示器线条的高度
        setStrokeWidth(array.getDimension(R.styleable.LinePagerIndicator_strokeWidth, defalutStrokeWidth));
        mPaintUnselected.setColor(array.getColor(R.styleable.LinePagerIndicator_selectedColor, defaultSelectedColor));
        mPaintselected.setColor(array.getColor(R.styleable.LinePagerIndicator_selectedColor, defaultSelectedColor));

        Drawable background = array.getDrawable(R.styleable.LinePagerIndicator_android_background);

        //设置指示器的背景图片
        if (background != null) {
            setBackgroundDrawable(background);
        }
        array.recycle();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        //设置手势移动的距离最小触发点距离(即：一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。)
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if(mViewPager == null){
            return;
        }

        if(mViewPager != null){
            mViewPager.setOnPageChangeListener(null);
        }

        if(viewPager.getAdapter() == null){
            throw  new IllegalStateException("ViewPager dose not have adapter instance!");
        }

        mViewPager = viewPager;
        //设置ViewPage页面监听
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {

        if(mViewPager != null){
            throw  new IllegalStateException("ViewPager has not been found!");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        pageChangeListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        invalidate();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
          if(pageChangeListener != null){
              pageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
          }
    }

    public void setmLineWidth(float mLineWidth) {
        this.mLineWidth = mLineWidth;
    }

    public void setmCentered(boolean mCentered) {
        this.mCentered = mCentered;
    }

    public void setmGapWidth(float mGapWidth) {
        this.mGapWidth = mGapWidth;
        invalidate();
    }

    /**
     * 设置画笔的宽度，即指示器线的高度
     *
     * @param lineHeight
     */
    public void setStrokeWidth(float lineHeight) {
        mPaintselected.setStrokeWidth(lineHeight);
        mPaintUnselected.setStrokeWidth(lineHeight);
        invalidate();
    }

    /**
     * 获取画笔的宽度,即指示器线条的高度
     *
     * @return
     */
    public float getStrokeWidth() {
        return mPaintselected.getStrokeWidth();
    }


    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }


    public void setSelectedColor(int selectedColor) {
        mPaintselected.setColor(selectedColor);
        invalidate();
    }

    public boolean ismCentered() {
        return mCentered;
    }

    public float getmGapWidth() {
        return mGapWidth;
    }

    public float getmLineWidth() {
        return mLineWidth;
    }


    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public int getSelectedColor() {
        return mPaintselected.getColor();

    }

    @Override

    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();

        if(pageChangeListener != null){
             pageChangeListener.onPageSelected(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if(pageChangeListener != null){
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
