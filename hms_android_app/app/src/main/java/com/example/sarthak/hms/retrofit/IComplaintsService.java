package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.ComplaintCategory;
import com.example.sarthak.hms.models.ComplaintPicture;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IComplaintsService {
    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid);

    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid, @Query("len") int len);

    @GET("complaints/{id}")
    Call<Complaint> getComplaintById(@Path("id") int id);

    @PUT("complaints/star/{cid}/{star}")
    Call<Complaint> updateComplaintStarStatus(@Path("cid") int cid, @Path("star") int star);

    @GET("complaints/pictures/{id}")
    Call<List<ComplaintPicture>> getComplaintPictures(@Path("id") int id);

    @GET("complaints/categories")
    Call<List<ComplaintCategory>> getAllComplaintCategories();

    @GET("complaints/titles/{id}")
    Call<List<String>> getDefaultComplaintTitles(@Path("id") int id);
}
