package com.example.anumbrella.viewpager;/*
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

import android.support.v4.view.ViewPager;

/**
 * @author anumbrella
 * 
 * @date 2015-9-19 下午1:41:41
 * 
 *       来源于Jake Wharton,查看源码,覆写并添加中文注释
 * 
 *       PagerIndicator是一个显示当前视图和总的视图数量的接口类
 * 
 *       //*_*英语比较差,欢迎指正
 * 
 */
public interface PagerIndicator extends ViewPager.OnPageChangeListener {

	/**
	 * 绑定指示器到ViewPager
	 * 
	 * @param viewPager
	 */
	void setViewPager(ViewPager viewPager);

	/**
	 * 绑定指示器到ViewPager(可指定初始位置的视图)
	 * 
	 * @param viewPager
	 * @param initialPosition
	 */
	void setViewPager(ViewPager viewPager, int initialPosition);

	/**
	 * 
	 * 设定当前在ViewPager和指示器当中的位置
	 * 
	 * 注意：这个方法必须使用,在屏幕绘制视图开始之前
	 * 
	 * @param item
	 */
	void setCurrentItem(int item);

	/**
	 * 设定滑动时page改变的监听接口
	 * 
	 * @param listener
	 */
	void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

	/**
	 * 通知指示器当前fragment的列表已经改变
	 */
	void notifyDataSetChanged();

}
