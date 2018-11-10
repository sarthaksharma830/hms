package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Appointment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IAppointmentsService {
    @GET("appointments/student/{sid}/p")
    Call<List<Appointment>> getPendingAppointmentsByStudent(@Path("sid") int sid);

    @GET("appointments/student/{sid}/c")
    Call<List<Appointment>> getCompletedAppointmentsByStudent(@Path("sid") int sid);

    @GET("appointments/complaint/{cid}")
    Call<List<Appointment>> getAppointmentsByComplaint(@Path("cid") int cid);
}
