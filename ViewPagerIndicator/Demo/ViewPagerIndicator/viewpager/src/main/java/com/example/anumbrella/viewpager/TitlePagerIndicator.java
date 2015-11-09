package com.example.anumbrella.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * Created by anumbrella on 15-11-8.
 * <p/>
 * 来源于Jake Wharton,查看源码,覆写并添加中文注释
 * <p/>
 * TitlePagerIndicator显示标题在屏幕的左边，当前选中的标题在中央。当用户滑动时标题也跟着滑动
 */
public class TitlePagerIndicator extends View implements PagerIndicator {


    /**
     * 枚举类型,定义指示器的样式
     */
    public enum IndicatorStyle {
        None(0), Triangle(1), Underline(2);

        private int value;

        /**
         * 枚举当中的构造器
         *
         * @param value
         */
        IndicatorStyle(int value) {
            this.value = value;
        }

        /**
         * 遍历枚举类型中匹配的类型
         *
         * @param value
         * @return
         */
        public static IndicatorStyle fromValue(int value) {
            for (IndicatorStyle style : IndicatorStyle.values()) {
                if (value == style.value) {
                    return style;
                }
            }
            return null;
        }
    }


    /**
     * 枚举类型,定义指示器的布局方位
     */
    public enum LinePosition {
        Bottom(0), Top(1);

        private int value;


        /**
         * 枚举当中的构造器
         *
         * @param value
         */
        LinePosition(int value) {
            this.value = value;
        }

        /**
         * 遍历枚举类型中匹配的方位值
         *
         * @param value
         * @return
         */
        public static LinePosition fromValue(int value) {
            for (LinePosition position : LinePosition.values()) {
                if (position.value == value) {
                    return position;
                }
            }
            return null;
        }

    }


    /**
     * 定义点击回调函数接口(当中间的那个标题被点击时调用)
     */
    public interface OnCenteredItem {

        /**
         * 定义回调点击函数
         *
         * @param position 点击的item的序列索引
         */
        void onCenteredClickItem(int position);
    }


    /**
     * 回调函数监听类
     */
    private OnCenteredItem mCenterItemClickListener;


    /**
     * 设置无效的触点值
     */
    private static final int INVALID_POINTER = -1;

    private ViewPager mViewPager;

    /**
     * ViewPager选择改变监听器
     */
    private ViewPager.OnPageChangeListener pageChangeListener;

    /**
     * 当前选中的页面
     */
    private int mCurrentPage = -1;

    /**
     * 页面滑动的状态标识
     */
    private int mScrollState;


    /**
     * 定义指示器上下边缘线的高度
     */
    private float mFooterLineHeight;


    /**
     * 设置指示器的高度值
     */
    private float mFooterIndicatorHeight;


    /**
     * 标题字体的颜色
     */
    private int mTextColor;


    /**
     * 选中页面时指示器字体的颜色
     */
    private int mSelectedColor;


    /**
     * 枚举类,显示指示的样式(下划线或三角形)
     */
    private IndicatorStyle mFooterIndicatorStyle;


    /**
     * 枚举类,指示器整体布局的位置
     */
    private LinePosition mLinePosition;


    /**
     *
     */
    private float mFooterIndicatorUnderlinePadding;


    /**
     * 选中页面时指示器标题是否加粗显示
     */
    private boolean mBoldText;

    /**
     * 字体绘制画笔
     */
    private final Paint mPaintText = new Paint();


    /**
     * 指示器上下边缘线条画笔
     */
    private final Paint mPaintFooterLine = new Paint();


    /**
     * 指示器画笔
     */
    private final Paint mPaintFooterIndicator = new Paint();


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


    private float mFooterPadding;


    public TitlePagerIndicator(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);


        //解决自定义视图无法编辑预览报错时调用
        if (isInEditMode()) {
            return;
        }

        //获取资源对象
        final Resources res = getResources();

        //加载默认的属性设置
        //显示指示的颜色(下划线或三角形)
        final int defaultFooterColor = res.getColor(R.color.default_title_indicator_footer_color);

        //指示器上下边缘线的高度
        final float defaultFooterLineHeight = res.getDimension(R.dimen.default_title_indicator_footer_line_height);

        //显示指示的样式(下划线或三角形)
        final int defaultFooterIndicatorStyle = res.getInteger(R.integer.default_title_indicator_footer_indicator_style);

        //指示器布局的高度()
        final float defaultFooterIndicatorHeight = res.getDimension(R.dimen.default_title_indicator_footer_indicator_height);

        //显示指示的高度(下滑线或三角形的高度)
        final float defaultFooterIndicatorUnderlineHeight = res.getDimension(R.dimen.default_title_indicator_footer_indicator_underline_padding);

        //
        final float defaultFooterPadding = res.getDimension(R.dimen.default_title_indicator_footer_padding);

        //指示器整体布局的位置
        final int defaultLinePosition = res.getInteger(R.integer.default_title_indicator_footer_line_position);

        //选中某页面时指示器标题颜色
        final int defaultSelectedColor = res.getColor(R.color.default_title_indicator_selected_color);

        //选中某页面时指示器标题是否加粗
        final boolean defaultSelectedBold = res.getBoolean(R.bool.default_title_indicator_selected_bold);

        //标题默认颜色
        final int defaultTextColor = res.getColor(R.color.default_title_indicator_text_color);

        //标题字体默认大小
        final float defaultTextSize = res.getDimension(R.dimen.default_title_indicator_text_size);

        final float defaultTextPaddingt = res.getDimension(R.dimen.default_title_indicator_text_padding);

        final float defaultClipPadding = res.getDimension(R.dimen.default_title_indicator_clip_padding);

        final float defaultTopPadding = res.getDimension(R.dimen.default_title_indicator_top_padding);

        //获取在布局xml中定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitlePagerIndicator, defaultStyle, 0);

        //指示器的高度
        mFooterIndicatorHeight = array.getDimension(R.styleable.TitlePagerIndicator_footerIndicatorHeight, defaultFooterIndicatorHeight);
        //显示指示的样式(下划线或三角形)
        mFooterIndicatorStyle = IndicatorStyle.fromValue(array.getInteger(R.styleable.TitlePagerIndicator_footerIndicatorStyle, defaultFooterIndicatorStyle));
        //指示器上下边缘线的高度
        mFooterLineHeight = array.getDimension(R.styleable.TitlePagerIndicator_footerLineHeight, defaultFooterLineHeight);
        //
        mFooterIndicatorUnderlinePadding = array.getDimension(R.styleable.TitlePagerIndicator_footerIndicatorUnderlinePadding, defaultFooterIndicatorUnderlineHeight);
        //
        mFooterPadding = array.getDimension(R.styleable.TitlePagerIndicator_footerPadding, defaultFooterPadding);

        //指示器布局的方位
        mLinePosition = LinePosition.fromValue(array.getInteger(R.styleable.TitlePagerIndicator_linePosition, defaultLinePosition));
        //指示器字体的颜色
        mTextColor = array.getColor(R.styleable.TitlePagerIndicator_android_textColor, defaultTextColor);
        //选中页面时指示器字体的颜色
        mSelectedColor = array.getColor(R.styleable.TitlePagerIndicator_selectedColor, defaultSelectedColor);
        //选中页面时指示器字体是否加粗显示
        mBoldText = array.getBoolean(R.styleable.TitlePagerIndicator_selectedBold, defaultSelectedBold);

        //字体的大小
        final float textSize = array.getDimension(R.styleable.TitlePagerIndicator_android_textSize, defaultTextSize);
        //指示器上下边缘线及指示的颜色
        final int footerColor = array.getColor(R.styleable.TitlePagerIndicator_footerColor, defaultFooterColor);
        mPaintText.setTextSize(textSize);
        //设置画笔保真，抗矩形
        mPaintText.setAntiAlias(true);
        //设置边缘线画笔描边和填充
        mPaintFooterLine.setStyle(Paint.Style.FILL_AND_STROKE);
        //设置边缘线画笔的宽度
        mPaintFooterLine.setStrokeWidth(mFooterLineHeight);
        //设置边缘线画笔的颜色
        mPaintFooterLine.setColor(footerColor);
        //设置指示器的画笔描边和填充
        mPaintFooterIndicator.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaintFooterIndicator.setColor(footerColor);

        array.recycle();
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);

    }

    public int getFooterColor() {
        return mPaintFooterIndicator.getColor();
    }

    public void setFooterColor(int color) {
        mPaintFooterIndicator.setColor(color);
        mPaintFooterLine.setColor(color);
        invalidate();
    }

    public float getFooterLineHeight() {
        return mFooterLineHeight;
    }

    public void setFooterLineHeight(float footerLineHeight) {
        mFooterLineHeight = footerLineHeight;
        mPaintFooterLine.setStrokeWidth(mFooterLineHeight);
        invalidate();
    }

    public float getFooterIndicatorHeight() {
        return mFooterIndicatorHeight;
    }

    public void setFooterIndicatorHeight(float footerIndicatorHeight) {
        mFooterIndicatorHeight = footerIndicatorHeight;
        invalidate();
    }

    public float getFooterIndicatorPadding() {
        return mFooterPadding;
    }

    public void setFooterIndicatorPadding(int footerIndicatorPadding) {
        mFooterPadding = footerIndicatorPadding;
        invalidate();
    }

    /**
     * 获得Circle指示器显示的样式
     *
     * @return
     */
    public IndicatorStyle getIndicatorStyle() {
        return mFooterIndicatorStyle;
    }

    public void setIndicatorStyle(IndicatorStyle indicatorStyle) {
        mFooterIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public LinePosition getLinePosition() {
        return mLinePosition;
    }

    public void setLinePosition(LinePosition linePosition) {
        mLinePosition = linePosition;
        invalidate();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
        invalidate();
    }

    public boolean isBoldText() {
        return mBoldText;
    }

    public void setBoldText(boolean boldText) {
        mBoldText = boldText;
        invalidate();
    }


    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mPaintText.setColor(textColor);
        invalidate();
    }

    public float getTextSize() {
        return mPaintText.getTextSize();
    }

    public void setTextSize(float textSize) {
        mPaintText.setTextSize(textSize);
        invalidate();
    }

    @Override
    public void setViewPager(ViewPager viewPager) {
        if (viewPager == mViewPager) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }

        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance!");
        }

        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
    }


    /**
     * 绘制视图
     *
     * @param canvas
     */
    @Override
    public void onDraw(Canvas canvas) {

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

    }


    /**
     * 获取用户手势的操作
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
                    final float halfWidth = width / 2f;
                    final float sixWidth = width / 6f;
                    final float leftThrid = halfWidth - sixWidth;
                    final float rightThrid = halfWidth + sixWidth;
                    final float eventX = event.getX();
                    if (eventX < leftThrid) {
                        if (mCurrentPage > 0) {
                            if (action != MotionEvent.ACTION_CANCEL) {
                                mViewPager.setCurrentItem(mCurrentPage - 1);
                            }
                            return true;
                        }
                    } else if (eventX > rightThrid) {
                        if (mCurrentPage < count - 1) {
                            if (action != MotionEvent.ACTION_CANCEL) {
                                mViewPager.setCurrentItem(mCurrentPage + 1);
                            }
                            return true;
                        }
                    } else {
                        //点击中间标题的触发回调函数
                        if ((mCenterItemClickListener != null) & action != MotionEvent.ACTION_CANCEL) {
                            mCenterItemClickListener.onCenteredClickItem(mCurrentPage);
                        }
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
                mLastMotionX = MotionEventCompat.getX(event, MotionEventCompat.findPointerIndex(event, mActivePointerId));
                break;
        }
        return true;
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
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


}
