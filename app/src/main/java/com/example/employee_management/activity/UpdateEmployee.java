package com.example.employee_management.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employee_management.R;
import com.example.employee_management.database.DatabaseHelper;
import com.example.employee_management.database.DatabaseRef;
import com.example.employee_management.domain.EmployeeInfo;
import com.example.employee_management.utils.Commons;
import com.example.employee_management.utils.Constants;
import com.example.employee_management.utils.Messages;
import com.example.employee_management.utils.SharedPrefHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class UpdateEmployee extends AppCompatActivity  implements AdapterView.OnItemSelectedListener{
    private EditText nameEditText,ageEditText;
    private Button addEmployeeButton, uploadEmployeeButton;
    private TextView idTextView;
    private Spinner spinner;
    private ImageView employeePicture;
    private String id;
    private String name,age,gender;
    private byte[] pictureByte;
    private ArrayList<EmployeeInfo> employeeInfos;
    private static final String[] genders = {"Select Gender", "Male", "Female"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_employee_layout);
        idTextView=findViewById(R.id.id_edt);
        nameEditText=findViewById(R.id.name_edt);
        ageEditText=findViewById(R.id.age_edt);
        addEmployeeButton=findViewById(R.id.add_employee_btn);
        uploadEmployeeButton=findViewById(R.id.change_photo_btn);
        spinner=findViewById(R.id.spinner);
        employeePicture=findViewById(R.id.picture);


        spinner = (Spinner)findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UpdateEmployee.this,
                android.R.layout.simple_spinner_item,genders);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        showPreviousDate();
        uploadEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    uploadEmployeeButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                }
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), Constants.GET_FROM_GALLERY);
            }
        });
        addEmployeeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    addEmployeeButton.setBackground(getResources().getDrawable(R.drawable.button_round_green));
                }
                if(nameEditText.getText()==null||ageEditText.getText()==null)
                {
                    Commons.showToast(getApplicationContext(),Messages.DATA_MISSING);
                }
                else if(gender.equals(""))
                {
                    Commons.showToast(getApplicationContext(), Messages.PLEASE_SELECT_GENDER);
                }
                else {
                    try {
                        Bitmap bitmap = ((BitmapDrawable) employeePicture.getDrawable()).getBitmap();
                        pictureByte = getBytesFromBitmap(bitmap);
                        name = String.valueOf(nameEditText.getText());
                        age = String.valueOf(ageEditText.getText());
                        if(age.length() <= 0 || name.length() <= 0 ) {
                            Commons.showToast(getApplicationContext(),Messages.DATA_MISSING);
                        }
                        else if (pictureByte.length <= 0)
                        {
                            Commons.showToast(getApplicationContext(),Messages.NO_IMAGE);
                        }
                        else {
                            DatabaseRef.getDb(getApplicationContext()).updateEmployee(new EmployeeInfo(Integer.parseInt(id), pictureByte, name, age, gender));
                            Intent mySuperIntent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(mySuperIntent);
                            finish();
                        }
                    } catch (Exception e) {
                        Commons.showToast(getApplicationContext(), Messages.DATA_INVALID);
                        Log.e("CreateEmployee", e.toString());
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    addEmployeeButton.setBackground(getResources().getDrawable(R.drawable.button_round));
                }
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                gender="";
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                gender="Male";
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                gender="Female";
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        gender="";

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==Constants.GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                employeePicture.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                uploadEmployeeButton.setBackground(getResources().getDrawable(R.drawable.button_round_blue));
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            uploadEmployeeButton.setBackground(getResources().getDrawable(R.drawable.button_round_blue));
        }
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 0, stream);
        return stream.toByteArray();
    }
    private void showPicture(byte[] pictureByte)
    {
        Bitmap bitmap= BitmapFactory.decodeByteArray(pictureByte,0,pictureByte.length);
        employeePicture.setImageBitmap(bitmap);
    }
    private void showPreviousDate()
    {
        Bundle extras=getIntent().getExtras();
        id= extras.getString(Constants.ID);
        name=extras.getString(Constants.NAME);
        age=extras.getString(Constants.AGE);
        gender=extras.getString(Constants.GENDER);
        pictureByte=extras.getByteArray(Constants.PICTURE);
        idTextView.setText("ID : "+id);
        if(pictureByte==null||pictureByte.length<=0) {
            Commons.showToast(getApplicationContext(), Messages.INVALID_IMAGE);
        }
        else
        {
            showPicture(pictureByte);
        }
        idTextView.setText("ID : "+id);
        nameEditText.setText(name);
        ageEditText.setText(age);
        if(gender.equals("Male"))
        {
            spinner.setSelection(1);
        }
        if(gender.equals("Female"))
        {
            spinner.setSelection(2);
        }

    }


}
