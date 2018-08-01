package com.shunlufou.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.shunlufou.R;

/**
 * Created by Chents on 2018/2/8.
 */

public class DriverFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.driver_fragment,null);
//        return super.onCreateView(inflater, container, savedInstanceState);
        return  view;
    }



    protected void initView(View view){

    }
    protected void initData(){

    }
}
