package com.example.serveme_m1.Models;

public class CategioryModel {

    int categoryId;
    String name;

    public CategioryModel() {
    }

    public CategioryModel(int categoryId, String name) {
        this.categoryId = categoryId;
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategioryModel{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }
}
