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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.callbacks.ILoginCallback;
import com.example.sarthak.hms.models.Credential;
import com.example.sarthak.hms.services.AuthenticationService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final LinearLayout logoLayout = findViewById(R.id.logoLayout);
        final RelativeLayout loginForm = findViewById(R.id.loginForm);
        final ProgressBar progressBar = findViewById(R.id.progressBar);

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
                        appTitleAfter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(MainActivity.this, ChangeDevConfigActivity.class));
                            }
                        });

                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);
                                final String rollno = rollNumberEditText.getText().toString();
                                String password = passwordEditText.getText().toString();
                                AuthenticationService authenticationService = new AuthenticationService();
                                authenticationService.loginAsync(new Credential(rollno, password), new ILoginCallback() {
                                    @Override
                                    public void onLogin(boolean result) {
                                        if (result) {
                                            Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                            intent.putExtra(Constants.EXTRA_STUDENT_ROLLNO, rollno);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.INVISIBLE);
                                    }

                                    @Override
                                    public void onError(Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.INVISIBLE);
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
