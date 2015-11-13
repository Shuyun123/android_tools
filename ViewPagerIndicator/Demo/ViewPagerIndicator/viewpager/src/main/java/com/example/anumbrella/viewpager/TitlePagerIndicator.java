package com.example.anumbrella.viewpager;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import java.util.ArrayList;

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


    /**
     * 设置标题，当没有从adapter中获取到标题时使用
     */
    private static final String EMPTY_TITLE = "";

    /**
     * 设定滑动指示器消失的屏幕的百分比值,0.25f表示屏幕的1/4
     */
    private static final float SELECTION_FADE_PERCENTAGE = 0.25f;

    /**
     * 设置滑动指示器字体加粗消失的百分比
     */
    private static final float BOLD_FADE_PERCENTAGE = 0.05f;

    private ViewPager mViewPager;

    /**
     * ViewPager选择改变监听器
     */
    private ViewPager.OnPageChangeListener pageChangeListener;

    /**
     * 屏幕滑动偏移百分比
     */
    private float mPositionOffset;


    /**
     * 屏幕两边的内边距(padding)
     */
    private float mClipPadding;


    /**
     * 标题距离屏幕上方的距离
     */
    private float mTopPadding;

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
     * 标题之间的间隔距离
     */
    private float mTextPadding;


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
     * 定义绘画路径
     */
    private final Path mPath = new Path();


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
     * 指示器上方距离标题下方的距离
     */
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

        //指示器布局的高度(下划线或三角形)
        final float defaultFooterIndicatorHeight = res.getDimension(R.dimen.default_title_indicator_footer_indicator_height);

        //标题距离指示器左右两边的距离
        final float defaultFooterIndicatorUnderlinePadding = res.getDimension(R.dimen.default_title_indicator_footer_indicator_underline_padding);

        //指示器上方距离标题下方的距离
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

        //标题之间的间隔距离
        final float defaultTextPadding = res.getDimension(R.dimen.default_title_indicator_text_padding);

        //屏幕两边的内边距(padding)
        final float defaultClipPadding = res.getDimension(R.dimen.default_title_indicator_clip_padding);

        //标题距离屏幕上方的距离
        final float defaultTopPadding = res.getDimension(R.dimen.default_title_indicator_top_padding);

        //获取在布局xml中定义的属性
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitlePagerIndicator, defaultStyle, 0);

        //指示器的高度
        mFooterIndicatorHeight = array.getDimension(R.styleable.TitlePagerIndicator_footerIndicatorHeight, defaultFooterIndicatorHeight);
        //显示指示的样式(下划线或三角形)
        mFooterIndicatorStyle = IndicatorStyle.fromValue(array.getInteger(R.styleable.TitlePagerIndicator_footerIndicatorStyle, defaultFooterIndicatorStyle));
        //指示器上下边缘线的高度
        mFooterLineHeight = array.getDimension(R.styleable.TitlePagerIndicator_footerLineHeight, defaultFooterLineHeight);
        //标题距离指示器左右两边的距离
        mFooterIndicatorUnderlinePadding = array.getDimension(R.styleable.TitlePagerIndicator_footerIndicatorUnderlinePadding, defaultFooterIndicatorUnderlinePadding);
        //指示器上方距离标题下方的距离
        mFooterPadding = array.getDimension(R.styleable.TitlePagerIndicator_footerPadding, defaultFooterPadding);

        //指示器布局的方位
        mLinePosition = LinePosition.fromValue(array.getInteger(R.styleable.TitlePagerIndicator_linePosition, defaultLinePosition));
        //指示器字体的颜色
        mTextColor = array.getColor(R.styleable.TitlePagerIndicator_android_textColor, defaultTextColor);
        //选中页面时指示器字体的颜色
        mSelectedColor = array.getColor(R.styleable.TitlePagerIndicator_selectedColor, defaultSelectedColor);
        //选中页面时指示器字体是否加粗显示
        mBoldText = array.getBoolean(R.styleable.TitlePagerIndicator_selectedBold, defaultSelectedBold);
        //屏幕两边的内边距(padding)
        mClipPadding = array.getDimension(R.styleable.TitlePagerIndicator_clipPadding, defaultClipPadding);
        //标题距离屏幕上方的距离
        mTopPadding = array.getDimension(R.styleable.TitlePagerIndicator_topPadding, defaultTopPadding);
        //标题之间的间隔距离
        mTextPadding = array.getDimension(R.styleable.TitlePagerIndicator_textPadding, defaultTextPadding);

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
        super.onDraw(canvas);
        if (mViewPager == null) {
            return;
        }

        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        // mCurrentPage = -1，是初始化的值
        if (mCurrentPage == -1 && mViewPager != null) {
            mCurrentPage = mViewPager.getCurrentItem();
        }

        //计算所有视图的界限
        ArrayList<Rect> bounds = calculateAllBounds(mPaintText);
        final int boundsSize = bounds.size();
        if (mCurrentPage >= boundsSize) {
            setCurrentItem(boundsSize - 1);
            return;
        }
        final int countMinusOne = count - 1;
        final float halfWidth = getWidth() / 2f;
        final int left = getLeft();
        //视图距离屏幕左右两边的padding
        final float leftClip = left + mClipPadding;
        final int width = getWidth();
        int height = getHeight();
        final int right = left + width;
        final float rightClip = right - mClipPadding;

        int page = mCurrentPage;
        float offsetPercent;

        //根据用户滑动的情况判断是否进入下一个页面
        if (mPositionOffset <= 0.5) {
            offsetPercent = mPositionOffset;
        } else {
            //进入下一个页面
            page += 1;
            offsetPercent = 1 - mPositionOffset;
        }

        //确定当前页面的判定方法
        final boolean currentSelected = (offsetPercent <= SELECTION_FADE_PERCENTAGE);
        final boolean currentBold = (offsetPercent <= BOLD_FADE_PERCENTAGE);
        final float selectedPercent = (SELECTION_FADE_PERCENTAGE - mPositionOffset) / SELECTION_FADE_PERCENTAGE;

        //修正视图直接距离超过屏幕过多或过少距离的情况
        Rect curPageBound = bounds.get(mCurrentPage);
        float curPageWidth = curPageBound.right - curPageBound.left;
        //当显示超过了屏幕左侧的距离时
        if (curPageBound.left < leftClip) {
            //修正视图超过屏幕的左侧的情况
            clipViewOnTheLeft(curPageBound, curPageWidth, left);
        }

        if (curPageBound.right > rightClip) {
            //修正视图超过屏幕的右侧的情况
            clipViewOnTheRight(curPageBound, curPageWidth, right);
        }

        //当前位置左边的视图
        if (mCurrentPage > 0) {
            for (int i = mCurrentPage - 1; i >= 0; i--) {
                Rect bound = bounds.get(i);
                //如果边界左侧超过屏幕的左侧
                if (bound.left < leftClip) {
                    int w = bound.right - bound.left;
                    //修正视图以显示在屏幕上
                    clipViewOnTheLeft(bound, w, left);
                    //检测视图是否与上一个视图产生交集
                    Rect rightBound = bounds.get(i + 1);
                    if (bound.right + mTextPadding > rightBound.left) {
                        bound.left = (int) (rightBound.left - w - mTextPadding);
                        bound.right = bound.left + w;
                    }
                }
            }
        }

        //当视图在屏幕的右边的情况
        if (mCurrentPage < countMinusOne) {
            for (int i = mCurrentPage; i < count; i++) {
                Rect bound = bounds.get(i);
                //修正超过屏幕右边的情况
                if (bound.right > rightClip) {
                    int w = bound.right - bound.left;
                    clipViewOnTheRight(bound, w, right);
                    Rect leftBound = bounds.get(i - 1);
                    if (bound.left - mTextPadding < leftBound.right) {
                        bound.left = (int) (leftBound.right + mTextPadding);
                        bound.right = bound.left + w;
                    }
                }
            }
        }

        //无符号右移动,空缺的位补0
        int colorTextAlpha = mTextColor >>> 24;
        //绘制屏幕当中的标题
        for (int i = 0; i < count; i++) {
            Rect bound = bounds.get(i);
            //判断当前视图的位置(必须要再当前的屏幕当中)
            if ((bound.left > left && bound.left < right) || (bound.right > left && bound.right < right)) {
                //是否等于当前的视图
                final boolean currentPage = (i == page);
                final CharSequence pageTitle = getTitle(i);
                //设置文本字体加粗
                mPaintText.setFakeBoldText(currentPage && currentBold && mBoldText);
                //设置字体的颜色
                mPaintText.setColor(mTextColor);
                if (currentPage & currentSelected) {
                    //设置透明度可以根据滑动的情况来改变
                    mPaintText.setAlpha(colorTextAlpha - (int) (colorTextAlpha * selectedPercent));
                }
                if (i < boundsSize - 1) {
                    Rect rightBound = bounds.get(i + 1);
                    //检查视图是否相交
                    if (bound.right + mTextPadding > rightBound.left) {
                        int w = bound.right - bound.left;
                        bound.left = (int) (rightBound.left - w - mTextPadding);
                        bound.right = bound.left + w;
                    }
                }
                canvas.drawText(pageTitle, 0, pageTitle.length(), bound.left, bound.bottom + mTopPadding, mPaintText);

                if (currentPage && currentSelected) {
                    mPaintText.setColor(mSelectedColor);
                    mPaintText.setAlpha((int) ((mSelectedColor >>> 24) * selectedPercent));
                    canvas.drawText(pageTitle, 0, pageTitle.length(), bound.left, bound.bottom + mTopPadding, mPaintText);
                }
            }
        }


        float footerLineHeight = mFooterLineHeight;
        float footerIndicatorHeight = mFooterIndicatorHeight;

        //判断指示的布局的方位
        if (mLinePosition == LinePosition.Bottom) {
            height = 0;
            footerLineHeight = -footerLineHeight;
            footerIndicatorHeight = -footerIndicatorHeight;
        }

        mPath.reset();
        //移动去绘制起点
        mPath.moveTo(0, height - footerLineHeight / 2f);
        mPath.lineTo(width, height - footerLineHeight / 2f);
        mPath.close();
        canvas.drawPath(mPath, mPaintFooterLine);

        float heightMinusLine = height - footerLineHeight;

        //配置指示器的类型来进行绘制
        switch (mFooterIndicatorStyle) {
            case Triangle:
                mPath.reset();
                mPath.moveTo(halfWidth, heightMinusLine - footerIndicatorHeight);
                mPath.lineTo(halfWidth + footerIndicatorHeight, heightMinusLine);
                mPath.lineTo(halfWidth - footerIndicatorHeight, heightMinusLine);
                mPath.close();
                canvas.drawPath(mPath, mPaintFooterIndicator);
                break;
            case Underline:
                if (!currentSelected || page >= boundsSize) {
                    return;
                }
                Rect underlineBounds = bounds.get(page);
                final float rightPlusPadding = underlineBounds.right + mFooterIndicatorUnderlinePadding;
                final float leftMinusPadding = underlineBounds.left - mFooterIndicatorUnderlinePadding;
                final float heightMinusLineIndicator = heightMinusLine - mFooterLineHeight;

                mPath.reset();
                mPath.moveTo(leftMinusPadding, heightMinusLine);
                mPath.lineTo(rightPlusPadding, heightMinusLine);
                mPath.lineTo(rightPlusPadding, heightMinusLineIndicator);
                mPath.lineTo(leftMinusPadding, heightMinusLineIndicator);
                mPath.close();
                mPaintFooterIndicator.setAlpha((int)(0xFF*selectedPercent));
                canvas.drawPath(mPath,mPaintFooterIndicator);
                mPaintFooterIndicator.setAlpha(0xFF);
                break;
        }
    }

    /**
     * 修正视图超过屏幕的右侧的情况
     *
     * @param curPageBound
     * @param curPageWidth
     * @param right
     */
    private void clipViewOnTheRight(Rect curPageBound, float curPageWidth, int right) {
        curPageBound.right = (int) (right - mClipPadding);
        curPageBound.left = (int) (curPageBound.right - curPageWidth);
    }

    /**
     * 修正视图超过屏幕的左侧的情况
     *
     * @param curPageBound
     * @param curPageWidth
     * @param left
     */
    private void clipViewOnTheLeft(Rect curPageBound, float curPageWidth, int left) {
        //刚好显示在屏幕的左侧
        curPageBound.left = (int) (left + mClipPadding);
        curPageBound.right = (int) (mClipPadding + curPageWidth);
    }

    /**
     * 计算所有视图的的界限
     *
     * @param paint
     * @return
     */
    private ArrayList<Rect> calculateAllBounds(Paint paint) {

        ArrayList<Rect> list = new ArrayList<Rect>();

        final int count = mViewPager.getAdapter().getCount();
        final int width = getWidth();
        final int halfWidth = getWidth() / 2;

        for (int i = 0; i < count; i++) {
            //计算标题的边界
            Rect bounds = calcBounds(i, paint);
            int w = bounds.right - bounds.left;
            int h = bounds.bottom - bounds.top;
            //在后面会对标题直接的距离进行修正
            bounds.left = (int) (halfWidth - (w / 2) + ((i - mCurrentPage - mPositionOffset) * width));
            bounds.right = bounds.left + w;
            bounds.top = 0;
            bounds.bottom = h;
            list.add(bounds);
        }

        return list;
    }

    /**
     * 计算标题的边界
     *
     * @param i
     * @param paint
     * @return
     */
    private Rect calcBounds(int i, Paint paint) {
        Rect bounds = new Rect();
        CharSequence title = getTitle(i);
        bounds.right = (int) (paint.measureText(title, 0, title.length()));
        //计算字体的高度,baseline 是y轴0坐标点
        bounds.bottom = (int) (paint.descent() - paint.ascent());
        return bounds;
    }

    /**
     * 获取具体的标题内容
     *
     * @param i
     * @return
     */
    private CharSequence getTitle(int i) {

        CharSequence title = mViewPager.getAdapter().getPageTitle(i);
        if (title == null) {
            title = EMPTY_TITLE;
        }
        return title;
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
        mCurrentPage = position;
        mPositionOffset = positionOffset;
        invalidate();
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

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
