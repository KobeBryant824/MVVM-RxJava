package com.cxh.mvvmsample.model.repository;


import com.cxh.mvvmsample.listener.OnRequestListener;

/**
 * 公共数据请求接口
 * Created by Hai (haigod7@gmail.com) on 2017/3/6 10:51.
 */
public interface IRequestBiz<T>{

    void requestData(OnRequestListener<T> listener);
}
