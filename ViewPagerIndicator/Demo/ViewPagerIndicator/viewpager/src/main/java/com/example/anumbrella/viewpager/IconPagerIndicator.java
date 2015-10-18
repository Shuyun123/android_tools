package com.example.anumbrella.viewpager;
/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by anumbrella on 15-10-18.
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * IconPagerIndicater可以根据配置让widget实现bar上的tab动态改变。
 */
public class IconPagerIndicator extends HorizontalScrollView implements PagerIndicator {


    /**
     * tabas选项卡上方布局
     */
    private final IcsLinearLayout mIconsLayout;

    private ViewPager mViewPager;


    /**
     * ViewPager选择改变监听器
     */
    private OnPageChangeListener pageChangeListener;


    /**
     * tab选项item移动的运行事件
     */
    private Runnable mIconSelector;


    /**
     * 选中的tab项的指针
     */
    private int mSelectedTabIndex;


    public IconPagerIndicator(Context context) {
        this(context, null);
    }

    public IconPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置HorizontalScrollBar是否可以拖动,显示滑动条
        setHorizontalScrollBarEnabled(false);
        //设置tabs选项卡上方属性(通过xml改变)
        mIconsLayout = new IcsLinearLayout(context, R.attr.vpiTabPagerIndicatorStyle);
        // 添加视图Tab到ViewGroup
        addView(mIconsLayout, new LayoutParams(WRAP_CONTENT,
                MATCH_PARENT, Gravity.CENTER));

    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mIconSelector != null) {
            post(mIconSelector);
        }
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mIconSelector != null) {
            removeCallbacks(mIconSelector);
        }
    }

    /**
     * 自定义设定ViewPager
     *
     * @param viewPager
     */
    @Override
    public void setViewPager(ViewPager viewPager) {
        if (mViewPager == viewPager) {
            return;
        }

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        PagerAdapter adapter = viewPager.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        viewPager.setOnPageChangeListener(this);
        //更新数据更改的情况
        notifyDataSetChanged();
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        // 设置默认打开的tab页面
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        mViewPager.setCurrentItem(mSelectedTabIndex);
        int tabCount = mIconsLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            View child = mIconsLayout.getChildAt(i);
            boolean isSelected = (i == item);
            //根据是否选中的情况依据XML的属性改变状态
            child.setSelected(isSelected);
            //选中就移动到当前的视图
            if (isSelected) {
                animateToIcon(item);
            }
        }
    }

    /**
     * 移动视图
     *
     * @param position
     */
    private void animateToIcon(int position) {
        final View iconView = mIconsLayout.getChildAt(position);
        if (mIconSelector != null) {
            //先删除先前的事件
            removeCallbacks(mIconSelector);
        }

        mIconSelector = new Runnable() {
            @Override
            public void run() {
                //当scrollPos < 0 时 并不会移动
                final int scrollPos = iconView.getLeft() - (getWidth() - iconView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mIconSelector = null;
            }
        };

        // 将runnable添加线程执行队列中
        post(mIconSelector);
    }

    /**
     * 设置回调监听类
     *
     * @param listener
     */
    @Override
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        pageChangeListener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        mIconsLayout.removeAllViews();
        IconPagerAdapter iconAdapter = (IconPagerAdapter) mViewPager.getAdapter();
        //计算多少条数据
        int count = iconAdapter.getCount();
        for (int i = 0; i < count; i++) {
            //可根据XML定义属性
            ImageView imageView = new ImageView(getContext(), null, R.attr.vpiIconPagerIndicatorStyle);
            imageView.setImageResource(iconAdapter.getIconResId(i));
            //添加到tab布局中
            mIconsLayout.addView(imageView);
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        //更新视图
        requestLayout();
    }

    /**
     * 页面滑动时会一直调用该方法
     *
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    /**
     * ViewPager页面选中后执行
     *
     * @param position
     */
    @Override
    public void onPageSelected(int position) {
        setCurrentItem(position);
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }
    }

    /**
     * 当滑动状态改变时调用
     *
     * @param state
     */
    @Override
    public void onPageScrollStateChanged(int state) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }
}
