package com.example.expensemanager.models;

import io.realm.RealmObject;

public class account  {
    public double accountamount;
    public String accountName;
    public account(){

    }

    public account(double accountamount, String accountName) {
        this.accountamount = accountamount;
        this.accountName = accountName;
    }

    public double getaccountamount() {
        return accountamount;
    }

    public void setAccountamount(double accountamount) {
        this.accountamount = accountamount;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }
}
