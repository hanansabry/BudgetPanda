package com.android.budgetpanda.control.statistics;

import com.android.budgetpanda.backend.items.ItemsRepository;

import java.util.Calendar;
import java.util.Locale;

public class StatisticsPresenter {

    private final ItemsRepository itemsRepository;

    public StatisticsPresenter(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public void getAllMonthsDataForGraph(ItemsRepository.AllMonthsDataRetrievingCallback callback) {
        itemsRepository.retrieveAllMonthsDataForGraph(callback);
    }

    public String getSelectedMonthAsString(int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
    }
}
