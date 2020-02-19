package com.android.budgetpanda.control.months;

import com.android.budgetpanda.backend.months.MonthsTracking;

import java.util.Calendar;
import java.util.Locale;

public class MonthsDataPresenter {

    private final MonthsTracking monthsTracking;

    public MonthsDataPresenter(MonthsTracking monthsTracking) {
        this.monthsTracking = monthsTracking;
    }

    public String getSelectedMonthAsString() {
        Calendar calendar = Calendar.getInstance();
        int selectedMonth = monthsTracking.getSelectedMonth();
        calendar.set(Calendar.MONTH, selectedMonth);
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }

    public int getSelectedMonth() {
        return monthsTracking.getSelectedMonth();
    }

    public int getSelectedYear() {
        return monthsTracking.getSelectedYear();
    }

    public int getFirstMonth() {
        return monthsTracking.getFirstMonth();
    }

    public void setSelectedMonthToPreviousOne() {
        monthsTracking.setSelectedMonthToPreviousOne();
    }

    public void setSelectedMonthToNextOne() {
        monthsTracking.setSelectedMonthToNextOne();
    }

    public int getFirstYear() {
        return monthsTracking.getFirstYear();
    }
}
