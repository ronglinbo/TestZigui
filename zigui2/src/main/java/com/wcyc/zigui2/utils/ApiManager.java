package com.wcyc.zigui2.utils;

import com.wcyc.zigui2.newapp.bean.NewMemberBean;

import com.wcyc.zigui2.newapp.bean.UserServiceInfo;
import com.wcyc.zigui2.newapp.module.email.MenuConfigBean;
import com.wcyc.zigui2.newapp.module.email.NewEmailBean;

import okhttp3.RequestBody;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by xiehua on 2017/3/7.
 */

public interface ApiManager {

    // option 2: using a dynamic URL
    @GET
    Call<ResponseBody> downloadFileWithDynamicUrlSync(@Url String fileUrl);
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("login/")
    Call<NewMemberBean> login(@Body RequestBody json);
    @Headers({"Content-Type: application/json","Accept: application/json"})
    @POST("EmailList/")
    Call<NewEmailBean> EmailList(@Body RequestBody json);
    @POST("getPersonalApplyList/")
    Call<MenuConfigBean> getPersonalApplyList(@Body RequestBody json);
//激活试用接口
    @POST("activeProductService/")
    Call<UserServiceInfo> activeProductService(@Body RequestBody json);
}
