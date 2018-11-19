package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Complaint;
import com.example.sarthak.hms.models.ComplaintCategory;
import com.example.sarthak.hms.models.ComplaintPicture;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IComplaintsService {
    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid);

    @GET("complaints/caretaker/{cid}")
    Call<List<Complaint>> getComplaintsByCaretaker(@Path("cid") int cid);

    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid, @Query("len") int len);

    @GET("complaints/caretaker/{cid}")
    Call<List<Complaint>> getComplaintsByCaretaker(@Path("cid") int cid, @Query("len") int len);

    @GET("complaints/{id}")
    Call<Complaint> getComplaintById(@Path("id") int id);

    @PUT("complaints/star/{cid}/{star}")
    Call<Complaint> updateComplaintStarStatus(@Path("cid") int cid, @Path("star") int star);

    @GET("complaints/categories")
    Call<List<ComplaintCategory>> getAllComplaintCategories();

    @GET("complaints/titles/{id}")
    Call<List<String>> getDefaultComplaintTitles(@Path("id") int id);

    @POST("complaints")
    Call<Complaint> createComplaint(@Body Complaint complaint);

    @POST("complaints/pictures/{cid}")
    Call<List<String>> uploadComplaintPictures(@Body List<ComplaintPicture> pictures, @Path("cid") int cid);

    @PUT("complaints/resolve/{id}")
    Call<Complaint> markComplaintAsResolved(@Path("id") int id);
}
