package com.example.sarthak.hms.callbacks;

import java.util.List;

public interface IComplaintTitleListCallback extends IApiCallback {
    void onTitles(List<String> titles);
}
