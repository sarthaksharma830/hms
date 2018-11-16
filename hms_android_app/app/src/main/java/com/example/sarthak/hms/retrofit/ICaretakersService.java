package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Caretaker;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ICaretakersService {
    @GET("caretakers/{username}")
    Call<Caretaker> getCaretakerInfo(@Path("username") String username);
}
