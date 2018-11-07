package com.example.sarthak.hms.services;

import android.os.Handler;

import com.example.sarthak.hms.callbacks.ILoginCallback;
import com.example.sarthak.hms.models.Credential;
import com.example.sarthak.hms.retrofit.ILoginService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AuthenticationService {
    public void loginAsync(Credential credential, final ILoginCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        ILoginService service = retrofit.create(ILoginService.class);

        service.login(credential).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Boolean result = Integer.parseInt(response.body()) == 1;
                callback.onLogin(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError(new Exception("Network Error"));
            }
        });
    }

    private boolean loginOffline(Credential credential) {
        return credential.getRollno().equals("101783037") && credential.getPassword().equals("test123") || true;
    }

    public void loginAsync(final Credential credential, final ILoginCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onLogin(loginOffline(credential));
                }
            }, 1000);
        } else {
            loginAsync(credential, callback);
        }
    }
}
