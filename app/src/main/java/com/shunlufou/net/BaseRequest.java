package com.shunlufou.net;

import android.text.TextUtils;

import com.shunlufou.utils.LogUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Chents on 2018/3/5.
 */

public class BaseRequest {

    protected void doGetRequest(String url,Callback callback){
        if (TextUtils.isEmpty(url)){
            return;
        }
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }


    protected void doPostRequest(String url, RequestBody body, Callback callback){
        Request request = null;
        try {
            if (TextUtils.isEmpty(url)){
                return;
            }
            OkHttpClient mOkHttpClient = new OkHttpClient();
             request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = mOkHttpClient.newCall(request).execute();
            callback.onResponse(response);
        } catch (IOException e) {
            e.printStackTrace();
            callback.onFailure(request,e);
        }
    }
}
