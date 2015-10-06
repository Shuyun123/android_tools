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
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by anumbrella on 15-10-6.
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * tabs选项卡上方选项布局
 * <p/>
 * 说明:
 * *********
 * <p/>
 * linearLayout线性布局的扩展,支持分割线(需要android4.0+)，分割线是接触子部件的，
 * 可通过设定layout param来改变，如果你想通过改变margin来设定，
 * 确定layout当中包含子部件
 */
public class IcsLinearLayout extends LinearLayout {


    /**
     * 设定xml可以设定的参数
     */
    private static final int[] LL = new int[]{
        /* 0 */      android.R.attr.divider,
        /* 1 */      android.R.attr.showDividers,
        /* 2 */      android.R.attr.dividerPadding
    };


    /**
     * 设定属性数组取值顺序
     */
    private static final int LL_DIVIDER = 0;
    private static final int LL_SHOWDIVIDERS = 1;
    private static final int LL_DIVIDERPADDING = 2;


    /**
     * 设置分割线的属性,图片属性，宽，高，显示位置，padding
     */
    private Drawable mDivider;
    private int mDividerWidth;
    private int mDividerHeight;
    private int mShowDividers;
    private int mDividerPadding;


    public IcsLinearLayout(Context context, int attrs) {
        super(context);


        //获取设置的xml属性
        TypedArray array = context.obtainStyledAttributes(null, LL, attrs, 0);
        setDividerDrawable(array.getDrawable(IcsLinearLayout.LL_DIVIDER));
        mDividerPadding = array.getDimensionPixelSize(IcsLinearLayout.LL_DIVIDERPADDING, 0);
        mShowDividers = array.getInteger(IcsLinearLayout.LL_SHOWDIVIDERS, SHOW_DIVIDER_NONE);
        array.recycle();
    }

    /**
     * 覆写分割线属性设定
     *
     * @param divider
     */
    @Override
    public void setDividerDrawable(Drawable divider) {

        if (divider == null) {
            return;
        }

        //设定divider
        mDivider = divider;

        //不为空，就获得原本固有的属性
        if (divider != null) {
            mDividerWidth = divider.getIntrinsicWidth();
            mDividerWidth = divider.getIntrinsicHeight();
        } else {
            mDividerWidth = 0;
            mDividerHeight = 0;
        }

        /**
         *设置view是否更改，如果用自定义的view，
         *重写ondraw()应该将调用此方法设置为false，这样程序会调用自定义的布局
         *
         * 如果divider为null,就不会调用ondraw()绘制
         */
        setWillNotDraw(divider == null);
        //更新视图
        requestLayout();
    }


    /**
     * 对子部件进行大小测量
     *
     * @param child
     * @param parentWidthMeasureSpec
     * @param widthUsed
     * @param parentHeightMeasureSpec
     * @param heightUsed
     */
    @Override
    protected void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed,
                                           int parentHeightMeasureSpec, int heightUsed) {

        //获取子视图的指针
        final int index = indexOfChild(child);

        //获得方向
        final int orientaion = getOrientation();

        //获取布局参数
        final LayoutParams params = (LayoutParams) child.getLayoutParams();

        //检测是否显示分割线
        if (hasDividerBeforeChildAt(index)) {

            if (orientaion == VERTICAL) {
                params.topMargin = mDividerHeight;
            } else {

                params.leftMargin = mDividerWidth;
            }

        }

        super.measureChildWithMargins(child, parentWidthMeasureSpec,
                widthUsed, parentHeightMeasureSpec, heightUsed);
    }


    /**
     * 绘制边界(分割线)
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        if (mDivider != null) {
            if (getOrientation() == VERTICAL) {
                drawDividerVertical(canvas);
            } else {
                drawDividerHorizontal(canvas);
            }
        }
        super.onDraw(canvas);
    }

    /**
     * 绘制垂直分割线
     *
     * @param canvas
     */
    private void drawDividerVertical(Canvas canvas) {

        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                if (hasDividerBeforeChildAt(i)) {
                    final LayoutParams parmas = (LayoutParams) child.getLayoutParams();
                    //减去分割线高度后距离父视图顶端的距离
                    final int top = child.getTop() - parmas.topMargin;
                    drawaHorizontalDivider(canvas, top);
                }
            }
        }
    }

    /**
     * 绘制垂直分割线时水平的长度
     *
     * @param canvas
     * @param top
     */
    private void drawaHorizontalDivider(Canvas canvas, int top) {

        //设置分割线的位置(大小、高度)
        mDivider.setBounds(getPaddingLeft() + mDividerPadding, top,
                getWidth() - getPaddingRight() - mDividerPadding, top + mDividerHeight);

        mDivider.draw(canvas);
    }

    /**
     * 绘制水平分割线
     *
     * @param canvas
     */
    private void drawDividerHorizontal(Canvas canvas) {

        final int count = getChildCount();
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child != null && child.getVisibility() != GONE) {
                if (hasDividerBeforeChildAt(i)) {
                    final LayoutParams parmas = (LayoutParams) child.getLayoutParams();
                    //减去分割线宽度后距离父视图左端的距离
                    final int left = child.getLeft() - parmas.leftMargin;
                    drawVerticalDivider(canvas, left);
                }
            }
        }


    }

    /**
     * 绘制水平分割线时垂直线的高度
     *
     * @param canvas
     * @param left
     */
    private void drawVerticalDivider(Canvas canvas, int left) {

        //设置分割线的位置和高度
        mDivider.setBounds(left, getTop() + mDividerPadding, left + mDividerWidth + mDividerPadding,
                getHeight() - getPaddingBottom() - mDividerPadding);

        mDivider.draw(canvas);
    }


    /**
     * 判断是否要显示分割线
     *
     * @param index
     * @return
     */
    private boolean hasDividerBeforeChildAt(int index) {

        if (index == 0 || index == getChildCount()) {
            return false;
        }

        //判断分割线的位置(只用在middle时&运行才可以不为0),SHOW_DIVIDER_MIDDLE = 2
        if ((mShowDividers & SHOW_DIVIDER_MIDDLE) != 0) {
            boolean hasVisibleViewBefore = false;
            //检测当前视图的前面是否可以显示
            for (int i = index; i >= 0; i--) {

                if (getChildAt(i).getVisibility() != GONE) {
                    hasVisibleViewBefore = true;
                    break;
                }

            }

            return hasVisibleViewBefore;
        }

        return false;
    }

}
