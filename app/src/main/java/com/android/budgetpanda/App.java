package com.android.budgetpanda;

import android.app.Application;
import android.content.Context;

import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //remove old shared preferences
        getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE).edit().clear().apply();
    }
}
