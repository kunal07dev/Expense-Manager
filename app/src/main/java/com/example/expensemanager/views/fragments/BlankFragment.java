package com.example.expensemanager.views.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.google.android.material.textfield.TextInputEditText;

public class BlankFragment extends Fragment {

    private TextInputEditText dailyAmountInput, monthlyAmountInput;
    private Button setLimitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_limit, container, false);

        // Initialize UI components
        dailyAmountInput = view.findViewById(R.id.damount);
        monthlyAmountInput = view.findViewById(R.id.mamount);
        setLimitButton = view.findViewById(R.id.Set);

        // Load saved limits from SharedPreferences
        loadSavedLimits();

        // Set click listener for the button
        setLimitButton.setOnClickListener(v -> saveLimits());

        return view;
    }

    /**
     * Load saved daily and monthly limits from SharedPreferences
     * and populate the input fields.
     */
    private void loadSavedLimits() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ExpenseManager", getContext().MODE_PRIVATE);

        // Retrieve saved values
        float dailyLimit = sharedPreferences.getFloat("daily_limit", 0);
        float monthlyLimit = sharedPreferences.getFloat("monthly_limit", 0);

        // Populate input fields
        dailyAmountInput.setText(String.valueOf(dailyLimit));
        monthlyAmountInput.setText(String.valueOf(monthlyLimit));
    }

    /**
     * Save the daily and monthly limits entered by the user into SharedPreferences.
     */
    private void saveLimits() {
        // Validate input
        String dailyInput = dailyAmountInput.getText().toString().trim();
        String monthlyInput = monthlyAmountInput.getText().toString().trim();
        float daily = 0;
        float monthly=0;
        if (dailyInput.isEmpty()) {
           daily = 0; // Default value if no input is provided
        } else {
            daily = Float.parseFloat(dailyInput);
        }

        if (monthlyInput.isEmpty()) {
            monthly = 0; // Default value if no input is provided
        } else {
            monthly = Float.parseFloat(monthlyInput);
        }



        // Save limits to SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ExpenseManager", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat("daily_limit", daily);
        editor.putFloat("monthly_limit", monthly);
        editor.apply();

        // Notify user of success
        dailyAmountInput.setError(null);
        monthlyAmountInput.setError(null);
        // Optional: Show a success message or Toast (uncomment the below line)
        Toast.makeText(getContext(), "Limits Saved!", Toast.LENGTH_SHORT).show();
    }
}
