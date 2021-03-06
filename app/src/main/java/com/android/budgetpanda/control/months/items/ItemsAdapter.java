package com.android.budgetpanda.control.months.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.budgetpanda.R;
import com.android.budgetpanda.model.Item;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private final ItemsPresenter presenter;
    private boolean deletable;

    public ItemsAdapter(ItemsPresenter presenter, boolean deletable) {
        this.presenter = presenter;
        this.deletable = deletable;
    }

    public void bindItems(ArrayList<Item> items) {
        presenter.bindItems(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        presenter.onBindItemAtPosition(holder, position);
    }

    @Override
    public int getItemCount() {
        return presenter.getItemsSize();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView itemNameTV, typeTV, priorityTV, amountTV;
        private ImageButton deleteButton;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            itemNameTV = itemView.findViewById(R.id.item_name_textview);
            typeTV = itemView.findViewById(R.id.type_textview);
            priorityTV = itemView.findViewById(R.id.priority_textview);
            amountTV = itemView.findViewById(R.id.amount_textview);
            deleteButton = itemView.findViewById(R.id.delete_btn);

            if (deletable) {
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.removeItem(getAdapterPosition());
                        notifyItemRemoved(getAdapterPosition());
                        notifyItemRangeChanged(getAdapterPosition(), presenter.getItemsSize());
                    }
                });
            } else {
                deleteButton.setVisibility(View.GONE);
            }
        }

        public void setItemData(Item item) {
            itemNameTV.setText(item.getName());
            typeTV.setText(String.format(Locale.getDefault(), "Type: %s", item.getType()));
            priorityTV.setText(String.format(Locale.getDefault(), "Priority: %d", item.getPriority()));
            amountTV.setText(String.valueOf(item.getAmount()));
        }
    }
}
