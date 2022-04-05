package com.liveearth.streetview.navigation.map.worldradio.globe.fm_api_source;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFMLiveEarthMapFm {
    private static Retrofit retrofitFM;
    //not working
   // private static final String BASE_URL = " https://fr1.api.radio-browser.info/";


    private static final String BASE_URL = " https://nl1.api.radio-browser.info/";

    public static Retrofit getRetrofitFM(Context mContext) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(30, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        client.addInterceptor(interceptor);
        if (retrofitFM == null) {
            retrofitFM = new Retrofit.Builder()
                    .client(client.build())
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofitFM;
    }
}
