package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.ComplaintPicture;

import java.util.List;

public interface IComplaintPicturesListCallback extends IApiCallback {
    void onComplaintPicturesList(List<ComplaintPicture> complaintPictures);
}
