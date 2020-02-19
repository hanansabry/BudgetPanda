package com.android.budgetpanda.control.months.expenses;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.budgetpanda.EmptyRecyclerView;
import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.control.months.additem.AddItemBottomFragment;
import com.android.budgetpanda.control.months.items.ItemsAdapter;
import com.android.budgetpanda.control.months.items.ItemsPresenter;
import com.android.budgetpanda.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExpensesFragment extends Fragment implements ItemsRepository.ItemsRetrievingCallback {

    private ItemsPresenter presenter;
    private ItemsAdapter adapter;

    public ExpensesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expenses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemBottomFragment bottomSheetFragment = AddItemBottomFragment.getInstance(Item.ITEM_CATEGORY.Expense);
                bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new ItemsPresenter(Injection.provideItemsRepository(sharedPreferences), Injection.provideMonthsTracking(sharedPreferences));
        presenter.retrieveItems(Item.ITEM_CATEGORY.Expense, this);
        initializeRecyclerView(view);
    }

    private void initializeRecyclerView(View view) {
        EmptyRecyclerView itemsRecyclerView = view.findViewById(R.id.items_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemsRecyclerView.getContext(),
                layoutManager.getOrientation());
        itemsRecyclerView.addItemDecoration(dividerItemDecoration);
        itemsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ItemsAdapter(presenter);
        itemsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemsRetrievedSuccessfully(ArrayList<Item> items) {
        adapter.bindItems(items);
    }

    @Override
    public void onItemsRetrievedFailed(String errmsg) {
        Toast.makeText(getContext(), errmsg, Toast.LENGTH_SHORT).show();
    }
}
