package com.example.sarthak.hms.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.callbacks.LoginCallback;
import com.example.sarthak.hms.models.Credential;
import com.example.sarthak.hms.retrofit.ILoginService;
import com.example.sarthak.hms.services.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout logoLayout = findViewById(R.id.logoLayout);
        final RelativeLayout loginForm = findViewById(R.id.loginForm);
        TextView appTitleBefore = findViewById(R.id.appTitleBefore);
        appTitleBefore.setTypeface(Typeface.createFromAsset(getAssets(), "font_regular.otf"));
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoLayout.animate().alpha(0.0f).setDuration(500).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        loginForm.animate().alpha(1.0f).setDuration(500).start();
                        loginForm.setVisibility(View.VISIBLE);

                        final EditText rollNumberEditText = findViewById(R.id.rollNumberEditText);
                        final EditText passwordEditText = findViewById(R.id.passwordEditText);
                        TextView appTitleAfter = findViewById(R.id.appTitleAfter);
                        appTitleAfter.setTypeface(Typeface.createFromAsset(getAssets(), "font_regular.otf"));
                        Button loginButton = findViewById(R.id.loginButton);

                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String rollno = rollNumberEditText.getText().toString();
                                String password = passwordEditText.getText().toString();
                                LoginService loginService = new LoginService();
                                loginService.loginAsync(new Credential(rollno, password), new LoginCallback() {
                                    @Override
                                    public void onLogin(boolean result) {
                                        if (result) {
                                            startActivity(new Intent(MainActivity.this, StudentActivity.class));
                                        } else {
                                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }, true);
                            }
                        });
                    }
                }).start();
            }
        }, 2000);
    }
}
