package com.example.tuempleo;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReporteActivity extends AppCompatActivity {

    private Button mButtonGoDowloadReport;
    static Bitmap bitmapPieChart;
    static Bitmap bitmapBarChart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reporte);

        mButtonGoDowloadReport = findViewById(R.id.buttonGoDowloadReport);
        BarChart barChart = findViewById(R.id.barChart);
        PieChart pieChart = findViewById(R.id.pieChart);

        mButtonGoDowloadReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bitmapPieChart = barChart.getChartBitmap();
                bitmapBarChart = pieChart.getChartBitmap();
                Intent intent = new Intent(ReporteActivity.this, DownloadReportsActivity.class);
                startActivity(intent);
                finish();
            }
        });

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