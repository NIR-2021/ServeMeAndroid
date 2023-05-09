package com.example.serveme_m1.Models;

import java.util.Date;

public class ServiceModel {

    public int serviceId;
    public String description ;
    public String id;
    public int vendorId ;
    public float bid;
    public int categoryId ;
    public String status ;


//details of the vendor in case the request is accepted.
    public  VendorModel vendor ;
//Details of the category the service belongs to.
    public  CategioryModel category ;

    public ServiceModel() {
    }

    public ServiceModel(int serviceId, String description, String id, int vendorId, float bid, int categoryId, String status, VendorModel vendor, CategioryModel category) {
        this.serviceId = serviceId;
        this.description = description;
        this.id = id;
        this.vendorId = vendorId;
        this.bid = bid;
        this.categoryId = categoryId;
        this.status = status;
        this.vendor = vendor;
        this.category = category;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public float getBid() {
        return bid;
    }

    public void setBid(float bid) {
        this.bid = bid;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public VendorModel getVendor() {
        return vendor;
    }

    public void setVendor(VendorModel vendor) {
        this.vendor = vendor;
    }

    public CategioryModel getCategory() {
        return category;
    }

    public void setCategory(CategioryModel category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ServiceModel{" +
                "serviceId=" + serviceId +
                ", description='" + description + '\'' +
                ", id='" + id + '\'' +
                ", vendorId=" + vendorId +
                ", bid=" + bid +
                ", categoryId=" + categoryId +
                ", status='" + status + '\'' +
                ", vendor=" + vendor +
                ", category=" + category +
                '}';
    }
}
