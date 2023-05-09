package com.example.serveme_m1.Models.ResponseModels;

import java.util.ArrayList;

public class ResponseTemp<T> {
    String message ;
    ArrayList<T> data;


    public ResponseTemp() {}

    public ResponseTemp(String message, ArrayList<T> data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
