package com.example.sarthak.hms.fragments;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.ViewPagerAdapter;
import com.example.sarthak.hms.callbacks.IStudentCallback;
import com.example.sarthak.hms.models.Student;
import com.example.sarthak.hms.services.StudentsService;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentAppointmentsFragment extends Fragment {


    private TabLayout tabs;
    private ViewPager viewPager;

    public StudentAppointmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_appointments, container, false);

        // Applying ActionBar
        Toolbar toolbar = rootView.findViewById(R.id.appointments_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Preparing Views
        prepareViews(rootView);

        if (Persistence.student != null) {
            populateViews();
        } else {
            StudentsService service = new StudentsService();
            service.getStudentByRollnoAsync(getActivity().getIntent().getStringExtra(Constants.EXTRA_STUDENT_ROLLNO), new IStudentCallback() {
                @Override
                public void onStudent(Student student) {
                    Persistence.student = student;
                    populateViews();
                }

                @Override
                public void onError(Exception e) {

                }
            }, false);
        }

        return rootView;
    }

    private void populateViews() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new StudentPendingAppointmentsFragment(), "Pending");
        adapter.addFragment(new StudentCompletedAppointmentsFragment(), "Completed");
        viewPager.setAdapter(adapter);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).select();
    }

    private void prepareViews(View rootView) {
        tabs = rootView.findViewById(R.id.tabs);
        viewPager = rootView.findViewById(R.id.viewPager);
    }

}
