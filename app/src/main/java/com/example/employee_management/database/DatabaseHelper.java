package com.example.employee_management.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.employee_management.domain.EmployeeInfo;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "employees";
    private static final String TABLE_EMPLOYEE = "employee";
    private static final String ID = "id";
    private static final String PICTURE ="picture";
    private static final String NAME = "name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_EMPLOYEE + "("
                + ID + " INTEGER PRIMARY KEY,"
                + PICTURE + " BLOB,"
                + NAME + " TEXT,"
                + AGE + " TEXT,"
                + GENDER + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);
    }

    public void addEmployee(EmployeeInfo newEmployee) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PICTURE, newEmployee.getPicture());
        values.put(NAME, newEmployee.getName());
        values.put(AGE, newEmployee.getAge());
        values.put(GENDER, newEmployee.getGender());

        db.insert(TABLE_EMPLOYEE, null, values);
        db.close();
    }

    public ArrayList<EmployeeInfo> getAllEmployee() {
        ArrayList<EmployeeInfo> empList = new ArrayList<EmployeeInfo>();
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                EmployeeInfo emp = new EmployeeInfo();
                emp.set_id(Integer.parseInt(cursor.getString(0)));
                emp.setPicture(cursor.getBlob(1));
                emp.setName(cursor.getString(2));
                emp.setAge(cursor.getString(3));
                emp.setGender(cursor.getString(4));
                empList.add(emp);
            } while (cursor.moveToNext());
        }

        return empList;
    }

    public int updateEmployee(EmployeeInfo employeeInfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PICTURE, employeeInfo.getPicture());
        values.put(NAME, employeeInfo.getName());
        values.put(AGE, employeeInfo.getAge());
        values.put(GENDER, employeeInfo.getGender());
        return db.update(TABLE_EMPLOYEE, values, ID + " = ?",
                new String[] { String.valueOf(employeeInfo.get_id()) });
    }

    public void deleteEmployee(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEE, ID + " = ?",
                new String[] { id });
        db.close();
    }
}