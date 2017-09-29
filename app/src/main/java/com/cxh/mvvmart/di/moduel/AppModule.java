package com.cxh.mvvmart.di.moduel;

import android.app.Application;
import android.content.Context;

import com.cxh.mvvmart.di.qualifier.ContextLife;
import com.socks.library.KLog;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.cxh.mvvmart.Constant.BASEURL;

/**
 * @author Hai (haigod7[at]gmail[dot]com)
 *         2017/6/7
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    @ContextLife
    Context provideApplication() {
        return mApplication.getApplicationContext();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl(BASEURL)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(KLog::e);
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); //包含header、body数据

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }


}
