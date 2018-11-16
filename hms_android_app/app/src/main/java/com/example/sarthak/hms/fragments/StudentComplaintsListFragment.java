package com.example.sarthak.hms.fragments;


import android.app.Activity;
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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.activities.StudentComplaintDetailActivity;
import com.example.sarthak.hms.activities.StudentNewComplaintActivity;
import com.example.sarthak.hms.adapters.RecentComplaintsListAdapter;
import com.example.sarthak.hms.callbacks.IComplaintItemClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemStarClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintListCallback;
import com.example.sarthak.hms.callbacks.IStudentCallback;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.Student;
import com.example.sarthak.hms.services.ComplaintsService;
import com.example.sarthak.hms.services.StudentsService;

import org.joda.time.DateTimeComparator;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentComplaintsListFragment extends Fragment {


    private RecyclerView complaintsListRecyclerView;
    private FloatingActionButton newComplaintFab;
    private ProgressBar complaintsListProgressBar;
    private AppBarLayout appBarLayout;
    private List<Complaint> complaints;
    private RecentComplaintsListAdapter adapter;

    public StudentComplaintsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_student_complaints_list, container, false);

        // Applying ActionBar
        Toolbar toolbar = rootView.findViewById(R.id.complaints_list_toolbar);
        DrawerLayout drawer = getActivity().findViewById(R.id.drawer_layout);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Preparing Views
        prepareViews(rootView);

        complaintsListRecyclerView.setVisibility(View.GONE);
        newComplaintFab.hide();
        complaintsListProgressBar.setVisibility(View.VISIBLE);

        if (Persistence.student == null) {
            StudentsService studentsService = new StudentsService();
            studentsService.getStudentByRollnoAsync(getActivity().getIntent().getStringExtra(Constants.EXTRA_STUDENT_ROLLNO), new IStudentCallback() {
                @Override
                public void onStudent(Student student) {
                    Persistence.student = student;
                    ComplaintsService complaintsService = new ComplaintsService();
                    complaintsService.getComplaintsByStudentAsync(Persistence.student.getId(), new IComplaintListCallback() {
                        @Override
                        public void onComplaintsList(List<Complaint> complaints) {
                            StudentComplaintsListFragment.this.complaints = complaints;
                            populateViews();
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            complaintsListProgressBar.setVisibility(View.GONE);
                        }
                    }, false);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }, true);
        } else {
            ComplaintsService complaintsService = new ComplaintsService();
            complaintsService.getComplaintsByStudentAsync(Persistence.student.getId(), new IComplaintListCallback() {
                @Override
                public void onComplaintsList(List<Complaint> complaints) {
                    StudentComplaintsListFragment.this.complaints = complaints;
                    populateViews();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    complaintsListProgressBar.setVisibility(View.GONE);
                }
            }, false);
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
                            for (int i = 0; i < complaints.size(); i++) {
                                if (complaints.get(i).getId() == complaintId) {
                                    index = i;
                                    break;
                                }
                            }
                            if (index != -1) {
                                complaints.set(index, complaint);
                                adapter.setComplaints(complaints);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } else if (requestCode == Constants.REQUEST_CODE_NEW_COMPLAINT) {
            if (resultCode == Activity.RESULT_OK) {
                Complaint c = Parcels.unwrap(data.getParcelableExtra(Constants.EXTRA_COMPLAINT));
                complaints.add(c);
                Collections.sort(complaints, new Comparator<Complaint>() {
                    @Override
                    public int compare(Complaint o1, Complaint o2) {
                        if (o1.isStarred() && !o2.isStarred()) {
                            return -1;
                        } else if (!o1.isStarred() && o2.isStarred()) {
                            return 1;
                        } else {
                            DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
                            return comparator.compare(o2.getDateTime(), o1.getDateTime());
                        }
                    }
                });
                adapter.setComplaints(complaints);
            }
        }
    }

    private void populateViews() {
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        complaintsListRecyclerView.setLayoutManager(layoutManager);
        adapter = new RecentComplaintsListAdapter(complaints);
        adapter.setOnItemClickCallback(new IComplaintItemClickCallback() {
            @Override
            public void onClick(Complaint complaint) {
                Intent intent = new Intent(getContext(), StudentComplaintDetailActivity.class);
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
                        for (int i = 0; i < complaints.size(); i++) {
                            if (complaints.get(i).getId() == complaint.getId()) {
                                index = i;
                                break;
                            }
                        }
                        if (index != -1) {
                            complaints.set(index, complaint);
                            adapter.setComplaints(complaints);
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
        complaintsListRecyclerView.setAdapter(adapter);

        newComplaintFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getContext(), StudentNewComplaintActivity.class), Constants.REQUEST_CODE_NEW_COMPLAINT);
            }
        });
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

        complaintsListProgressBar.setVisibility(View.GONE);
        complaintsListRecyclerView.setVisibility(View.VISIBLE);
        newComplaintFab.show();
    }

    private void prepareViews(View rootView) {
        complaintsListRecyclerView = rootView.findViewById(R.id.complaintsListRecyclerView);
        newComplaintFab = rootView.findViewById(R.id.newComplaint);
        complaintsListProgressBar = rootView.findViewById(R.id.complaintsListProgressBar);
        appBarLayout = rootView.findViewById(R.id.complaintsListAppBar);
    }

}
