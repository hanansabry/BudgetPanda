package com.android.budgetpanda.backend.todo;

import com.android.budgetpanda.model.TodoItem;

import java.util.ArrayList;

public interface TodoListRepository {

    interface TodoItemsRetrievingCallback {
        void onItemsRetrievedSuccessfully(ArrayList<TodoItem> items);

        void onItemsRetrievedFailed(String errmsg);
    }

    interface TodoItemInsertingCallback {
        void onItemInsertedSuccessfully();

        void onItemInsertedFailed(String errmsg);
    }

    void retrieveTodoItems(TodoItemsRetrievingCallback callback);

    void insertNewTodoItem(TodoItem item, TodoItemInsertingCallback callback);

    void updateTodoItemStatus(TodoItem item, String status);
}
