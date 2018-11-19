package com.example.sarthak.hms.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.Utils;
import com.example.sarthak.hms.adapters.ComplaintPicturesListAdapter;
import com.example.sarthak.hms.callbacks.IAppointmentsListCallback;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.models.Appointment;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.services.AppointmentsService;
import com.example.sarthak.hms.services.ComplaintsService;
import com.github.akashandroid90.imageletter.MaterialLetterIcon;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CaretakerComplaintDetailActivity extends AppCompatActivity {

    private NestedScrollView nestedScrollView;
    private AppBarLayout appBarLayout;
    private TextView complaintTitleTextView;
    private TextView toolbarTitle, complaintStatus, category, time, date, description, appointmentDate, appointmentFromTime, appointmentToTime, appointmentStatus;
    private ImageView categoryIcon;
    private RecyclerView complaintPicturesRecyclerView;
    private Complaint complaint;
    private LinearLayout noAppointmentLayout, appointmentLayout;
    private ProgressBar appointmentProgressBar;
    private List<Appointment> appointments;
    private ProgressBar picturesProgressBar;
    private TextView noComplaintPictures;
    private LinearLayout feedbackLayout;
    private TextView feedback, studentName, studentRollNumber, studentRoomNumber;
    private MaterialLetterIcon studentLetterIcon;
    private LinearLayout appointmentForm;
    private TextView chooseAppointmentDate, chooseAppointmentFromTime, chooseAppointmentToTime;
    private MaterialButton addAppointmentButton;
    private TextView prefDate, prefFromTime, prefToTime;
    private ImageView editAppointmentButton;
    private RelativeLayout editAppointmentLayout, finishEditAppointmentLayout;
    private ImageView editAppointmentCancel, editAppointmentDone;
    private SimpleDateFormat longDateFormat = new SimpleDateFormat("EEEE, MMMM d, YYYY", Locale.getDefault());
    private SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caretaker_complaint_detail);

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
        complaintTitleTextView.setText(complaint.getTitle());
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
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        time.setText(timeFormat.format(complaint.getDateTime()));
        date.setText(dateFormat.format(complaint.getDateTime()));
        description.setText(complaint.getDescription());
        studentName.setText(complaint.getStudent().getName());
        studentRollNumber.setText(complaint.getStudent().getRollno());
        studentRoomNumber.setText(complaint.getStudent().getHostel().getRoomNumber() + " • Hostel " + Persistence.caretaker.getHostel().getName());
        studentLetterIcon.setLetter((complaint.getStudent().getName().charAt(0) + "").toUpperCase());
        studentLetterIcon.setShapeColor(ContextCompat.getColor(this, Utils.getRandomColor()));
        if (complaint.getAppointmentDatePreference() != null)
            prefDate.setText(longDateFormat.format(complaint.getAppointmentDatePreference()));
        if (complaint.getAppointmentFromTimePreference() != null)
            prefFromTime.setText(timeFormat.format(complaint.getAppointmentFromTimePreference()));
        if (complaint.getAppointmentToTimePreference() != null)
            prefToTime.setText(timeFormat.format(complaint.getAppointmentToTimePreference()));

        if (complaint.getFeedback() == null || complaint.getFeedback().isEmpty()) {
            feedbackLayout.setVisibility(View.GONE);
        } else {
            feedback.setText(complaint.getFeedback());
            feedbackLayout.setVisibility(View.VISIBLE);
        }


        if (complaint.getComplaintStatus() != 0) {
            appointmentForm.setVisibility(View.GONE);
            editAppointmentLayout.setVisibility(View.GONE);
            finishEditAppointmentLayout.setVisibility(View.GONE);
            noAppointmentLayout.setVisibility(View.GONE);
            appointmentLayout.setVisibility(View.INVISIBLE);
            appointmentStatus.setVisibility(View.INVISIBLE);
            appointmentProgressBar.setVisibility(View.VISIBLE);

            fetchAppointments();

        } else {
            appointmentForm.setVisibility(View.GONE);
            editAppointmentLayout.setVisibility(View.GONE);
            finishEditAppointmentLayout.setVisibility(View.GONE);
            appointmentProgressBar.setVisibility(View.INVISIBLE);
            appointmentLayout.setVisibility(View.INVISIBLE);
            appointmentStatus.setVisibility(View.INVISIBLE);
            noAppointmentLayout.setVisibility(View.VISIBLE);

            setAddAppointmentButtonListener();
        }

        complaintPicturesRecyclerView.setVisibility(View.GONE);
        noComplaintPictures.setVisibility(View.GONE);
        picturesProgressBar.setVisibility(View.VISIBLE);

        if (complaint.getPictures() == null || complaint.getPictures().size() == 0) {
            complaintPicturesRecyclerView.setVisibility(View.GONE);
            noComplaintPictures.setVisibility(View.VISIBLE);
        } else {
            complaintPicturesRecyclerView.setLayoutManager(new GridLayoutManager(CaretakerComplaintDetailActivity.this, 4));
            ComplaintPicturesListAdapter adapter = new ComplaintPicturesListAdapter(complaint.getPictures());
            complaintPicturesRecyclerView.setAdapter(adapter);
            noComplaintPictures.setVisibility(View.GONE);
            complaintPicturesRecyclerView.setVisibility(View.VISIBLE);
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

    private void setAddAppointmentButtonListener() {
        addAppointmentButton.setOnClickListener(new View.OnClickListener() {

            private Date chosenDate = new Date(), chosenFromTime = new Date(), chosenToTime = new Date(chosenFromTime.getTime() + 1000 * 60 * 60);

            @Override
            public void onClick(View v) {
                noAppointmentLayout.setVisibility(View.GONE);
                appointmentProgressBar.setVisibility(View.INVISIBLE);
                appointmentLayout.setVisibility(View.INVISIBLE);
                appointmentForm.setVisibility(View.VISIBLE);
                finishEditAppointmentLayout.setVisibility(View.VISIBLE);

                chooseAppointmentDate.setText(longDateFormat.format(chosenDate));
                chooseAppointmentFromTime.setText(timeFormat.format(chosenFromTime));
                chooseAppointmentToTime.setText(timeFormat.format(chosenToTime));
                final LocalDateTime now = LocalDateTime.now();

                editAppointmentDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editAppointmentLayout.setVisibility(View.GONE);
                        finishEditAppointmentLayout.setVisibility(View.GONE);
                        appointmentStatus.setVisibility(View.GONE);
                        appointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentForm.setVisibility(View.INVISIBLE);
                        noAppointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentProgressBar.setVisibility(View.VISIBLE);

                        Appointment a = new Appointment();
                        Complaint c = new Complaint();
                        c.setId(complaint.getId());
                        a.setComplaint(c);
                        a.setDate(chosenDate);
                        a.setFromTime(chosenFromTime);
                        a.setToTime(chosenToTime);

                        AppointmentsService service = new AppointmentsService();
                        service.createAppointment(a, new IAppointmentsListCallback() {
                            @Override
                            public void onAppointmentsList(List<Appointment> appointments) {
                                CaretakerComplaintDetailActivity.this.appointments = appointments;
                                fetchAppointments();
                                complaint.setComplaintStatus(1);
                                setResult(Constants.RESULT_CODE_COMPLAINT_UPDATED, new Intent().putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(CaretakerComplaintDetailActivity.this.complaint)));
                                complaintStatus.setText("Scheduled");
                                complaintStatus.setTextColor(ContextCompat.getColor(CaretakerComplaintDetailActivity.this, R.color.orange));
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(CaretakerComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                appointmentProgressBar.setVisibility(View.GONE);
                                noAppointmentLayout.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                editAppointmentCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appointmentForm.setVisibility(View.GONE);
                        editAppointmentLayout.setVisibility(View.GONE);
                        finishEditAppointmentLayout.setVisibility(View.GONE);
                        appointmentProgressBar.setVisibility(View.INVISIBLE);
                        appointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentStatus.setVisibility(View.INVISIBLE);
                        noAppointmentLayout.setVisibility(View.VISIBLE);
                    }
                });

                chooseAppointmentDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(CaretakerComplaintDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                chosenDate = new Date(calendar.getTimeInMillis());
                                chooseAppointmentDate.setText(longDateFormat.format(chosenDate));
                            }
                        }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                chooseAppointmentFromTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                if (chosenDate != null) {
                                    calendar.setTime(chosenDate);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                if (chosenToTime != null) {
                                    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
                                    int res = comparator.compare(new Date(calendar.getTimeInMillis()), chosenToTime);
                                    if (res >= 0) {
                                        Toast.makeText(CaretakerComplaintDetailActivity.this, "From Time can't be after or same as To Time", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                DateTimeComparator comparator = DateTimeComparator.getInstance();
                                if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                                    chosenFromTime = new Date(calendar.getTimeInMillis());
                                    chooseAppointmentFromTime.setText(timeFormat.format(chosenFromTime));
                                } else {
                                    Toast.makeText(CaretakerComplaintDetailActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CaretakerComplaintDetailActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                        timePickerDialog.show();
                    }
                });

                chooseAppointmentToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                if (chosenDate != null) {
                                    calendar.setTime(chosenDate);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                if (chosenFromTime != null) {
                                    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
                                    int res = comparator.compare(new Date(calendar.getTimeInMillis()), chosenFromTime);
                                    if (res <= 0) {
                                        Toast.makeText(CaretakerComplaintDetailActivity.this, "To Time can't be before or same as From Time", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                DateTimeComparator comparator = DateTimeComparator.getInstance();
                                if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                                    chosenToTime = new Date(calendar.getTimeInMillis());
                                    chooseAppointmentToTime.setText(timeFormat.format(chosenToTime));
                                } else {
                                    Toast.makeText(CaretakerComplaintDetailActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CaretakerComplaintDetailActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                        timePickerDialog.show();
                    }
                });
            }
        });
    }

    private void fetchAppointments() {
        if (this.appointments != null && this.appointments.size() > 0) {
            populateAppointmentView();
        } else {
            AppointmentsService service = new AppointmentsService();
            service.getAppointmentsByComplaint(complaint.getId(), new IAppointmentsListCallback() {
                @Override
                public void onAppointmentsList(List<Appointment> appointmentsList) {
                    CaretakerComplaintDetailActivity.this.appointments = appointmentsList;
                    populateAppointmentView();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(CaretakerComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    editAppointmentLayout.setVisibility(View.GONE);
                    finishEditAppointmentLayout.setVisibility(View.GONE);
                    noAppointmentLayout.setVisibility(View.GONE);
                    appointmentProgressBar.setVisibility(View.INVISIBLE);
                    appointmentStatus.setVisibility(View.INVISIBLE);
                    appointmentLayout.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    private void populateAppointmentView() {
        if (appointments != null && appointments.size() > 0) {
            SimpleDateFormat appDateFormat = new SimpleDateFormat("EEEE, MMMM d, YYYY", Locale.getDefault());
            final SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            appointmentDate.setText(appDateFormat.format(appointments.get(0).getDate()));
            appointmentFromTime.setText(timeFormat.format(appointments.get(0).getFromTime()));
            appointmentToTime.setText(timeFormat.format(appointments.get(0).getToTime()));

            if (appointments.get(0).getStatus()) {
                appointmentStatus.setText("Completed");
                appointmentStatus.setTextColor(ContextCompat.getColor(CaretakerComplaintDetailActivity.this, R.color.green));
                editAppointmentLayout.setVisibility(View.GONE);
            } else {
                appointmentStatus.setText("Pending");
                appointmentStatus.setTextColor(ContextCompat.getColor(CaretakerComplaintDetailActivity.this, R.color.red));
                editAppointmentLayout.setVisibility(View.VISIBLE);
            }

            appointmentForm.setVisibility(View.GONE);
            noAppointmentLayout.setVisibility(View.GONE);
            finishEditAppointmentLayout.setVisibility(View.GONE);
            appointmentLayout.setVisibility(View.VISIBLE);
            appointmentStatus.setVisibility(View.VISIBLE);

            setEditAppointmentButtonListener();
        } else {
            appointmentForm.setVisibility(View.GONE);
            editAppointmentLayout.setVisibility(View.GONE);
            finishEditAppointmentLayout.setVisibility(View.GONE);
            appointmentLayout.setVisibility(View.INVISIBLE);
            appointmentStatus.setVisibility(View.INVISIBLE);
            noAppointmentLayout.setVisibility(View.VISIBLE);

            setAddAppointmentButtonListener();
        }
        appointmentProgressBar.setVisibility(View.INVISIBLE);
    }

    private void setEditAppointmentButtonListener() {
        editAppointmentButton.setOnClickListener(new View.OnClickListener() {

            private Date chosenDate = new Date(), chosenFromTime = new Date(), chosenToTime = new Date(chosenFromTime.getTime() + 1000 * 60 * 60);

            @Override
            public void onClick(View v) {
                noAppointmentLayout.setVisibility(View.GONE);
                editAppointmentLayout.setVisibility(View.GONE);
                appointmentProgressBar.setVisibility(View.INVISIBLE);
                appointmentLayout.setVisibility(View.INVISIBLE);
                appointmentForm.setVisibility(View.VISIBLE);
                finishEditAppointmentLayout.setVisibility(View.VISIBLE);

                chooseAppointmentDate.setText(longDateFormat.format(chosenDate));
                chooseAppointmentFromTime.setText(timeFormat.format(chosenFromTime));
                chooseAppointmentToTime.setText(timeFormat.format(chosenToTime));
                final LocalDateTime now = LocalDateTime.now();

                editAppointmentDone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editAppointmentLayout.setVisibility(View.GONE);
                        finishEditAppointmentLayout.setVisibility(View.GONE);
                        appointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentForm.setVisibility(View.INVISIBLE);
                        noAppointmentLayout.setVisibility(View.INVISIBLE);
                        appointmentStatus.setVisibility(View.GONE);
                        appointmentProgressBar.setVisibility(View.VISIBLE);

                        Appointment a = new Appointment();
                        a.setId(appointments.get(0).getId());
                        a.setDate(chosenDate);
                        a.setFromTime(chosenFromTime);
                        a.setToTime(chosenToTime);

                        AppointmentsService service = new AppointmentsService();
                        service.updateAppointment(a, new IAppointmentsListCallback() {
                            @Override
                            public void onAppointmentsList(List<Appointment> appointments) {
                                CaretakerComplaintDetailActivity.this.appointments = appointments;
                                fetchAppointments();
                                complaint.setComplaintStatus(1);
                                setResult(Constants.RESULT_CODE_COMPLAINT_UPDATED, new Intent().putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(CaretakerComplaintDetailActivity.this.complaint)));
                                complaintStatus.setText("Scheduled");
                                complaintStatus.setTextColor(ContextCompat.getColor(CaretakerComplaintDetailActivity.this, R.color.orange));
                            }

                            @Override
                            public void onError(Exception e) {
                                Toast.makeText(CaretakerComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                appointmentProgressBar.setVisibility(View.GONE);
                                editAppointmentLayout.setVisibility(View.VISIBLE);
                                appointmentLayout.setVisibility(View.VISIBLE);
                                appointmentStatus.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });

                editAppointmentCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        appointmentForm.setVisibility(View.GONE);
                        editAppointmentLayout.setVisibility(View.VISIBLE);
                        finishEditAppointmentLayout.setVisibility(View.GONE);
                        appointmentProgressBar.setVisibility(View.INVISIBLE);
                        appointmentLayout.setVisibility(View.VISIBLE);
                        appointmentStatus.setVisibility(View.VISIBLE);
                        noAppointmentLayout.setVisibility(View.GONE);
                    }
                });

                chooseAppointmentDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog datePickerDialog = new DatePickerDialog(CaretakerComplaintDetailActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                chosenDate = new Date(calendar.getTimeInMillis());
                                chooseAppointmentDate.setText(longDateFormat.format(chosenDate));
                            }
                        }, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                    }
                });

                chooseAppointmentFromTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                if (chosenDate != null) {
                                    calendar.setTime(chosenDate);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                if (chosenToTime != null) {
                                    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
                                    int res = comparator.compare(new Date(calendar.getTimeInMillis()), chosenToTime);
                                    if (res >= 0) {
                                        Toast.makeText(CaretakerComplaintDetailActivity.this, "From Time can't be after or same as To Time", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                DateTimeComparator comparator = DateTimeComparator.getInstance();
                                if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                                    chosenFromTime = new Date(calendar.getTimeInMillis());
                                    chooseAppointmentFromTime.setText(timeFormat.format(chosenFromTime));
                                } else {
                                    Toast.makeText(CaretakerComplaintDetailActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CaretakerComplaintDetailActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                        timePickerDialog.show();
                    }
                });

                chooseAppointmentToTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                if (chosenDate != null) {
                                    calendar.setTime(chosenDate);
                                }
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);

                                if (chosenFromTime != null) {
                                    DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
                                    int res = comparator.compare(new Date(calendar.getTimeInMillis()), chosenFromTime);
                                    if (res <= 0) {
                                        Toast.makeText(CaretakerComplaintDetailActivity.this, "To Time can't be before or same as From Time", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }

                                DateTimeComparator comparator = DateTimeComparator.getInstance();
                                if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                                    chosenToTime = new Date(calendar.getTimeInMillis());
                                    chooseAppointmentToTime.setText(timeFormat.format(chosenToTime));
                                } else {
                                    Toast.makeText(CaretakerComplaintDetailActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        };
                        TimePickerDialog timePickerDialog = new TimePickerDialog(CaretakerComplaintDetailActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                        timePickerDialog.show();
                    }
                });
            }
        });
    }

    private void prepareViews() {
        nestedScrollView = findViewById(R.id.complaintDetailNsv);
        appBarLayout = findViewById(R.id.complaintDetailAppBar);
        complaintTitleTextView = findViewById(R.id.complaintTitle);
        toolbarTitle = findViewById(R.id.complaintDetailToolbarTitle);
        complaintStatus = findViewById(R.id.complaintStatus);
        category = findViewById(R.id.category);
        categoryIcon = findViewById(R.id.categoryIcon);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
        complaintPicturesRecyclerView = findViewById(R.id.complaintPicturesRecyclerView);
        appointmentLayout = findViewById(R.id.appointmentLayout);
        appointmentForm = findViewById(R.id.appointmentForm);
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
        studentName = findViewById(R.id.studentName);
        studentRollNumber = findViewById(R.id.studentRollNumber);
        studentRoomNumber = findViewById(R.id.studentRoomNumber);
        studentLetterIcon = findViewById(R.id.studentLetterIcon);
        chooseAppointmentDate = findViewById(R.id.chooseAppointmentDate);
        chooseAppointmentFromTime = findViewById(R.id.chooseAppointmentFromTime);
        chooseAppointmentToTime = findViewById(R.id.chooseAppointmentToTime);
        addAppointmentButton = findViewById(R.id.addAppointmentButton);
        prefDate = findViewById(R.id.prefDate);
        prefFromTime = findViewById(R.id.fromTime);
        prefToTime = findViewById(R.id.toTime);
        editAppointmentButton = findViewById(R.id.editAppointmentButton);
        editAppointmentLayout = findViewById(R.id.editAppointmentLayout);
        finishEditAppointmentLayout = findViewById(R.id.finishEditAppointmentLayout);
        editAppointmentDone = findViewById(R.id.editAppointmentDone);
        editAppointmentCancel = findViewById(R.id.editAppointmentCancel);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (this.complaint != null) {
            menu.findItem(R.id.menu_mark_as_resolved).setVisible(complaint.getComplaintStatus() != 2);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_caretaker_complaint_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            super.onBackPressed();
        } else if (item.getItemId() == R.id.menu_mark_as_resolved) {
            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyle);
            pb.setIndeterminate(true);
            pb.setScaleX(0.6f);
            pb.setScaleY(0.6f);
            item.setActionView(pb);

            ComplaintsService service = new ComplaintsService();
            service.markComplaintAsResolved(complaint.getId(), new IComplaintCallback() {
                @Override
                public void onComplaint(Complaint complaint) {
                    Toast.makeText(CaretakerComplaintDetailActivity.this, "Complaint Resolved!", Toast.LENGTH_SHORT).show();
                    CaretakerComplaintDetailActivity.this.complaint = complaint;
                    setResult(Constants.RESULT_CODE_COMPLAINT_UPDATED, new Intent().putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(CaretakerComplaintDetailActivity.this.complaint)));
                    item.setActionView(null);
                    populateViews();
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(CaretakerComplaintDetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    item.setActionView(null);
                }
            });

        }

        return super.onOptionsItemSelected(item);
    }
}
