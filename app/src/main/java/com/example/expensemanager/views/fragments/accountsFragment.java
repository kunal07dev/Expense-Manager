package com.example.expensemanager.views.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentAccountsBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.viewModels.Mainviewmodel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;

public class accountsFragment extends Fragment {

    private FragmentAccountsBinding binding;
    private Calendar calendar;
    public Mainviewmodel viewModel;
    public static int selectedTab_account = 0;
    private PieChart pieChart;

    public accountsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountsBinding.inflate(inflater, container, false);

        pieChart = binding.pieChartView;
        viewModel = new ViewModelProvider(this).get(Mainviewmodel.class);
        calendar = Calendar.getInstance();

        setupChart();
        setupObservers();
        setupTabLayoutListener();
        setupDateNavigationListeners();

        // Restore selected tab state if exists
        restoreSelectedTab();

        // Initialize date and fetch transactions
        updateDate();

        return binding.getRoot();
    }

    private void setupChart() {
        pieChart.setBackgroundColor(getResources().getColor(R.color.blue_light));
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleAlpha(0);
        pieChart.setHoleRadius(50f);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setDrawCenterText(true);
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);

        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                if (e instanceof PieEntry) {
                    PieEntry pieEntry = (PieEntry) e;
                    String label = pieEntry.getLabel();
                    float value = pieEntry.getValue();
                    String message = String.format("Category: %s\nValue: %.2f", label, value);
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onNothingSelected() {
                // Optionally handle when no value is selected
            }
        });
    }

    private void setupObservers() {
        viewModel.catetransaction.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transcation>>() {
            @Override
            public void onChanged(RealmResults<Transcation> transactions) {
                if (transactions != null && transactions.size() > 0) {
                    updateChart(transactions);
                } else {
                    pieChart.setVisibility(View.GONE);
                    binding.empty2.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateChart(RealmResults<Transcation> transactions) {
        List<PieEntry> entries = new ArrayList<>();
        Map<String, Float> categoryMap = new HashMap<>();

        float totalAmount = 0f;

        for (Transcation transaction : transactions) {
            String account = transaction.getAccount();
            float amount = (float) transaction.getAmmount();
            categoryMap.merge(account, amount, Float::sum);
            totalAmount += amount;
        }

        float minimumDisplayAmount = totalAmount * 0.02f; // Example: 2% of the total amount

        for (Map.Entry<String, Float> entry : categoryMap.entrySet()) {
            float displayAmount = entry.getValue();
            if (displayAmount < minimumDisplayAmount) {
                displayAmount = minimumDisplayAmount;
            }
            entries.add(new PieEntry(displayAmount, entry.getKey()));
        }

        if (entries.isEmpty()) {
            pieChart.setVisibility(View.GONE);
            binding.empty2.setVisibility(View.VISIBLE);
        } else {
            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            dataSet.setValueTextColor(getResources().getColor(R.color.black));
            dataSet.setValueTextSize(16f);
            dataSet.setValueFormatter(new PercentFormatter());

            PieData data = new PieData(dataSet);
            pieChart.setData(data);
            pieChart.setDrawEntryLabels(false);
            pieChart.setCenterText("");

            Legend legend = pieChart.getLegend();
            legend.setEnabled(true);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            legend.setFormSize(10f);
            legend.setForm(Legend.LegendForm.SQUARE);

            pieChart.invalidate();
            binding.empty2.setVisibility(View.GONE);
            pieChart.setVisibility(View.VISIBLE);
        }
    }

    private void setupTabLayoutListener() {
        binding.tabLayout1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText() != null) {
                    if (tab.getText().equals("Monthly")) {
                        selectedTab_account = 1;
                    } else if (tab.getText().equals("Daily")) {
                        selectedTab_account = 0;
                    }
                    updateDate();
                }
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
        binding.next.setOnClickListener(c -> {
            if (selectedTab_account == 0) {
                calendar.add(Calendar.DATE, 1);
            } else if (selectedTab_account == 1) {
                calendar.add(Calendar.MONTH, 1);
            }
            updateDate();
        });

        binding.pre.setOnClickListener(c -> {
            if (selectedTab_account == 0) {
                calendar.add(Calendar.DATE, -1);
            } else if (selectedTab_account == 1) {
                calendar.add(Calendar.MONTH, -1);
            }
            updateDate();
        });
    }

    private void updateDate() {
        if (selectedTab_account == 0) {
            binding.dat.setText(helper.formatedate(calendar.getTime()));
        } else if (selectedTab_account == 1) {
            binding.dat.setText(helper.formatedateByMonth(calendar.getTime()));
        }
        viewModel.gettran(calendar);
    }

    private void restoreSelectedTab() {
        TabLayout.Tab tab = binding.tabLayout1.getTabAt(selectedTab_account);
        if (tab != null) {
            tab.select();
        }
    }
}
