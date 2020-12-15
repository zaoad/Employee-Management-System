package com.example.employee_management.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.employee_management.R;
import com.example.employee_management.adapter.EmployeeAdapter;
import com.example.employee_management.database.DatabaseHelper;
import com.example.employee_management.database.DatabaseRef;
import com.example.employee_management.domain.EmployeeInfo;
import com.example.employee_management.domain.sortByIdComparator;
import com.example.employee_management.domain.sortByNameComparator;
import com.example.employee_management.fileio.ExportFile;
import com.example.employee_management.fileio.ImportFile;
import com.example.employee_management.fileio.SelectedFilePath;
import com.example.employee_management.utils.Commons;
import com.example.employee_management.utils.Constants;
import com.example.employee_management.utils.Messages;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,AdapterView.OnItemSelectedListener {

    private DatabaseHelper databaseHelper;
    private int id;
    private String name,age,gender;
    private ArrayList<EmployeeInfo> employeeInfos,tempEmployeeInfos;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;
    public EmployeeAdapter employeeAdapter;
    private LinearLayoutManager manager;
    private Spinner sortSpinner;
    private SearchView searchView;
    private static final String[] sortBy = {"sort by", "id", "name"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(this);
        setContentView(R.layout.show_employee_list);
        sortSpinner=findViewById(R.id.sort_spinner);
        searchView=findViewById(R.id.search_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //initialize drawer
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //create header view
        View headeview=navigationView.getHeaderView(0);
        recyclerView = findViewById(R.id.recyclerViewItem);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        employeeInfos=databaseHelper.getAllEmployee();
        employeeAdapter = new EmployeeAdapter(this,employeeInfos);
        recyclerView.setAdapter(employeeAdapter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                android.R.layout.simple_spinner_item,sortBy);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);
        sortSpinner.setOnItemSelectedListener(this);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                tempEmployeeInfos=new ArrayList<EmployeeInfo>();
                for(EmployeeInfo e:employeeInfos)
                {
                    if(e.getName().contains(s))
                    {
                        tempEmployeeInfos.add(e);
                    }
                }
                employeeAdapter = new EmployeeAdapter(MainActivity.this,tempEmployeeInfos);
                recyclerView.setAdapter(employeeAdapter);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                tempEmployeeInfos=new ArrayList<EmployeeInfo>();
                for(EmployeeInfo e:employeeInfos)
                {
                    if(e.getName().contains(s))
                    {
                        tempEmployeeInfos.add(e);
                    }
                }
                employeeAdapter = new EmployeeAdapter(MainActivity.this,tempEmployeeInfos);
                recyclerView.setAdapter(employeeAdapter);
                return false;
            }
        });

    }
    //Drawer fragment click listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_add_employee:
                Intent intent = new Intent(getApplicationContext(),CreateEmployee.class);
                startActivity(intent);
                break;
            case R.id.nav_import_csv:
                try {
                    Intent newIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    newIntent.setType("text/csv");
                    startActivityForResult(newIntent, Constants.REQUEST_CODE_IMPORT);
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),Messages.IMPORT_ERROR);
                }
                break;
            case R.id.nav_export_csv:

                ExportFile.exportCSV(MainActivity.this,employeeInfos);
                Commons.showToast(getApplicationContext(),"stored in csv file");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //navigation override method

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:

                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                Collections.sort(employeeInfos,new sortByIdComparator());
                employeeAdapter.notifyDataSetChanged();

                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                Collections.sort(employeeInfos, new sortByNameComparator());
                employeeAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        gender="";
        // TODO Auto-generated method stub
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        //Detects request codes
        if(requestCode==Constants.REQUEST_CODE_IMPORT && resultCode == Activity.RESULT_OK) {
            if(Build.VERSION.SDK_INT>22)
            {
                requestPermissions(new String[] {READ_EXTERNAL_STORAGE},1);
            }
            Uri uri = data.getData();
            String path = SelectedFilePath.getPath(MainActivity.this,uri);
            Log.e("MainActivity",path);
            File newFile=new File(path);

            //path = path.substring(path.indexOf(":") + 1);
            try { ImportFile.importCSV(getApplicationContext(),newFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Commons.showToast(getApplicationContext(),"Imported data from "+path);
            Intent mySuperIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mySuperIntent);
            finish();
        }
    }
}
