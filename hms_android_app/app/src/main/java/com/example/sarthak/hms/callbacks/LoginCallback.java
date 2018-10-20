package com.example.sarthak.hms.callbacks;

public interface LoginCallback {
    void onLogin(boolean result);
    void onError(Exception e);
}
