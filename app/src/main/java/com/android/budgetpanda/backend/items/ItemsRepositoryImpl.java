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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

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
                .child(monthId)
                .child(itemCategory.name())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Item> items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            if (!itemSnapshot.getKey().equals("totalAmount")) {
                                Item item = itemSnapshot.getValue(Item.class);
                                item.setId(itemSnapshot.getKey());
                                items.add(item);
                            }
                        }
                        callback.onItemsRetrievedSuccessfully(sortByPriorityAsc(items));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onItemsRetrievedFailed(databaseError.getMessage());
                    }
                });
    }

    @Override
    public void retrieveLowPrioritiesItems(int month, int year, final ItemsRetrievingCallback callback) {
        String monthId = month + "_" + year;
        mDatabase.child(userId)
                .child(monthId)
                .child(Item.ITEM_CATEGORY.Expense.name())
                .orderByChild("priority")
                .limitToLast(3)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<Item> items = new ArrayList<>();
                        for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                            if (!itemSnapshot.getKey().equals("totalAmount")) {
                                Item item = itemSnapshot.getValue(Item.class);
                                item.setId(itemSnapshot.getKey());
                                items.add(item);
                            }
                        }
                        callback.onItemsRetrievedSuccessfully(sortByPriorityDesc(items));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        callback.onItemsRetrievedFailed(databaseError.getMessage());
                    }
                });
    }


    private ArrayList<Item> sortByPriorityDesc(ArrayList<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPriority() == o2.getPriority())
                    return 0;
                else if (o1.getPriority() > o2.getPriority())
                    return -1;
                else
                    return 1;
            }
        });
        return items;
    }

    private ArrayList<Item> sortByPriorityAsc(ArrayList<Item> items) {
        Collections.sort(items, new Comparator<Item>() {
            @Override
            public int compare(Item o1, Item o2) {
                if (o1.getPriority() == o2.getPriority())
                    return 0;
                else if (o1.getPriority() < o2.getPriority())
                    return -1;
                else
                    return 1;
            }
        });
        return items;
    }


    @Override
    public void addNewItem(Item.ITEM_CATEGORY itemCategory, final Item item, final ItemInsertingCallback callback) {
        String monthId = monthsTracking.getSelectedMonth() + "_" + monthsTracking.getSelectedYear();

        final DatabaseReference monthRef = mDatabase.child(userId).child(monthId).child(itemCategory.name());
        monthRef.push()
                .setValue(item)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.onItemInsertedSuccessfully();
                            updateTotalAmount(monthRef, item.getAmount(), true);
                        } else {
                            callback.onItemInsertedFailed(task.getException().getMessage());
                        }
                    }
                });
    }

    private void updateTotalAmount(final DatabaseReference monthRef, final double newAmount, final boolean add) {
        monthRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long totalAmount = 0;
                if (dataSnapshot.hasChild("totalAmount")) {
                    totalAmount = dataSnapshot.child("totalAmount").getValue(Long.class);
                }
                HashMap<String, Object> totalAmountValue = new HashMap<>();
                if (add) {
                    totalAmountValue.put("totalAmount", totalAmount + newAmount);
                } else {
                    totalAmountValue.put("totalAmount", totalAmount - newAmount);
                }
                monthRef.updateChildren(totalAmountValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void removeItem(final Item item) {
        String monthId = monthsTracking.getSelectedMonth() + "_" + monthsTracking.getSelectedYear();
        final DatabaseReference monthRef = mDatabase.child(userId).child(monthId).child(Item.ITEM_CATEGORY.Expense.name());
        monthRef.child(item.getId())
                .removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateTotalAmount(monthRef, item.getAmount(), false);
                        }
                    }
                });
    }
}
