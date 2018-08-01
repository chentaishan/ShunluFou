package com.shunlufou.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.shunlufou.R;
import com.shunlufou.adapter.FragmentAdapter;
import com.shunlufou.fragment.BaseFragment;
import com.shunlufou.fragment.CustomFragment;
import com.shunlufou.fragment.DriverFragment;
import com.shunlufou.utils.ScreenUtils;
import com.shunlufou.view.AHViewPagerTabBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {


    private AHViewPagerTabBar viewPagerTabbar;
    private ViewPager viewPager;
    private TextView myInfo;
    private CustomFragment customFragment;

    private List<BaseFragment> mListFragments = new ArrayList<BaseFragment>();
    private DriverFragment driverFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();

        generateFragments(getSupportFragmentManager(),viewPagerTabbar,viewPager,0);

    }


    @Override
    protected void initView() {
        super.initView();

        viewPagerTabbar = (AHViewPagerTabBar)findViewById(R.id.viewpager_tabbar);
        viewPager = (ViewPager)findViewById(R.id.main_pager);
        myInfo = (TextView)findViewById(R.id.my_info);

        viewPagerTabbar.setFirstTabMarginLeft(ScreenUtils.dpToPxInt(this,5f));
        viewPagerTabbar.setTextSize(14);

        viewPagerTabbar.setDividerPadding(ScreenUtils.dpToPxInt(this,4f));
//        viewPagerTabbar.setTabPaddingLeftRight(ScreenUtils.dpToPxInt(this,15f));
        viewPagerTabbar.setTabTextColor(getResources().getColorStateList(R.color.color_b3));
        viewPagerTabbar.setIndicatorPadding(ScreenUtils.dpToPxInt(this,1f));
        viewPagerTabbar.setTabTextColorFocus(getResources().getColor(R.color.color_a1));
        viewPagerTabbar.setIndicatorColor(getResources().getColor(R.color.color_a1));
        viewPagerTabbar.setBackgroundColor(Color.TRANSPARENT);

        viewPagerTabbar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    /**
     * 产生fragment
     *
     * @param fm
     * @param tabs
     * @param viewPager
     * @param lastPosition
     */
    private void generateFragments(FragmentManager fm, AHViewPagerTabBar tabs, ViewPager viewPager, int lastPosition) {
        if (null == tabs || null == viewPager) {
            return;
        }
        List<String> titles = Arrays.asList("人找车", "车找人");

        customFragment = new CustomFragment();
        driverFragment = new DriverFragment();
//        customFragment.setIsExistViewPage(true);
        mListFragments.add(customFragment);
        mListFragments.add(driverFragment);



        FragmentAdapter adapter = (FragmentAdapter) viewPager.getAdapter();
        if (adapter == null) {
            adapter = new FragmentAdapter(fm, titles, mListFragments);
            viewPager.setAdapter(adapter);

            viewPagerTabbar.setViewPager(viewPager);
            viewPagerTabbar.getAdapter().notifyDataSetChanged();
            viewPagerTabbar.setTabHorizontalMargin(ScreenUtils.dpToPxInt(this, 9f));

        } else {
            adapter.setListFragments(titles, mListFragments);
            tabs.getAdapter().notifyDataSetChanged();
        }
    }

}
