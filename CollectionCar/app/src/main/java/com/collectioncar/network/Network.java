// (c)2016 Flipboard Inc, All Rights Reserved.

package com.collectioncar.network;


import com.collectioncar.network.api.ZhuangbiApi;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static ZhuangbiApi zhuangbiApi;

    private static OkHttpClient okHttpClient = new OkHttpClient();
    private static Converter.Factory gsonConverterFactory = GsonConverterFactory.create();
    private static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static ZhuangbiApi getZhuangbiApi(String baseUrl) {
        if (zhuangbiApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(baseUrl)
                   /* //增加返回值为String的支持
                    .addConverterFactory(ScalarsConverterFactory.create())*/
                    //增加返回值为Gson的支持(以实体类返回)
                    .addConverterFactory(gsonConverterFactory)
                    //增加返回值为Oservable<T>的支持
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            //创建一个Observable 即被观察者（可以理解为一个View） 而观察者Observer 理解为一个view 的onclick 事件
            zhuangbiApi = retrofit.create(ZhuangbiApi.class);
        }
        return zhuangbiApi;
    }


}
