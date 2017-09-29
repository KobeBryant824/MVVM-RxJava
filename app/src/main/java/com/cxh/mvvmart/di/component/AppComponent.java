package com.cxh.mvvmart.di.component;

import android.content.Context;

import com.cxh.mvvmart.di.moduel.AppModule;
import com.cxh.mvvmart.di.qualifier.ContextLife;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/6/7 15:43
 */
@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ContextLife
    Context getApplication();

    Retrofit getRetrofit();

    OkHttpClient getOkHttpClient();

}
