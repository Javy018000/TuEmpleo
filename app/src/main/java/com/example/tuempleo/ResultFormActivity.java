package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class ResultFormActivity extends AppCompatActivity {
    private RadioButton rb0,rb1,rb2,rb3,rb4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_form);
        rb0=(RadioButton) findViewById(R.id.radioButton0);
        rb1=(RadioButton) findViewById(R.id.radioButton1);
        rb2=(RadioButton) findViewById(R.id.radioButton2);
        rb3=(RadioButton) findViewById(R.id.radioButton3);
        rb4=(RadioButton) findViewById(R.id.radioButton4);
    }
    public void goToResultPrue(View view){
        Intent intent= new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    public void goToCheck(View view){
        if(rb0.isChecked()==true){
            Intent intent= new Intent(this,R1Activity.class);
            startActivity(intent);
        }
        else if(rb1.isChecked()==true){
            Intent intent= new Intent(this,R2Activity.class);
            startActivity(intent);
        }
        else if(rb2.isChecked()==true){
            Intent intent= new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else if(rb3.isChecked()==true){
            Intent intent= new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else if(rb4.isChecked()==true){
            Intent intent= new Intent(this,HomeActivity.class);
            startActivity(intent);
        }
        else{
            Intent intent= new Intent(this,TrActivity.class);
            startActivity(intent);
        }
    }
}