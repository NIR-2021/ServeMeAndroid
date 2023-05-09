package com.example.serveme_m1.Models.RequestModels;

public class GetServiceRequest {
    String id;

    public GetServiceRequest() {
    }

    public GetServiceRequest(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
