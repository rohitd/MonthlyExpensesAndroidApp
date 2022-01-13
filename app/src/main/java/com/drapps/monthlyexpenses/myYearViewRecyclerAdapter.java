package com.drapps.monthlyexpenses;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ROHIT on 21/12/2017.
 */

public class myYearViewRecyclerAdapter extends RecyclerView.Adapter<myYearViewRecyclerAdapter.myYearViewHolder>{
    private int[] costs;
    private String[] months;

    public myYearViewRecyclerAdapter(String[] stringArray, int[] allMonthsCost) {
        months = stringArray;
        costs = allMonthsCost;
    }

    @Override
    public myYearViewRecyclerAdapter.myYearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.year_view_row, parent, false);
        return new myYearViewRecyclerAdapter.myYearViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myYearViewRecyclerAdapter.myYearViewHolder holder, int position) {
        String currency = MainActivity.currency;
        holder.expenseName.setText(""+months[position]);
        holder.expenseCost.setText(""+costs[position]+ " "+currency);
    }

    @Override
    public int getItemCount() {
        return 12;
    }

    public static class myYearViewHolder extends RecyclerView.ViewHolder {
        public TextView expenseName,expenseCost;
        public myYearViewHolder(View view) {
            super(view);
            expenseName = view.findViewById(R.id.name);
            expenseCost = view.findViewById(R.id.cost);

        }
    }
}
