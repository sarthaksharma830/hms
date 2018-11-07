package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Complaint;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IComplaintService {
    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid);

    @GET("complaints/student/{sid}")
    Call<List<Complaint>> getComplaintsByStudent(@Path("sid") int sid, @Query("len") int len);
}
