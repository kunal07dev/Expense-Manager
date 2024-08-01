package com.example.expensemanager.views.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.expensemanager.R;
import com.example.expensemanager.adapter.transcationadpt;
import com.example.expensemanager.databinding.FragmentTransBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.viewModels.Mainviewmodel;
import com.example.expensemanager.views.activities.MainActivity;
import com.google.android.material.tabs.TabLayout;
import java.util.Calendar;
import io.realm.RealmResults;

public class TransFragment extends Fragment {

    public TransFragment() {
        // Required empty public constructor
    }

    public static int selectedTab = 0;
    Calendar calendar;
    public Mainviewmodel viewModel;
    FragmentTransBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTransBinding.inflate(inflater);
        calendar = Calendar.getInstance();
        viewModel = ((MainActivity) getActivity()).viewModel; // Initialize viewModel

        updateDate();
        binding.nextdate.setOnClickListener(c -> {
            if (selectedTab == 0) {
                calendar.add(Calendar.DATE, 1);
            } else if (selectedTab == 1) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.predate.setOnClickListener(c -> {
            if (selectedTab == 0) {
                calendar.add(Calendar.DATE, -1);
            } else if (selectedTab == 1) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });

        binding.floatingActionButton.setOnClickListener(c -> {
            new Addtrans().show(getParentFragmentManager(), null);
        });

        binding.recycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    selectedTab = 1;
                } else if (tab.getText().equals("Daily")) {
                    selectedTab = 0;
                }
                updateDate();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselected if needed
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselected if needed
            }
        });

        viewModel.transaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transcation>>() {
            @Override
            public void onChanged(RealmResults<Transcation> transactions) {
                transcationadpt adapter = new transcationadpt(getContext(), transactions);
                binding.recycle.setAdapter(adapter);
                if (transactions.size() > 0) {
                    binding.empty2.setVisibility(View.GONE);
                } else {
                    binding.empty2.setVisibility(View.VISIBLE);
                }
            }
        });
        viewModel.gettrans(calendar);

        viewModel.total.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.totalrate.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalExpense.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.expenserate.setText(String.valueOf(aDouble));
            }
        });
        viewModel.totalincome.observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                binding.incomerate.setText(String.valueOf(aDouble));
            }
        });

        return binding.getRoot();
    }

    void updateDate() {
        if (selectedTab == 0) {
            binding.date.setText(helper.formatedate(calendar.getTime()));
        } else if (selectedTab == 1) {
            binding.date.setText(helper.formatedateByMonth(calendar.getTime()));
        }
        viewModel.gettrans(calendar);
    }
}