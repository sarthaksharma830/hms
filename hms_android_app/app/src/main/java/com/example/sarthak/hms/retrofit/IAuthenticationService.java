package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Credential;

import retrofit2.Call;
import retrofit2.http.*;

public interface IAuthenticationService {
    @POST("authentication/login/s")
    Call<String> studentLogin(@Body Credential credential);

    @POST("authentication/login/c")
    Call<String> caretakerLogin(@Body Credential credential);
}
