package com.drapps.monthlyexpenses;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ROHIT on 17/12/2017.
 */

public class ExpensesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "expensesManager";
    private static final String TABLE_EXPENSES = "expenses";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_COST = "cost";
    private static final String KEY_MONTH = "month";
    private static final String KEY_YEAR = "year";
    private static final String KEY_DATE = "date";

    private static final String monthsList [] = {"January", "February", "March", "April" ,"May", "June", "July", "August", "September", "October", "November","December"};

    public ExpensesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_COST + " INTEGER," + KEY_MONTH + " TEXT," + KEY_YEAR + " INTEGER," + KEY_DATE + " INTEGER" + ")";
        sqLiteDatabase.execSQL(CREATE_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldversion, int newversion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        onCreate(sqLiteDatabase);
    }

    void addExpense(String name, int cost, int position, int selectedDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_COST, cost);
        values.put(KEY_MONTH, monthsList[position]);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        //int date = Calendar.getInstance().get(Calendar.DATE);
        values.put(KEY_YEAR, year);
        values.put(KEY_DATE, selectedDate);
        db.insert(TABLE_EXPENSES, null, values);
        db.close();
    }

    public List<Expenses> getAllExpenses(int position) {
        List<Expenses> expensesList = new ArrayList<Expenses>();
        int y = Calendar.getInstance().get(Calendar.YEAR);
        int mon = Calendar.getInstance().get(Calendar.MONTH);
        if(position>mon)
            y--;
        String s = Integer.toString(y);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXPENSES, new String[] { KEY_ID,
                        KEY_NAME, KEY_COST,KEY_MONTH,KEY_YEAR,KEY_DATE }, KEY_MONTH + "= ? AND "+KEY_YEAR+ "= ?",
                new String[] { monthsList[position],s }, null, null, KEY_DATE, null);
        if (cursor != null)
            cursor.moveToFirst();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {

            do {
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                int cost = Integer.parseInt(cursor.getString(2));
                String month = cursor.getString(3);
                int year = Integer.parseInt(cursor.getString(4));
                int date = Integer.parseInt(cursor.getString(5));
                Expenses expense = new Expenses(id,name,cost,month,year,date);
                expensesList.add(expense);
            } while (cursor.moveToNext());
        }
        return expensesList;
    }

    public int[] getAllMonthsCost() {
        int [] tempArray = new int[12];
        for (int i=0;i<12;i++)
            tempArray[i] =0;
        int y = Calendar.getInstance().get(Calendar.YEAR);
        String s = Integer.toString(y);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE "+ KEY_YEAR + "= ?";
        Cursor cursor = db.rawQuery(selectQuery,  new String[] {s});
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int cost = Integer.parseInt(cursor.getString(2));
                String month = cursor.getString(3);
                switch (month)
                {
                    case "January":
                        tempArray[0] += cost;
                        break;
                    case "February":
                        tempArray[1] += cost;
                        break;
                    case "March":
                        tempArray[2] += cost;
                        break;
                    case "April":
                        tempArray[3] += cost;
                        break;
                    case "May":
                        tempArray[4] += cost;
                        break;
                    case "June":
                        tempArray[5] += cost;
                        break;
                    case "July":
                        tempArray[6] += cost;
                        break;
                    case "August":
                        tempArray[7] += cost;
                        break;
                    case "September":
                        tempArray[8] += cost;
                        break;
                    case "October":
                        tempArray[9] += cost;
                        break;
                    case "November":
                        tempArray[10] += cost;
                        break;
                    case "December":
                        tempArray[11] += cost;
                        break;
                }
            } while (cursor.moveToNext());
        }
        return tempArray;
    }

    public int[] getAllMonthsCost(int year) {
        int [] tempArray = new int[12];
        for (int i=0;i<12;i++)
            tempArray[i] =0;
        int y = year;
        String s = Integer.toString(y);
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EXPENSES + " WHERE "+ KEY_YEAR + "= ?";
        Cursor cursor = db.rawQuery(selectQuery,  new String[] {s});
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.moveToFirst()) {
            do {
                int cost = Integer.parseInt(cursor.getString(2));
                String month = cursor.getString(3);
                switch (month)
                {
                    case "January":
                        tempArray[0] += cost;
                        break;
                    case "February":
                        tempArray[1] += cost;
                        break;
                    case "March":
                        tempArray[2] += cost;
                        break;
                    case "April":
                        tempArray[3] += cost;
                        break;
                    case "May":
                        tempArray[4] += cost;
                        break;
                    case "June":
                        tempArray[5] += cost;
                        break;
                    case "July":
                        tempArray[6] += cost;
                        break;
                    case "August":
                        tempArray[7] += cost;
                        break;
                    case "September":
                        tempArray[8] += cost;
                        break;
                    case "October":
                        tempArray[9] += cost;
                        break;
                    case "November":
                        tempArray[10] += cost;
                        break;
                    case "December":
                        tempArray[11] += cost;
                        break;
                }
            } while (cursor.moveToNext());
        }
        return tempArray;
    }

    public void deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EXPENSES, KEY_ID + " = ?",
                new String[] {
                String.valueOf(id)});
        db.close();
    }

    public int updateExpense(int id, String name, int cost, int selectedDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_COST, cost);
        values.put(KEY_DATE, selectedDate);
        return db.update(TABLE_EXPENSES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}