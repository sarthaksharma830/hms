package com.example.sarthak.hms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.AppointmentsRecyclerViewAdapter;
import com.example.sarthak.hms.callbacks.IAppointmentsListCallback;
import com.example.sarthak.hms.models.Appointment;
import com.example.sarthak.hms.services.AppointmentsService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentCompletedAppointmentsFragment extends Fragment {


    private RecyclerView recyclerView;
    private ProgressBar completedAppointmentsProgressBar;
    private List<Appointment> appointments;

    public StudentCompletedAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_completed_appointments, container, false);

        prepareViews(rootView);
        recyclerView.setVisibility(View.GONE);
        completedAppointmentsProgressBar.setVisibility(View.VISIBLE);

        AppointmentsService service = new AppointmentsService();
        service.getCompletedAppointmentsByStudent(Persistence.student.getId(), new IAppointmentsListCallback() {
            @Override
            public void onAppointmentsList(List<Appointment> appointments) {
                StudentCompletedAppointmentsFragment.this.appointments = appointments;
                populateViews();
                recyclerView.setVisibility(View.VISIBLE);
                completedAppointmentsProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                completedAppointmentsProgressBar.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    private void populateViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter adapter = new AppointmentsRecyclerViewAdapter(appointments);
        recyclerView.setAdapter(adapter);
    }

    private void prepareViews(View rootView) {
        recyclerView = rootView.findViewById(R.id.recyclerView);
        completedAppointmentsProgressBar = rootView.findViewById(R.id.completedAppointmentsProgressBar);
    }

}
