package com.example.expensemanager.views.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.expensemanager.R;
import com.example.expensemanager.adapter.accountadpt;
import com.example.expensemanager.adapter.catadapt;
import com.example.expensemanager.databinding.CatDialogueBinding;
import com.example.expensemanager.databinding.FragmentAddtransBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.models.account;
import com.example.expensemanager.models.category;
import com.example.expensemanager.utils.constants;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.views.activities.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class Addtrans extends BottomSheetDialogFragment {

    public Addtrans() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentAddtransBinding binding;
    Transcation transaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddtransBinding.inflate(inflater);
        inputfliter.apply(binding.amount);

        transaction = new Transcation();
        binding.ibtn.setOnClickListener(v -> {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.income_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ibtn.setTextColor(getContext().getColor(R.color.green));
            transaction.setType("Income");
        });
        binding.ebtn.setOnClickListener(v -> {
            binding.ibtn.setBackground(getContext().getDrawable(R.drawable.default_sel));
            binding.ebtn.setBackground(getContext().getDrawable(R.drawable.expense_sel));
            binding.ibtn.setTextColor(getContext().getColor(R.color.textco));
            binding.ebtn.setTextColor(getContext().getColor(R.color.red));
            transaction.setType("Expense");
        });
        binding.date.setOnClickListener(f -> {
            DatePickerDialog d = new DatePickerDialog(getContext());
            d.setOnDateSetListener((view, year, month, dayOfMonth) -> {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.DAY_OF_MONTH, view.getDayOfMonth());
                cal.set(Calendar.MONTH, view.getMonth());
                cal.set(Calendar.YEAR, view.getYear());
                String show = helper.formatedate(cal.getTime());
                binding.date.setText(show);
                transaction.setDate(cal.getTime());
                transaction.setId(cal.getTime().getTime());
            });
            d.show();
        });

        binding.category.setOnClickListener(c -> {
            CatDialogueBinding dialogBinding = CatDialogueBinding.inflate(inflater);
            AlertDialog cate = new AlertDialog.Builder(getContext()).create();
            cate.setView(dialogBinding.getRoot());

            catadapt adapter = new catadapt(getContext(), constants.categories, new catadapt.Categoryclicklis() {
                @Override
                public void onCategoryclicked(category category) {
                    binding.category.setText(category.getCatName());
                    transaction.setCategory(category.getCatName());
                    cate.dismiss();
                }
            });
            dialogBinding.recycleview.setLayoutManager(new GridLayoutManager(getContext(), 2));
            dialogBinding.recycleview.setAdapter(adapter);
            cate.show();
        });

        binding.account.setOnClickListener(c -> {
            CatDialogueBinding dialogBinding = CatDialogueBinding.inflate(inflater);
            AlertDialog accountdia = new AlertDialog.Builder(getContext()).create();
            accountdia.setView(dialogBinding.getRoot());
            ArrayList<account> accounts = new ArrayList<>();
            accounts.add(new account(500, "Cash"));
            accounts.add(new account(0, "Bank"));
            accounts.add(new account(0, "Online"));
            accounts.add(new account(0, "Card"));

            accountadpt accountadpt = new accountadpt(getContext(), accounts, new accountadpt.accountsclickListener() {
                @Override
                public void onaccountlist(account account) {
                    Log.d("AccountClick", "Selected Account: " + account.getAccountName());
                    binding.account.setText(account.getAccountName());
                    transaction.setAccount(account.getAccountName());
                    accountdia.dismiss();
                }
            });
            dialogBinding.recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
            dialogBinding.recycleview.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
            dialogBinding.recycleview.setAdapter(accountadpt);
            accountdia.show();
        });

        binding.savet.setOnClickListener(v -> {
            String date = binding.date.getText().toString();
            String amount = binding.amount.getText().toString();
            String category = binding.category.getText().toString();
            String account = binding.account.getText().toString();
            String type = transaction.getType();

            // Validate all fields
            if (date.isEmpty()) {
                Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (amount.isEmpty()) {
                Toast.makeText(getContext(), "Please enter an amount", Toast.LENGTH_SHORT).show();
                return;
            }
            if (category.isEmpty()) {
                Toast.makeText(getContext(), "Please select a category", Toast.LENGTH_SHORT).show();
                return;
            }
            if (account.isEmpty()) {
                Toast.makeText(getContext(), "Please select an account", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type == null || type.isEmpty()) {
                Toast.makeText(getContext(), "Please select a transaction type", Toast.LENGTH_SHORT).show();
                return;
            }

            String note = binding.note.getText().toString();
            Double amt = Double.parseDouble(amount);

            if (transaction.getType().equals("Expense")) {
                transaction.setAmmount(amt * -1);

            } else {
                transaction.setAmmount(amt);
            }

            transaction.setNote(note);

            ((MainActivity) getActivity()).viewModel.addtran(transaction);
            ((MainActivity) getActivity()).getTransaction();

            dismiss();
        });

        return binding.getRoot();
    }



}
