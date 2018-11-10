package com.example.sarthak.hms.callbacks;

import android.view.View;

import com.example.sarthak.hms.models.Complaint;

public interface IComplaintItemStarClickCallback {
    void onItemStarClick(View v, Complaint complaint);
}
