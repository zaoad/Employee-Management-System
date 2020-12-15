package com.example.employee_management.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefHelper {

    private SharedPreferences sharedpreferences;
    private Context context;

    public SharedPrefHelper(Context context){
        this.context = context;
        sharedpreferences = context.getSharedPreferences(Constants.SHARED_PREF_NAME, context.MODE_PRIVATE);
    }
    public boolean saveDataToSharedPref(String key, String value){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public String getStringFromSharedPref(String key){
        return sharedpreferences.getString(key, null);
    }
}
