package com.akl.livetelecastgiftdemo;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author:gtf
 * @date: 2016/9/23 16:07
 * @company:yj
 * @description:
 * @version:1.0
 */

public class RetrofitControler {
    public static final String API_BASE_URL = "http://api.aikxian.com/";


    private static RetrofitService service;
    private static Retrofit retrofit;

    public static RetrofitService getService() {
        if (service == null) {
            service = getRetrofit().create(RetrofitService.class);
        }
        return service;
    }
    /**
     * 拦截器  给所有的请求添加消息头
     */
    private static Interceptor mInterceptor = new Interceptor(){
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader("test","test")
                    .addHeader("Cache-Control", "5")
                    .build();
            return chain.proceed(request);
        }
    };
    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            // log拦截器  打印所有的log
            HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("HttpLog", message);
                }
            });
            logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .addInterceptor(mInterceptor)
                    .addInterceptor(logInterceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(client)
                    .baseUrl(API_BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
