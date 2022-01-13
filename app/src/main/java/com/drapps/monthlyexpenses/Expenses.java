package com.drapps.monthlyexpenses;

/**
 * Created by ROHIT on 17/12/2017.
 */

public class Expenses {
    int id;
    String name;
    int cost;
    String month;
    int year;
    int date;
    boolean isSelected;

    Expenses(int i,String n, int c, String m, int y, int d)
    {
        id =i;
        name = n;
        cost = c;
        month = m;
        year = y;
        date = d;
    }

}
