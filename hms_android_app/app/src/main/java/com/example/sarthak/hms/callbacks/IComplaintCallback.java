package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Complaint;

public interface IComplaintCallback extends IApiCallback {
    void onComplaint(Complaint complaint);
}
