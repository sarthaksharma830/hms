package com.example.sarthak.hms.callbacks;

import java.util.List;

public interface IComplaintPicturesListCallback extends IApiCallback {
    void onPictures(List<String> pictures);
}
