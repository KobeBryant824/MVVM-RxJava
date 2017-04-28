package com.cxh.mvvmsample.model.repository;

import android.util.Log;

import com.cxh.mvvmsample.listener.OnRequestListener;
import com.cxh.mvvmsample.manager.RxDisposable;
import com.cxh.mvvmsample.model.api.XXXApi;
import com.cxh.mvvmsample.util.RetrofitProvider;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 具体页面的数据请求
 * Created by Hai (haigod7@gmail.com) on 2017/3/6 10:51.
 */
public class XXXDataRepository implements IRequestBiz<XXXApi.WelcomeEntity> {

    public void requestData(final OnRequestListener<XXXApi.WelcomeEntity> listener) {

        XXXApi xxxApi = RetrofitProvider.getInstance().create(XXXApi.class);

        Disposable subscribe = xxxApi.getWelcomeEntity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onSuccess, // Java8 方法引用
                        throwable -> listener.onFailed(),
                        () -> {
                });

        Disposable subscribe1 = Flowable.interval(1, TimeUnit.SECONDS)
                .doOnCancel(() -> Log.e("hh", "Unsubscribing subscription from onCreate()"))
                .subscribe(aLong -> Log.e("hh", "Started in onCreate(), running until onDestroy(): " + aLong));

        RxDisposable.add(subscribe);

        RxDisposable.add(subscribe1);

    }

}
