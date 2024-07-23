package com.example.expensemanager.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensemanager.R;
import com.example.expensemanager.databinding.SampleBinding;
import com.example.expensemanager.models.category;

import java.util.ArrayList;
import java.util.Locale;

public class catadapt extends RecyclerView.Adapter <catadapt.CategoryViewHolder>{
    Context context;
    ArrayList<category> categories;
    public interface Categoryclicklis{
        void onCategoryclicked(category category);
    }
    Categoryclicklis categoryclicklis;
    public catadapt(Context context, ArrayList<category> categories,Categoryclicklis categoryclicklis) {
        this.categoryclicklis=categoryclicklis;
        this.context=context;
        this.categories=categories;
        
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new CategoryViewHolder(LayoutInflater.from(context).inflate(R.layout.sample,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        category category=categories.get(position);
        holder.binding.party.setText(category.getCatName());
        holder.binding.catpart.setImageResource(category.getCatimg());
        holder.itemView.setOnClickListener(c->{
            categoryclicklis.onCategoryclicked(category);
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder{
            SampleBinding binding;
        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= SampleBinding.bind(itemView);

        }
    }
}
