package com.android.budgetpanda.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.control.months.MonthDataActivity;
import com.android.budgetpanda.control.statistics.StatisticsActivity;
import com.android.budgetpanda.control.todo.TodoListActivity;
import com.android.budgetpanda.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class ControlActivity extends AppCompatActivity {

    private ControlPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new ControlPresenter(Injection.provideMonthsTracking(sharedPreferences));
    }

    public void onBackClicked(View view) {
        onBackPressed();
    }

    public void onTodoListClicked(View view) {
        startActivity(new Intent(this, TodoListActivity.class));
    }

    public void onStatisticsClicked(View view) {
        startActivity(new Intent(this, StatisticsActivity.class));
    }

    public void onMonthsClicked(View view) {
        setupMonthPickerDialog();
    }

    private void setupMonthPickerDialog() {
        Calendar calendar = Calendar.getInstance();
        final int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(ControlActivity.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        presenter.setSelectedMonth(selectedMonth, selectedYear);
                        startActivity(new Intent(ControlActivity.this, MonthDataActivity.class));
                    }
                }, year, month);

        builder.setActivatedMonth(month)
                .setMinYear(presenter.getFirstYear())
                .setActivatedYear(year)
                .setMaxYear(2050)
                .setMinMonth(presenter.getFirstMonth())
                .setTitle("Select Month")
                .build()
                .show();

    }

    public void onLogoutClicked(View view) {
        FirebaseAuth.getInstance().signOut();
        goToLoginScreen();
    }

    private void goToLoginScreen() {
        Intent homeIntent = new Intent(this, LoginActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeIntent);
    }
}
