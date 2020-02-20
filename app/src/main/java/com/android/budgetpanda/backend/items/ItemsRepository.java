package com.android.budgetpanda.backend.items;

import com.android.budgetpanda.model.Item;

import java.util.ArrayList;
import java.util.HashMap;

public interface ItemsRepository {



    interface ItemsRetrievingCallback {
        void onItemsRetrievedSuccessfully(ArrayList<Item> items);

        void onItemsRetrievedFailed(String errmsg);
    }

    interface AllMonthsDataRetrievingCallback {
        void onDataRetrievedSuccessfully(HashMap<Integer, Double> monthsStatus);

        void onDataRetrievedFailed(String errmsg);
    }

    interface ItemInsertingCallback {
        void onItemInsertedSuccessfully();

        void onItemInsertedFailed(String errmsg);
    }

    interface TotalAmountCallback {
        void onGetTotalAmount(double amount);

        void onGetTotalAmountFailed(String errmgs);
    }

    void retrieveItemsByMonth(Item.ITEM_CATEGORY itemCategory, int month, int year, ItemsRetrievingCallback callback);

    void addNewItem(Item.ITEM_CATEGORY itemCategory, Item item, ItemInsertingCallback callback);

    void removeItem(Item item);

    void retrieveLowPrioritiesItems(int month, int year, ItemsRetrievingCallback callback);

    void retrieveAllMonthsDataForGraph(AllMonthsDataRetrievingCallback callback);

    void getTotalAmount(Item.ITEM_CATEGORY expenseOrIncome, TotalAmountCallback callback);
}
