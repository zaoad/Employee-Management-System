package com.example.employee_management.utils;

import android.content.Context;
import android.widget.Toast;

public class Commons {
    public static void showToast(Context context, String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
