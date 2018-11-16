package com.example.sarthak.hms.callbacks;

import com.example.sarthak.hms.models.Caretaker;

public interface ICaretakerCallback extends IApiCallback {
    void onCaretaker(Caretaker caretaker);
}
