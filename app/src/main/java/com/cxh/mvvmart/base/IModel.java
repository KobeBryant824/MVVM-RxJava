package com.cxh.mvvmart.base;


import com.cxh.mvvmart.callback.OnRequestListener;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/3/6
 */
public interface IModel<T>{

    void requestData(RxAppCompatActivity activity, OnRequestListener<T> listener);

}
