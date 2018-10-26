package com.example.sarthak.hms.fragments;


import android.content.Intent;
import android.os.Bundle;
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
        complaintsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecentComplaintsListAdapter adapter = new RecentComplaintsListAdapter(getContext());
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

        return rootView;
    }

}
