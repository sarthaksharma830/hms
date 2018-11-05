package com.example.sarthak.hms.activities;

import android.graphics.Rect;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.ComplaintPicturesListAdapter;

public class ComplaintDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint_detail);

        final Toolbar toolbar = findViewById(R.id.complaint_detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final NestedScrollView nestedScrollView = findViewById(R.id.complaintDetailNsv);
        final AppBarLayout appBarLayout = findViewById(R.id.complaintDetailAppBar);
        final TextView complaintTitleTextView = findViewById(R.id.complaintTitle);
        final TextView toolbarTitle = findViewById(R.id.complaintDetailToolbarTitle);

        RecyclerView complaintPicturesRecyclerView = findViewById(R.id.complaintPicturesRecyclerView);
        complaintPicturesRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        ComplaintPicturesListAdapter adapter = new ComplaintPicturesListAdapter(this);
        complaintPicturesRecyclerView.setAdapter(adapter);

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = nestedScrollView.getScrollY();
                if (scrollY == 0) {
                    appBarLayout.setElevation(0.0f);
                } else {
                    appBarLayout.setElevation(10.0f);
                }
                Rect scrollBounds = new Rect();
                if (complaintTitleTextView.getLocalVisibleRect(scrollBounds)) {
                    toolbarTitle.setVisibility(View.INVISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_complaint_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
