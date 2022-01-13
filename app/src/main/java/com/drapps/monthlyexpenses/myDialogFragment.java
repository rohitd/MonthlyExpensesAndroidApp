package com.drapps.monthlyexpenses;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.YearMonth;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by ROHIT on 18/12/2017.
 */

public class myDialogFragment extends DialogFragment {
    EditText name,cost;
    Spinner daySpinner;
    String expenseName;
    int expenseCost, expenseId, expenseDate;
    boolean isForUpdate = false;
    private static final String monthsList [] = {"JAN", "FEB", "MAR", "APR" ,"MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV","DEC"};


    static myDialogFragment newInstance(int id,String name,int cost, int date) {
        myDialogFragment f = new myDialogFragment();
        Bundle args = new Bundle();
        args.putInt("id", id);
        args.putInt("cost",cost);
        args.putString("name",name);
        args.putInt("date",date);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;


        setStyle(style,0);
        if(getArguments()!= null && getArguments().containsKey("id")) {
            expenseId = getArguments().getInt("id");
            expenseCost = getArguments().getInt("cost");
            expenseDate = getArguments().getInt("date");
            expenseName = getArguments().getString("name");
            isForUpdate = true;
        }
        else
            isForUpdate = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.enter_expenses));
        View v = inflater.inflate(R.layout.add_dialog, container, false);
        name = v.findViewById(R.id.nameText);
        cost = v.findViewById(R.id.costText);
        daySpinner = v.findViewById(R.id.daySpinner);
        String[] dayArray = getDaysArray();
        ArrayAdapter<String> dayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner_item1,dayArray);
        dayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_list1);
        daySpinner.setAdapter(dayAdapter);
        if(isForUpdate) {
            int iYear = Calendar.getInstance().get(Calendar.YEAR);
            int iMonth = ((MainActivity)getActivity()).getSelectedMonth(); // 1 (months begin with 0)
            int iDay = 1;
            Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
            int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
            if(expenseDate<=daysInMonth)
                daySpinner.setSelection(expenseDate - 1);
            else
                daySpinner.setSelection(daysInMonth-1);
        }
        else if(((MainActivity)getActivity()).getSelectedMonth() == Calendar.getInstance().get(Calendar.MONTH))
            daySpinner.setSelection(Calendar.getInstance().get(Calendar.DATE)-1);//array start from 0 so date -1
        else
            daySpinner.setSelection(0);


        if(isForUpdate) {
            name.setText(expenseName);
            cost.setText(""+expenseCost);
        }
        name.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        Button save = v.findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(name.getText()) || TextUtils.isEmpty(cost.getText())) {
                    Snackbar.make(view, "Please Enter Expense name and cost!!!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else {
                    int selectedDate = daySpinner.getSelectedItemPosition()+1;
                    String n = name.getText().toString();
                    int c = Integer.parseInt(cost.getText().toString());
                    if(isForUpdate)
                        ((MainActivity)getActivity()).updateExpense(expenseId,n,c,selectedDate);
                    else
                        ((MainActivity)getActivity()).addExpense(n,c,selectedDate);
                    dismiss();
                }
            }
        });
        Button cancel = v.findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return v;
    }

    public String[] getDaysArray() {
        String[]daysArray;
        int iYear = Calendar.getInstance().get(Calendar.YEAR);
        int iMonth = ((MainActivity)getActivity()).getSelectedMonth(); // 1 (months begin with 0)
        int iDay = 1;
        Calendar mycal = new GregorianCalendar(iYear, iMonth, iDay);
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH); // 28
        daysArray = new String[daysInMonth];
        for (int i=0; i<daysInMonth;i++)
            daysArray[i] = (i+1) + " "+monthsList[iMonth];
        return daysArray;
    }
}
