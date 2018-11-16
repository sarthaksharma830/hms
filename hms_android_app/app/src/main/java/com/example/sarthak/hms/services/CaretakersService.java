package com.example.sarthak.hms.services;

import com.example.sarthak.hms.callbacks.ICaretakerCallback;
import com.example.sarthak.hms.models.Caretaker;
import com.example.sarthak.hms.retrofit.ICaretakersService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CaretakersService {
    public void getCaretakerInfo(String username, final ICaretakerCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        ICaretakersService service = retrofit.create(ICaretakersService.class);
        service.getCaretakerInfo(username).enqueue(new Callback<Caretaker>() {
            @Override
            public void onResponse(Call<Caretaker> call, Response<Caretaker> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onCaretaker(response.body());
                }
            }

            @Override
            public void onFailure(Call<Caretaker> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }
}
