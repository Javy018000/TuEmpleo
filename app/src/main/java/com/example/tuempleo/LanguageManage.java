package com.example.tuempleo;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguageManage {
    private Context ct;
    private SharedPreferences sharedPreferences;
    
    public LanguageManage(Context ctx)
    {
        ct=ctx;
        sharedPreferences =ct.getSharedPreferences("LANG", Context.MODE_PRIVATE);
    }
    public void updateResoulce(String code){
        Locale locale =new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale =locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        setLang(code);
    }
    public String getLan(){
        return sharedPreferences.getString("lang","en");
    }

    public void setLang(String code){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lang",code);
        editor.commit();
    }
}
