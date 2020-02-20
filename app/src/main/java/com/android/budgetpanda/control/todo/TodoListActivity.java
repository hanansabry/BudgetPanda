package com.android.budgetpanda.control.todo;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.budgetpanda.EmptyRecyclerView;
import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.todo.TodoListRepository;
import com.android.budgetpanda.model.TodoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

public class TodoListActivity extends AppCompatActivity implements TodoListRepository.TodoItemInsertingCallback, TodoListRepository.TodoItemsRetrievingCallback {

    private TodoListPresenter presenter;
    private TodoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);
        setTitle("Todo List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        presenter = new TodoListPresenter(Injection.provideTodoListRepository());
        presenter.retrieveTodoItems(this);
        initializeRecyclerView();
    }

    private void initializeRecyclerView() {
        EmptyRecyclerView itemsRecyclerView = findViewById(R.id.todo_items_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemsRecyclerView.getContext(),
                layoutManager.getOrientation());
        itemsRecyclerView.addItemDecoration(dividerItemDecoration);
        itemsRecyclerView.setLayoutManager(layoutManager);

        adapter = new TodoListAdapter(presenter);
        itemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    public void onSendClicked(View view) {
        EditText activityEditText = findViewById(R.id.activityEditText);
        if (activityEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please enter activity", Toast.LENGTH_SHORT).show();
        } else {
            TodoItem item = new TodoItem();
            item.setItemName(activityEditText.getText().toString().trim());
            item.setDate(getCurrentDate());
            item.setStatus(TodoItem.TodoStatus.NotStarted.name());

            presenter.addNewTodoItem(item, this);
        }
    }

    private static String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    @Override
    public void onItemInsertedSuccessfully() {
        Toast.makeText(this, "Item is added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemInsertedFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemsRetrievedSuccessfully(ArrayList<TodoItem> items) {
        adapter.bindItems(items);
    }

    @Override
    public void onItemsRetrievedFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_SHORT).show();
    }
}
