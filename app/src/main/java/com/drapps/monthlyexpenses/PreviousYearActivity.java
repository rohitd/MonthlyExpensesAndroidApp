package com.drapps.monthlyexpenses;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;
import com.facebook.ads.*;

public class PreviousYearActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private int[] tempArray;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_year_view);
        ExpensesDatabaseHelper dbHelper = new ExpensesDatabaseHelper(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView tv = findViewById(R.id.totalText);

        adView = new AdView(this, "580168832418154_580233712411666", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.bannerAdView);
        adContainer.addView(adView);
        if(!isNetworkConnected())
            adContainer.setVisibility(View.GONE);
        else
            adView.loadAd();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        int year = Calendar.getInstance().get(Calendar.YEAR)-1;
        this.setTitle(year +getString(R.string.years_expenses));


        mRecyclerView = findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tempArray = new int[12];
        tempArray = dbHelper.getAllMonthsCost(year);
        RecyclerView.Adapter mAdapter = new myYearViewRecyclerAdapter(getResources().getStringArray(R.array.months),tempArray);
        mRecyclerView.setAdapter(mAdapter);
        int total =0;
        for (int i=0; i<12;i++)
            total += tempArray[i];
        tv.setText(getString(R.string.total_test) +" "+total +" "+getCurrency());
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            return true;
    }

    private String getCurrency()
    {
        String[] temp = getResources().getStringArray(R.array.currencies);
        return temp[MainActivity.selectedCurrency];
    }
}

