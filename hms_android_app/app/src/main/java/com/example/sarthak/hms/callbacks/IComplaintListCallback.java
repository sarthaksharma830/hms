package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Complaint;

import java.util.List;

public interface IComplaintListCallback extends IApiCallback {
    void onComplaintsList(List<Complaint> complaints);
}
