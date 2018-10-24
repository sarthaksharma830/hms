package com.example.sarthak.hms.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;

import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.RecentComplaintsListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {


    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);
        Toolbar toolbar = rootView.findViewById(R.id.student_profile_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        RecyclerView recentComplaintsList = rootView.findViewById(R.id.recentComplaintsList);
        recentComplaintsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        RecentComplaintsListAdapter adapter = new RecentComplaintsListAdapter(getContext());
        recentComplaintsList.setAdapter(adapter);

        return rootView;
    }

}
