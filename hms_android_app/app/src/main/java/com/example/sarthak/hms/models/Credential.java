package com.example.sarthak.hms.models;

public class Credential {
    private String rollno;

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

    private String password;

    public Credential() {}

    public Credential(String rollno, String password) {
        this.rollno = rollno;
        this.password = password;
    }
}
