package com.shunlufou.adapter;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.shunlufou.fragment.BaseFragment;

import java.util.List;

/**
 * Created by 蒴 on 2016/9/19.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<String> mTitles;
    private List<? extends BaseFragment> mListFragments;

    public FragmentAdapter(FragmentManager fm, List<String> titles, List<? extends BaseFragment> listFragments) {
        super(fm);
        this.mTitles = titles;
        this.mListFragments = listFragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    public void setListFragments(List<? extends BaseFragment> mListFragments) {
        this.mListFragments = mListFragments;
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
        super.restoreState(arg0, arg1);
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }


    public void setTitles(List<String> mTitles) {
        this.mTitles = mTitles;
    }

    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public List<String> getTitles() {
        return mTitles;
    }

    public List<? extends BaseFragment> getFragments() {
        return mListFragments;
    }

    public void setListFragments(List<String> titles, List<? extends BaseFragment> fragments) {
        if (mTitles != null && mListFragments != null) {
            mTitles.clear();
            mTitles = null;
            mTitles = titles;

            mListFragments.clear();
            mListFragments = null;
            mListFragments = fragments;
            notifyDataSetChanged();
        }
    }

}
