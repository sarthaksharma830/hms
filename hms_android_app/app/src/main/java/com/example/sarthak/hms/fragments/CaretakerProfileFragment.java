package com.example.sarthak.hms.fragments;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.Utils;
import com.example.sarthak.hms.activities.CaretakerComplaintDetailActivity;
import com.example.sarthak.hms.activities.StudentComplaintDetailActivity;
import com.example.sarthak.hms.adapters.CaretakerComplaintsListAdapter;
import com.example.sarthak.hms.adapters.RecentComplaintsListAdapter;
import com.example.sarthak.hms.callbacks.ICaretakerCallback;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintItemStarClickCallback;
import com.example.sarthak.hms.callbacks.IComplaintListCallback;
import com.example.sarthak.hms.models.Caretaker;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.services.CaretakersService;
import com.example.sarthak.hms.services.ComplaintsService;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import org.joda.time.DateTimeComparator;
import org.parceler.Parcels;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class CaretakerProfileFragment extends Fragment {

    private String username;
    private TextView toolbarTitle;
    private TextView nameTextView;
    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private ProgressBar profileProgressBar, recentComplaintsProgressBar;
    private MaterialLetterIcon caretakerLetterIcon;
    private TextView usernameTextView;
    private TextView contactTextView;
    private TextView hostelTextView;
    private TextView emailTextView;
    private RecyclerView recentComplaintsList;
    private Caretaker caretaker;
    private RelativeLayout seeMoreRow;
    private LinearLayout recentComplaintsListLayout;
    private NavigationView navigationView;
    private List<Complaint> recentComplaints;
    private CaretakerComplaintsListAdapter adapter;

    public CaretakerProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        username = getActivity().getIntent().getStringExtra(Constants.EXTRA_CARETAKER_USERNAME);
        View rootView = inflater.inflate(R.layout.fragment_caretaker_profile, container, false);

        // Applying action bar
        final Toolbar toolbar = rootView.findViewById(R.id.caretaker_profile_toolbar);
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
        CaretakersService caretakersService = new CaretakersService();
        if (Persistence.caretaker == null) {
            caretakersService.getCaretakerInfo(username, new ICaretakerCallback() {
                @Override
                public void onCaretaker(Caretaker caretaker) {
                    CaretakerProfileFragment.this.caretaker = caretaker;
                    Persistence.caretaker = caretaker;
                    populateViews();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    profileProgressBar.setVisibility(View.GONE);
                }
            });
        } else {
            this.caretaker = Persistence.caretaker;
            populateViews();
        }

        return rootView;
    }

    private void populateViews() {
        toolbarTitle.setText(caretaker.getName());
        nameTextView.setText(caretaker.getName());
        usernameTextView.setText(caretaker.getUsername());
        contactTextView.setText(caretaker.getContact());
        hostelTextView.setText("Hostel " + caretaker.getHostel().getName());
        emailTextView.setText(caretaker.getEmail());
        caretakerLetterIcon.setLetter((caretaker.getName().charAt(0) + " ").toUpperCase());
        caretakerLetterIcon.setShapeColor(ContextCompat.getColor(getContext(), Utils.getRandomColor()));

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
        complaintsService.getComplaintsByCaretaker(caretaker.getId(), 3, new IComplaintListCallback() {
            @Override
            public void onComplaintsList(final List<Complaint> complaints) {
                recentComplaints = complaints;
                recentComplaintsList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                adapter = new CaretakerComplaintsListAdapter(recentComplaints);
                adapter.setOnItemClickCallback(new IComplaintItemClickCallback() {
                    @Override
                    public void onClick(Complaint complaint) {
                        Intent intent = new Intent(getContext(), CaretakerComplaintDetailActivity.class);
                        intent.putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(complaint));
                        startActivityForResult(intent, Constants.REQUEST_CODE_VIEW_COMPLAINT_DETAIL);
                    }
                });
                recentComplaintsList.setAdapter(adapter);
                seeMoreRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        navigationView.setCheckedItem(R.id.nav_complaints);
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        transaction.replace(R.id.fragment_container, new CaretakerComplaintsListFragment());
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
        });

        profileProgressBar.setVisibility(View.INVISIBLE);
        nestedScrollView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_VIEW_COMPLAINT_DETAIL) {
            if (resultCode == Constants.RESULT_CODE_COMPLAINT_UPDATED) {
                Complaint c = Parcels.unwrap(data.getParcelableExtra(Constants.EXTRA_COMPLAINT));
                if (c != null) {
                    int complaintId = c.getId();
                    int index = -1;
                    for (int i = 0; i < recentComplaints.size(); i++) {
                        if (recentComplaints.get(i).getId() == complaintId) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        recentComplaints.set(index, c);
                        Collections.sort(recentComplaints, new Comparator<Complaint>() {
                            @Override
                            public int compare(Complaint o1, Complaint o2) {
                                if (o1.getComplaintStatus() != o2.getComplaintStatus()) {
                                    return o1.getComplaintStatus() - o2.getComplaintStatus();
                                } else {
                                    DateTimeComparator comparator = DateTimeComparator.getDateOnlyInstance();
                                    return comparator.compare(o2.getDateTime(), o1.getDateTime());
                                }
                            }
                        });
                        adapter.setComplaints(recentComplaints);
                    }
                }
            }
        }

    }

    private void prepareViews(View rootView) {
        toolbarTitle = rootView.findViewById(R.id.profileToolbarTitle);
        nameTextView = rootView.findViewById(R.id.caretakerName);
        nestedScrollView = rootView.findViewById(R.id.caretakerProfileNsv);
        appBarLayout = rootView.findViewById(R.id.profileAppBar);
        profileProgressBar = rootView.findViewById(R.id.profileProgressBar);
        recentComplaintsProgressBar = rootView.findViewById(R.id.recentComplaintsProgressBar);
        caretakerLetterIcon = rootView.findViewById(R.id.caretakerLetterIcon);
        usernameTextView = rootView.findViewById(R.id.username);
        contactTextView = rootView.findViewById(R.id.contact);
        hostelTextView = rootView.findViewById(R.id.hostel);
        emailTextView = rootView.findViewById(R.id.email);
        recentComplaintsList = rootView.findViewById(R.id.recentComplaintsList);
        seeMoreRow = rootView.findViewById(R.id.seeMoreRow);
        nestedScrollView.setVisibility(View.INVISIBLE);
        recentComplaintsListLayout = rootView.findViewById(R.id.recentComplaintsListLayout);
        navigationView = Objects.requireNonNull(getActivity()).findViewById(R.id.nav_view);
    }

}
