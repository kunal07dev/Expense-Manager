package com.example.expensemanager.views.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.utils.constants;
import com.example.expensemanager.viewModels.Mainviewmodel;
import com.example.expensemanager.views.fragments.BlankFragment;
import com.example.expensemanager.views.fragments.accountsFragment;
import com.example.expensemanager.views.fragments.StatsFragment;
import com.example.expensemanager.views.fragments.TransFragment;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    public static int selectedTab = 0;
    public static int selectedTab_stats = 0;

    Calendar calendar;
    public Mainviewmodel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setTitle("Transactions");

        viewModel = new ViewModelProvider(this).get(Mainviewmodel.class);

        constants.setCategories();

        calendar = Calendar.getInstance();
        if (savedInstanceState == null) {
            loadFragment(new TransFragment());
        }

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                String title = "";

                if (item.getItemId() == R.id.Trans) {
                    fragment = new TransFragment();
                    title = "Transactions";
                } else if (item.getItemId() == R.id.stats) {
                    fragment = new StatsFragment();
                    title = "Statistics";
                } else if (item.getItemId() == R.id.account) {
                    fragment = new accountsFragment();
                    title = "Accounts";
                }
                else if (item.getItemId() == R.id.limit) {
                    fragment = new BlankFragment();
                    title = "Limit";
                }

                if (fragment != null) {
                    replaceFragment(fragment, title);
                    return true;
                }
                return false;
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.commitAllowingStateLoss();
    }

    public void getTransaction() {
        viewModel.gettrans(calendar);
    }

    private void replaceFragment(Fragment fragment, String title) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, fragment);
        transaction.addToBackStack(null);
        transaction.commitAllowingStateLoss();
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.topmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}
