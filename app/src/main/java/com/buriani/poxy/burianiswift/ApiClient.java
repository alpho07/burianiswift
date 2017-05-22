package com.buriani.poxy.burianiswift;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Poxy on 5/16/2017.
 */
public class ApiClient {
    final static String base_url = "https://buriani.co.ke/mobile/";
    static Retrofit retrofit = null;


    public static Retrofit getClient() {
       // HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
       // OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
       // httpClient.addInterceptor(logging);
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.MINUTES)
                .readTimeout(30, TimeUnit.MINUTES)
                .build();
        if (retrofit == null) {
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)

                    .build();
        }
        return retrofit;
    }
}