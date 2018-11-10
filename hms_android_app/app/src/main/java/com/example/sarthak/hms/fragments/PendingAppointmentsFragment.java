package com.example.sarthak.hms.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.AppointmentsRecyclerViewAdapter;
import com.example.sarthak.hms.callbacks.IAppointmentsListCallback;
import com.example.sarthak.hms.models.Appointment;
import com.example.sarthak.hms.services.AppointmentsService;

import org.joda.time.DateTimeComparator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PendingAppointmentsFragment extends Fragment {


    private RecyclerView overdueAppointmentsRecyclerView;
    private RecyclerView todayAppointmentsRecyclerView;
    private RecyclerView upcomingAppointmentsRecyclerView;
    private LinearLayout contentView;
    private ProgressBar pendingAppointmentsProgressBar;

    private List<Appointment> overdueAppointments = new ArrayList<>();
    private List<Appointment> todayAppointments = new ArrayList<>();
    private List<Appointment> upcomingAppointments = new ArrayList<>();

    public PendingAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pending_appointments, container, false);

        // Preparing Views
        prepareViews(rootView);

        contentView.setVisibility(View.GONE);
        pendingAppointmentsProgressBar.setVisibility(View.VISIBLE);

        AppointmentsService service = new AppointmentsService();
        service.getPendingAppointmentsByStudent(Persistence.student.getId(), new IAppointmentsListCallback() {
            @Override
            public void onAppointmentsList(List<Appointment> appointments) {
                Date now = new Date();
                for (Appointment a : appointments) {

                    DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();
                    int res = cmp.compare(now, a.getDate());

                    if (res < 0) upcomingAppointments.add(a);
                    else if (res > 0) overdueAppointments.add(a);
                    else todayAppointments.add(a);

                    populateViews();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }, false);

        return rootView;
    }

    private void populateViews() {
        overdueAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter overdueAdapter = new AppointmentsRecyclerViewAdapter(overdueAppointments);
        overdueAppointmentsRecyclerView.setAdapter(overdueAdapter);

        todayAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter todayAdapter = new AppointmentsRecyclerViewAdapter(todayAppointments);
        todayAppointmentsRecyclerView.setAdapter(todayAdapter);

        upcomingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        AppointmentsRecyclerViewAdapter upcomingAdapter = new AppointmentsRecyclerViewAdapter(upcomingAppointments);
        upcomingAppointmentsRecyclerView.setAdapter(upcomingAdapter);

        pendingAppointmentsProgressBar.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
    }

    private void prepareViews(View rootView) {
        overdueAppointmentsRecyclerView = rootView.findViewById(R.id.overdueAppointmentsRecyclerView);
        upcomingAppointmentsRecyclerView = rootView.findViewById(R.id.upcomingAppointmentsRecyclerView);
        todayAppointmentsRecyclerView = rootView.findViewById(R.id.todayAppointmentsRecyclerView);
        contentView = rootView.findViewById(R.id.contentView);
        pendingAppointmentsProgressBar = rootView.findViewById(R.id.pendingAppointmentsProgressBar);
    }

}
