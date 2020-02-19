package com.android.budgetpanda.backend.items;

import com.android.budgetpanda.backend.months.MonthsTracking;
import com.android.budgetpanda.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ItemsRepositoryImpl implements ItemsRepository {

    private final MonthsTracking monthsTracking;
    private final DatabaseReference mDatabase;
    private final String userId;

    public ItemsRepositoryImpl(MonthsTracking monthsTracking) {
        this.monthsTracking = monthsTracking;
        mDatabase = FirebaseDatabase.getInstance().getReference("items");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void retrieveItemsByMonth(Item.ITEM_CATEGORY itemCategory, int month, int year, final ItemsRetrievingCallback callback) {
        String monthId = month + "_" + year;
        mDatabase.child(userId)
                .child(itemCategory.name())
                .child(monthId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Item> items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            Item item = itemSnapshot.getValue(Item.class);
                            items.add(item);
                        }
                        callback.onItemsRetrievedSuccessfully(items);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onItemsRetrievedFailed(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void addNewItem(Item.ITEM_CATEGORY itemCategory, Item item, final ItemInsertingCallback callback) {
        String monthId = monthsTracking.getSelectedMonth() + "_" + monthsTracking.getSelectedYear();
        mDatabase.child(userId)
                .child(itemCategory.name())
                .child(monthId)
                .push()
                .setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onItemInsertedSuccessfully();
                        } else {
                            callback.onItemInsertedFailed(task.getException().getMessage());
                        }
                    }
                });
    }
}
