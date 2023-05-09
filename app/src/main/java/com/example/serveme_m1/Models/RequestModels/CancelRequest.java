package com.example.serveme_m1.Models.RequestModels;

public class CancelRequest
{
    String id;
    int serviceId;

    public CancelRequest() {
    }

    public CancelRequest(String id, int serviceId) {
        this.id = id;
        this.serviceId = serviceId;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
}
