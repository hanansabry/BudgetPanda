package com.android.budgetpanda.control.todo;

import com.android.budgetpanda.backend.todo.TodoListRepository;
import com.android.budgetpanda.model.TodoItem;

import java.util.ArrayList;

public class TodoListPresenter {

    private ArrayList<TodoItem> items = new ArrayList<>();
    private final TodoListRepository todoListRepository;

    public TodoListPresenter(TodoListRepository todoListRepository) {
        this.todoListRepository = todoListRepository;
    }

    public void bindItems(ArrayList<TodoItem> items) {
        this.items = items;
    }

    public void onBindTodoItemAtPosition(TodoListAdapter.TodoItemViewHolder holder, int position) {
        TodoItem item = items.get(position);
        holder.setTodoItemData(item);
    }

    public int getItemsSize() {
        return items.size();
    }

    public void addNewTodoItem(TodoItem item, TodoListRepository.TodoItemInsertingCallback callback) {
        todoListRepository.insertNewTodoItem(item, callback);
    }

    public void retrieveTodoItems(TodoListRepository.TodoItemsRetrievingCallback callback) {
        todoListRepository.retrieveTodoItems(callback);
    }

    public void updateItemStatus(int position, String status) {
        TodoItem item = items.get(position);
        todoListRepository.updateTodoItemStatus(item, status);
    }
}
