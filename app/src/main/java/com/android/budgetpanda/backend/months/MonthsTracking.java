package com.android.budgetpanda.backend.months;

import android.content.SharedPreferences;

import java.util.Calendar;

import static java.util.Calendar.DECEMBER;
import static java.util.Calendar.JANUARY;

public class MonthsTracking {

    private final SharedPreferences sharedPreferences;
    private final Calendar calendar;

    public MonthsTracking(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        calendar = Calendar.getInstance();
    }

    public int getFirstMonth() {
        int current = calendar.get(Calendar.MONTH);
        return sharedPreferences.getInt("FIRST_MONTH", current);
    }

    public void setFirstMonth() {
        if (isFirstUse()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("FIRST_MONTH", getFirstMonth());
            editor.apply();
            setFirstYear();
        }
    }

    public int getFirstYear() {
        int current = calendar.get(Calendar.YEAR);
        return sharedPreferences.getInt("FIRST_YEAR", current);
    }

    public void setFirstYear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("FIRST_YEAR", getFirstYear());
        editor.apply();
    }

    public boolean isCurrentMonth(int month) {
        int currentMonth = calendar.get(Calendar.MONTH);
        return currentMonth == month;
    }

    public boolean isPreviousMonth() {
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);
        if (getSelectedMonth() < currentMonth && getSelectedYear() == currentYear) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isFirstUse() {
        boolean firstUse = sharedPreferences.getBoolean("FIRST_USE", true);
        if (firstUse) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("FIRST_USE", false);
            editor.apply();
        }
        return firstUse;
    }

    public void setSelectedMonth(int selectedMonth, int selectedYear) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("MONTH", selectedMonth);
        editor.apply();
        editor.putInt("YEAR", selectedYear);
        editor.apply();
    }

    public int getSelectedMonth() {
        return sharedPreferences.getInt("MONTH", getFirstMonth());
    }

    public int getSelectedYear() {
        return sharedPreferences.getInt("YEAR", getFirstYear());
    }

    public void setSelectedMonthToPreviousOne() {
        int currentMonth = getSelectedMonth();
        int year = getSelectedYear();
        int previousMonth = currentMonth - 1;
        if (previousMonth == -1) {
            previousMonth = DECEMBER;
            year = year - 1;
        }
        setSelectedMonth(previousMonth, year);
    }

    public void setSelectedMonthToNextOne() {
        int currentMonth = getSelectedMonth();
        int year = getSelectedYear();
        int nextMonth = currentMonth + 1;
        if (nextMonth == 12) {
            nextMonth = JANUARY;
            year = year + 1;
        }
        setSelectedMonth(nextMonth, year);
    }
}
