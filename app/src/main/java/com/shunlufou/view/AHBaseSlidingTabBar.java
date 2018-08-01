package com.shunlufou.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.shunlufou.R;

import java.util.Locale;

/**
 * <p>导航条控件带滚动条和下分隔线，支持子View之间左右滑动的，支持设置显示下划引导线的功能。导航菜单可为文本或图片，可以只有一个导航菜单。</p>
 */
public class AHBaseSlidingTabBar extends HorizontalScrollView implements OnClickListener {

    /**
     * tab带有margin边距
     */
    protected LinearLayout.LayoutParams defaultTabLayoutParams;
    /**
     * 第一个tab带有margin边距，扩大左边距
     */
    protected LinearLayout.LayoutParams firstTabLayoutParams;
    /**
     * 最后一个tab带有margin边距
     */
    protected LinearLayout.LayoutParams lastTabLayoutParams;
    /**
     * tab不带有margin边距
     */
    protected LinearLayout.LayoutParams expandedTabLayoutParams;

    /**
     * {@link HorizontalScrollView}下的tab容器。
     */
    protected LinearLayout tabsContainer;
    /**
     * 当前选中的菜单位置
     */
    protected int currentPosition = 0;
    /**
     * 导航菜单滑动偏移量
     */
    protected float currentPositionOffset = 0f;

    protected Paint rectPaint;
    /**
     * tab导航文字下划线蓝色
     */
    protected int indicatorColor;
    /**
     * tab导航下边线颜色
     */
    protected int underlineColor;
    /**
     * 为true tab不带margin，tab要间隔小，主要决定addTab时layoutParam 用哪个（{@link #defaultTabLayoutParams}和{@link #expandedTabLayoutParams}）
     */
    protected boolean shouldExpand = false;
    /**
     * 滚动条滚动偏移量
     */
    protected int scrollOffset = 0;
    /**
     * 下划线高度
     */
    private int indicatorHeight = 0;
    /**
     * 导航底部灰色底边
     */
    private int underlineHeight = 0;
    /**
     * 是否显示下划线
     */
    private boolean indicatorVisible = true;
    /**
     * 是否明确显示导航线
     */
    private boolean indicatorVisibleBySet = true;
    /**
     * 是否显示导航底部灰色底边
     */
    private boolean underlineVisible = false;
    /**
     * 导航栏分隔线上下边距
     */
    private int dividerPadding = 0;
    /**
     * tab导航View的Padding
     */
    protected int tabPadding = 0;
    /**
     * tab导航View的间隔
     */
    protected int tabMargins = 0;
    /**
     * tab第一个导航View的多余marginLeft
     */
    protected int fistTabExtraMarginLeft = 0;
    /**
     * tab第一个导航View的marginLeft
     */
    protected int firstTabMarginLeft = 0;
    /**
     * tab最后一个导航View的marginRight
     */
    protected int lastTabMarginRight = 0;
    /**
     * tab第一个导航View的paddingLeft
     */
    protected int firstTabPaddingLeft = 0;
    /**
     * tab最后一个导航View的paddingRight
     */
    protected int lastTabPaddingRight = 0;
    /**
     * 下划线水平方向padding
     */
    private int indicatorPadding = 0;
    private int lastScrollX = 0;
    protected int clickPosition = -1;
    protected boolean needScrollToClick = false;
    protected Locale locale;

    private AHBaseSlidingBarAdapter mAdapter;
    private DataSetObserver mDataSetObserver;
    private OnItemClickListener mOnItemClickListener;
    private boolean mIsCenterMode = false;
    protected boolean mIsBindViewPager = false;
    /**
     * 下划线长度
     */
    private int indicatorLength;

    /**
     * 构造函数
     *
     * @param context
     */
    public AHBaseSlidingTabBar(Context context) {
        this(context, null);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AHBaseSlidingTabBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AHBaseSlidingTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        setFillViewport(true);
        setWillNotDraw(false);
        setOverScrollMode(OVER_SCROLL_NEVER);
        setBackgroundColor(getResources().getColor(R.color.common_color09));

        tabsContainer = new LinearLayout(getContext());
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT));

        if (mIsCenterMode) {
            tabsContainer.setGravity(Gravity.CENTER);
        }

        addView(tabsContainer);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        indicatorLength = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20f, dm);
        tabMargins = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6f, dm);
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1f, dm);
        underlineHeight = 1;
        fistTabExtraMarginLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3f, dm);
        firstTabMarginLeft = tabMargins + fistTabExtraMarginLeft;
        firstTabPaddingLeft = tabPadding;
        lastTabMarginRight = tabMargins;
        lastTabPaddingRight = tabPadding;
//        tabsContainer.setPadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, dm), tabsContainer.getPaddingTop(),
//                tabsContainer.getPaddingRight(), tabsContainer.getPaddingBottom());

        changedSkin();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultTabLayoutParams.setMargins(tabMargins, 0, tabMargins, 0);
        firstTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        firstTabLayoutParams.setMargins(firstTabMarginLeft, 0, tabMargins, 0);
        lastTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        lastTabLayoutParams.setMargins(tabMargins, 0, lastTabMarginRight, 0);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void changedSkin() {
        indicatorColor = getContext().getResources().getColor(R.color.common_color02);
        //ResUtil.getColor(getContext(), ResUtil.TEXT_COLOR_02);
        underlineColor = getContext().getResources().getColor(R.color.common_color07);
        //ResUtil.getColor(getContext(), ResUtil.BG_COLOR_03);
    }

    /**
     * 数据变化，重新生成所有item view，第一个和最后一个item的padding和LayoutParams需要特殊处理
     */
    protected void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        int tabCount = mAdapter.getCount();

        for (int i = 0; i < tabCount; i++) {
            View tabView = mAdapter.getView(i, null, this);
            tabView.setTag(i);
            tabView.setOnClickListener(this);
            int leftPadding = tabPadding;
            if (i == 0) {
                leftPadding = firstTabPaddingLeft;
            }
            int rightPadding = tabPadding;
            if (i == tabCount - 1) {
                rightPadding = lastTabPaddingRight;
            }
            tabView.setPadding(leftPadding, 0, rightPadding, 0);
            LinearLayout.LayoutParams params;
            if (shouldExpand) {
                // 各item等分
               params = expandedTabLayoutParams;
            } else {
                params = defaultTabLayoutParams;
                if (i == 0 && !isCenterMode()) {
                   params = firstTabLayoutParams;
                } else if (i == tabCount - 1 && !isCenterMode()) {
                    params = lastTabLayoutParams;
                }
            }
            tabsContainer.addView(tabView, i, params);
        }
        tabsContainer.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (tabsContainer.getViewTreeObserver().isAlive()) {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        tabsContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        tabsContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
                // 等分时，动态计算每个item view的宽度
                int itemAvailableWidth = AHBaseSlidingTabBar.this.getWidth() - getPaddingLeft() - getPaddingRight();
                int maxItemWidth = 0;
                int childCount = tabsContainer.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabsContainer.getChildAt(i).getLayoutParams();
                    int width = tabsContainer.getChildAt(i).getWidth();
                    if (params != null) {
                        width = width + params.leftMargin + params.rightMargin;
                    }
                    maxItemWidth = Math.max(width, maxItemWidth);
                }
                if (shouldExpand && childCount > 0 && maxItemWidth > itemAvailableWidth / childCount) {
                    for (int i = 0; i < childCount; i++) {
                        int itemWidth = itemAvailableWidth / childCount;
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tabsContainer.getChildAt(i).getLayoutParams();
                        if (params != null) {
                            itemWidth = itemWidth - params.leftMargin - params.rightMargin;
                        } else {
                            params = new LinearLayout.LayoutParams(itemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
                            params.gravity = Gravity.CENTER;
                        }
                        params.width = itemWidth;
                        tabsContainer.getChildAt(i).setLayoutParams(params);
                    }
                }
                if (!mIsBindViewPager) {
                    scrollToChild(currentPosition, 0f);
                }
            }
        });
    }

    private void registerObserver() {
        if (mAdapter != null && mDataSetObserver == null) {
            mDataSetObserver = new DataSetObserver() {
                @Override
                public void onChanged() {
                    super.onChanged();
                    if (mAdapter == null) {
                        return;
                    }
                    notifyDataSetChanged();
                }
            };
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
    }

    private void unregisterObserver() {
        if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            mDataSetObserver = null;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        registerObserver();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        unregisterObserver();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || mAdapter == null || mAdapter.getCount() == 0) {
            return;
        }
        final int height = getHeight();

        // draw indicator line
        rectPaint.setColor(indicatorColor);

        // default: line below current tab
//        View currentTab = tabsContainer.getChildAt(currentPosition);
//        float lineLeft = currentTab.getLeft();
//        float lineRight = currentTab.getRight();
//        if (currentTab instanceof TextView && shouldExpand) {
//            float gap = calculateGap((TextView) currentTab);
//            lineLeft = lineLeft + gap;
//            lineRight = lineRight - gap;
//        }

        View currentTab = tabsContainer.getChildAt(currentPosition);
        if (currentTab == null) {
//            LogUtil.e("AHBaseSlidingTabBar", "Invalid position=" + currentPosition +
//                    ",tabs count=" + tabsContainer.getChildCount());
            return;
        }
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        if (currentTab instanceof AHTabBarTextBadgeView) {
//            lineLeft += (currentTab.getMeasuredWidth() /2 - indicatorLength / 2);
            lineLeft += ((AHTabBarTextBadgeView) currentTab).leftTargetGap() + (calculateTextLen(((AHTabBarTextBadgeView) currentTab).getTargetView()) - indicatorLength) / 2;
            lineRight = lineLeft + indicatorLength;
        }else {
            float tabLen = lineRight - lineLeft;
            lineLeft += (tabLen / 2 - indicatorLength / 2);
            lineRight = lineLeft + indicatorLength;
        }

        if (currentPositionOffset > 0f && currentPosition < mAdapter.getCount() - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            float nextTabLeft = nextTab.getLeft();
            float nextTabRight = nextTab.getRight();
//            if (nextTab instanceof AHTabBarTextBadgeView) {
////                nextTabLeft += (nextTab.getMeasuredWidth()/2 - indicatorLength / 2);
//                nextTabLeft += ((AHTabBarTextBadgeView) nextTab).leftTargetGap() + (calculateTextLen(((AHTabBarTextBadgeView) nextTab).getTargetView()) - indicatorLength) / 2;
//                nextTabRight = nextTabLeft + indicatorLength;
//            }else {
//                float nextTabLen = nextTabRight - nextTabRight;
//                nextTabLeft += (nextTabLen / 2 - indicatorLength / 2);
//                nextTabRight = nextTabLeft + indicatorLength;
//            }
            if (nextTab instanceof AHTabBarTextBadgeView) {
                // 重新计算指示线坐标，与文字宽度相等
                nextTabLeft += ((AHTabBarTextBadgeView) nextTab).leftTargetGap();
                nextTabRight -= ((AHTabBarTextBadgeView) nextTab).rightTargetGap();
                if (shouldExpand) {
                    float gap = calculateGap(((AHTabBarTextBadgeView) nextTab).getTargetView());
                    nextTabLeft = nextTabLeft + gap;
                    nextTabRight = nextTabRight - gap;
                }
            }

            // 得到tab指示线左右坐标
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset)
                    * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset)
                    * lineRight);

        }
        if (indicatorVisible && indicatorVisibleBySet) {
            // 绘制tab指示线
            float indicatorTop = underlineVisible ? height - indicatorHeight - underlineHeight : height - indicatorHeight;
            //draw indicator
            canvas.drawRect(lineLeft + indicatorPadding, indicatorTop,
                    lineRight - indicatorPadding, indicatorTop + indicatorHeight, rectPaint);
        }

        if (underlineVisible) {
            // draw underline，绘制下划线
            rectPaint.setColor(underlineColor);
            canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        }
    }

    /**
     * 计算TextView文字与控件边界的间距
     * @param badgeTextView
     * @return
     */
    private float calculateGap(TextView badgeTextView) {
        Paint paint = badgeTextView.getPaint();
        float textWidth = paint.measureText(badgeTextView.getText().toString());
        return (badgeTextView.getWidth() - textWidth) / 2;
    }

    /**
     * 计算文本长度
     * @param badgeTargetView
     * @return
     */
    private float calculateTextLen(TextView badgeTargetView){
        Paint paint = badgeTargetView.getPaint();
        if(TextUtils.isEmpty(badgeTargetView.getText())){
            return 0;
        }
        float textWidth = paint.measureText(badgeTargetView.getText().toString());
        return textWidth;
    }

    /**
     * 设置第一个Tab的marginLeft，在notifyDataSetChanged之前调用
     * @param firstTabMarginLeft
     */
    public void setFirstTabMarginLeft(int firstTabMarginLeft) {
        this.firstTabMarginLeft = firstTabMarginLeft;
        this.firstTabLayoutParams.leftMargin = firstTabMarginLeft;
    }

    /**
     * 设置最后一个Tab的marginRight，在notifyDataSetChanged之前调用
     * @param lastTabMarginRight
     */
    public void setLastTabMarginRight(int lastTabMarginRight) {
        this.lastTabMarginRight = lastTabMarginRight;
        this.lastTabLayoutParams.rightMargin = lastTabMarginRight;
    }

    /**
     * 设置第一个Tab的paddingLeft，在notifyDataSetChanged之前调用
     * @param firstTabPaddingLeft
     */
    public void setFirstTabPaddingLeft(int firstTabPaddingLeft) {
        this.firstTabPaddingLeft = firstTabPaddingLeft;
    }

    /**
     * 设置最后一个Tab的paddingRight，在notifyDataSetChanged之前调用
     * @param lastTabPaddingRight
     */
    public void setLastTabPaddingRight(int lastTabPaddingRight) {
        this.lastTabPaddingRight = lastTabPaddingRight;
    }

    /**
     * 设置第一个Tab的marginLeft和paddingLeft为0，,最后一个Tab的marginRight和paddingRight为0
     */
    public void setHeadAndTailTabGapZero() {
        setFirstTabMarginLeft(0);
        setFirstTabPaddingLeft(0);
        setLastTabMarginRight(0);
        setLastTabPaddingRight(0);
    }

    /**
     * 设置底部的导航条的tab引导线的颜色值
     *
     * @param indicatorColor 颜色值
     */
    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    /**
     * 设置底部的导航条的tab引导线的颜色资源ID
     *
     * @param resId 颜色的资源ID
     */
    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    /**
     * 获得底部引导线的颜色值
     *
     * @return
     */
    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    /**
     * 设置底部的导航条的引导线的高度
     *
     * @param indicatorLineHeightPx 引导线高度
     */
    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    /**
     * 获得底部引导线的高度
     *
     * @return
     */
    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    /**
     * 设置底部划线的颜色
     *
     * @param underlineColor
     */
    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    /**
     * 设置底部划线的颜色
     *
     * @param colorResId
     */
    public void setUnderlineColorResource(int colorResId) {
        this.underlineColor = getResources().getColor(colorResId);
        invalidate();
    }

    /**
     * 得到底部划线的颜色
     */
    public int getUnderlineColor() {
        return underlineColor;
    }

    /**
     * 设置底部划线的高度
     */
    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    /**
     * 得到底部划线的高度
     */
    public int getUnderlineHeight() {
        return underlineHeight;
    }

    /**
     * 设置Divider的padding
     *
     * @param dividerPaddingPx
     */
    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    /**
     * 得到Divider的padding
     */
    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    /**
     *
     * 设置Tab是否等分，在notifyDataSetChanged之前调用
     * @param shouldExpand
     */
    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    /**
     * 设置导航栏每个tab左右边距，在notifyDataSetChanged之前调用
     */
    public void setTabHorizontalPadding(int paddingPx) {
        this.tabPadding = paddingPx;
    }

    /**
     * 设置导航栏每个tab左右margin边距
     */
    public void setTabHorizontalMargin(int marginPx) {
        this.tabMargins = marginPx;
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        defaultTabLayoutParams.setMargins(tabMargins, 0, tabMargins, 0);
        this.firstTabMarginLeft = tabMargins + fistTabExtraMarginLeft;
        this.lastTabMarginRight = tabMargins;
        defaultTabLayoutParams.setMargins(tabMargins, 0, tabMargins, 0);
        firstTabLayoutParams.setMargins(firstTabMarginLeft, 0, tabMargins, 0);
        lastTabLayoutParams.setMargins(tabMargins, 0, lastTabMarginRight, 0);
        notifyDataSetChanged();
        requestLayout();
    }

    /**
     * 获取导航栏每个tab左右边距
     */
    public int getTabHorizontalPadding() {
        return tabPadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        if (savedState.currentPosition > 0 && savedState.currentPosition < tabsContainer.getChildCount()) {
            currentPosition = savedState.currentPosition;
            requestLayout();
        } else if (tabsContainer.getChildCount() > 0) {
//            LogUtil.w("AHBaseSlidingTabBar", "Invalid restore position=" + currentPosition);
            currentPosition = 0;
            requestLayout();
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 设置adapter
     * @param adapter
     */
    public void setAdapter(AHBaseSlidingBarAdapter adapter) {
        unregisterObserver();
        this.mAdapter = adapter;
        registerObserver();
        // 特殊逻辑，当只有一个tab时，为居中模式，若想单个不居中，setAdapter后调用setIsCenterMode(false)并调用notifyDataSetChanged()
        boolean isSingle = mAdapter != null && mAdapter.getCount() == 1;
        indicatorVisible = !isSingle;
        setIsCenterMode(isSingle);
        notifyDataSetChanged();
    }

    public AHBaseSlidingBarAdapter getAdapter() {
        return mAdapter;
    }


    public boolean isCenterMode() {
        return mIsCenterMode;
    }

    /**
     * 设置是否为居中模式，在notifyDataSetChanged之前调用
     * @param mIsCenterMode
     */
    public void setIsCenterMode(boolean mIsCenterMode) {
        this.mIsCenterMode = mIsCenterMode;
        if (mIsCenterMode) {
            tabsContainer.setGravity(Gravity.CENTER);
        } else {
            tabsContainer.setGravity(Gravity.START | Gravity.TOP);
        }
    }

    @Override
    public void onClick(View view) {
        int position = (Integer) view.getTag();
        if (mIsBindViewPager) {
            // 若已经绑定ViewPager，仅计算是否需要滑动，点击事件由mOnItemClickListener处理
            clickPosition = position;
            int[] viewPosition = new int[2];
            view.getLocationOnScreen(viewPosition);
            int[] containerPosition = new int[2];
            this.getLocationOnScreen(containerPosition);
            needScrollToClick = viewPosition[0] + view.getWidth() -
                    (containerPosition[0] + getWidth() - tabsContainer.getPaddingRight()) > 0
                    || viewPosition[0] - containerPosition[0] - tabsContainer.getPaddingLeft() <= 0;
        } else {
            // 直接滚动view
            scrollToChild(position, 0f);
            invalidate();
        }
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(position, view, this);
        }
    }

    /**
     * 设置Item点击的监听器
     *
     * @param listener 监听器
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    /**
     * 滑动到特定Child的某位置
     *
     * @param position
     * @param relativeOffset 相对偏移量，从0到1
     */
    public void scrollToChild(int position, float relativeOffset) {
        if (tabsContainer == null || tabsContainer.getChildAt(position) == null) {
            return;
        }
        int absoluteOffset = Math.round(relativeOffset * tabsContainer.getChildAt(position).getWidth());
        scrollToChild(position, absoluteOffset);
    }

    /**
     * 滑动到特定Child的某位置
     *
     * @param position
     * @param absoluteOffset 绝对偏移量，单位：px
     */
    public void scrollToChild(int position, int absoluteOffset) {
        scrollToChild(position, absoluteOffset, false);
    }

    /**
     * 滑动到特定Child的某位置
     *
     * @param position
     * @param absoluteOffset 绝对偏移量，单位：px
     * @param ignoreLast 是否忽略上次滑动坐标值，lastScrollX
     */
    public void scrollToChild(int position, int absoluteOffset, boolean ignoreLast) {
        // 计算绝对偏移量
        if (mAdapter.getCount() == 0) {
            return;
        }
        currentPosition = position;
        currentPositionOffset = ((float) absoluteOffset) / tabsContainer.getChildAt(position).getWidth();
        if (currentPositionOffset > 0.998) {
            currentPosition = position + 1;
            currentPositionOffset = 0;
        }
        updateChildViewWhenScroll(currentPosition, absoluteOffset);
        if (mIsBindViewPager && clickPosition > 0 && !needScrollToClick) {
            return;
        }
        clickPosition = -1;
        needScrollToClick = false;
        boolean needScroll = false;
        View view = tabsContainer.getChildAt(position);
        // 计算是否需要滚动，如果当前tab已经在可视范围内，就无需滚动
        if (view != null) {
            int[] viewPosition = new int[2];
            view.getLocationOnScreen(viewPosition);
            int[] containerPosition = new int[2];
            this.getLocationOnScreen(containerPosition);
            int gap = viewPosition[0] + view.getWidth() - (containerPosition[0] + getWidth() - tabsContainer.getPaddingRight());
            needScroll = gap > 0 || viewPosition[0] - containerPosition[0] - tabsContainer.getPaddingLeft() <= 0;
        }
        if (needScroll) {
            int newScrollX = tabsContainer.getChildAt(position).getLeft() + absoluteOffset;
            // TextView添加Margins后需要修改滑动起始点
//            newScrollX = newScrollX - ((position * 2) + 1) * tabMargins;
            newScrollX = newScrollX - tabMargins;
            if (position > 0 || absoluteOffset > 0) {
                newScrollX -= scrollOffset;
            }
            if (newScrollX != lastScrollX || ignoreLast) {
                lastScrollX = newScrollX;
                scrollTo(newScrollX, 0);
            }
        }
    }

    protected void updateChildViewWhenScroll(int position, int absoluteOffset) {
    }

    /**
     * 选中导航中的某一项
     *
     * @param position
     */
    public void setSelection(int position) {
        // check adapter
        if (mAdapter == null) {
            return;
        }
        // check position
        if (position < 0 || position >= mAdapter.getCount()) {
            return;
        }
        // start scroll
        scrollToChild(position, 0f);
    }

    public boolean isIndicatorVisible() {
        return indicatorVisible;
    }

    /**
     * 是否显示导航器
     * @param indicatorVisible
     */
    public void setIndicatorVisible(boolean indicatorVisible) {
        this.indicatorVisible = indicatorVisible;
        this.indicatorVisibleBySet = indicatorVisible;
        invalidate();
    }

    public boolean isUnderlineVisible() {
        return underlineVisible;
    }

    /**
     * 是否显示下划线
     * @param underlineVisible
     */
    public void setUnderlineVisible(boolean underlineVisible) {
        this.underlineVisible = underlineVisible;
        invalidate();
    }

    public int getIndicatorPadding() {
        return indicatorPadding;
    }

    public void setIndicatorPadding(int indicatorPadding) {
        this.indicatorPadding = indicatorPadding;
        invalidate();
    }

    public void setContainerRightPadding(int px) {
        tabsContainer.setPadding(tabsContainer.getPaddingLeft(), tabsContainer.getPaddingTop(),
                px, tabsContainer.getPaddingBottom());
    }

    /**
     * Item点击的监听器
     */
    public interface OnItemClickListener {
        void onItemClick(int position, View view, ViewGroup parentView);
    }

}
