package com.android.budgetpanda.control.statistics;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.budgetpanda.Injection;
import com.android.budgetpanda.R;
import com.android.budgetpanda.backend.items.ItemsRepository;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;

public class StatisticsActivity extends AppCompatActivity implements ItemsRepository.AllMonthsDataRetrievingCallback {

    private StatisticsPresenter presenter;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("Months Statistics");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        presenter = new StatisticsPresenter(Injection.provideItemsRepository(sharedPreferences));
        presenter.getAllMonthsDataForGraph(this);

        initializeChart();
    }

    private void initializeChart() {
        chart = findViewById(R.id.chart);
        chart.setDescription("chart for difference between income and expenses");
        chart.animateXY(2000, 2000);
        chart.invalidate();
        chart.setStartAtZero(false);
        chart.setValueTextSize(24);
        chart.setValueTextColor(getResources().getColor(R.color.colorPrimary));
        chart.setGridColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public void onDataRetrievedSuccessfully(HashMap<Integer, Double> monthsStatus) {
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();
        int i=0;
        for (int month : monthsStatus.keySet()) {
            labels.add(presenter.getSelectedMonthAsString(month));
            entries.add(new BarEntry(monthsStatus.get(month).floatValue(), i++));
        }

        BarDataSet dataset = new BarDataSet(entries, "Months");
//        dataset.setColor(Color.rgb(241, 109, 97));
        dataset.setColor(Color.rgb(246, 209, 58));
        BarData data = new BarData(labels, dataset);
        chart.setData(data);
    }

    private ArrayList getXAxisValues() {
        ArrayList xAxis = new ArrayList();
        xAxis.add("JAN");
        xAxis.add("FEB");
        xAxis.add("MAR");
        xAxis.add("APR");
        xAxis.add("MAY");
        xAxis.add("JUN");

        return xAxis;

    }

    @Override
    public void onDataRetrievedFailed(String errmsg) {
        Toast.makeText(this, errmsg, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
}
