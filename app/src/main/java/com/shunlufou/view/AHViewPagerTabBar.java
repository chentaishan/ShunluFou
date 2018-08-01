package com.shunlufou.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.shunlufou.utils.LogUtil;


/**
 * <p>用于显示导航菜单栏，提示栏目导航功能，支持设置显示下划引导线的功能。
 * 将tab和ViewPager绑定的导航条，支持OnPageChangeListener监听。
 * 调用setViewPager方法，传入ViewPager对象，与ViewPager进行绑定。
 * </p>
 */
public class AHViewPagerTabBar extends AHSlidingTabBar {
    /**
     * 页面状态改变监听器，{@link OnPageChangeListener}实现对象
     */
    private final PageListener mPageListener = new PageListener();
    /**
     * 设置ViewPager的监听器，由外部传入
     */
    public OnPageChangeListener mDelegatePageListener;
    /**
     * 与导航条关联起来的ViewPager对象
     */
    private ViewPager mViewPager;
    /**
     * ViewPager滑动停止监听器
     */
    private OnScrollStopListener mOnScrollStopListener;
    /** ViewPager滑动停止监听器 */
    private OnScrollStopListner mOnScrollstopListner;
    /**
     * ViewPager结束滑动时的监听器
     */
    private OnPageSelectedCustomListener mOnPageSelectedCustomListener;
    /**
     * 数据Adapter
     */
    private AHBaseSlidingBarAdapter mInnerAdapter;

    /**
     * 构造函数
     *
     * @param context
     */
    public AHViewPagerTabBar(Context context) {
        super(context);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     */
    public AHViewPagerTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造函数
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public AHViewPagerTabBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 将导航条与ViewPager进行关联
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager, boolean usePageTitle) {
        this.mViewPager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mIsBindViewPager = true;
        if (usePageTitle) {
            createInnerAdapter();
            if (mInnerAdapter != null) {
                setAdapter(mInnerAdapter);
                notifyDataSetChanged();
            }
        }
        setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int position, View view, ViewGroup parentView) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(position);
                }
            }
        });
        pager.setOnPageChangeListener(mPageListener);
    }

    /**
     * 将导航条与ViewPager进行关联
     *
     * @param pager
     */
    public void setViewPager(ViewPager pager) {
        setViewPager(pager, true);
    }

    /**
     * 根据ViewPager的内容，设置数据Adapter
     */
    private void createInnerAdapter() {
        if (mViewPager == null) {
            return;
        }
        mInnerAdapter = new AHBaseSlidingBarAdapter() {

            @Override
            public int getCount() {
                if (mViewPager.getAdapter() != null) {
                    return mViewPager.getAdapter().getCount();
                }
                return 0;
            }

            @Override
            public Object getItem(int position) {
                if (mViewPager.getAdapter() != null) {
                    return mViewPager.getAdapter().getPageTitle(position).toString();
                }
                return "";
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (mViewPager.getAdapter() != null) {
                    return createTextTab(mViewPager.getAdapter().getPageTitle(position).toString());
                }
                return null;
            }
        };
    }

    /**
     * 设置ViewPager的监听器
     *
     * @param listener
     */
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.mDelegatePageListener = listener;
    }

    /**
     * 设置ViewPager滑动停止监听器
     *
     * @param listener
     */
    public void setOnScrollStopListner(OnScrollStopListener listener) {
        mOnScrollStopListener = listener;
    }

    /**
     * 设置ViewPager滑动停止监听器
     *
     * @param listener
     */
    @Deprecated
    public void setOnScrollStopListner(OnScrollStopListner listener) {
        mOnScrollstopListner = listener;
    }

    /**
     * 设置ViewPager结束滑动时的监听器
     *
     * @param listener
     */
    public void setOnPageSelectedCustom(OnPageSelectedCustomListener listener) {
        mOnPageSelectedCustomListener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mOnScrollStopListener != null) {
            mOnScrollStopListener.onScrollStopped();
        }
        // 旧的兼容逻辑
        if (mOnScrollstopListner != null) {
            mOnScrollstopListner.onScrollStoped();
        }
    }

    /**
     * 设置当前选中项，并水平滚动指定距离
     * @param position 位置
     * @param distance 水平滚动距离
     * @param isNeedCallbackScroll 是否回调滚动发生变化，@see {@link #setScrollChangedListener(OnScrollChangedListener)}
     */
    public void setSelectionWithScroll(int position, int distance, boolean isNeedCallbackScroll){
        this.isNeedCallbackScroll = isNeedCallbackScroll;
        currentPosition = position;
        updateChildViewWhenScroll(currentPosition, 0);

        scrollTo(distance, 0);
        invalidate();

        if(mIsBindViewPager && mViewPager != null){
            mViewPager.setCurrentItem(position, false);
        }
    }

    /**
     * 滑动监听(控制遮罩显示)
     */
    public interface OnScrollStopListener {
        void onScrollStopped();
    }

    /**
     * 滑动监听(ViewPager结束滑动调用)
     */
    public interface OnPageSelectedCustomListener {
        void onPageSelectedCustom(int position);
    }

    /**
     * <p>页面状态改变监听器，是{@link OnPageChangeListener}的实现类。</p>
     *
     * @author Administrator
     */
    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            LogUtil.d("PageListener", "onPageScrolled :" + position);

            scrollToChild(position, positionOffset);
            invalidate();

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
//                scrollToChild(mViewPager.getCurrentItem(), 0);
                clickPosition = -1;
                needScrollToClick = false;
            }

            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mDelegatePageListener != null) {
                mDelegatePageListener.onPageSelected(position);
            }
            updateTabTextColor(position);

            if (mOnPageSelectedCustomListener != null) {
                mOnPageSelectedCustomListener.onPageSelectedCustom(position);
            }
        }

    }


}
