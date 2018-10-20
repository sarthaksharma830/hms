package com.example.sarthak.hms.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitProvider {
    public static Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.33:5000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
