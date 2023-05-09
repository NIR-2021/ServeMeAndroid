package com.example.serveme_m1.Models;

import com.example.serveme_m1.R;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRetrofit {
    private static Retrofit getRetrofit(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String azureString = "https://servemem220230228193649.azurewebsites.net/";
        String localString = "https://10.0.2.2:44327/";

        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
//                .baseUrl(String.valueOf(R.string.base_url))
                .baseUrl(azureString)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create() )
                .build();


        return retrofit;
    }

    public static ServeMeApi getCancelRequest(){
        ServeMeApi cancelRequest = getRetrofit().create(ServeMeApi.class);
        return cancelRequest;
    }

    public static ServeMeApi getCall(){
        ServeMeApi cancelRequest = getRetrofit().create(ServeMeApi.class);
        return cancelRequest;
    }
}
