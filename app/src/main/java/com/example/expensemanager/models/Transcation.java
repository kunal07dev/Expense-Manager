package com.example.expensemanager.models;

import java.util.Date;

public class Transcation {
    private String type ,category,note;
    private Date date;
    private double ammount;
    private long id;

    public Transcation() {
    }

    public Transcation(String type, String category, String note, Date date, double ammount, long id) {
        this.type = type;
        this.category = category;
        this.note = note;
        this.date = date;
        this.ammount = ammount;
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmmount() {
        return ammount;
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
