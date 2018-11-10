package com.example.sarthak.hms.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.Toolbar;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.activities.ComplaintDetailActivity;
import com.example.sarthak.hms.adapters.RecentComplaintsListAdapter;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemStarClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintListCallback;
import com.example.sarthak.hms.callbacks.IStudentCallback;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.Student;
import com.example.sarthak.hms.services.ComplaintsService;
import com.example.sarthak.hms.services.StudentService;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentProfileFragment extends Fragment {

    private String rollno;
    private TextView toolbarTitle;
    private TextView nameTextView;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private ProgressBar profileProgressBar, recentComplaintsProgressBar;
    private MaterialLetterIcon studentLetterIcon;
    private TextView rollnoTextView;
    private TextView personalContactTextView;
    private TextView parentContactTextView;
    private TextView hostelTextView;
    private TextView emailTextView;
    private RecyclerView recentComplaintsList;
    private Student student;
    private RelativeLayout seeMoreRow;
    private LinearLayout recentComplaintsListLayout;
    private NavigationView navigationView;
    private List<Complaint> recentComplaints;
    private RecentComplaintsListAdapter adapter;

    public StudentProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rollno = getActivity().getIntent().getStringExtra(Constants.EXTRA_STUDENT_ROLLNO);
        View rootView = inflater.inflate(R.layout.fragment_student_profile, container, false);

        // Applying action bar
        final Toolbar toolbar = rootView.findViewById(R.id.student_profile_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Preparing Views
        prepareViews(rootView);

        profileProgressBar.setVisibility(View.VISIBLE);

        StudentService studentService = new StudentService();
        if (Persistence.student == null) {
            studentService.getStudentByRollnoAsync(rollno, new IStudentCallback() {
                @Override
                public void onStudent(Student student) {
                    StudentProfileFragment.this.student = student;
                    Persistence.student = student;
                    populateViews();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    profileProgressBar.setVisibility(View.GONE);
                }
            }, false);
        } else {
            this.student = Persistence.student;
            populateViews();
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_VIEW_COMPLAINT_DETAIL) {
            if (resultCode == Constants.RESULT_CODE_COMPLAINT_UPDATED) {
                final int complaintId = data.getIntExtra(Constants.EXTRA_COMPLAINT_ID, -1);
                if (complaintId != -1) {
                    ComplaintsService service = new ComplaintsService();
                    service.getComplaintById(complaintId, new IComplaintCallback() {
                        @Override
                        public void onComplaint(Complaint complaint) {
                            int index = -1;
                            for (int i = 0; i < recentComplaints.size(); i++) {
                                if (recentComplaints.get(i).getId() == complaintId) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                recentComplaints.set(index, complaint);
                                adapter.setComplaints(recentComplaints);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }
    }

    private void populateViews() {
        toolbarTitle.setText(student.getName());
        nameTextView.setText(student.getName());
        rollnoTextView.setText(student.getRollno());
        parentContactTextView.setText(student.getParentContact());
        personalContactTextView.setText(student.getPersonalContact());
        hostelTextView.setText("Hostel " + student.getHostel().getName() + " • " + student.getHostel().getRoomNumber());
        emailTextView.setText(student.getEmail());
        studentLetterIcon.setLetter(student.getName().charAt(0) + " ");

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = nestedScrollView.getScrollY();
                Rect scrollBounds = new Rect();
                if (nameTextView.getLocalVisibleRect(scrollBounds)) {
                    toolbarTitle.setVisibility(View.INVISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.VISIBLE);
                }

                if (scrollY == 0) {
                    appBarLayout.setElevation(0.0f);
                } else {
                    appBarLayout.setElevation(10.0f);
                }
            }
        });

        recentComplaintsProgressBar.setVisibility(View.VISIBLE);
        recentComplaintsListLayout.setVisibility(View.GONE);
        ComplaintsService complaintsService = new ComplaintsService();
        complaintsService.getComplaintsByStudentAsync(student.getId(), 3, new IComplaintListCallback() {
            @Override
            public void onComplaintsList(final List<Complaint> complaints) {
                recentComplaints = complaints;
                recentComplaintsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                adapter = new RecentComplaintsListAdapter(recentComplaints);
                adapter.setOnItemClickCallback(new IComplaintItemClickCallback() {
                    @Override
                    public void onClick(Complaint complaint) {
                        Intent intent = new Intent(getContext(), ComplaintDetailActivity.class);
                        intent.putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(complaint));
                        startActivityForResult(intent, Constants.REQUEST_CODE_VIEW_COMPLAINT_DETAIL);
                    }
                });
                adapter.setOnItemStarClickCallback(new IComplaintItemStarClickCallback() {
                    @Override
                    public void onItemStarClick(final View v, Complaint complaint) {
                        v.setClickable(false);
                        ComplaintsService service = new ComplaintsService();
                        service.updateComplaintStarStatus(complaint.getId(), !complaint.isStarred(), new IComplaintCallback() {
                            @Override
                            public void onComplaint(Complaint complaint) {
                                int index = -1;
                                for (int i = 0; i < recentComplaints.size(); i++) {
                                    if (recentComplaints.get(i).getId() == complaint.getId()) {
                                        index = i;
                                        break;
                                    }
                                }
                                if (index != -1) {
                                    recentComplaints.set(index, complaint);
                                    adapter.setComplaints(recentComplaints);
                                }
                                v.setClickable(true);
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
                                v.setClickable(true);
                            }
                        });
                    }
                });
                recentComplaintsList.setAdapter(adapter);
                seeMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigationView.setCheckedItem(R.id.nav_complaints);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new ComplaintsListFragment());
                        transaction.commit();
                    }
                });
                recentComplaintsProgressBar.setVisibility(View.GONE);
                recentComplaintsListLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(getContext(), "Recent Complaints: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                recentComplaintsProgressBar.setVisibility(View.GONE);
            }
        }, false);

        profileProgressBar.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    private void prepareViews(View rootView) {
        toolbarTitle = rootView.findViewById(R.id.profileToolbarTitle);
        nameTextView = rootView.findViewById(R.id.studentName);
        nestedScrollView = rootView.findViewById(R.id.studentProfileNsv);
        appBarLayout = rootView.findViewById(R.id.profileAppBar);
        profileProgressBar = rootView.findViewById(R.id.profileProgressBar);
        recentComplaintsProgressBar = rootView.findViewById(R.id.recentComplaintsProgressBar);
        studentLetterIcon = rootView.findViewById(R.id.studentLetterIcon);
        rollnoTextView = rootView.findViewById(R.id.rollno);
        personalContactTextView = rootView.findViewById(R.id.personalContact);
        parentContactTextView = rootView.findViewById(R.id.parentContact);
        hostelTextView = rootView.findViewById(R.id.hostel);
        emailTextView = rootView.findViewById(R.id.email);
        recentComplaintsList = rootView.findViewById(R.id.recentComplaintsList);
        seeMoreRow = rootView.findViewById(R.id.seeMoreRow);
        nestedScrollView.setVisibility(View.INVISIBLE);
        recentComplaintsListLayout = rootView.findViewById(R.id.recentComplaintsListLayout);
        navigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view);
    }

}
