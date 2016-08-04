package in.ashwanik.udacitystockhawk.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.ashwanik.retroclient.utils.Json;
import in.ashwanik.udacitystockhawk.R;
import in.ashwanik.udacitystockhawk.db.StockDao;
import in.ashwanik.udacitystockhawk.db.StockHistoryModel;
import in.ashwanik.udacitystockhawk.db.StockModel;

/**
 * A placeholder fragment containing a simple view.
 */
public class GraphActivityFragment extends BaseFragment {

    View view;
    @Bind(R.id.chart)
    LineChart lineChart;
    List<StockHistoryModel> history;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_graph, container, false);
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this, view);
        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle == null) {
            bundle = getArguments();
        }
        long stockId = bundle.getLong("stockId");
        history = StockDao.getStockHistory(stockId);
        Collections.reverse(history);
        XAxis xAxis = lineChart.getXAxis();
        StockModel stockModel = StockDao.getStock(stockId);
        lineChart.setDescription(stockModel.getName());
        lineChart.setContentDescription(stockModel.getName());

        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(true);
        xAxis.setTextColor(getResources().getColor(R.color.accent));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(60000L); // one minute in millis
        xAxis.setValueFormatter(new AxisValueFormatter() {

            private FormattedStringCache.Generic<Long, Date> mFormattedStringCache = new FormattedStringCache
                    .Generic<>(new SimpleDateFormat("dd MMM HH:mm"));

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Long v = (long) value;
                return mFormattedStringCache.getFormattedValue(new Date(v), v);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setAxisMaxValue(1000f);
        leftAxis.setGranularity(.2f);
        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(getResources().getColor(R.color.accent));

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);
        ArrayList<Entry> yValues = new ArrayList<>();
        for (StockHistoryModel historyModel : history) {
            yValues.add(new Entry(historyModel.getCreatedAt().getTime(), Float.parseFloat(historyModel.getBid())));
        }

        logDebug(Json.serialize(yValues));
        LineDataSet set1 = new LineDataSet(yValues, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);
        lineChart.setData(data);
        List<ILineDataSet> sets = lineChart.getData()
                .getDataSets();

        for (ILineDataSet iSet : sets) {
            LineDataSet set = (LineDataSet) iSet;
            set.setDrawCircles(true);
        }
        lineChart.invalidate();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
