package com.drapps.monthlyexpenses;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ROHIT on 17/12/2017.
 */

public class myRecyclerViewAdapter extends RecyclerView.Adapter<myRecyclerViewAdapter.myViewHolder> {
        private List<Expenses> expensesList;
        private static int month;
        private RecyclerViewClickListener mListener;
        private static boolean isDeleteView;

        public interface RecyclerViewClickListener {
            void onClick(View view, int position);
        }

        public myRecyclerViewAdapter(List<Expenses> list, int m, RecyclerViewClickListener listener, boolean isDelete) {
            month = m+1;
            expensesList = list;
            this.mListener =listener;
            isDeleteView = isDelete;
        }

        @Override
        public myRecyclerViewAdapter.myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.expenseview_row, parent, false);
            return new myViewHolder(itemView,mListener);
        }

        @Override
        public void onBindViewHolder(myViewHolder holder, int position) {
            Expenses expense = expensesList.get(position);
            String currency = MainActivity.currency
                    ;
            holder.expenseName.setText(expense.name);
            holder.expenseCost.setText(expense.cost + " " + currency);
            holder.expenseDate.setText(expense.date+ "/" + month+"/"+expense.year);
            holder.deleteCheckBox.setTag(expense);
            holder.deleteCheckBox.setChecked(expense.isSelected);
            holder.deleteCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox cb = (CheckBox) view;
                    Expenses expense = (Expenses) cb.getTag();
                    expense.isSelected = cb.isChecked();

                }
            });
            if(position %2 == 0) {
                holder.itemView.setBackgroundColor(Color.parseColor("#fff9c4"));
            }
            else {
                holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }

        }

        @Override
        public int getItemCount() {
            return expensesList.size();
        }

    public static class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView expenseName,expenseCost,expenseDate;
        public CheckBox deleteCheckBox;
        private RecyclerViewClickListener mListener;

        public myViewHolder(View view, RecyclerViewClickListener listener) {
            super(view);
            expenseName = view.findViewById(R.id.name);
            expenseCost = view.findViewById(R.id.cost);
            expenseDate = view.findViewById(R.id.date);
            deleteCheckBox = view.findViewById(R.id.deleteCheckBox);
            if (isDeleteView)
                deleteCheckBox.setVisibility(View.VISIBLE);
            mListener = listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }

    }
}
