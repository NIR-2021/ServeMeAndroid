package com.example.serveme_m1.Models;

import org.joda.time.DateTime;

import java.util.Date;

public class VendorModel {




    protected int vendorId ;
    protected String vendorName ;
    protected String vendorEmail ;
    protected String vendorPhone ;
    protected String vendorAddress ;
    protected String isActive ;

    public VendorModel() {
    }

    public VendorModel(int vendorId, String vendorName, String vendorEmail, String vendorPhone, String vendorAddress, String isActive, String isDeleted, DateTime date) {
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorEmail = vendorEmail;
        this.vendorPhone = vendorPhone;
        this.vendorAddress = vendorAddress;
        this.isActive = isActive;
        this.isDeleted = isDeleted;
        this.date = date;
    }

    protected String isDeleted ;
    protected DateTime date ;

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getVendorPhone() {
        return vendorPhone;
    }

    public void setVendorPhone(String vendorPhone) {
        this.vendorPhone = vendorPhone;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public DateTime getDate() {
        return date;
    }

    public void setDate(DateTime date) {
        this.date = date;
    }
}
