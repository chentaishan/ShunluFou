package com.shunlufou.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shunlufou.R;
import com.shunlufou.net.GetSMSCodeRequest;
import com.shunlufou.utils.LogUtil;

import java.io.IOException;
/**
 * Created by Chents on 2018/2/8.
 */

public class CustomFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.custom_fragment,null);
        initView(view);
        return view;
    }



    protected void initView(View view){

        Button button = (Button)view.findViewById(R.id.send_msg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request();
            }
        });
    }
    protected void initData(){

    }

    private void request(){
        GetSMSCodeRequest request = new GetSMSCodeRequest();
        request.doGetRequest("http://47.95.193.131:8761/account/mobileVerificationCode?mobile=18510088600",new Callback(){

            @Override
            public void onFailure(Request request, IOException e) {
                LogUtil.e("TEST",request.body().toString()+"");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                LogUtil.e("TEST",response.body().string()+"");
            }
        });

    }
}
