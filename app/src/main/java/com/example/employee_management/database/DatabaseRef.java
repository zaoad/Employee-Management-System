package com.example.employee_management.database;

import android.content.Context;

public class DatabaseRef {
    private static DatabaseHelper dbc;
    private DatabaseRef(){}
    public static DatabaseHelper getDb(Context context)
    {
        if(dbc==null)
        {
            dbc=new DatabaseHelper(context);
        }
        return dbc;
    }

}
