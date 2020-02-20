package com.android.budgetpanda.control.months.evaluate;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.budgetpanda.EmptyRecyclerView;
import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.evaluation.StatusEvaluationRepository;
import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.control.months.items.ItemsAdapter;
import com.android.budgetpanda.control.months.items.ItemsPresenter;
import com.android.budgetpanda.model.Item;

import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class EvaluateFragment extends Fragment implements ItemsRepository.ItemsRetrievingCallback, StatusEvaluationRepository.StatusEvaluationCallback, View.OnClickListener {

    private ItemsPresenter presenter;
    private ItemsAdapter adapter;
    private TextView statusTextView, statusAmountTextView;
    private Button evaluateButton;
    private View mainLayout;
    private Button reEvaluateButton;
    private View recommandationLayout;

    public EvaluateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evaluate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new ItemsPresenter(
                Injection.provideItemsRepository(sharedPreferences),
                Injection.provideMonthsTracking(sharedPreferences),
                Injection.provideStatusEvaluationRepository(sharedPreferences));


        initializeRecyclerView(view);
        initializeViews(view);

        mainLayout = view.findViewById(R.id.main_layout);
        recommandationLayout = view.findViewById(R.id.recmmandation_layout);
        evaluateButton = view.findViewById(R.id.evaluate_btn);
        reEvaluateButton = view.findViewById(R.id.re_evaluate_btn);

        if (presenter.isPreviousMonth()) {
            evaluateButton.setVisibility(View.GONE);
            reEvaluateButton.setVisibility(View.GONE);
            recommandationLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
            presenter.evaluateStatus(EvaluateFragment.this);
        } else {
            evaluateButton.setOnClickListener(this);
            reEvaluateButton.setOnClickListener(this);
        }
    }

    private void initializeViews(View view) {
        statusTextView = view.findViewById(R.id.status_textview);
        statusAmountTextView = view.findViewById(R.id.status_amount_textview);
    }

    private void initializeRecyclerView(View view) {
        EmptyRecyclerView itemsRecyclerView = view.findViewById(R.id.low_priority_items_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemsRecyclerView.getContext(),
                layoutManager.getOrientation());
        itemsRecyclerView.addItemDecoration(dividerItemDecoration);
        itemsRecyclerView.setLayoutManager(layoutManager);

        adapter = new ItemsAdapter(presenter, true);
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

    @Override
    public void onStatusEvaluated(StatusEvaluationRepository.Status status, double amount) {
        evaluateButton.setVisibility(View.GONE);
        mainLayout.setVisibility(View.VISIBLE);
        statusTextView.setText(status.name());
        statusAmountTextView.setText(String.format(Locale.getDefault(), "%.1f KD", amount));
        presenter.retrieveLowPriorityItems(this);
    }

    @Override
    public void onStatusEvaluatedFailed(String errmsg) {
        Toast.makeText(getContext(), errmsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        presenter.evaluateStatus(EvaluateFragment.this);
    }
}
