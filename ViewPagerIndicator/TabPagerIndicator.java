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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author anumbrella
 * 
 * @date 2015-9-19 下午1:16:05
 * 
 *       来源于Jake Wharton,查看源码,覆写并添加中文注释
 * 
 *       TabPagerIndicator可以根据配置让widget实现bar上的tab动态改变。
 */
public class TabPagerIndicator extends HorizontalScrollView implements
		PagerIndicator {

	/**
	 * 当adapter没有提供主题时,默认使用该主题
	 */
	private static final CharSequence EMPTY_TITLE = "";

	/**
	 * 定义一个监听接口,用于当tab被重新选择时调用
	 */
	public interface OnTabReselectedListener {
		/**
		 * 回调函数,当tab被重新选择时调用
		 * 
		 * @param position
		 *            当前tab的位置
		 */
		void OnTabReselected(int position);
	}

	private Runnable mTabSelector;

	private final LinearLayout mTabLayout;

	private ViewPager mViewPager;

	/**
	 * ViewPager选择改变监听器
	 */
	private ViewPager.OnPageChangeListener pageChangeListener;

	/**
	 * 定义单个tab最大的宽度
	 */
	private int mMaxTabWidth;

	/**
	 * 选中的tab项的指针
	 */
	private int mSelectedTabIndex;

	/**
	 * 回调函数接口监听
	 */
	private OnTabReselectedListener mTabReselectedListener;

	/**
	 * Tab的点击监听事件
	 */
	private final OnClickListener mTabClickListenter = new OnClickListener() {

		@Override
		public void onClick(View v) {
			TabView tabView = (TabView) v;
			final int oldSelected = mViewPager.getCurrentItem();
			final int newSelected = tabView.getIndex();
			mViewPager.setCurrentItem(newSelected);
			// 判断是否是重新选中(回调函数的启用)
			if (oldSelected == newSelected && mTabReselectedListener != null) {
				mTabReselectedListener.OnTabReselected(newSelected);
			}

		}
	};

	public TabPagerIndicator(Context context) {
		this(context, null);
	}

	public TabPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// 设置HorizontalScrollBar是否可以拖动
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new LinearLayout(getContext());
		// 添加视图Tab到ViewGroup
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	/**
	 * 设置tab选择回调函数监听
	 * 
	 * @param listener
	 */
	public void setOnTabReselectedListener(OnTabReselectedListener listener) {
		mTabReselectedListener = listener;
	}

	/**
	 * 向bar布局当中添加tab选项
	 * 
	 * @param text
	 * @param index
	 */
	private void addTab(CharSequence text, int index) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		// 为每一个tab都添加单独的一个监听
		tabView.setOnClickListener(mTabClickListenter);
		tabView.setText(text);
		// 将单个TabView添加到bar布局当中去
		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0,
				MATCH_PARENT, 1));
	}

	/**
	 * 测量视图大小
	 */
	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// 是否是匹配父类(match_parent)
		final boolean lockedExpand = (widthMode == MeasureSpec.EXACTLY);
		setFillViewport(lockedExpand);

		final int count = mTabLayout.getChildCount();

		if (count > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (count > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();
		// 屏幕重新测量后的操作
		if (lockedExpand && oldWidth != newWidth) {
			// 重新覆写该方法
			setCurrentItem(mSelectedTabIndex);
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (pageChangeListener != null) {
			pageChangeListener.onPageScrollStateChanged(arg0);
		}

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		if (pageChangeListener != null) {
			pageChangeListener.onPageScrolled(arg0, arg1, arg2);
		}

	}

	@Override
	public void onPageSelected(int arg0) {
		// 设置ViewPage的页面
		setCurrentItem(arg0);
		if (pageChangeListener != null) {
			pageChangeListener.onPageSelected(arg0);
		}
	}

	@Override
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == mViewPager) {
			return;
		}
		if (mViewPager != null) {
			mViewPager.setOnPageChangeListener(null);
		}

		final PagerAdapter adapter = viewPager.getAdapter();
		if (adapter == null) {
			throw new IllegalStateException(
					"ViewPager does not have adapter instance.");
		}
		mViewPager = viewPager;
		viewPager.setOnPageChangeListener(this);
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
		// 设置当前页面的指针
		mViewPager.setCurrentItem(mSelectedTabIndex);

		final int tabCount = mTabLayout.getChildCount();

		// 设定选中tab和移动动画
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			boolean isSelected = (i == item);
			child.setSelected(isSelected);
			if (child.isSelected()) {
				// 如果选中就将当前的选中移动到当前位置
				animateToTab(item);
			}
		}
	}

	/**
	 * 选中tab的移动动画(当tab的宽度超过手机屏幕才会移动)
	 * 
	 * @param item
	 */
	private void animateToTab(int item) {
		final View tabView = mTabLayout.getChildAt(item);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			@Override
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		// 将runnable添加线程执行队列中
		post(mTabSelector);
	}

	@Override
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		pageChangeListener = listener;
	}

	@Override
	public void notifyDataSetChanged() {
		mTabLayout.removeAllViews();
		PagerAdapter adapter = mViewPager.getAdapter();
		// 返回数据更新后数据的长度
		final int count = adapter.getCount();
		for (int i = 0; i < count; i++) {
			CharSequence title = adapter.getPageTitle(i);
			if (title == null) {
				title = EMPTY_TITLE;
			}
			addTab(title, i);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	/*
	 * 定位view附加到窗口上
	 * 
	 * View和Window绑定时就会调用这个函数
	 * 
	 * 一般会在这里进行ui的宽和高的设定(在onResume()方法后启动)
	 */
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}

	/**
	 * 设置bar添加的TabView视图
	 */
	private class TabView extends TextView {

		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPagerIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// 如果每个tab超过最大的尺寸,就重新测量
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}

		}

		public int getIndex() {
			return mIndex;
		}

	}

}
