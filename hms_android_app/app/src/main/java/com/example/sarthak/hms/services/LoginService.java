package com.example.sarthak.hms.services;

import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.sarthak.hms.activities.MainActivity;
import com.example.sarthak.hms.activities.StudentActivity;
import com.example.sarthak.hms.callbacks.LoginCallback;
import com.example.sarthak.hms.models.Credential;
import com.example.sarthak.hms.retrofit.ILoginService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginService {
    public void loginAsync(Credential credential, final LoginCallback callback) {
        Retrofit retrofit = RetrofitProvider.get();
        ILoginService service = retrofit.create(ILoginService.class);

        service.login(credential).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Boolean result = Boolean.parseBoolean(response.body());
                callback.onLogin(result);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.onError(new Exception("Network Error"));
            }
        });
    }

    private boolean loginOffline(Credential credential) {
        return credential.rollno.equals("101783037") && credential.password.equals("test123") || true;
    }

    public void loginAsync(Credential credential, final LoginCallback callback, boolean offline) {
        if (offline) {
            callback.onLogin(loginOffline(credential));
        } else {
            loginAsync(credential, callback);
        }
    }
}
