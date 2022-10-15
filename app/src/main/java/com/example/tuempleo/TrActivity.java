package com.example.tuempleo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

public class TrActivity extends AppCompat {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tr);

        ImageButton es=findViewById(R.id.btn_ln_es);
        ImageButton en=findViewById(R.id.btn_ln_en);
        ImageButton hi=findViewById(R.id.btn_ln_hi);
        ImageButton zh=findViewById(R.id.btn_ln_zh);

        LanguageManage lang = new LanguageManage(this);
        es.setOnClickListener(view ->
        { lang.updateResoulce("es");
            recreate();
        });

        en.setOnClickListener(view ->
        { lang.updateResoulce("en");
            recreate();
        });

        hi.setOnClickListener(view ->
        { lang.updateResoulce("hi");
            recreate();
        });
        zh.setOnClickListener(view ->
        { lang.updateResoulce("zh");
            recreate();
        });
    }
}