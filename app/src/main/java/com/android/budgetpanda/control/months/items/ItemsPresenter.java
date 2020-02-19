package com.android.budgetpanda.control.months.items;

import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.backend.months.MonthsTracking;
import com.android.budgetpanda.model.Item;

import java.util.ArrayList;

public class ItemsPresenter {

    private ArrayList<Item> items = new ArrayList<>();
    private final ItemsRepository itemsRepository;
    private final MonthsTracking monthsTracking;

    public ItemsPresenter(ItemsRepository itemsRepository, MonthsTracking monthsTracking) {
        this.itemsRepository = itemsRepository;
        this.monthsTracking = monthsTracking;
    }

    public void bindItems(ArrayList<Item> items) {
        this.items = items;
    }

    public void onBindItemAtPosition(ItemsAdapter.ItemViewHolder holder, int position) {
        Item item = items.get(position);
        holder.setItemData(item);
    }

    public int getItemsSize() {
        return items.size();
    }

    public void retrieveItems(Item.ITEM_CATEGORY itemCategory, ItemsRepository.ItemsRetrievingCallback callback) {
        itemsRepository.retrieveItemsByMonth(itemCategory, monthsTracking.getSelectedMonth(), monthsTracking.getSelectedYear(), callback);
    }
}
