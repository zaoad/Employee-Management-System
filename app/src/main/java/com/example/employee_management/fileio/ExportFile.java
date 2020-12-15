package com.example.employee_management.fileio;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import androidx.core.content.FileProvider;

import com.example.employee_management.domain.EmployeeInfo;
import com.example.employee_management.utils.Constants;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EmptyStackException;

public class ExportFile {
    public static void exportCSV(Context context, ArrayList<EmployeeInfo> employeeInfos) {
        //generate data
        StringBuilder data = new StringBuilder();
        for(EmployeeInfo e: employeeInfos)
        {
            byte[] bytearr = e.getPicture();
            String strPicture = Base64.encodeToString(bytearr, Base64.DEFAULT);
            Log.e("strPicture",strPicture);
            strPicture=strPicture.replaceAll("\\n","#");
            Log.e("strPicture",strPicture);
            data.append("\n"+String.valueOf(e.get_id())+" , "+strPicture+" ,"+e.getName()+" , "+e.getAge()+" , "+e.getGender());
        }
        /*for (int i = 0; i < 5; i++) {
            data.append("\n" + String.valueOf(i) + "," + String.valueOf(i * i));
        }
        */
        try {
            //saving the file into device
            FileOutputStream out = context.openFileOutput("data.csv", Context.MODE_PRIVATE);
            out.write((data.toString()).getBytes());
            out.close();

        /*
        File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, Constants.EXPORTED_FILE_NAME);
        String dir=file.getParent();
        try {
            file.createNewFile();
            CSVWriter csvWrite = new CSVWriter(new FileWriter(file));

            for(EmployeeInfo e : employeeInfos) {
                byte[] bytearr = e.getPicture();
                String strPicture = Base64.encodeToString(bytearr, Base64.DEFAULT);
                // str = new String(bytearr, "UTF-8");
                String arrStr[] ={""+e.get_id(), strPicture, e.getName(), e.getAge(), e.getGender()};
                csvWrite.writeNext(arrStr);
            }
            csvWrite.close();*/
            //exporting
            File filelocation = new File(context.getFilesDir(), "data.csv");
            Uri path = FileProvider.getUriForFile(context, "com.example.exportcsv.fileprovider", filelocation);
            Intent fileIntent = new Intent(Intent.ACTION_SEND);
            fileIntent.setType("text/csv");
            fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Data");
            fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            fileIntent.putExtra(Intent.EXTRA_STREAM, path);
            context.startActivity(Intent.createChooser(fileIntent, "Send mail"));
        /*}
        catch(Exception sqlEx) {
            Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
        }
        return dir;*/
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

