package com.example.sarthak.hms.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sarthak.hms.Constants;
import com.example.sarthak.hms.Persistence;
import com.example.sarthak.hms.R;
import com.example.sarthak.hms.adapters.CategorySpinnerAdapter;
import com.example.sarthak.hms.adapters.TitleChipsAdapter;
import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.callbacks.IComplaintCategoriesListCallback;
import com.example.sarthak.hms.callbacks.IComplaintTitleListCallback;
import com.example.sarthak.hms.callbacks.TitleChipOnClickCallback;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.ComplaintCategory;
import com.example.sarthak.hms.models.Student;
import com.example.sarthak.hms.services.ComplaintsService;

import org.joda.time.DateTimeComparator;
import org.joda.time.LocalDateTime;
import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class StudentNewComplaintActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private AppCompatEditText titleEditText;
    private AppCompatSpinner categorySpinner;
    private RecyclerView titleChips;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private List<ComplaintCategory> complaintCategories;
    private ProgressBar newComplaintProgressBar;
    private NestedScrollView nestedScrollView;
    private TitleChipsAdapter titleChipsAdapter;
    private EditText descriptionEditText;
    private RelativeLayout chooseDateRow, chooseTimeRow;
    private LinearLayout chooseFromTimeRow, chooseToTimeRow;
    private TextView date, fromTime, toTime;
    private Date chosenDate = null;
    private Date chosenFromTime = null, chosenToTime = null;
    private List<String> pictures = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_complaint);

        // Applying ActionBar
        Toolbar toolbar = findViewById(R.id.new_complaint_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(VectorDrawableCompat.create(getResources(), R.drawable.ic_clear_black_24dp, getTheme()));

        // Preparing Views
        prepareViews();

        newComplaintProgressBar.setVisibility(View.VISIBLE);
        nestedScrollView.setVisibility(View.INVISIBLE);

        ComplaintsService service = new ComplaintsService();
        service.getAllComplaintCategories(new IComplaintCategoriesListCallback() {
            @Override
            public void onComplaintCategoriesList(List<ComplaintCategory> complaintCategoriesList) {
                StudentNewComplaintActivity.this.complaintCategories = complaintCategoriesList;
                populateViews();

                nestedScrollView.setVisibility(View.VISIBLE);
                newComplaintProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(StudentNewComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                newComplaintProgressBar.setVisibility(View.GONE);
                finish();
            }
        });
    }

    private void populateViews() {
        categorySpinnerAdapter = new CategorySpinnerAdapter(StudentNewComplaintActivity.this, R.layout.category_spinner_item, complaintCategories);
        categorySpinner.setAdapter(categorySpinnerAdapter);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ComplaintsService service = new ComplaintsService();
                service.getDefaultComplaintTitles(complaintCategories.get(position).getId(), new IComplaintTitleListCallback() {
                    @Override
                    public void onTitles(List<String> titles) {
                        titleChipsAdapter.setTitles(titles);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(StudentNewComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        titleChipsAdapter.setTitles(new ArrayList<String>());
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        titleChips.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        titleChipsAdapter = new TitleChipsAdapter(this, new ArrayList<String>());
        titleChipsAdapter.setOnTitleChipClickCallback(new TitleChipOnClickCallback() {
            @Override
            public void onClick(String title) {
                titleEditText.setText(title);
                titleEditText.setError(null);
            }
        });
        titleChips.setAdapter(titleChipsAdapter);
        categorySpinner.setSelection(0);
        final LocalDateTime now = LocalDateTime.now();

        chooseDateRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(StudentNewComplaintActivity.this, StudentNewComplaintActivity.this, now.getYear(), now.getMonthOfYear() - 1, now.getDayOfMonth());
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        chooseFromTimeRow.setOnClickListener(new View.OnClickListener() {
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
                                Toast.makeText(StudentNewComplaintActivity.this, "From Time can't be after or same as To Time", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        DateTimeComparator comparator = DateTimeComparator.getInstance();
                        if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                            chosenFromTime = new Date(calendar.getTimeInMillis());

                            TypedValue val = new TypedValue();
                            getTheme().resolveAttribute(android.R.attr.textColorPrimary, val, true);
                            fromTime.setTextColor(ContextCompat.getColor(StudentNewComplaintActivity.this, val.resourceId));
                            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            fromTime.setText(timeFormat.format(chosenFromTime));
                            fromTime.setTypeface(Typeface.DEFAULT);

                            if (chosenToTime != null) {
                                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, val, true);
                                chooseTimeRow.setBackgroundResource(val.resourceId);
                            }
                        } else {
                            Toast.makeText(StudentNewComplaintActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(StudentNewComplaintActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                timePickerDialog.show();
            }
        });

        chooseToTimeRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calendar = new GregorianCalendar();
                        if (chosenDate != null) {
                            calendar.setTime(chosenDate);
                        }
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);

                        if (chosenFromTime != null) {
                            DateTimeComparator comparator = DateTimeComparator.getTimeOnlyInstance();
                            int res = comparator.compare(new Date(calendar.getTimeInMillis()), chosenFromTime);
                            if (res <= 0) {
                                Toast.makeText(StudentNewComplaintActivity.this, "To Time can't be before or same as From Time", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        DateTimeComparator comparator = DateTimeComparator.getInstance();

                        if (comparator.compare(new Date(), new Date(calendar.getTimeInMillis())) < 0) {
                            chosenToTime = new Date(calendar.getTimeInMillis());

                            TypedValue val = new TypedValue();
                            getTheme().resolveAttribute(android.R.attr.textColorPrimary, val, true);
                            toTime.setTextColor(ContextCompat.getColor(StudentNewComplaintActivity.this, val.resourceId));
                            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                            toTime.setText(timeFormat.format(chosenToTime));
                            toTime.setTypeface(Typeface.DEFAULT);

                            if (chosenFromTime != null) {
                                getTheme().resolveAttribute(android.R.attr.selectableItemBackground, val, true);
                                chooseTimeRow.setBackgroundResource(val.resourceId);
                            }
                        } else {
                            Toast.makeText(StudentNewComplaintActivity.this, "This time is in the past!", Toast.LENGTH_SHORT).show();
                        }
                    }
                };
                TimePickerDialog timePickerDialog = new TimePickerDialog(StudentNewComplaintActivity.this, timeSetListener, now.getHourOfDay(), now.getMinuteOfHour(), false);
                timePickerDialog.show();
            }
        });
    }

    private void prepareViews() {
        titleEditText = findViewById(R.id.titleEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        titleChips = findViewById(R.id.titleChips);
        newComplaintProgressBar = findViewById(R.id.newComplaintProgressBar);
        nestedScrollView = findViewById(R.id.newComplaintNsv);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        chooseDateRow = findViewById(R.id.chooseDateRow);
        chooseFromTimeRow = findViewById(R.id.chooseFromTimeRow);
        chooseToTimeRow = findViewById(R.id.chooseToTimeRow);
        chooseTimeRow = findViewById(R.id.chooseTimeRow);
        date = findViewById(R.id.date);
        fromTime = findViewById(R.id.fromTime);
        toTime = findViewById(R.id.toTime);
    }

    private boolean checkErrors() {
        boolean errors = false;

        if (TextUtils.isEmpty(titleEditText.getText())) {
            titleEditText.setError("Title can't be empty");
            errors = true;
        }

        if (TextUtils.isEmpty(descriptionEditText.getText())) {
            descriptionEditText.setError("Description can't be empty");
            errors = true;
        }

        if (chosenDate == null) {
            chooseDateRow.setBackgroundColor(Color.parseColor("#25EF4545"));
            errors = true;
        }

        if (chosenFromTime == null || chosenToTime == null) {
            chooseTimeRow.setBackgroundColor(Color.parseColor("#25EF4545"));
            errors = true;
        }

        return errors;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_new_complaint, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        } else if (id == R.id.menu_done) {
            ProgressBar pb = new ProgressBar(this, null, android.R.attr.progressBarStyle);
            pb.setIndeterminate(true);
            pb.setScaleX(0.6f);
            pb.setScaleY(0.6f);
            item.setActionView(pb);

            boolean errors = checkErrors();
            if (errors) {
                item.setActionView(null);
            } else {
                Complaint c = new Complaint();
                c.setTitle(titleEditText.getText().toString().trim());
                Student s = new Student();
                s.setId(Persistence.student.getId());
                c.setStudent(s);
                c.setComplaintCategory(categorySpinnerAdapter.getItem(categorySpinner.getSelectedItemPosition()));
                c.setDateTime(new Date());
                c.setDescription(descriptionEditText.getText().toString().trim());
                c.setAppointmentDatePreference(chosenDate);
                c.setAppointmentFromTimePreference(chosenFromTime);
                c.setAppointmentToTimePreference(chosenToTime);
                c.setPictures(pictures);

                ComplaintsService service = new ComplaintsService();
                service.createComplaint(c, new IComplaintCallback() {
                    @Override
                    public void onComplaint(Complaint complaint) {
                        Toast.makeText(StudentNewComplaintActivity.this, "Complaint Registered Successfully!", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();
                        data.putExtra(Constants.EXTRA_COMPLAINT, Parcels.wrap(complaint));
                        setResult(RESULT_OK, data);
                        item.setActionView(null);
                        finish();
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(StudentNewComplaintActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        item.setActionView(null);
                    }
                });
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        chosenDate = new Date(calendar.getTimeInMillis());
        TypedValue val = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, val, true);
        chooseDateRow.setBackgroundResource(val.resourceId);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy");
        date.setText(sdf.format(chosenDate));

        getTheme().resolveAttribute(android.R.attr.textColorPrimary, val, true);
        date.setTextColor(ContextCompat.getColor(this, val.resourceId));
        date.setTypeface(Typeface.DEFAULT);
    }
}
