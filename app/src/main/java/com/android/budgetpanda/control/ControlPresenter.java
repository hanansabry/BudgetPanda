package com.android.budgetpanda.control;

import com.android.budgetpanda.backend.months.MonthsTracking;

public class ControlPresenter {

    private final MonthsTracking monthsTracking;

    public ControlPresenter(MonthsTracking monthsTracking) {
        this.monthsTracking = monthsTracking;
    }

    public int getFirstYear() {
        return monthsTracking.getFirstYear();
    }

    public int getFirstMonth() {
        return monthsTracking.getFirstMonth();
    }

    public void setSelectedMonth(int selectedMonth, int selectedYear) {
        monthsTracking.setSelectedMonth(selectedMonth, selectedYear);
    }
}
