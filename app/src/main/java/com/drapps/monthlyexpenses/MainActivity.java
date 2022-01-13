package com.drapps.monthlyexpenses;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.ads.AdListener;
//import com.google.android.gms.ads.AdRequest;
//import com.google.android.gms.ads.AdView;

//import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.ads.MobileAds;
import com.facebook.ads.*;


import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static ExpensesDatabaseHelper dbHelper;
    private TextView tv,TotalText;
    private Spinner sp;
    private RecyclerView mRecyclerView;
    private static List<Expenses> expenses;
    private static myRecyclerViewAdapter.RecyclerViewClickListener listener;
    private static boolean isDeleteView;
    private InterstitialAd mInterstitialAd;
    private AdView adView;
    public static int selectedCurrency = 0;
    public static String currency = "Rs.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adView = new AdView(this, "580168832418154_580233712411666", AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.bannerAdView);
        adContainer.addView(adView);
        if(!isNetworkConnected())
            adContainer.setVisibility(View.GONE);
        else
            adView.loadAd();

        mInterstitialAd = new InterstitialAd(this, "580168832418154_580170195751351");

        mInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                finish();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                // Show the ad
                //mInterstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        mInterstitialAd.loadAd();



        initialize();
        updateView();
    }

    private void initialize() {
        dbHelper = new ExpensesDatabaseHelper(getApplicationContext());
        tv = findViewById(R.id.middleText);
        TotalText = findViewById(R.id.totalText);
        Toolbar toolbar = findViewById(R.id.toolbar);
        sp = findViewById(R.id.spinner);
        mRecyclerView = findViewById(R.id.recyclerview);
        isDeleteView = false;

        //Tool Bar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_list);
        sp.setAdapter(adapter);
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        sp.setSelection(currentMonth);

        //Spinner listener
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                isDeleteView = false;
                invalidateOptionsMenu();
                updateView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //Recycler View
        listener = new myRecyclerViewAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                if(!isDeleteView)
                    startModifyExpenseDialog(expenses.get(position));
                else {
                    CheckBox cb = view.findViewById(R.id.deleteCheckBox);
                    cb.performClick();
                }

            }
        };
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onBackPressed() {
        if (isDeleteView) {
            isDeleteView = false;
            updateView();
            invalidateOptionsMenu();
        }
        else
        {
            if (mInterstitialAd != null && mInterstitialAd.isAdLoaded() && !mInterstitialAd.isAdInvalidated()) {
                mInterstitialAd.show();
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (mInterstitialAd != null) {
            mInterstitialAd.destroy();
        }
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem yearView = menu.findItem(R.id.yearView);
        MenuItem setCurrency = menu.findItem(R.id.setCurrency);
        MenuItem addButton = menu.findItem(R.id.addButton);
        MenuItem Delete = menu.findItem(R.id.delete);
        MenuItem DeleteButton = menu.findItem(R.id.deleteButton);
        yearView.setVisible(!isDeleteView);
        setCurrency.setVisible(!isDeleteView);
        addButton.setVisible(!isDeleteView);
        Delete.setVisible(!isDeleteView);
        DeleteButton.setVisible(isDeleteView);

        if (expenses.isEmpty())
        {
            Delete.setVisible(false);
            DeleteButton.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.addButton)
        {
            startAddExpenseDialog();
            return true;
        }
        else if (id == R.id.delete)
        {
            startDeleteActivity();
            return true;
        }
        else if (id == R.id.deleteButton)
        {
            deleteSelectedItems();
            return true;
        }
        else if (id == R.id.yearView)
        {
            startYearViewActivity();
            return true;
        }
        else if (id == R.id.setCurrency)
        {
            startCurrencySelectionDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    private void startCurrencySelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Currency");
        final String[] currencies = getResources().getStringArray(R.array.currencies);
        int checkedItem = selectedCurrency;
        builder.setSingleChoiceItems(currencies, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user checked an item
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // user clicked OK
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                selectedCurrency = selectedPosition;
                currency = getCurrency();
                updateView();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void startYearViewActivity() {
        Intent i = new Intent(this,YearViewActivity.class);
        startActivity(i);
    }

    private void startDeleteActivity() {
        isDeleteView = true;
        updateView();
        invalidateOptionsMenu();
    }

    private void deleteSelectedItems()
    {
        isDeleteView = false;
        for (int i = 0; i < expenses.size(); i++) {
            Expenses e = expenses.get(i);
            if (e.isSelected) {
                deleteExpense(e.id);
            }
        }
        updateView();
        invalidateOptionsMenu();
    }

    private void startAddExpenseDialog() {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("addDialogFragment");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        myDialogFragment addExpenseDialog = new myDialogFragment();
        addExpenseDialog.show(manager, "addDialogFragment");
    }

    private void startModifyExpenseDialog (Expenses e) {
        FragmentManager manager = getFragmentManager();
        Fragment frag = manager.findFragmentByTag("addDialogFragment");
        if (frag != null) {
            manager.beginTransaction().remove(frag).commit();
        }
        myDialogFragment addExpenseDialog = myDialogFragment.newInstance(e.id,e.name,e.cost,e.date);
        addExpenseDialog.show(manager, "addDialogFragment");
    }

    public void addExpense(String name,int cost, int selectedDate)
    {
        dbHelper.addExpense(name,cost,sp.getSelectedItemPosition(),selectedDate);
        updateView();
        invalidateOptionsMenu();
    }

    public void updateExpense(int expenseId, String n, int c, int selectedDate) {
        dbHelper.updateExpense(expenseId,n,c,selectedDate);
        updateView();
    }

    private void deleteExpense(int id) {
        dbHelper.deleteExpense(id);
    }

    private void updateView()
    {
        expenses = dbHelper.getAllExpenses(sp.getSelectedItemPosition());
        if (!expenses.isEmpty())
            tv.setVisibility(View.GONE);
        else
            tv.setVisibility(View.VISIBLE);
        RecyclerView.Adapter mAdapter = new myRecyclerViewAdapter(expenses,sp.getSelectedItemPosition(),listener,isDeleteView);
        mRecyclerView.setAdapter(mAdapter);
        int total = 0;
        for (Expenses expense : expenses) {
            total += expense.cost;
        }
        TotalText.setText(getString(R.string.total_test) +" "+total +" "+getCurrency());
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null)
        {
            return false;
        }
        else {
            return true;
        }
    }

    private String getCurrency()
    {
        String[] temp = getResources().getStringArray(R.array.currencies);
        return temp[selectedCurrency];
    }
    public int getSelectedMonth()
    {
        return sp.getSelectedItemPosition();
    }
}
