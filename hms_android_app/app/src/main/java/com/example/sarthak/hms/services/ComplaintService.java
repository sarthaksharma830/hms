package com.example.sarthak.hms.services;

import android.os.Handler;
import android.util.Log;

import com.example.sarthak.hms.callbacks.IComplaintCallback;
import com.example.sarthak.hms.models.*;
import com.example.sarthak.hms.retrofit.IComplaintService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComplaintService {

    private List<Complaint> getDummyComplaints() {
        List<Complaint> complaints = new ArrayList<>();
        try {
            Student s = new Student();
            s.setId(1);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");

            Complaint c = new Complaint();
            c.setId(5);
            c.setTitle("Water tap leaking");
            c.setStudent(s);
            ComplaintCategory cc = new ComplaintCategory();
            cc.setId(2);
            cc.setName("Plumber");
            cc.setCode("plumb");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-24T08:45:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-10-24T00:00:00"));
            c.setAppointmentFromTimePreference(stf.parse("12:15:00"));
            c.setAppointmentToTimePreference(stf.parse("13:00:00"));
            c.setDescription("The tap has been leaking since 6 in the morning.");
            c.setFeedback("");
            c.setComplaintStatus(ComplaintStatus.Pending);
            c.setStarred(true);
            complaints.add(c);

            c = new Complaint();
            c.setId(4);
            c.setTitle("Washing machine not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(3);
            cc.setName("Housekeeping");
            cc.setCode("house");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-20T19:15:00"));
            c.setAppointmentDatePreference(null);
            c.setAppointmentFromTimePreference(null);
            c.setAppointmentToTimePreference(null);
            c.setDescription("When I put my clothes in, the machine was working fine. But when I went to take them out after about an hour, the clothes were completely dry and it looked like the machine didn't run at all.");
            c.setFeedback("");
            c.setComplaintStatus(ComplaintStatus.Scheduled);
            c.setStarred(false);
            complaints.add(c);

            c = new Complaint();
            c.setId(3);
            c.setTitle("Door handle broken");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(1);
            cc.setName("Carpenter");
            cc.setCode("carp");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-10-04T15:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-01-06T00:10:00"));
            c.setAppointmentFromTimePreference(stf.parse("10:00:00"));
            c.setAppointmentToTimePreference(stf.parse("11:00:00"));
            c.setDescription("The door handle is jammed. Tried to oil it but didn't work out.");
            c.setFeedback("The handle has been fixed now");
            c.setComplaintStatus(ComplaintStatus.Resolved);
            c.setStarred(false);
            complaints.add(c);

            c = new Complaint();
            c.setId(2);
            c.setTitle("Router not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(4);
            cc.setName("Technical Support");
            cc.setCode("tech");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-09-15T12:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-09-16T00:09:00"));
            c.setAppointmentFromTimePreference(stf.parse("12:00:00"));
            c.setAppointmentToTimePreference(stf.parse("13:00:00"));
            c.setDescription("The Wifi disconnects within 5 minutes when I try to connect. This happens every single time. Mostly it shows \"disconnected by admin\", but it even disconnects without any message sometimes, even when using the Network Client on Windows. Updated the network drivers as suggest, but the problem still persists.");
            c.setFeedback("");
            c.setComplaintStatus(ComplaintStatus.Scheduled);
            c.setStarred(true);
            complaints.add(c);

            c = new Complaint();
            c.setId(1);
            c.setTitle("AC not working");
            c.setStudent(s);
            cc = new ComplaintCategory();
            cc.setId(5);
            cc.setName("Electrician");
            cc.setCode("elec");
            c.setComplaintCategory(cc);
            c.setDateTime(sdf.parse("2018-08-29T11:00:00"));
            c.setAppointmentDatePreference(sdf.parse("2018-08-30T00:08:00"));
            c.setAppointmentFromTimePreference(stf.parse("13:00:00"));
            c.setAppointmentToTimePreference(stf.parse("14:00:00"));
            c.setDescription("The indicator lights on the AC start blinking whenever I turn it on, and then they go off in a few minutes.");
            c.setFeedback("Everything works fine now");
            c.setComplaintStatus(ComplaintStatus.Resolved);
            c.setStarred(false);
            complaints.add(c);
        } catch (ParseException e) {
            Log.e("HMS", e.getMessage());
        }

        return complaints;
    }

    public void getComplaintsByStudentAsync(int sid, final IComplaintCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintService service = retrofit.create(IComplaintService.class);
        service.getComplaintsByStudent(sid).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                callback.onComplaints(response.body());
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                callback.onError(new Exception("Network Error"));
            }
        });
    }

    public void getComplaintsByStudentAsync(int sid, final IComplaintCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onComplaints(getDummyComplaints());
                }
            }, 2000);
        } else {
            getComplaintsByStudentAsync(sid, callback);
        }
    }

    public void getComplaintsByStudentAsync(int sid, int len, final IComplaintCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IComplaintService service = retrofit.create(IComplaintService.class);
        service.getComplaintsByStudent(sid, len).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                callback.onComplaints(response.body());
            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable t) {
                callback.onError(new Exception("Network Error"));
            }
        });
    }

    public void getComplaintsByStudentAsync(int sid, final int len, final IComplaintCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onComplaints(getDummyComplaints().subList(0, len));
                }
            }, 2000);
        } else {
            getComplaintsByStudentAsync(sid, len, callback);
        }
    }

}
