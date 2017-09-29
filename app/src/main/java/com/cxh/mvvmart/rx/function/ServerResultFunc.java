package com.cxh.mvvmart.rx.function;


import com.cxh.mvvmart.base.Result;
import com.cxh.mvvmart.rx.exception.ServerException;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/8/30
 */
public class ServerResultFunc<T> implements Function<Result<T>, T> {

    @Override
    public T apply(@NonNull Result<T> httpResult) throws Exception {
        if (httpResult.code != 1) {
            throw new ServerException(httpResult.code, httpResult.msg);
        }
        return httpResult.data;
    }
}