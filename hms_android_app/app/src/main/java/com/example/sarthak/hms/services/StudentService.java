package com.example.sarthak.hms.services;

import android.os.Handler;

import com.example.sarthak.hms.callbacks.IStudentCallback;
import com.example.sarthak.hms.models.*;
import com.example.sarthak.hms.retrofit.IStudentsService;
import com.example.sarthak.hms.retrofit.RetrofitProvider;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class StudentService {

    private Student getDummyStudent() {
        Student s = new Student();
        s.setId(1);
        s.setName("Sarthak Sharma");
        s.setRollno("101783037");
        s.setEmail("ssharma60_be17@thapar.edu");
        s.setGender('M');
        s.setParentContact("+918288007880");
        s.setPersonalContact("+916280611919");
        Hostel h = new Hostel();
        h.setId(1);
        h.setName("M");
        h.setRoomNumber("D 627");
        h.setType("Boys");
        s.setHostel(h);
        return s;
    }

    public void getStudentByRollnoAsync(String rollno, final IStudentCallback callback) {
        Retrofit retrofit = RetrofitProvider.newInstance();
        IStudentsService studentService = retrofit.create(IStudentsService.class);
        studentService.getStudentByRollno(rollno).enqueue(new Callback<Student>() {
            @Override
            public void onResponse(Call<Student> call, Response<Student> response) {
                if (response.errorBody() != null) {
                    callback.onError(new Exception(response.message()));
                } else {
                    callback.onStudent(response.body());
                }
            }

            @Override
            public void onFailure(Call<Student> call, Throwable t) {
                callback.onError(new Exception(t.toString()));
            }
        });
    }

    public void getStudentByRollnoAsync(String rollno, final IStudentCallback callback, boolean offline) {
        if (offline) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onStudent(getDummyStudent());
                }
            }, 2000);
        } else getStudentByRollnoAsync(rollno, callback);
    }

}
