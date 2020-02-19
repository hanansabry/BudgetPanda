package com.android.budgetpanda.control.months.additem;

import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.backend.types.TypesRepository;
import com.android.budgetpanda.model.Item;

public class AddItemPresenter {

    private final AddItemBottomFragment addItemBottomFragment;
    private final TypesRepository typesRepository;
    private final ItemsRepository itemsRepository;

    public AddItemPresenter(AddItemBottomFragment addItemBottomFragment, TypesRepository typesRepository, ItemsRepository itemsRepository) {
        this.addItemBottomFragment = addItemBottomFragment;
        this.typesRepository = typesRepository;
        this.itemsRepository = itemsRepository;
    }

    public void retrieveTypes(Item.ITEM_CATEGORY itemCategory, TypesRepository.TypesRetrievingCallback callback) {
        typesRepository.retrieveTypes(itemCategory, callback);
    }

    public boolean validateItemData(Item item) {
        boolean validate = true;
        if (item.getName() == null || item.getName().isEmpty()) {
            validate = false;
            addItemBottomFragment.setItemNameInputTextErrorMessage();
        }

        if (item.getType() == null || item.getType().isEmpty()) {
            validate = false;
            addItemBottomFragment.setTypeErrorMessage();
        }

        if (item.getPriority() == 0) {
            validate = false;
            addItemBottomFragment.setPriorityErrorMessage();
        }

        if (item.getAmount() == 0) {
            validate = false;
            addItemBottomFragment.setAmountErrorMessage();
        }

        return validate;

    }

    public void addNewItem(Item.ITEM_CATEGORY itemCategory, Item item, ItemsRepository.ItemInsertingCallback callback) {
        itemsRepository.addNewItem(itemCategory, item, callback);
    }
}
