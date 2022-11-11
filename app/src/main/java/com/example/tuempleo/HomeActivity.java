package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    private RatingBar ratingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ratingBar= (RatingBar)findViewById(R.id.estrellas);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean b) {
                Toast.makeText(HomeActivity.this,"Gracias por calificarnos con"+rating,Toast.LENGTH_LONG).show();
            }
        });
    }
    public void goToFormulario(View view){
        Intent intent= new Intent(this,ResultFormActivity.class);
        startActivity(intent);
    }
    public void goToCerti1(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oyejuanjo.com/2019/11/curso-online-de-html-y-css-certificado.html"));
        startActivity(intent);
    }
    public void goToCerti2(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oyejuanjo.com/2018/05/curso-gratis-comercio-electronico-google.html"));
        startActivity(intent);
    }
    public void goToCerti3(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.oyejuanjo.com/2019/11/diploma-marketing-digital-google-certificado.html"));
        startActivity(intent);
    }
    public void goToBlog1(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.saldarriagaconcha.org/inclusion-laboral-de-personas-con-discapacidad-en-el-sector-tecnologico/"));
        startActivity(intent);
    }
    public void goToBlog2(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.plenainclusioncyl.org/innovacion/accesibilidad-cognitiva/17"));
        startActivity(intent);
    }
    public void goToInfo1(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.divulgaciondinamica.es/caracteristicas-y-tipos-de-discapacidad/"));
        startActivity(intent);
    }
    public void goToInfo2(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/bienestarmx/status/1534930462413844480"));
        startActivity(intent);
    }
}