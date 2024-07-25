package com.example.expensemanager.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.models.category;
import com.example.expensemanager.utils.constants;
import com.example.expensemanager.utils.helper;
import com.example.expensemanager.views.activities.MainActivity;

import io.realm.RealmResults;

public class transcationadpt extends RecyclerView.Adapter<transcationadpt.transviewholder> {
    Context context;
    RealmResults<Transcation> transactions;

    public transcationadpt(Context context, RealmResults<Transcation> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public transviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new transviewholder(LayoutInflater.from(context).inflate(R.layout.row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull transviewholder holder, int position) {
        Transcation transaction = transactions.get(position);

        // Display transaction amount
        holder.binding.transam.setText(String.valueOf(transaction.getAmmount()));

        // Display account name
        holder.binding.accounttype.setText(transaction.getAccount());

        // Display formatted date
        holder.binding.transdate.setText(helper.formatedate(transaction.getDate()));

        // Display category name
        holder.binding.transcat.setText(transaction.getCategory());

        // Get category details
        category transca = constants.getcatdetails(transaction.getCategory());
        if (transca != null) {
            holder.binding.categoryicon.setImageResource(transca.getCatimg());
        } else {
            // Set a default image or hide the icon if category is not found
            holder.binding.categoryicon.setImageResource(R.drawable.bus);

        }

        // Set text color based on transaction type
        if (transaction.getType().equals("Income")) {
            holder.binding.transam.setTextColor(context.getColor(R.color.green));
        } else {
            holder.binding.transam.setTextColor(context.getColor(R.color.red));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void  onClick(View v) {
                AlertDialog deleteDialog=new AlertDialog.Builder(context).create();
                deleteDialog.setTitle("Delete Transaction");
                deleteDialog.setMessage("Are you sure you want to delete this transaction?");
                deleteDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)context).viewModel.deleteTran(transaction);

                    }



                });
                deleteDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDialog.dismiss();

                    }
                });
                deleteDialog.show();

                return ;

            }
        });

    }
    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class transviewholder extends RecyclerView.ViewHolder {
        RowBinding binding;

        public transviewholder(@NonNull View itemView) {
            super(itemView);
            binding = RowBinding.bind(itemView);
        }
    }
}
