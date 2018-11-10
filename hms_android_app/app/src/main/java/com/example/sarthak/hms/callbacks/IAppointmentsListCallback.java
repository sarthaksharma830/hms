package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Appointment;

import java.util.List;

public interface IAppointmentsListCallback extends IApiCallback {
    void onAppointmentsList(List<Appointment> appointments);
}
