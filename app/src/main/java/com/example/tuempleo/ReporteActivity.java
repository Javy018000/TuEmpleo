package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);
        PieChart pieChart = findViewById(R.id.pieChart);
        ArrayList<PieEntry> inf = new ArrayList<>();
        inf.add(new PieEntry(508, "2014"));
        inf.add(new PieEntry(700, "2017"));
        inf.add(new PieEntry(618, "2019"));
        inf.add(new PieEntry(410, "2015"));

        PieDataSet pieDataSet = new PieDataSet(inf, "Usuarios registrados");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(14f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("Registrados");
        pieChart.animate();

        BarChart barChart = findViewById(R.id.barChart);
        ArrayList<BarEntry> infBar = new ArrayList<>();
        infBar.add(new BarEntry(415, 142));
        infBar.add(new BarEntry(240, 354));
        infBar.add(new BarEntry(147, 257));
        infBar.add(new BarEntry(397, 512));
        infBar.add(new BarEntry(514, 417));

        BarDataSet barDataSet = new BarDataSet(infBar, "Label");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(14f);

        BarData barData = new BarData(barDataSet);
        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Label2");
        barChart.animateY(2000);

    }
}