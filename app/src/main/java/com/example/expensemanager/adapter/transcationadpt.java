package com.example.expensemanager.adapter;

import static com.example.expensemanager.databinding.SampleBinding.inflate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.RowBinding;
import com.example.expensemanager.models.Transcation;
import com.example.expensemanager.utils.helper;

import java.util.ArrayList;

public class transcationadpt extends RecyclerView.Adapter<transcationadpt.transviewholder> {
    Context context;
    ArrayList<Transcation> transactions;

    public transcationadpt(Context context, ArrayList<Transcation> transcations) {
        this.context=context;
        this.transactions=transcations;
    }

    @NonNull
    @Override
    public transviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new  transviewholder(LayoutInflater.from(context).inflate(R.layout.row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull transviewholder holder, int position) {
        Transcation transcation=transactions.get(position);
        holder.binding.transam.setText((String.valueOf(transcation.getAmmount())));
        holder.binding.type.setText(transcation.getType());

        holder.binding.transdate.setText(helper.formatedate(transcation.getDate()));
        holder.binding.transcat.setText(transcation.getCategory());
        if(transcation.getType().equals("Income")){
            holder.binding.transam.setTextColor(context.getColor(R.color.green));


        }
        else {
            holder.binding.transam.setTextColor(context.getColor(R.color.red));
        }


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public class transviewholder extends RecyclerView.ViewHolder{
      RowBinding binding;
        public transviewholder(@NonNull View itemView) {
            super(itemView);
            binding=RowBinding.bind(itemView);
        }
    }
}
