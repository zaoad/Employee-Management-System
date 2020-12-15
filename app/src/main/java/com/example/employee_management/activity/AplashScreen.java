package com.example.employee_management.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.employee_management.R;
import com.example.employee_management.utils.Constants;
import com.example.employee_management.utils.SharedPrefHelper;


public class AplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME=1500;
    private SharedPrefHelper sharedPrefHelper;
    private String initIdCount="0";//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        initIdCount=sharedPrefHelper.getStringFromSharedPref(Constants.ID_COUNT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        if(initIdCount==null || initIdCount.equals("0"))
        {
            sharedPrefHelper.saveDataToSharedPref(Constants.ID_COUNT,"1");
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mySuperIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
}
