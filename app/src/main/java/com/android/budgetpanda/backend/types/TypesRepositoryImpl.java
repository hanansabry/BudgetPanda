package com.android.budgetpanda.backend.types;

import com.android.budgetpanda.model.Item;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class TypesRepositoryImpl implements TypesRepository {

    private final DatabaseReference mDatabase;

    public TypesRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("types");
    }

    @Override
    public void retrieveTypes(Item.ITEM_CATEGORY itemCategory, final TypesRetrievingCallback callback) {
        DatabaseReference itemsRef = mDatabase;
        if (itemCategory == Item.ITEM_CATEGORY.Expense) {
            itemsRef = mDatabase.child("expenses");
        } else {
            itemsRef = mDatabase.child("income");
        }
        itemsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<String> types = (ArrayList<String>) dataSnapshot.getValue();
                callback.onTypesRetrievedSuccessfully(types);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onTypesRetrievedFailed(databaseError.getMessage());
            }
        });
    }
}
