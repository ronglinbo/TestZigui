/*
* 文 件 名:Callbacks.java
* 创 建 人： 姜韵雯
* 日    期： 2014-09-23 
* 版 本 号： 1.00
*/
package com.wcyc.zigui2.utils;

import java.util.List;

//2014-9-23
/**
 * 回调接口
 * @author 姜韵雯
 * @version 1.01
 * @since 1.01
 */
public interface Callbacks<T> {

    void startBinding();
    
    void bindItems(List<T> list);
    
    void bindItems(List<T> list, int index);
    
    //增加可以返回单个对象的回调方法
    void bindItem(T item);
    //jiang增加返回code的方法
    void bindCode(int code);
    void finishBindingItems();
}
