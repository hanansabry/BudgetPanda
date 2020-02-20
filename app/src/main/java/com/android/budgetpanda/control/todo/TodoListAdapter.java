package com.android.budgetpanda.control.todo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.budgetpanda.R;
import com.android.budgetpanda.model.TodoItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.TodoItemViewHolder> {

    private final TodoListPresenter presenter;

    public TodoListAdapter(TodoListPresenter presenter) {
        this.presenter = presenter;
    }

    public void bindItems(ArrayList<TodoItem> items) {
        presenter.bindItems(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TodoItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_item_layout, parent, false);
        return new TodoItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoItemViewHolder holder, int position) {
        presenter.onBindTodoItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemsSize();
    }

    class TodoItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTextView, dateTextView, statusTextView;
        private Spinner statusSpinner;
        private Context context;

        public TodoItemViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            itemNameTextView = itemView.findViewById(R.id.todo_item_name_textview);
            dateTextView = itemView.findViewById(R.id.date_textview);
            statusTextView = itemView.findViewById(R.id.status_textview);
            statusSpinner = itemView.findViewById(R.id.status_spinner);
            initializeSpinner();
        }

        private void initializeSpinner() {
            final StatusAdapter statusAdapter = new StatusAdapter(itemView.getContext(), android.R.layout.simple_spinner_dropdown_item);
            statusAdapter.add("Set Status");
            statusAdapter.add("Not Started");
            statusAdapter.add("In Progress");
            statusAdapter.add("Done");
            statusSpinner.setAdapter(statusAdapter);
            statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ((TextView) parent.getChildAt(0)).setTextColor(context.getResources().getColor(R.color.white));
                    ((TextView) parent.getChildAt(0)).setTextSize(16);
                    ((TextView) parent.getChildAt(0)).setTypeface(ResourcesCompat.getFont(context, R.font.didact_gothic));

                    if (position > 0) {
                        String status = statusAdapter.getItem(position);
                        presenter.updateItemStatus(getAdapterPosition(), status);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        public void setTodoItemData(TodoItem item) {
            itemNameTextView.setText(item.getItemName());
            dateTextView.setText(item.getDate());
            statusTextView.setText(item.getStatus());
        }
    }
}
