package com.example.expensemanager.views.fragments;

import static com.anychart.enums.LegendLayout.HORIZONTAL;
import static com.example.expensemanager.views.activities.MainActivity.selectedTab_stats;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentStatsBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.viewModels.Mainviewmodel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

public class StatsFragment extends Fragment {

    private FragmentStatsBinding binding;
    private Calendar calendar;
    public Mainviewmodel viewModel;
    public static String selectedtype = "Income";
    private Pie pie;

    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentStatsBinding.inflate(inflater, container, false);

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(Mainviewmodel.class);

        // Initialize calendar
        calendar = Calendar.getInstance();

        // Setup chart
        setupChart();

        // Setup observers
        setupObservers();

        // Setup button click listeners
        setupButtonListeners();

        // Setup tab layout listener
        setupTabLayoutListener();

        // Setup date navigation listeners
        setupDateNavigationListeners();

        // Restore selected states if exists
        restoreSelectedStates();

        // Initialize date and fetch transactions
        updateDate();

        return binding.getRoot();
    }

    private void setupChart() {
        AnyChartView anyChartView = binding.anychart;
        pie = AnyChart.pie();
        anyChartView.setChart(pie);
        pie.background().fill("#DCEEFD");
        pie.labels().position("outside");
        pie.legend().position("center-bottom").itemsLayout(HORIZONTAL).align(Align.CENTER);
    }

    private void setupObservers() {
        viewModel.catetransaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transcation>>() {
            @Override
            public void onChanged(RealmResults<Transcation> transactions) {
                if (transactions != null && transactions.size() > 0) {
                    updateChart(transactions);
                } else {
                    binding.anychart.setVisibility(View.GONE);
                    binding.empty2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateChart(RealmResults<Transcation> transactions) {
        List<DataEntry> data = new ArrayList<>();
        binding.empty2.setVisibility(View.GONE);
        binding.anychart.setVisibility(View.VISIBLE);
        Map<String, Double> categoryMap = new HashMap<>();
        for (Transcation transaction : transactions) {
            String category = transaction.getCategory();
            double amount = transaction.getAmmount();
            if (categoryMap.containsKey(category)) {
                double currentTotal = categoryMap.get(category);
                currentTotal += Math.abs(amount);
                categoryMap.put(category, currentTotal);
            } else {
                categoryMap.put(category, Math.abs(amount));
            }
        }
        for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
            data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
        }
        pie.data(data);
    }

    private void setupButtonListeners() {
        binding.ibtn.setOnClickListener(v -> {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.income_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ibtn.setTextColor(getContext().getColor(R.color.green));
            selectedtype = "Income";
            updateDate();
        });

        binding.ebtn.setOnClickListener(v -> {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.expense_sel));
            binding.ibtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ebtn.setTextColor(getContext().getColor(R.color.red));
            selectedtype = "Expense";
            updateDate();
        });
    }

    private void setupTabLayoutListener() {
        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ("Monthly".equals(tab.getText())) {
                    selectedTab_stats = 1;
                } else if ("Daily".equals(tab.getText())) {
                    selectedTab_stats = 0;
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
    }

    private void setupDateNavigationListeners() {
        binding.nextdate.setOnClickListener(c -> {
            if (selectedTab_stats == 0) {
                calendar.add(Calendar.DATE, 1);
            } else if (selectedTab_stats == 1) {
                calendar.add(Calendar.MONTH, 1);
            }
            Log.d("StatsFragment", "Calendar after next: " + calendar.getTime());
            updateDate();
        });

        binding.predate.setOnClickListener(c -> {
            if (selectedTab_stats == 0) {
                calendar.add(Calendar.DATE, -1);
            } else if (selectedTab_stats == 1) {
                calendar.add(Calendar.MONTH, -1);
            }
            Log.d("StatsFragment", "Calendar after previous: " + calendar.getTime());
            updateDate();
        });
    }

    private void updateDate() {
        if (selectedTab_stats == 0) {
            binding.date.setText(helper.formatedate(calendar.getTime()));
        } else if (selectedTab_stats == 1) {
            binding.date.setText(helper.formatedateByMonth(calendar.getTime()));
        }
        Log.d("StatsFragment", "Updating date: " + calendar.getTime());
        viewModel.gettrans(calendar, selectedtype);
    }

    private void restoreSelectedStates() {
        // Restore selected tab state
        TabLayout.Tab tab = binding.tabLayout2.getTabAt(selectedTab_stats);
        if (tab != null) {
            tab.select();
        }

        // Restore selected button state
        if ("Income".equals(selectedtype)) {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.income_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ibtn.setTextColor(getContext().getColor(R.color.green));
        } else if ("Expense".equals(selectedtype)) {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.expense_sel));
            binding.ibtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ebtn.setTextColor(getContext().getColor(R.color.red));
        }
    }
}
