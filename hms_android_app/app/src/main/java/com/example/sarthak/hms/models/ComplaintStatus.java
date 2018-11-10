package com.example.sarthak.hms.models;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel
public enum ComplaintStatus {
    Pending(0),
    Scheduled(1),
    Resolved(2);

    private int val;

    @ParcelConstructor
    ComplaintStatus(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
