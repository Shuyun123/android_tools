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

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;


/**
 * Created by anumbrella on 15-11-5.
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * CirclePagerIndicator圆形适配器,为每个页面绘制圆形，当前页面圆形被填充，其它页面为空
 */
public class CirclePagerIndicator extends View implements PagerIndicator {


    /**
     * 设置无效的触点值
     */
    private static final int INVALID_POINTER = -1;


    /**
     * 设置指示器中圆圈的半径大小
     */
    private float mRadius;


    /**
     * 设置画笔保真，指示器圆圈填充的颜色
     */
    private final Paint mPaintPageFill = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 设置画笔保真，没有被填充的空心圆圈绘制的画笔
     */
    private final Paint mPaintStroke = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 设置画笔保真,选中页面指示器圆圈绘制的画笔
     */
    private final Paint mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);

    private ViewPager mViewPager;


    /**
     * 设置ViewPager页面滑动改变的监听类
     */
    private ViewPager.OnPageChangeListener pageChangeListener;


    /**
     * 设置当前页面的索引
     */
    private int mCurrentPage;


    /**
     * 设置直接快速传导页面的索引
     */
    private int mSnapPage;

    /**
     * 每次滑动偏移页面的百分比
     */
    private float mPositionOffset;

    /**
     * 页面滑动的状态标识
     */
    private int mScrollState;


    /**
     * 指示器布局方位
     */
    private int mOrientation;


    /**
     * 指示器是否在页面中心位置
     */
    private boolean mCentered;


    /**
     * 移动至少要滑动的距离
     */
    private int mTouchSlop;


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
     * 指示器的圆圈填充是否快速传过去
     */
    private boolean mSnap;



    public CirclePagerIndicator(Context context) {
        this(context, null);
    }

    public CirclePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiCirclePagerIndicatorStyle);
    }

    public CirclePagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        //解决自定义视图无法编辑预览报错时调用
        if (isInEditMode()) {
            return;
        }

        //获取资源对象
        final Resources res = getResources();

        //加载默认设置属性
        //选中当前的指示器的颜色

        //选择当前页面圆形填充的颜色
        final int defaultPageColor = res.getColor(R.color.default_circle_indicator_page_color);
        //圆形填充颜色
        final int defaultFillColor = res.getColor(R.color.default_circle_indicator_fill_color);
        //指示器默认方位
        final int defaultOrientation = res.getInteger(R.integer.default_circle_indicator_orientation);
        //画笔的颜色
        final int defaultStrokeColor = res.getColor(R.color.default_circle_indicator_stroke_color);
        //画笔的宽度
        final float defaultStrokeWidth = res.getDimension(R.dimen.default_circle_indicator_stroke_width);
        //圆圈的默认半径大小
        final float defaultRadius = res.getDimension(R.dimen.default_circle_indicator_radius);
        //是否位于圆心
        final boolean defaultCentered = res.getBoolean(R.bool.default_circle_indicator_centered);
        //指示器的圆圈填充是否快速传过去(是否填充圆有移动的动画(或者就是直接显示在下一个圆圈))
        final boolean defaultSnap = res.getBoolean(R.bool.default_circle_indicator_snap);

        //加载xml中设置的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CirclePagerIndicator, defStyle, 0);

        mCentered = array.getBoolean(R.styleable.CirclePagerIndicator_centered, defaultCentered);
        mOrientation = array.getInteger(R.styleable.CirclePagerIndicator_android_orientation, defaultOrientation);
        //设置画笔为实心填充
        mPaintPageFill.setStyle(Paint.Style.FILL);
        //设置画笔的颜色
        mPaintPageFill.setColor(array.getColor(R.styleable.CirclePagerIndicator_pageColor, defaultPageColor));
        //设置画笔为实心填充
        mPaintFill.setStyle(Paint.Style.FILL);
        //设置画笔的颜色
        mPaintFill.setColor(array.getColor(R.styleable.CirclePagerIndicator_fillColor, defaultFillColor));
        //设置画笔的颜色
        mPaintStroke.setColor(array.getColor(R.styleable.CirclePagerIndicator_strokeColor, defaultStrokeColor));
        //设置画笔为空心填充(仅仅描边)
        mPaintStroke.setStyle(Paint.Style.STROKE);
        //设置画笔的宽度
        mPaintStroke.setStrokeWidth(array.getDimension(R.styleable.CirclePagerIndicator_strokeWidth, defaultStrokeWidth));
        //获取的圆圈的半径大小
        mRadius = array.getDimension(R.styleable.CirclePagerIndicator_radius, defaultRadius);
        //获取视图的背景
        mSnap = array.getBoolean(R.styleable.CirclePagerIndicator_snap, defaultSnap);
        Drawable background = array.getDrawable(R.styleable.CirclePagerIndicator_android_background);
        if (background != null) {
            setBackgroundDrawable(background);
        }
        array.recycle();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }


    public boolean isCentered() {
        return mCentered;
    }

    public void setCentered(boolean isCentered) {
        mCentered = isCentered;
        invalidate();
    }

    public int getPageColor() {
        return mPaintPageFill.getColor();
    }

    public void setPageColor(int pageColor) {
        mPaintPageFill.setColor(pageColor);
        invalidate();
    }

    public void setFillColor(int fillColor) {
        mPaintFill.setColor(fillColor);
        invalidate();
    }

    public int getFillColor() {
        return mPaintFill.getColor();
    }

    public void setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL:
            case VERTICAL:
                mOrientation = orientation;
                requestLayout();
                break;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL!");
        }
    }

    public int getOrientation() {
        return mOrientation;
    }


    public void setStrokeColor(int strokeColor) {
        mPaintStroke.setColor(strokeColor);
        invalidate();
    }

    public int getStrokeColor() {
        return mPaintStroke.getColor();
    }


    public void setStrokeWidth(float strokeWidth) {
        mPaintStroke.setStrokeWidth(strokeWidth);
        invalidate();
    }


    public float getStrokeWidth() {
        return mPaintStroke.getStrokeWidth();
    }


    public void setRadius(float radius) {
        mRadius = radius;
        invalidate();
    }

    public float getRadius() {
        return mRadius;
    }

    public void setSnap(boolean snap) {
        mSnap = snap;
        invalidate();
    }

    public boolean isSnap() {
        return mSnap;
    }


    /**
     * 设置ViewPager视图
     *
     * @param viewPager
     */
    @Override
    public void setViewPager(ViewPager viewPager) {

        if (viewPager == mViewPager) {
            return;
        }

        if (viewPager != null) {
            viewPager.setOnPageChangeListener(null);
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager dose not have adapter instance.");
        }

        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }


    /**
     * 绘制圆形指示器
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {
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

        //定义指示器的视图的长度
        int longSize;
        //定义起初值的padding
        int longPaddingBefore;
        //定义之后的padding
        int longPaddingAfter;
        //定义起初的较短的padding
        int shortPaddingBefore;
        if (mOrientation == HORIZONTAL) {
            longSize = getWidth();
            longPaddingBefore = getPaddingLeft();
            longPaddingAfter = getPaddingRight();
            shortPaddingBefore = getPaddingTop();
        } else {
            longSize = getHeight();
            longPaddingBefore = getPaddingTop();
            longPaddingAfter = getPaddingBottom();
            shortPaddingBefore = getPaddingLeft();
        }

        //设置元圆点指示器当中每个圆的大小，每个彼此间相隔半个圆的距离
        final float threeRadius = mRadius * 3;
        //设置指示器偏移量的大小(horizontal为上下的偏移量,vertical为左右的偏移量)
        final float shortOffset = shortPaddingBefore + mRadius;
        //设置指示器布局位置显示的偏移量
        float longOffset = longPaddingAfter + mRadius;
        if (mCentered) {
            longOffset += ((longSize - longPaddingBefore - longPaddingAfter) / 2.0f) - ((count * threeRadius) / 2.0f);
        }
        //设定x,y坐标的值
        float dx;
        float dy;
        //设置选中页面的填充圆圈的半径大小
        float pageFillRadius = mRadius;
        if (mPaintStroke.getStrokeWidth() > 0) {
            pageFillRadius -= mPaintStroke.getStrokeWidth() / 2.0f;
        }
        //开始绘制圆点指示器的圆圈
        for (int iLoop = 0; iLoop < count; iLoop++) {
            float drawLong = longOffset + (iLoop * threeRadius);
            if (mOrientation == HORIZONTAL) {
                dx = drawLong;
                dy = shortOffset;
            } else {
                dx = shortOffset;
                dy = drawLong;
            }
            //只绘制画笔的透明度不为0的填充视图
            if (mPaintPageFill.getAlpha() > 0) {
                canvas.drawCircle(dx, dy, pageFillRadius, mPaintPageFill);
            }
            //只绘制填充视图半径比指示器圆圈小的视图圆圈
            if (pageFillRadius != mRadius) {
                canvas.drawCircle(dx, dy, mRadius, mPaintStroke);
            }
        }
        //绘制选中页面时的填充指示器圆圈
        float cx = (mSnap ? mSnapPage : mCurrentPage) * threeRadius;
        if (!mSnap) {
            cx += mPositionOffset * threeRadius;
        }

        if (mOrientation == HORIZONTAL) {
            dx = longOffset + cx;
            dy = shortOffset;
        } else {
            dx = shortOffset;
            dy = longOffset + cx;
        }
        canvas.drawCircle(dx, dy, mRadius, mPaintFill);
    }

    /**
     * 屏幕手势操作的判断
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {

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
                mActivePointerId = MotionEventCompat.getPointerId(event,0);
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
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (!isDragging) {
                    //通过点击指示器左右两边实现切换
                    final int count = mViewPager.getAdapter().getCount();
                    //设置移动的距离方位
                    final int width = getWidth();
                    final float halfWidth = width / 2f;
                    final float sixWidth = width / 6f;
                    //设置左点击的事件,向左滑动
                    if ((mCurrentPage > 0) && (event.getX() < halfWidth - sixWidth)) {
                        if (action != MotionEvent.ACTION_CANCEL) {
                            mViewPager.setCurrentItem(mCurrentPage - 1);
                        }
                    } else if ((mCurrentPage < count - 1) && (event.getX() > halfWidth + sixWidth)) {
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
                mActivePointerId = MotionEventCompat.getPointerId(event,index);
                break;
            case MotionEventCompat.ACTION_POINTER_UP:
                final int pointerIndex = MotionEventCompat.getActionIndex(event);
                final int pointerId = MotionEventCompat.getPointerId(event, pointerIndex);
                if (pointerId == mActivePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mActivePointerId = MotionEventCompat.getPointerId(event, newPointerIndex);
                }
                mLastMotionX = MotionEventCompat.getX(event, MotionEventCompat.findPointerIndex(event, mActivePointerId));
                break;
        }
        return true;
    }

    @Override
    public void setViewPager(ViewPager viewPager, int initialPosition) {
        setViewPager(viewPager);
        setCurrentItem(initialPosition);
    }


    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
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
        mPositionOffset = positionOffset;
        mCurrentPage = position;
        invalidate();
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mSnap || mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mSnapPage = position;
            invalidate();
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
     * 测量绘制视图的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mOrientation == HORIZONTAL) {
            setMeasuredDimension(measureLong(widthMeasureSpec), measureShort(heightMeasureSpec));
        } else {
            setMeasuredDimension(measureShort(widthMeasureSpec), measureLong(heightMeasureSpec));
        }

    }


    /**
     * 测量视图的宽度
     *
     * @param measureSpec
     * @return
     */
    public int measureLong(int measureSpec) {

        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        //开始测量视图为空，视图模式：如match_parent
        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //当没有视图时就为实际测量的结果
            result = specSize;
        } else {
            //通过计算得出最终的视图的宽的大小
            //计算视图的总数
            final int count = mViewPager.getAdapter().getCount();
            result = (int) (getPaddingLeft() + getPaddingRight() + (count * 2 * mRadius) + (count - 1) * mRadius + 1);
            //如果视图的类型为AT_MOST(如:wrap_content)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量视图的高度
     *
     * @param measureSpec
     * @return
     */
    public int measureShort(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            //当没有视图时就为实际测量的结果
            result = specSize;
        } else {
            result = (int) (2 * mRadius + getPaddingBottom() + getPaddingTop() + 1);
            //如果视图的类型为AT_MOST(如:wrap_content)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
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
        mSnapPage = savedState.currentPage;
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
