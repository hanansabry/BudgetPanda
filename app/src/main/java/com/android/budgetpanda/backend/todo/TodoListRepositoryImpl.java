package com.android.budgetpanda.backend.todo;

import com.android.budgetpanda.model.TodoItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;

public class TodoListRepositoryImpl implements TodoListRepository{

    private final DatabaseReference mDatabase;
    private final String userId;

    public TodoListRepositoryImpl() {
        mDatabase = FirebaseDatabase.getInstance().getReference("todo");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    @Override
    public void retrieveTodoItems(final TodoItemsRetrievingCallback callback) {
        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<TodoItem> items = new ArrayList<>();
                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {
                    TodoItem item = itemSnapshot.getValue(TodoItem.class);
                    item.setId(itemSnapshot.getKey());
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
    public void insertNewTodoItem(TodoItem item, final TodoItemInsertingCallback callback) {
        mDatabase.child(userId).push().setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    @Override
    public void updateTodoItemStatus(TodoItem item, String status) {
        HashMap<String, Object> statusValue = new HashMap<>();
        statusValue.put("status", status);
        mDatabase.child(userId).child(item.getId()).updateChildren(statusValue);
    }
}
