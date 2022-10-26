package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class MenuPrincipalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }
    public void goToInicio(View view){
        Intent intent= new Intent(this,HomeActivity.class);
        startActivity(intent);
    }
    public void goToAbout(View view){
        Intent intent= new Intent(this,AboutActivity.class);
        startActivity(intent);
    }
    public void goToSigIn(View view){
        Intent intent= new Intent(this,SignUpActivity.class);
        startActivity(intent);
    }
    public void goToLogIn(View view){
        Intent intent= new Intent(this,LogInActivity.class);
        startActivity(intent);
    }
}