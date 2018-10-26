package com.example.sarthak.hms.activities;

import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.CategorySpinnerAdapter;
import com.example.sarthak.hms.adapters.TitleChipsAdapter;
import com.example.sarthak.hms.callbacks.TitleChipOnClickCallback;

public class NewComplaintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);
        Toolbar toolbar = findViewById(R.id.new_complaint_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_clear_black_24dp, getTheme()));

        final AppCompatEditText titleEditText = findViewById(R.id.titleEditText);

        AppCompatSpinner categorySpinner = findViewById(R.id.categorySpinner);
        CategorySpinnerAdapter adapter = new CategorySpinnerAdapter(this, R.layout.category_spinner_item, new String[]{"Carpenter", "Electrician", "Plumber", "Housekeeping", "Technical Support"});
        categorySpinner.setAdapter(adapter);

        RecyclerView titleChips = findViewById(R.id.titleChips);
        titleChips.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TitleChipsAdapter titleChipsAdapter = new TitleChipsAdapter(this, new String[]{"Door handle broken", "Balcony door broken", "Almira lock broken", "Door broken", "Chair broken", "Soft board on desk broken"});
        titleChipsAdapter.setOnTitleChipClickCallback(new TitleChipOnClickCallback() {
            @Override
            public void onClick(String title) {
                titleEditText.setText(title);
            }
        });
        titleChips.setAdapter(titleChipsAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_complaint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
