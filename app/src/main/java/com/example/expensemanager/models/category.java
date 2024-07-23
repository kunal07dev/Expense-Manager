package com.example.expensemanager.models;

public class category {
    private  String catName;
    private int  catimg;

    public category() {
    }

    public category(String catName, int catimg) {
        this.catName = catName;
        this.catimg = catimg;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatimg() {
        return catimg;
    }

    public void setCatimg(int catimg) {
        this.catimg = catimg;
    }
}
