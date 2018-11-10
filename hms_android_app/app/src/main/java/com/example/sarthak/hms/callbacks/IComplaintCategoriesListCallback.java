package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.ComplaintCategory;

import java.util.List;

public interface IComplaintCategoriesListCallback extends IApiCallback {
    void onComplaintCategoriesList(List<ComplaintCategory> complaintCategoriesList);
}
