package com.example.expensemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.models.account;

import java.util.List;

public class accountadpt extends RecyclerView.Adapter<accountadpt.AccountViewHolder> {

    private Context context;
    private List<account> accountList;
    private accountsclickListener listener;

    public interface accountsclickListener {
        void onaccountlist(account account);
    }

    public accountadpt(Context context, List<account> accountList, accountsclickListener listener) {
        this.context = context;
        this.accountList = accountList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_accounts, parent, false);
        return new AccountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountViewHolder holder, int position) {
        account currentAccount = accountList.get(position);
        holder.accountName.setText(currentAccount.getAccountName());
        holder.itemView.setOnClickListener(v -> listener.onaccountlist(currentAccount));
    }

    @Override
    public int getItemCount() {
        return accountList.size();
    }

    public static class AccountViewHolder extends RecyclerView.ViewHolder {

        TextView accountName;

        public AccountViewHolder(@NonNull View itemView) {
            super(itemView);
            accountName = itemView.findViewById(R.id.accountname);
        }
    }
}
