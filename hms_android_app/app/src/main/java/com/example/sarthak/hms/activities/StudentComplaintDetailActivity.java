package com.example.sarthak.hms.activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.ComplaintPicturesListAdapter;
import com.example.sarthak.hms.callbacks.IAppointmentsListCallback;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.models.Appointment;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.services.AppointmentsService;
import com.example.sarthak.hms.services.ComplaintsService;

import org.joda.time.DateTimeComparator;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class StudentComplaintDetailActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private TextView complaintTitleTextView;
    private TextView toolbarTitle, complaintTitle, complaintStatus, category, time, date, description, appointmentDate, appointmentFromTime, appointmentToTime, appointmentStatus;
    private ImageView categoryIcon;
    private RecyclerView complaintPicturesRecyclerView;
    private Complaint complaint;
    private LinearLayout noAppointmentLayout, appointmentLayout;
    private ProgressBar appointmentProgressBar;
    private List<Appointment> appointments;
    private ProgressBar picturesProgressBar;
    private TextView noComplaintPictures;
    private LinearLayout feedbackLayout;
    private TextView feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_complaint_detail);

        // Applying ActionBar
        final Toolbar toolbar = findViewById(R.id.complaint_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        prepareViews();

        complaint = Parcels.unwrap(getIntent().getParcelableExtra(Constants.EXTRA_COMPLAINT));

        populateViews();
    }

    private void populateViews() {

        toolbarTitle.setText(complaint.getTitle());
        complaintTitle.setText(complaint.getTitle());
        switch (complaint.getComplaintStatus()) {
            case 0:
                complaintStatus.setText("Pending");
                complaintStatus.setTextColor(ContextCompat.getColor(this, R.color.red));
                break;
            case 1:
                complaintStatus.setText("Scheduled");
                complaintStatus.setTextColor(ContextCompat.getColor(this, R.color.orange));
                break;
            case 2:
                complaintStatus.setText("Resolved");
                complaintStatus.setTextColor(ContextCompat.getColor(this, R.color.green));
                break;
        }
        category.setText(complaint.getComplaintCategory().getName());
        int categoryRes = 0;
        switch (complaint.getComplaintCategory().getCode()) {
            case "elec":
                categoryRes = R.drawable.ic_socket;
                break;
            case "house":
                categoryRes = R.drawable.ic_clean;
                break;
            case "tech":
                categoryRes = R.drawable.ic_server;
                break;
            case "carp":
                categoryRes = R.drawable.ic_saw;
                break;
            case "plumb":
                categoryRes = R.drawable.ic_pipe;
                break;
        }
        categoryIcon.setImageDrawable(VectorDrawableCompat.create(getResources(), categoryRes, getTheme()));
        final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        time.setText(timeFormat.format(complaint.getDateTime()));
        date.setText(dateFormat.format(complaint.getDateTime()));
        description.setText(complaint.getDescription());

        if (complaint.getFeedback() == null || complaint.getFeedback().isEmpty()) {
            feedbackLayout.setVisibility(View.GONE);
        } else {
            feedback.setText(complaint.getFeedback());
            feedbackLayout.setVisibility(View.VISIBLE);
        }

        invalidateOptionsMenu();

        if (complaint.getComplaintStatus() != 0) {
            appointmentProgressBar.setVisibility(View.VISIBLE);
            appointmentLayout.setVisibility(View.INVISIBLE);
            noAppointmentLayout.setVisibility(View.INVISIBLE);
            appointmentStatus.setVisibility(View.INVISIBLE);

            AppointmentsService service = new AppointmentsService();
            service.getAppointmentsByComplaint(complaint.getId(), new IAppointmentsListCallback() {
                @Override
                public void onAppointmentsList(List<Appointment> appointmentsList) {
                    StudentComplaintDetailActivity.this.appointments = appointmentsList;
                    if (appointments.size() > 0) {
                        SimpleDateFormat appDateFormat = new SimpleDateFormat("EEEE, MMMM d, YYYY");
                        appointmentDate.setText(appDateFormat.format(appointments.get(0).getDate()));
                        appointmentFromTime.setText(timeFormat.format(appointments.get(0).getFromTime()));
                        appointmentToTime.setText(timeFormat.format(appointments.get(0).getToTime()));

                        if (appointments.get(0).getStatus()) {
                            appointmentStatus.setText("Completed");
                            appointmentStatus.setTextColor(ContextCompat.getColor(StudentComplaintDetailActivity.this, R.color.green));
                        } else {
                            Date now = new Date();
                            DateTimeComparator cmp = DateTimeComparator.getDateOnlyInstance();
                            int res = cmp.compare(now, appointments.get(0).getDate());

                            if (res < 0) {
                                appointmentStatus.setText("Upcoming");
                                appointmentStatus.setTextColor(ContextCompat.getColor(StudentComplaintDetailActivity.this, R.color.orange));
                            } else if (res > 0) {
                                appointmentStatus.setText("Overdue");
                                appointmentStatus.setTextColor(ContextCompat.getColor(StudentComplaintDetailActivity.this, R.color.red));
                            } else {
                                appointmentStatus.setText("Today");
                                appointmentStatus.setTextColor(ContextCompat.getColor(StudentComplaintDetailActivity.this, R.color.blue));
                            }
                        }

                        appointmentLayout.setVisibility(View.VISIBLE);
                        appointmentStatus.setVisibility(View.VISIBLE);
                        noAppointmentLayout.setVisibility(View.INVISIBLE);
                    } else {
                        noAppointmentLayout.setVisibility(View.VISIBLE);
                        appointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentStatus.setVisibility(View.INVISIBLE);
                    }
                    appointmentProgressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(StudentComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    appointmentProgressBar.setVisibility(View.INVISIBLE);
                    appointmentStatus.setVisibility(View.INVISIBLE);
                    appointmentLayout.setVisibility(View.INVISIBLE);
                    noAppointmentLayout.setVisibility(View.INVISIBLE);
                }
            });
        } else {
            appointmentProgressBar.setVisibility(View.INVISIBLE);
            appointmentLayout.setVisibility(View.INVISIBLE);
            appointmentStatus.setVisibility(View.INVISIBLE);
            noAppointmentLayout.setVisibility(View.VISIBLE);
        }

        complaintPicturesRecyclerView.setVisibility(View.GONE);
        noComplaintPictures.setVisibility(View.GONE);
        picturesProgressBar.setVisibility(View.VISIBLE);

        if (complaint.getPictures() == null || complaint.getPictures().size() == 0) {
            complaintPicturesRecyclerView.setVisibility(View.GONE);
            noComplaintPictures.setVisibility(View.VISIBLE);
        } else {
            complaintPicturesRecyclerView.setLayoutManager(new GridLayoutManager(StudentComplaintDetailActivity.this, 4));
            ComplaintPicturesListAdapter adapter = new ComplaintPicturesListAdapter(complaint.getPictures());
            complaintPicturesRecyclerView.setAdapter(adapter);
            complaintPicturesRecyclerView.setVisibility(View.VISIBLE);
            noComplaintPictures.setVisibility(View.GONE);
        }

        picturesProgressBar.setVisibility(View.GONE);

        nestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = nestedScrollView.getScrollY();
                if (scrollY == 0) {
                    appBarLayout.setElevation(0.0f);
                } else {
                    appBarLayout.setElevation(10.0f);
                }
                Rect scrollBounds = new Rect();
                if (complaintTitleTextView.getLocalVisibleRect(scrollBounds)) {
                    toolbarTitle.setVisibility(View.INVISIBLE);
                } else {
                    toolbarTitle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void prepareViews() {
        nestedScrollView = findViewById(R.id.complaintDetailNsv);
        appBarLayout = findViewById(R.id.complaintDetailAppBar);
        complaintTitleTextView = findViewById(R.id.complaintTitle);
        toolbarTitle = findViewById(R.id.complaintDetailToolbarTitle);
        complaintStatus = findViewById(R.id.complaintStatus);
        complaintTitle = findViewById(R.id.complaintTitle);
        category = findViewById(R.id.category);
        categoryIcon = findViewById(R.id.categoryIcon);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        complaintPicturesRecyclerView = findViewById(R.id.complaintPicturesRecyclerView);
        appointmentLayout = findViewById(R.id.appointmentLayout);
        noAppointmentLayout = findViewById(R.id.noAppointmentLayout);
        appointmentProgressBar = findViewById(R.id.appointmentProgressBar);
        appointmentDate = findViewById(R.id.appointmentDate);
        appointmentFromTime = findViewById(R.id.appointmentFromTime);
        appointmentToTime = findViewById(R.id.appointmentToTime);
        appointmentStatus = findViewById(R.id.appointmentStatus);
        picturesProgressBar = findViewById(R.id.picturesProgressBar);
        noComplaintPictures = findViewById(R.id.noComplaintPictures);
        feedbackLayout = findViewById(R.id.feedbackLayout);
        feedback = findViewById(R.id.feedback);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.complaint != null) {
            menu.findItem(R.id.star).setIcon(VectorDrawableCompat.create(getResources(), complaint.isStarred() ? R.drawable.ic_star_filled : R.drawable.ic_star_border, getTheme()));
            menu.findItem(R.id.feedback).setVisible(((this.complaint.getFeedback() == null) || this.complaint.getFeedback().isEmpty()) && (this.complaint.getComplaintStatus() == 2));
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_student_complaint_detail, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.star) {
            setResult(Constants.RESULT_CODE_COMPLAINT_UPDATED, new Intent().putExtra(Constants.EXTRA_COMPLAINT_ID, this.complaint.getId()));
            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyle);
            pb.setIndeterminate(true);
            pb.setScaleX(0.6f);
            pb.setScaleY(0.6f);
            item.setActionView(pb);

            ComplaintsService service = new ComplaintsService();
            service.updateComplaintStarStatus(complaint.getId(), !this.complaint.isStarred(), new IComplaintCallback() {
                @Override
                public void onComplaint(Complaint complaint) {
                    StudentComplaintDetailActivity.this.complaint = complaint;
                    item.setActionView(null);
                    invalidateOptionsMenu();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(StudentComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    invalidateOptionsMenu();
                }
            });

        } else if (item.getItemId() == R.id.feedback) {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
