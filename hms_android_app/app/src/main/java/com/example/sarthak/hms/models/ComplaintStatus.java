package com.example.sarthak.hms.models;

public enum ComplaintStatus {
    Pending(0),
    Scheduled(1),
    Resolved(2);

    private int val;

    ComplaintStatus(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }
}
