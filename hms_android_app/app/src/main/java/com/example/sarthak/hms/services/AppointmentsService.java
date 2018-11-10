package com.example.sarthak.hms.services;

import android.os.Handler;

import com.example.sarthak.hms.callbacks.IAppointmentsListCallback;
import com.example.sarthak.hms.models.Appointment;
import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.ComplaintCategory;
import com.example.sarthak.hms.retrofit.IAppointmentsService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AppointmentsService {

    private List<Appointment> getDummyAppointments(boolean status) {
        List<Appointment> appointments = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        SimpleDateFormat stf = new SimpleDateFormat("hh:mm:ss");

        try {
            Appointment a = new Appointment();
            a.setId(3);
            Complaint c = new Complaint();
            c.setId(3);
            c.setTitle("Door Handle Broken");
            ComplaintCategory cc = new ComplaintCategory();
            cc.setId(1);
            cc.setName("Carpenter");
            cc.setCode("carp");
            c.setComplaintCategory(cc);
            a.setComplaint(c);
            a.setDate(sdf.parse("2018-10-06T00:00:00"));
            a.setFromTime(stf.parse("10:00:00"));
            a.setToTime(stf.parse("11:00:00"));
            a.setStatus(status);
            appointments.add(a);

            a = new Appointment();
            a.setId(2);
            c = new Complaint();
            c.setId(2);
            c.setTitle("Router not working");
            cc = new ComplaintCategory();
            cc.setId(4);
            cc.setName("Technical Support");
            cc.setCode("tech");
            c.setComplaintCategory(cc);
            a.setComplaint(c);
            a.setDate(sdf.parse("2018-09-16T00:00:00"));
            a.setFromTime(stf.parse("12:00:00"));
            a.setToTime(stf.parse("13:00:00"));
            a.setStatus(status);
            appointments.add(a);

            a = new Appointment();
            a.setId(1);
            c = new Complaint();
            c.setId(1);
            c.setTitle("AC not working");
            cc = new ComplaintCategory();
            cc.setId(5);
            cc.setName("Electrician");
            cc.setCode("elec");
            c.setComplaintCategory(cc);
            a.setComplaint(c);
            a.setDate(sdf.parse("2018-08-30T00:00:00"));
            a.setFromTime(stf.parse("13:00:00"));
            a.setToTime(stf.parse("14:00:00"));
            a.setStatus(status);
            appointments.add(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appointments;
    }

    public void getPendingAppointmentsByStudent(int sid, final IAppointmentsListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IAppointmentsService service = retrofit.create(IAppointmentsService.class);
        service.getPendingAppointmentsByStudent(sid).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onAppointmentsList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getCompletedAppointmentsByStudent(int sid, final IAppointmentsListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IAppointmentsService service = retrofit.create(IAppointmentsService.class);
        service.getCompletedAppointmentsByStudent(sid).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onAppointmentsList(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getAppointmentsByComplaint(int cid, final IAppointmentsListCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IAppointmentsService service = retrofit.create(IAppointmentsService.class);
        service.getAppointmentsByComplaint(cid).enqueue(new Callback<List<Appointment>>() {
            @Override
            public void onResponse(Call<List<Appointment>> call, Response<List<Appointment>> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onAppointmentsList(response.body() != null ? response.body() : new ArrayList<Appointment>());
                }
            }

            @Override
            public void onFailure(Call<List<Appointment>> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getPendingAppointmentsByStudent(int sid, final IAppointmentsListCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onAppointmentsList(getDummyAppointments(false));
                }
            }, 2000);
        } else {
            getPendingAppointmentsByStudent(sid, callback);
        }
    }

    public void getCompletedAppointmentsByStudent(int sid, final IAppointmentsListCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onAppointmentsList(getDummyAppointments(true));
                }
            }, 2000);
        } else {
            getCompletedAppointmentsByStudent(sid, callback);
        }
    }

}
