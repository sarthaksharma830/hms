package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Complaint;

import java.util.List;

public interface IComplaintCallback extends IApiCallback {
    void onComplaints(List<Complaint> complaints);
}
