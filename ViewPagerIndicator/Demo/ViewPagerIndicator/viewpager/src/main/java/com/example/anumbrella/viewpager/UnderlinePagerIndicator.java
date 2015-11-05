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
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by anumbrella on 15-11-4.
 * <p/>
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * UnderlinePagerIndicator绘制线条为当前选中的每一个页面
 */
public class UnderlinePagerIndicator extends View implements PagerIndicator {


    /**
     * 设置无效的触点值
     */
    private static final int INVALID_POINTER = -1;


    /**
     * 每次线程再次进行操作的时间
     */
    private static final int FADE_TIME = 30;


    /**
     * 指示器是否消失
     */
    private boolean mFades;

    /**
     * 指示器消失的时间
     */
    private int mFadeDelay;

    /**
     * 定义指示器的长度
     */
    private int mFadeLength;

    private ViewPager mViewPager;

    /**
     * 设置ViewPager页面滑动改变的监听类
     */
    private ViewPager.OnPageChangeListener pageChangeListener;


    /**
     * 当前选中的页面
     */
    private int mCurrentPage;


    /**
     * 移动至少要滑动的距离
     */
    private int mTouchSlop;


    /**
     * 设置画笔保真
     */
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 设置拖动的标识
     */
    private boolean isDragging = false;


    /**
     * 指定触点的标识
     */
    private int mActivePointerId = INVALID_POINTER;


    /**
     * 设定最后移动时x坐标的值
     */
    private float mLastMotionX = -1;


    /**
     * 每次透明度减少的值
     */
    private int mFadeBy;


    /**
     * 每次滑动偏移页面的百分比
     */
    private float mPositionOffset;


    /**
     * 定义一个线程对指示器每次透明度的值进行操作
     */
    private final Runnable mFadeRunable = new Runnable() {
        @Override
        public void run() {
            if (!mFades) {
                return;
            }
            //每次取相减过后的最大值，直到最后的值为0
            final int alpha = Math.max(mPaint.getAlpha() - mFadeBy, 0);
            mPaint.setAlpha(alpha);
            invalidate();
            //如何透明度的值大于0，不停的执行线程操作
            if (alpha > 0) {
                postDelayed(this, FADE_TIME);
            }
        }
    };


    /**
     * 页面滑动的状态标识
     */
    private int mScrollState;

    public UnderlinePagerIndicator(Context context) {
        this(context, null);
    }


    public UnderlinePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiUnderlinePagerIndicatorStyle);
    }


    public UnderlinePagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //解决自定义视图无法编辑预览报错时调用
        if (isInEditMode()) {
            return;
        }

        //获取资源对象
        final Resources res = getResources();

        //加载默认的属性设置
        //指示器是否消失
        final boolean defaultFades = res.getBoolean(R.bool.default_underline_indicator_fades);
        //指示器消失的时间
        final int defaultFadeDelay = res.getInteger(R.integer.default_underline_indicator_fade_delay);
        //指示器线的长度
        final int defaultFadeLength = res.getInteger(R.integer.default_underline_indicator_fade_length);
        //选中当前页面指示器显示的颜色
        final int defaultSelectedColor = res.getColor(R.color.default_underline_indicator_selected_color);


        //获取在布局xml中定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.UnderlinePagerIndicator, defStyle, 0);

        setFades(array.getBoolean(R.styleable.UnderlinePagerIndicator_fades, defaultFades));
        setSelectedColor(array.getColor(R.styleable.UnderlinePagerIndicator_selectedColor, defaultSelectedColor));
        setFadeDelay(array.getInteger(R.styleable.UnderlinePagerIndicator_fadeDelay, defaultFadeDelay));
        setFadeLength(array.getInteger(R.styleable.UnderlinePagerIndicator_fadeLength, defaultFadeLength));

        Drawable background = array.getDrawable(R.styleable.UnderlinePagerIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }
        array.recycle();

        final ViewConfiguration configuration = ViewConfiguration.get(context);
        //设置手势移动的距离最小触发点距离(即：一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。)
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }


    /**
     * 设置指示器是否消失
     *
     * @param isFades
     */
    public void setFades(boolean isFades) {
        if (mFades != isFades) {
            mFades = isFades;
            if (mFades) {
                post(mFadeRunable);
            } else {
                //指示器不消失,删除线程操作
                removeCallbacks(mFadeRunable);
                /**
                 * 取值范围是0---255,数值越小，越透明，颜色上表现越淡
                 * 0xFF = 255
                 */
                mPaint.setAlpha(0xFF);
                invalidate();
            }
        }
    }


    /**
     * 设置指示器消失的时长
     *
     * @param fadeDelay
     */
    public void setFadeDelay(int fadeDelay) {
        mFadeDelay = fadeDelay;
    }


    /**
     * 设置指示器的长度
     *
     * @param fadeLength
     */
    public void setFadeLength(int fadeLength) {
        mFadeLength = fadeLength;

        //定义每次透明度减少的值
        // 0xFF = 255 ,
        // 默认值:mFadeLength = 400 ; FADE_TIME = 30
        // mFadeBy = 19.125
        // mFadeBy最终为19
        mFadeBy = 0xFF / (mFadeLength / FADE_TIME);
    }


    /**
     * 设置指示器选中页面时的颜色
     *
     * @param selectedColor
     */

    public void setSelectedColor(int selectedColor) {
        mPaint.setColor(selectedColor);
        invalidate();
    }


    public boolean getFades() {
        return mFades;
    }


    public int getFadeDelay() {
        return mFadeDelay;
    }

    public int getFadeLength() {
        return mFadeLength;
    }

    public int getSelectedColor() {
        return mPaint.getColor();
    }


    /**
     * 绘制下划线指示器
     */
    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }

        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final float paddingLeft = getPaddingLeft();
        final float paddingRight = getPaddingRight();
        final float pageWidth = (getWidth() - paddingLeft - paddingRight) / (1f * count);
        final float left = paddingLeft + pageWidth * (mCurrentPage + mPositionOffset);
        final float top = getPaddingTop();
        final float bottom = getHeight() - getPaddingBottom();
        final float right = left + pageWidth;
        //绘制下划线
        canvas.drawRect(left, top, right, bottom, mPaint);
    }


    /**
     * 屏幕手势操作的判断
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (super.onTouchEvent(event)) {
            return true;
        }

        if ((mViewPager == null) || (mViewPager.getAdapter().getCount() == 0)) {
            return false;
        }

        /**
         * ACTION_POINTER_DOWN:有一个非主要的手指按下了
         * ACTION_POINTER_UP:一个非主要的手指抬起来了
         * ACTION_MASK & ACTION 结果都是会ACTION_POINTER_DOWN或者ACTION_POINTER_UP
         */
        final int action = event.getAction() & MotionEventCompat.ACTION_MASK;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //getPointerId从0到getPointerCount-1,返回一个触摸点的标识,默认获取第0个
                mActivePointerId = MotionEventCompat.getPointerId(event, 0);
                mLastMotionX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                //获取活跃的pointer的信息的index索引
                final int activePointerIndex = MotionEventCompat.findPointerIndex(event, mActivePointerId);
                //获取活跃pointer的x轴的信息
                final float x = MotionEventCompat.getX(event, activePointerIndex);
                //获取x轴的值的改变量
                final float deltaX = x - mLastMotionX;

                if (!isDragging) {
                    if (Math.abs(deltaX) > mTouchSlop) {
                        isDragging = true;
                    }
                }
                if (isDragging) {
                    mLastMotionX = x;
                    if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
                        mViewPager.fakeDragBy(deltaX);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                //通过点击指示器左右两边实现切换
                if (!isDragging) {
                    final int count = mViewPager.getAdapter().getCount();
                    final int width = getWidth();

                    //设置移动的距离方位
                    final float halfWidth = width / 2f;
                    final float sixthWidth = width / 6f;
                    //设置左点击的事件,向左滑动
                    if ((mCurrentPage > 0) && (event.getX() < halfWidth - sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                        return true;
                    } else if ((mCurrentPage < count - 1) && (event.getX() > halfWidth + sixthWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage + 1);
                        }
                        return true;
                    }
                }

                isDragging = false;
                mActivePointerId = INVALID_POINTER;
                if (mViewPager.isFakeDragging()) {
                    mViewPager.endFakeDrag();
                }
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
                final int index = MotionEventCompat.getActionIndex(event);
                mLastMotionX = MotionEventCompat.getX(event, index);
                mActivePointerId = MotionEventCompat.getPointerId(event, index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(event,
                        MotionEventCompat.findPointerIndex(event, mActivePointerId));
                break;
        }
        return true;
    }


    @Override
    public void setViewPager(ViewPager viewPager) {
        if (viewPager == mViewPager) {
            return;
        }

        if (mViewPager != null) {
            //清除先前绑定的监听
            mViewPager.setOnPageChangeListener(null);
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager dose not have adapter instance!");
        }

        mViewPager = viewPager;
        //设置本页面的监听
        mViewPager.setOnPageChangeListener(this);
        invalidate();
        //开始指示器消失操作
        post(new Runnable() {
            @Override
            public void run() {
                if (mFades) {
                    post(mFadeRunable);
                }
            }
        });
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been found!");
        }
        mCurrentPage = item;
        mViewPager.setCurrentItem(item);
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
        mCurrentPage = position;
        mPositionOffset = positionOffset;
        if (mFades) {
            //如果在滑动就取消指示器消失操作
            if (positionOffsetPixels > 0) {
                removeCallbacks(mFadeRunable);
                mPaint.setAlpha(0xFF);
                //如何没有在进行滑动,就进行指示器消失操作
            } else if (mScrollState != ViewPager.SCROLL_STATE_DRAGGING) {
                postDelayed(mFadeRunable, mFadeDelay);
            }
        }
        invalidate();

        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        //活动页面状态是空闲的
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mPositionOffset = 0;
            invalidate();
            mFadeRunable.run();
        }
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }


    /**
     * 获取先前保存的视图状态
     *
     * @param state
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mCurrentPage = savedState.currentPage;
        requestLayout();
    }

    /**
     * 保存视图被销毁之前的状态
     *
     * @return
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPage = mCurrentPage;
        return savedState;
    }


    /**
     * 存储数据类,继承BaseSavedState
     */
    public static class SavedState extends BaseSavedState {

        private int currentPage;


        private SavedState(Parcel in) {
            super(in);
            currentPage = in.readInt();
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(currentPage);
        }

        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
