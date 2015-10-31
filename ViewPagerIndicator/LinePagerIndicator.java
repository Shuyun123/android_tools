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
     * 设置无效的触点值
     */
    private final int INVALID_POINTER = -1;


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
    private final Paint mPaintSelected = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 移动至少要滑动的距离
     */
    private int mTouchSlop;

    private ViewPager mViewPager;

    /**
     * 当前页面索引
     */
    private int mCurrentPage;


    /**
     * 指定触点的标识
     */
    private int mActivePointerId = INVALID_POINTER;


    /**
     * 设定最后移动时x坐标的值
     */
    private float mLastMotionX = -1;


    /**
     * 设置拖动的标识
     */
    private boolean isDragging = false;


    public LinePagerIndicator(Context context) {
        this(context, null);
    }

    public LinePagerIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.vpiLinePagerIndicatorStyle);
    }


    public LinePagerIndicator(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        if (isInEditMode()){
            return;
        }

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
        final float defalutGapWidth = res.getDimension(R.dimen.default_line_indicator_gap_width);
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
        mPaintUnselected.setColor(array.getColor(R.styleable.LinePagerIndicator_unselectedColor, defaultUnselectedColor));
        mPaintSelected.setColor(array.getColor(R.styleable.LinePagerIndicator_selectedColor, defaultSelectedColor));

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
        if (mViewPager == viewPager) {
            return;
        }

        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager dose not have adapter instance!");
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

        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been found!");
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
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void setLineWidth(float mLineWidth) {
        this.mLineWidth = mLineWidth;
        invalidate();
    }


    public void setCentered(boolean mCentered) {
        this.mCentered = mCentered;
        invalidate();
    }

    public void setGapWidth(float mGapWidth) {
        this.mGapWidth = mGapWidth;
        invalidate();
    }

    /**
     * 设置画笔的宽度，即指示器线的高度
     *
     * @param lineHeight
     */
    public void setStrokeWidth(float lineHeight) {
        mPaintSelected.setStrokeWidth(lineHeight);
        mPaintUnselected.setStrokeWidth(lineHeight);
        invalidate();
    }

    /**
     * 获取画笔的宽度,即指示器线条的高度
     *
     * @return
     */
    public float getStrokeWidth() {
        return mPaintSelected.getStrokeWidth();
    }


    public void setUnselectedColor(int unselectedColor) {
        mPaintUnselected.setColor(unselectedColor);
        invalidate();
    }


    public void setSelectedColor(int selectedColor) {
        mPaintSelected.setColor(selectedColor);
        invalidate();
    }

    public boolean isCentered() {
        return mCentered;
    }

    public float getGapWidth() {
        return mGapWidth;
    }

    public float getLineWidth() {
        return mLineWidth;
    }


    public int getUnselectedColor() {
        return mPaintUnselected.getColor();
    }

    public int getSelectedColor() {
        return mPaintSelected.getColor();
    }

    @Override

    public void onPageSelected(int position) {
        mCurrentPage = position;
        invalidate();

        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrollStateChanged(state);
        }
    }


    /**
     * 重新绘制视图
     *
     * @param canvas
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

        final float lineWidthAndGap = mLineWidth + mGapWidth;
        final float indicatorWidth = (count * lineWidthAndGap) - mGapWidth;
        final float paddingTop = getPaddingTop();
        final float paddingLeft = getPaddingLeft();
        final float paddingRight = getPaddingRight();


        float verticalOffset = paddingTop + ((getHeight() - paddingTop - getPaddingBottom()) / 2.0f);
        float horizontalOffset = paddingLeft;

        //判断指示器是否位于中心位置
        if (mCentered) {
            horizontalOffset += ((getWidth() - paddingLeft - paddingRight) / 2.0f) - (indicatorWidth / 2.0f);
        }

        //绘制图形
        for (int i = 0; i < count; i++) {
            float dx1 = horizontalOffset + (i * lineWidthAndGap);
            float dx2 = dx1 + mLineWidth;
            canvas.drawLine(dx1, verticalOffset, dx2, verticalOffset, (i == mCurrentPage) ? mPaintSelected : mPaintUnselected);
        }
    }


    /**
     * 设置点击事件的处理逻辑
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
                //pointerIndex从0到getPointerCount-1,返回一个触摸点的标识,获取第0个
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

    /**
     * 测量视图的大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }


    /**
     * 确定视图的宽度
     *
     * @param measureSpec
     * @return
     */
    public int measureWidth(int measureSpec) {
        float result;
        //获取视图的宽度设定类型
        int specMode = MeasureSpec.getMode(measureSpec);

        int specSize = MeasureSpec.getSize(measureSpec);

        if ((specMode == MeasureSpec.EXACTLY) || (mViewPager == null)) {
            //当没有视图时就为实际测量的结果
            result = specSize;
        } else {
            //通过计算得出最终的视图的宽的大小
            //计算视图的总数
            final int count = mViewPager.getAdapter().getCount();
            //计算视图的最终的宽度
            result = getPaddingLeft() + getPaddingRight() + (count * mLineWidth) + ((count - 1) * mGapWidth);

            //如果视图的类型为AT_MOST(如:wrap_content)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return (int) Math.ceil(result);
    }


    /**
     * 确定视图的高度
     *
     * @param measureSpec
     * @return
     */
    public int measureHeight(int measureSpec) {
        float result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {

            //测量高度
            result = mPaintSelected.getStrokeWidth() + getPaddingTop() + getPaddingBottom();

            //如果视图的类型为AT_MOST,(如：wrap_content)
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }

        }

        return (int) Math.ceil(result);
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
