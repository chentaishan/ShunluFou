package com.shunlufou.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shunlufou.R;


/**
 * 仅供SlidingTabBar使用，带标记显示的文本控件，可以显示文本或红点标记
 */
class AHTabBarTextBadgeView extends LinearLayout {

    private AHBadgeView badgeView;
    private TextView targetView;

    public AHTabBarTextBadgeView(Context context) {
        super(context);
        inflateView();
    }

    public AHTabBarTextBadgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView();
    }

    public AHTabBarTextBadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView();
    }

    @TargetApi(21)
    public AHTabBarTextBadgeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inflateView();
    }

    private void inflateView() {
        setGravity(Gravity.CENTER);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.badge_text_view, this);
        badgeView = (AHBadgeView) findViewById(R.id.text_badge_view);
        targetView = (TextView) findViewById(R.id.text_badge);
//        badgeView.setTargetShowInCenter(true);
        badgeView.setTargetShowInCenterHorizontal(false);
        badgeView.setTargetShowInCenterVertical(true);
    }

    public AHBadgeView getBadgeView() {
        return badgeView;
    }

    public TextView getTargetView() {
        return targetView;
    }

    /**
     * 计算目标view与容器view起始位置x轴坐标间距，数值为正
     * @return
     */
    public int leftTargetGap() {
        int[] containerPosition = new int[2];
        this.getLocationOnScreen(containerPosition);
        int[] targetPosition = new int[2];
        targetView.getLocationOnScreen(targetPosition);
        return targetPosition[0] - containerPosition[0];
    }

    /**
     * 计算目标view与容器view终止位置x轴坐标间距，数值为正
     * @return
     */
    public int rightTargetGap() {
        int[] containerPosition = new int[2];
        this.getLocationOnScreen(containerPosition);
        int[] targetPosition = new int[2];
        targetView.getLocationOnScreen(targetPosition);
        return (containerPosition[0] + this.getMeasuredWidth()) - (targetPosition[0] + targetView.getMeasuredWidth());
    }

}
