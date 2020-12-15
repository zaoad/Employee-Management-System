package com.example.employee_management.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.employee_management.R;
import com.example.employee_management.database.DatabaseRef;
import com.example.employee_management.utils.Commons;
import com.example.employee_management.utils.Constants;
import com.example.employee_management.utils.Messages;

public class ShowEmployeeDetails extends AppCompatActivity {
    private ImageView editDetailsImg,pictureImg;
    private Button deleteEmployeeBtn;
    private TextView nameTextView,idTextView,ageTextView,genderTextView;
    private String name,age,gender;
    private String id;
    private byte[] pictureByte;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.employee_profile);
        editDetailsImg=findViewById(R.id.edit_btn);
        pictureImg=findViewById(R.id.picture);
        deleteEmployeeBtn=findViewById(R.id.delete_btn);
        nameTextView=findViewById(R.id.employee_name);
        idTextView=findViewById(R.id.employee_id);
        ageTextView=findViewById(R.id.employee_age);
        genderTextView=findViewById(R.id.employee_gender);
        Bundle extras=getIntent().getExtras();
        id= extras.getString(Constants.ID);
        name=extras.getString(Constants.NAME);
        age=extras.getString(Constants.AGE);
        gender=extras.getString(Constants.GENDER);
        pictureByte=extras.getByteArray(Constants.PICTURE);
        if(pictureByte==null||pictureByte.length<=0) {
            Commons.showToast(getApplicationContext(), Messages.INVALID_IMAGE);
        }
        else
        {
            showPicture(pictureByte);
        }
        idTextView.setText(""+id);
        nameTextView.setText(name);
        ageTextView.setText(age);
        genderTextView.setText(gender);
        editDetailsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),UpdateEmployee.class);
                intent.putExtra(Constants.ID, ""+id);
                intent.putExtra(Constants.NAME, name);
                intent.putExtra(Constants.AGE, age);
                intent.putExtra(Constants.GENDER, gender);
                intent.putExtra(Constants.PICTURE,pictureByte);
                startActivity(intent);
            }
        });
        deleteEmployeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseRef.getDb(getApplicationContext()).deleteEmployee(id);
                Intent mySuperIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mySuperIntent);
                finish();
            }
        });
    }
    public void showPicture(byte[] pictureByte)
    {
        Log.e("checkpicture",pictureImg.getWidth()+" "+pictureImg.getHeight());
        Bitmap bitmap= BitmapFactory.decodeByteArray(pictureByte,0,pictureByte.length);
        pictureImg.setImageBitmap(bitmap);
    }
}
