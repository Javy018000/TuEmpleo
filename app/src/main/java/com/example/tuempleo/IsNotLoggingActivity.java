package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class IsNotLoggingActivity extends AppCompatActivity {


    Button mButtonIrRegistrar;
    Button mButtonIrIniciarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_is_not_logging);
        mButtonIrIniciarSesion = findViewById(R.id.btnGoLogIn);
        mButtonIrRegistrar = findViewById(R.id.btnGoRegister);

        mButtonIrIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IsNotLoggingActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        mButtonIrRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IsNotLoggingActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}