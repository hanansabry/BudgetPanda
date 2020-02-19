package com.android.budgetpanda.backend.types;

import com.android.budgetpanda.model.Item;

import java.util.ArrayList;

public interface TypesRepository {

    interface TypesRetrievingCallback {
        void onTypesRetrievedSuccessfully(ArrayList<String> types);

        void onTypesRetrievedFailed(String errmsg);
    }

    void retrieveTypes(Item.ITEM_CATEGORY itemCategory, TypesRetrievingCallback callback);
}
