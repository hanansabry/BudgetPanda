package com.android.budgetpanda.control.months;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.control.months.evaluate.EvaluateFragment;
import com.android.budgetpanda.control.months.expenses.ExpensesFragment;
import com.android.budgetpanda.control.months.income.IncomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class MonthDataActivity extends AppCompatActivity {

    private MonthsDataAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MonthsDataPresenter presenter;
    private ImageButton previous;
    private ImageButton next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_data);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new MonthsDataPresenter(Injection.provideMonthsTracking(sharedPreferences));

        setMonthTitle();
        setupTabLayout();
        initializeMonthsPreviousAndNext();
    }

    private void initializeMonthsPreviousAndNext() {
        previous = findViewById(R.id.previous_button);
        next = findViewById(R.id.forward_button);
    }

    private void setMonthTitle() {
        TextView monthTitleTv = findViewById(R.id.month_title_tv);
        monthTitleTv.setText(String.format(Locale.getDefault(), "%s - %d",
                presenter.getSelectedMonthAsString(), presenter.getSelectedYear()));
    }

    private void setupTabLayout() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        adapter = new MonthsDataAdapter(getSupportFragmentManager());

        adapter.addFragment(new IncomeFragment(), "Income");
        adapter.addFragment(new ExpensesFragment(), "Expenses");
        adapter.addFragment(new EvaluateFragment(), "Evaluate");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        setTabsIcons();
    }

    private void setTabsIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.income);
        tabLayout.getTabAt(1).setIcon(R.drawable.expenses);
        tabLayout.getTabAt(2).setIcon(R.drawable.evaluaate);
    }

    public void onPreviousClicked(View view) {
        if (presenter.getSelectedMonth() == presenter.getFirstMonth()
                && presenter.getSelectedYear() == presenter.getFirstYear()) {
            onBackPressed();
        } else if (presenter.getSelectedMonth() > presenter.getFirstMonth()
                || presenter.getSelectedYear() != presenter.getFirstYear()) {
            presenter.setSelectedMonthToPreviousOne();
            setMonthTitle();
            adapter.notifyDataSetChanged();
            setTabsIcons();
        } else {
            onBackPressed();
        }
    }

    public void onNextClicked(View view) {
        presenter.setSelectedMonthToNextOne();
        setMonthTitle();
        adapter.notifyDataSetChanged();
        setTabsIcons();
    }
}
