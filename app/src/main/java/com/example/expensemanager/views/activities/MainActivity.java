package com.example.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import com.example.expensemanager.utils.constants;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.views.fragments.Addtrans;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
   ActivityMainBinding binding;

   Calendar calender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");

        constants.setCategories();


        calender=Calendar.getInstance();
        updateDate();
        binding.nextdate.setOnClickListener(c->{
            calender.add(Calendar.DATE,1);
            updateDate();
        });
        binding.predate.setOnClickListener(c->{
            calender.add(Calendar.DATE,-1);
            updateDate();
        });
        binding.floatingActionButton.setOnClickListener(c->{
            new Addtrans().show(getSupportFragmentManager(),null);

        });
    }
    void updateDate(){
     //   SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM YYYY");
        binding.date.setText(helper.formatedate(calender.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}