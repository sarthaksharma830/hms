package com.example.sarthak.hms.activities;

import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.CategorySpinnerAdapter;
import com.example.sarthak.hms.adapters.TitleChipsAdapter;
import com.example.sarthak.hms.callbacks.IComplaintCategoriesListCallback;
import com.example.sarthak.hms.callbacks.IComplaintTitleListCallback;
import com.example.sarthak.hms.callbacks.TitleChipOnClickCallback;
import com.example.sarthak.hms.models.ComplaintCategory;
import com.example.sarthak.hms.services.ComplaintsService;

import java.util.ArrayList;
import java.util.List;

public class NewComplaintActivity extends AppCompatActivity {

    private AppCompatEditText titleEditText;
    private AppCompatSpinner categorySpinner;
    private RecyclerView titleChips;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private List<ComplaintCategory> complaintCategories;
    private ProgressBar newComplaintProgressBar;
    private NestedScrollView nestedScrollView;
    private TitleChipsAdapter titleChipsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        // Applying ActionBar
        Toolbar toolbar = findViewById(R.id.new_complaint_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_clear_black_24dp, getTheme()));

        // Preparing Views
        prepareViews();

        newComplaintProgressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);

        ComplaintsService service = new ComplaintsService();
        service.getAllComplaintCategories(new IComplaintCategoriesListCallback() {
            @Override
            public void onComplaintCategoriesList(List<ComplaintCategory> complaintCategoriesList) {
                NewComplaintActivity.this.complaintCategories = complaintCategoriesList;
                populateViews();

                nestedScrollView.setVisibility(View.VISIBLE);
                newComplaintProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(NewComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                newComplaintProgressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void populateViews() {
        categorySpinnerAdapter = new CategorySpinnerAdapter(NewComplaintActivity.this, R.layout.category_spinner_item, complaintCategories);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComplaintsService service = new ComplaintsService();
                service.getDefaultComplaintTitles(complaintCategories.get(position).getId(), new IComplaintTitleListCallback() {
                    @Override
                    public void onTitles(List<String> titles) {
                        titleChipsAdapter.setTitles(titles);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(NewComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        titleChipsAdapter.setTitles(new ArrayList<String>());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        titleChips.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        titleChipsAdapter = new TitleChipsAdapter(this, new ArrayList<String>());
        titleChipsAdapter.setOnTitleChipClickCallback(new TitleChipOnClickCallback() {
            @Override
            public void onClick(String title) {
                titleEditText.setText(title);
            }
        });
        titleChips.setAdapter(titleChipsAdapter);
        categorySpinner.setSelection(0);
    }

    private void prepareViews() {
        titleEditText = findViewById(R.id.titleEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        titleChips = findViewById(R.id.titleChips);
        newComplaintProgressBar = findViewById(R.id.newComplaintProgressBar);
        nestedScrollView = findViewById(R.id.newComplaintNsv);
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
