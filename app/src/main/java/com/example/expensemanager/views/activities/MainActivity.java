package com.example.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;

import com.example.expensemanager.adapter.transcationadpt;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.constants;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.views.fragments.Addtrans;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");

        // Initialize categories
        constants.setCategories();

        calendar = Calendar.getInstance();
        updateDate();

        binding.nextdate.setOnClickListener(c -> {
            calendar.add(Calendar.DATE, 1);
            updateDate();
        });

        binding.predate.setOnClickListener(c -> {
            calendar.add(Calendar.DATE, -1);
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new Addtrans().show(getSupportFragmentManager(), null);
        });

        ArrayList<Transcation> transactions = new ArrayList<>();
        transactions.add(new Transcation("Income", "Business", "Cash", "Some note here", new Date(), 5000, 2));
       transactions.add(new Transcation("Expense", "Rent", "Cash", "Some note here", new Date(), 4000, 3));
        transactions.add(new Transcation("Expense", "Party", "Online", "Some note here", new Date(), 2000, 4));
        transactions.add(new Transcation("Income", "Other", "Cash", "Some note here", new Date(), 5000, 6));

        transcationadpt adapter = new transcationadpt(this, transactions);
        binding.recycle.setLayoutManager(new LinearLayoutManager(this));
        binding.recycle.setAdapter(adapter);
    }

    void updateDate() {
        binding.date.setText(helper.formatedate(calendar.getTime()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
