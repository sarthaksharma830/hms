package com.example.sarthak.hms.models;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ComplaintPicture {

    String data;
    String fileName;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
