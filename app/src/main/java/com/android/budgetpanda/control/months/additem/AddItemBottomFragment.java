package com.android.budgetpanda.control.months.additem;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.items.ItemsRepository;
import com.android.budgetpanda.backend.types.TypesRepository;
import com.android.budgetpanda.model.Item;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class AddItemBottomFragment extends BottomSheetDialogFragment implements View.OnClickListener, ItemsRepository.ItemInsertingCallback {

    private AddItemPresenter mPresenter;
    private TextInputLayout itemNameTextInput, priorityTextInput, amountTextInput;
    private EditText itemNameEditText, priorityNameEditText, amountEditText;
    private ProgressBar progressBar;
    private Button doneButton;
    private String selectedType;
    private static Item.ITEM_CATEGORY itemCategory;

    public static AddItemBottomFragment getInstance(Item.ITEM_CATEGORY itemCategory) {
        AddItemBottomFragment.itemCategory = itemCategory;
        return new AddItemBottomFragment();
    }

    public AddItemBottomFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.add_item_bottom_sheet_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        mPresenter = new AddItemPresenter(this, Injection.provideTypesRepository(), Injection.provideItemsRepository(sharedPreferences));
        initializeView(view);
        mPresenter.retrieveTypes(itemCategory, new TypesRepository.TypesRetrievingCallback() {
            @Override
            public void onTypesRetrievedSuccessfully(ArrayList<String> types) {
                setupTypeSpinner(types);
            }

            @Override
            public void onTypesRetrievedFailed(String errmsg) {
                Toast.makeText(getContext(), errmsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeView(View view) {
        itemNameTextInput = view.findViewById(R.id.itemaname_text_input_edittext);
        priorityTextInput = view.findViewById(R.id.priority_text_input_edittext);
        amountTextInput = view.findViewById(R.id.amountn_text_input_edittext);


        itemNameEditText = view.findViewById(R.id.item_name_edit_text);
        itemNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                itemNameTextInput.setError(null);
                itemNameTextInput.setErrorEnabled(false);
            }
        });
        priorityNameEditText = view.findViewById(R.id.priority_edit_text);
        priorityNameEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                priorityTextInput.setError(null);
                priorityTextInput.setErrorEnabled(false);
            }
        });

        amountEditText = view.findViewById(R.id.amount_edit_text);
        amountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                amountTextInput.setError(null);
                amountTextInput.setErrorEnabled(false);
            }
        });

        progressBar = view.findViewById(R.id.progress_bar);
        doneButton = view.findViewById(R.id.done_button);
        doneButton.setOnClickListener(this);
    }

    private void setupTypeSpinner(ArrayList<String> types) {
        TypesAdapter typesAdapter = new TypesAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item);
        typesAdapter.add("Type");

        for (String type : types) {
            typesAdapter.add(type);
        }

        Spinner typeSpinner = getView().findViewById(R.id.type_spinner);
        typeSpinner.setAdapter(typesAdapter);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String fieldHeader = (String) parent.getItemAtPosition(position);
                if (position > 0) {
                    selectedType = fieldHeader;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {
        showProgressBar();
        Item item = getItemInputDate();
        if (mPresenter.validateItemData(item)) {
            mPresenter.addNewItem(itemCategory, item, this);
        } else {
            hideProgressBar();
        }
    }

    private Item getItemInputDate() {
        Item item = new Item();
        item.setName(itemNameEditText.getText().toString().trim());
        item.setPriority(priorityNameEditText.getText().toString().isEmpty() ? 0 : Integer.valueOf(priorityNameEditText.getText().toString()));
        item.setType(selectedType);
        item.setAmount(amountEditText.getText().toString().isEmpty() ? 0 :
                Long.valueOf(amountEditText.getText().toString().trim()));
        return item;
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        doneButton.setVisibility(View.VISIBLE);
    }

    public void setItemNameInputTextErrorMessage() {
        itemNameTextInput.setError("Item name can't be empty");
    }

    public void setTypeErrorMessage() {
        Toast.makeText(getContext(), "You must select type", Toast.LENGTH_SHORT).show();
    }

    public void setPriorityErrorMessage() {
        priorityTextInput.setError("Priority can't be empty");
    }

    public void setAmountErrorMessage() {
        amountTextInput.setError("Amount can't be empty");
    }

    @Override
    public void onItemInsertedSuccessfully() {
        dismiss();
        Snackbar.make(getActivity().findViewById(android.R.id.content)
                , "Item is added successfully", Snackbar.LENGTH_LONG);
    }

    @Override
    public void onItemInsertedFailed(String errmsg) {
        dismiss();
        Toast.makeText(getContext(), errmsg, Toast.LENGTH_SHORT).show();
    }
}
