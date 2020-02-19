package com.android.budgetpanda;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.android.budgetpanda.backend.months.MonthsTracking;
import com.google.firebase.database.FirebaseDatabase;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //remove old shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        MonthsTracking monthsTracking = Injection.provideMonthsTracking(sharedPreferences);
        monthsTracking.setFirstMonth();
    }
}
