package com.example.expensemanager.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class helper {
    public static  String formatedate(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM yyyy");
       return  dateFormat.format(date);
    }

    public static  String formatedateByMonth(Date date){
        SimpleDateFormat dateFormat=new SimpleDateFormat("MMMM yyyy");
        return  dateFormat.format(date);
    }

}
