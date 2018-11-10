package com.example.sarthak.hms.retrofit;

import com.example.sarthak.hms.models.Student;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IStudentsService {
    @GET("students/{rollno}")
    Call<Student> getStudentByRollno(@Path("rollno") String rollno);
}
