package com.example.sarthak.hms.models;

import org.parceler.Parcel;

@Parcel
public class Credential {
    String rollno;

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    String password;

    public Credential() {}

    public Credential(String rollno, String password) {
        this.rollno = rollno;
        this.password = password;
    }
}
