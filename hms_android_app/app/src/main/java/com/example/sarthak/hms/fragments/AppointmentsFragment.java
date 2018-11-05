package com.example.sarthak.hms.fragments;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.AppointmentsRecyclerViewAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppointmentsFragment extends Fragment {


    public AppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_appointments, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.appointments_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView overdueAppointmentsRecyclerView = rootView.findViewById(R.id.overdueAppointmentsRecyclerView);
        overdueAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter overdueAdapter = new AppointmentsRecyclerViewAdapter(0);
        overdueAppointmentsRecyclerView.setAdapter(overdueAdapter);

        RecyclerView todayAppointmentsRecyclerView = rootView.findViewById(R.id.todayAppointmentsRecyclerView);
        todayAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter todayAdapter = new AppointmentsRecyclerViewAdapter(2);
        todayAppointmentsRecyclerView.setAdapter(todayAdapter);

        RecyclerView upcomingAppointmentsRecyclerView = rootView.findViewById(R.id.upcomingAppointmentsRecyclerView);
        upcomingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter upcomingAdapter = new AppointmentsRecyclerViewAdapter(10);
        upcomingAppointmentsRecyclerView.setAdapter(upcomingAdapter);

        final AppBarLayout appBarLayout = rootView.findViewById(R.id.appointmentsAppBar);
        NestedScrollView nestedScrollView = rootView.findViewById(R.id.appointmentsNsv);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            nestedScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY == 0) {
                        appBarLayout.setElevation(0.0f);
                    } else {
                        appBarLayout.setElevation(10.0f);
                    }
                }
            });
        }

        return rootView;
    }

}
