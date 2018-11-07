package com.example.sarthak.hms.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.R;

public class ChangeDevConfigActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_dev_config);

        final EditText baseUrlEditText = findViewById(R.id.baseUrlEditText);
        Button changeBaseUrlButton = findViewById(R.id.changeBaseUrl);

        baseUrlEditText.setText(Constants.API_BASE_URL);
        changeBaseUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.API_BASE_URL = baseUrlEditText.getText().toString();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}
