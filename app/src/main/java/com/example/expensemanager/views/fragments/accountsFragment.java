package com.example.expensemanager.views.fragments;

import static com.anychart.enums.LegendLayout.HORIZONTAL;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

public class StatFragment extends Fragment {

    private FragmentStatsBinding binding;
    private Calendar calendar;
    public Mainviewmodel viewModel;
    public static String selectedtype = "Income";
    public static int selectedTab_stats = 0;

    public StatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStatsBinding.inflate(inflater, container, false);

        // Initialize viewModel
        viewModel = new ViewModelProvider(this).get(Mainviewmodel.class);

        // Initialize calendar
        calendar = Calendar.getInstance();
        updateDate();

        // Setup chart
        AnyChartView anyChartView = binding.anychart;
        Pie pie = AnyChart.pie();

        viewModel.catetransaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transcation>>() {
            @Override
            public void onChanged(RealmResults<Transcation> transactions) {
                if (transactions.size() > 0) {
                    List<DataEntry> data = new ArrayList<>();
                    pie.background().fill("#DCEEFD");
                    pie.labels().position("outside");
                    binding.empty2.setVisibility(View.GONE);
                    binding.anychart.setVisibility(View.VISIBLE);
                    Map<String, Double> categoryMap = new HashMap<>();
                    for (Transcation transaction : transactions) {
                        String account = transaction.getAccount();
                        double amount = transaction.getAmmount();
                        if (categoryMap.containsKey(account)) {
                            double currentTotal = categoryMap.get(account).doubleValue();
                            currentTotal += Math.abs(amount);
                            categoryMap.put(account, currentTotal);
                        } else {
                            categoryMap.put(account, Math.abs(amount));
                        }
                    }
                    for (Map.Entry<String, Double> entry : categoryMap.entrySet()) {
                        data.add(new ValueDataEntry(entry.getKey(), entry.getValue()));
                    }

                    pie.data(data);






                    pie.legend().position("center-bottom").itemsLayout(HORIZONTAL).align(Align.CENTER);





                } else {
                    binding.anychart.setVisibility(View.GONE);
                    binding.empty2.setVisibility(View.VISIBLE);
                }
            }
        });

        viewModel.gettrans(calendar, selectedtype);
        anyChartView.setChart(pie);

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

        binding.tabLayout2.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Monthly")) {
                    selectedTab_stats = 1;
                } else if (tab.getText().equals("Daily")) {
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

        // Set up date navigation
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

        // Initialize date and fetch transactions
        updateDate();

        return binding.getRoot();
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
}
