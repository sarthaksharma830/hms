package com.example.sarthak.hms.fragments;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.activities.ComplaintDetailActivity;
import com.example.sarthak.hms.activities.NewComplaintActivity;
import com.example.sarthak.hms.adapters.RecentComplaintsListAdapter;
import com.example.sarthak.hms.callbacks.ComplaintsListRecyclerViewOnItemClickCallback;
import com.example.sarthak.hms.models.Complaint;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ComplaintsListFragment extends Fragment {


    public ComplaintsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_complaints_list, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.complaints_list_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView complaintsListRecyclerView = rootView.findViewById(R.id.complaintsListRecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        complaintsListRecyclerView.setLayoutManager(layoutManager);
        RecentComplaintsListAdapter adapter = new RecentComplaintsListAdapter(new ArrayList<Complaint>());
        adapter.setOnItemClickCallback(new ComplaintsListRecyclerViewOnItemClickCallback() {
            @Override
            public void onClick() {
                startActivity(new Intent(getContext(), ComplaintDetailActivity.class));
            }
        });
        complaintsListRecyclerView.setAdapter(adapter);

        FloatingActionButton newComplaintFab = rootView.findViewById(R.id.newComplaint);
        newComplaintFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NewComplaintActivity.class));
            }
        });
        final AppBarLayout appBarLayout = rootView.findViewById(R.id.complaintsListAppBar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            complaintsListRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                        appBarLayout.setElevation(0.0f);
                    } else {
                        appBarLayout.setElevation(10.0f);
                    }
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }

        return rootView;
    }

}
