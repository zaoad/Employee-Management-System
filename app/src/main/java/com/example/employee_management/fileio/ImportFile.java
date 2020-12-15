package com.example.employee_management.fileio;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.example.employee_management.activity.MainActivity;
import com.example.employee_management.database.DatabaseHelper;
import com.example.employee_management.database.DatabaseRef;
import com.example.employee_management.domain.EmployeeInfo;
import com.example.employee_management.utils.Commons;
import com.example.employee_management.utils.Constants;
import com.example.employee_management.utils.SharedPrefHelper;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.LinkOption;
import java.util.ArrayList;

public class ImportFile {
    public static ArrayList<EmployeeInfo>employeeInfos;
    private static SharedPrefHelper sharedPrefHelper;
    public static void importCSV(Context context,File file) throws IOException {

        sharedPrefHelper=new SharedPrefHelper(context);
        int id=Integer.parseInt(sharedPrefHelper.getStringFromSharedPref(Constants.ID_COUNT));

        //File file = new File(Environment.getExternalStorageDirectory(), path);
        CSVReader reader = new CSVReader(new FileReader(file));
        String[] nextLine;
        boolean header = true;
        boolean image=true;
        int index=1;
        while ((nextLine = reader.readNext()) != null) {
            Log.e("importFileCheck",""+nextLine.length);
            if(nextLine.length<5)
                continue;
            String eid=nextLine[0];
            String encodedImg = nextLine[1];
            encodedImg.replaceAll("#","\\n");
            String name = nextLine[2];
            String age = nextLine[3];
            String gender = nextLine[4];
            byte[] imageData = Base64.decode(encodedImg, Base64.DEFAULT);
            EmployeeInfo emp = new EmployeeInfo(imageData, name, age, gender);
            DatabaseRef.getDb(context).addEmployee(emp);
        }
        return;
    }

}
