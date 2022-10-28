package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class R1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r1);
    }
    public void goToLink(View view){
        Intent intent= new Intent(Intent.ACTION_VIEW, Uri.parse(""));
        startActivity(intent);
    }
}
