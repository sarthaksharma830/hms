package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Credential;

import retrofit2.Call;
import retrofit2.http.*;

public interface ILoginService {
    @POST("authentication/login")
    Call<String> login(@Body Credential credential);
}
