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

    private Button loginButton, continueButton;
    private TextView appTitleAfter;
    private TextView appTitleBefore;
    private ProgressBar progressBar;
    private RelativeLayout loginForm;
    private RelativeLayout userTypeLayout;
    private LinearLayout logoLayout, caretaker, student;
    private int userType = 0;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prepareViews();

        appTitleBefore.setTypeface(Typeface.createFromAsset(getAssets(), "font_regular.otf"));
        appTitleAfter.setTypeface(Typeface.createFromAsset(getAssets(), "font_regular.otf"));
        appTitleAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ChangeDevConfigActivity.class));
            }
        });

        logoLayout.setVisibility(View.VISIBLE);
        userTypeLayout.setVisibility(View.GONE);
        loginForm.setVisibility(View.GONE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                logoLayout.setVisibility(View.GONE);
                userTypeLayout.setVisibility(View.VISIBLE);

                invalidateUserType();

                caretaker.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userType = 0;
                        invalidateUserType();
                    }
                });

                student.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userType = 1;
                        invalidateUserType();
                    }
                });

                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userTypeLayout.setVisibility(View.GONE);
                        loginForm.setVisibility(View.VISIBLE);

                        loginButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                progressBar.setVisibility(View.VISIBLE);
                                final String username = usernameEditText.getText().toString().trim();
                                String password = passwordEditText.getText().toString();

                                AuthenticationService authenticationService = new AuthenticationService();
                                Credential credential = new Credential(username, password);
                                if (userType == 0) {
                                    authenticationService.caretakerLogin(credential, new ILoginCallback() {
                                        @Override
                                        public void onLogin(boolean result) {
                                            if (result) {
                                                Intent intent = new Intent(MainActivity.this, CaretakerActivity.class);
                                                intent.putExtra(Constants.EXTRA_CARETAKER_USERNAME, username);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(MainActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                                            }
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }

                                        @Override
                                        public void onError(Exception e) {

                                        }
                                    });
                                } else {
                                    authenticationService.studentLogin(credential, new ILoginCallback() {
                                        @Override
                                        public void onLogin(boolean result) {
                                            if (result) {
                                                Intent intent = new Intent(MainActivity.this, StudentActivity.class);
                                                intent.putExtra(Constants.EXTRA_STUDENT_ROLLNO, username);
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
                                    }, false);
                                }
                            }
                        });
                    }
                });
            }
        }, 2000);
    }

    private void invalidateUserType() {
        if (userType == 0) {
            caretaker.setBackgroundResource(R.drawable.border_green);
            student.setBackground(null);
        } else {
            student.setBackgroundResource(R.drawable.border_green);
            caretaker.setBackground(null);
        }
    }

    private void prepareViews() {
        logoLayout = findViewById(R.id.logoLayout);
        userTypeLayout = findViewById(R.id.userTypeLayout);
        loginForm = findViewById(R.id.loginForm);
        progressBar = findViewById(R.id.progressBar);
        appTitleBefore = findViewById(R.id.appTitleBefore);
        appTitleAfter = findViewById(R.id.appTitleAfter);
        loginButton = findViewById(R.id.loginButton);
        caretaker = findViewById(R.id.caretaker);
        student = findViewById(R.id.student);
        continueButton = findViewById(R.id.continueButton);
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
    }
}
