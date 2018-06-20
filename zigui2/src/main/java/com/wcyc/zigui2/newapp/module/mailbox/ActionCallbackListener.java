package com.wcyc.zigui2.newapp.module.mailbox;

/**
 * Created by xiehua on 2016/8/26.
 */
public interface ActionCallbackListener<T> {
    void onSuccess(T data);
    void onFailure(String errorEvent, String message);
}
