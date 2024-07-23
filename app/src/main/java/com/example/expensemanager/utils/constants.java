package com.example.expensemanager.utils;

import com.example.expensemanager.R;
import com.example.expensemanager.models.category;

import java.util.ArrayList;

public class constants {
    public static ArrayList<category> categories;
    public static void setCategories() {
        categories=new ArrayList<>();
        categories.add(new category("Buisness", R.drawable.briefcase));
        categories.add(new category("Party", R.drawable.dance));
        categories.add(new category("Education", R.drawable.education));
        categories.add(new category("Food", R.drawable.fast_food));
        categories.add(new category("Transport", R.drawable.train));
        categories.add(new category("Rent", R.drawable.house));
        categories.add(new category("Beauty", R.drawable.makeup_pouch));
        categories.add(new category("Health", R.drawable.healthcare));
        categories.add(new category("Gift", R.drawable.gift));
    }
}
