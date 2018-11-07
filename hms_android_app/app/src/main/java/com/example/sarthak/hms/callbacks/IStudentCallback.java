package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Student;

public interface IStudentCallback extends IApiCallback {
    void onStudent(Student student);
}
