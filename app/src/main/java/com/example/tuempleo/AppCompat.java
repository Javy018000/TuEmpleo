package com.example.tuempleo;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AppCompat extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LanguageManage languageManage= new LanguageManage(this);
        languageManage.updateResoulce(languageManage.getLan());
    }
}