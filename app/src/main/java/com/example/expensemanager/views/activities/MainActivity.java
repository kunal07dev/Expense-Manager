package com.example.expensemanager.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.example.expensemanager.adapter.transcationadpt;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.constants;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.viewModels.Mainviewmodel;
import com.example.expensemanager.views.fragments.Addtrans;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static int selectedTab=0;

    Calendar calendar;
  public  Mainviewmodel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");

        viewModel = new ViewModelProvider(this).get(Mainviewmodel.class);

        // Initialize categories
        constants.setCategories();


        calendar = Calendar.getInstance();
        updateDate();

        binding.nextdate.setOnClickListener(c -> {
            if(selectedTab==0){
                calendar.add(Calendar.DATE, 1);
            }
            else if(selectedTab==1){
                calendar.add(Calendar.MONTH, 1);
            }

            updateDate();
        });

        binding.predate.setOnClickListener(c -> {
            if(selectedTab==0){
                calendar.add(Calendar.DATE, -1);
            }
            else if(selectedTab==1){
                calendar.add(Calendar.MONTH, -1);
            }

            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new Addtrans().show(getSupportFragmentManager(), null);
        });


        binding.recycle.setLayoutManager(new LinearLayoutManager(this));
        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                                        @Override
                                                        public void onTabSelected(TabLayout.Tab tab) {

                                                            if(tab.getText().equals("Monthly")){
                                                                selectedTab=1;
                                                                updateDate();
                                                            }
                                                            else if(tab.getText().equals("Daily")){
                                                                selectedTab=0;
                                                                updateDate();
                                                            }

                                                        }

                                                        @Override
                                                        public void onTabUnselected(TabLayout.Tab tab) {

                                                        }

                                                        @Override
                                                        public void onTabReselected(TabLayout.Tab tab) {

                                                        }
        });

        viewModel.transaction.observe(this, new Observer<RealmResults<Transcation>>() {
            @Override
            public void onChanged(RealmResults<Transcation> transcations) {
                transcationadpt adapter = new transcationadpt(MainActivity.this, transcations);
                binding.recycle.setAdapter(adapter);
              if(transcations.size()>0) {
                  binding.empty.setVisibility(View.GONE);
              }
              else{
                  binding.empty.setVisibility(View.VISIBLE);
              }
            }
        });
        viewModel.gettrans(calendar);

        viewModel.total.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalrate.setText(String.valueOf(aDouble));

            }
        });
        viewModel.totalExpense.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenserate.setText(String.valueOf(aDouble));

            }
        });
        viewModel.totalincome.observe(this, new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomerate.setText(String.valueOf(aDouble));

            }
        });

    }





    void updateDate() {
        if(selectedTab==0) {
            binding.date.setText(helper.formatedate(calendar.getTime()));
        }
        else if(selectedTab==1){
            binding.date.setText(helper.formatedateByMonth(calendar.getTime()));
        }

        viewModel.gettrans(calendar);
    }
public  void getTransaction(){
        viewModel.gettrans(calendar);
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}