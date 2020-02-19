package com.android.budgetpanda.backend.items;

import com.android.budgetpanda.model.Item;

import java.util.ArrayList;

public interface ItemsRepository {

    interface ItemsRetrievingCallback {
        void onItemsRetrievedSuccessfully(ArrayList<Item> items);

        void onItemsRetrievedFailed(String errmsg);
    }

    interface ItemInsertingCallback {
        void onItemInsertedSuccessfully();

        void onItemInsertedFailed(String errmsg);
    }

    void retrieveItemsByMonth(Item.ITEM_CATEGORY itemCategory, int month, int year, ItemsRetrievingCallback callback);

    void addNewItem(Item.ITEM_CATEGORY itemCategory, Item item, ItemInsertingCallback callback);
}
