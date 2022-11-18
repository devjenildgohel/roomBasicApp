package com.example.roomexampleapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roomexampleapp.Adapter.DataAdapter;
import com.example.roomexampleapp.DAO.ItemDAO;
import com.example.roomexampleapp.Database.APPDB;
import com.example.roomexampleapp.Entity.ItemClass;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    CardView data;
    RecyclerView recyclerView;
    TextView textView;
    AppCompatButton addNewData;

    Context context;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        textView = findViewById(R.id.empty);
        data = findViewById(R.id.data);
        recyclerView = findViewById(R.id.dbData_listView);
        addNewData = findViewById(R.id.addDataToDatabase);

        progressBar = findViewById(R.id.progressbar);

        final APPDB database = Room.databaseBuilder(this, APPDB.class, "itemdb")
                .allowMainThreadQueries()
                .build();

        buildRecyclerView(database);


        addNewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

                View alertView = getLayoutInflater().inflate(R.layout.add_new_data_layout, null);
                alert.setView(alertView);

                EditText taskNameEditText = alertView.findViewById(R.id.courseNameEdittext);
                EditText taskDescriptionEditText = alertView.findViewById(R.id.courseDescriptionEdittext);
                AppCompatButton AddData = alertView.findViewById(R.id.add);


                final AlertDialog alertDialog = alert.show();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                AddData.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            String taskName = taskNameEditText.getText().toString().trim();
                            String taskDesc = taskDescriptionEditText.getText().toString().trim();

                            if(taskName.isEmpty())
                            {
                                taskNameEditText.setError("Required");
                                taskNameEditText.requestFocus();
                                return;
                            }
                            if(taskDesc.isEmpty())
                            {
                                taskDescriptionEditText.setError("Required");
                                taskDescriptionEditText.requestFocus();
                                return;
                            }

                            dataAdding(taskName,taskDesc);

                        } catch (Exception e) {
                            Log.e("TAG", "onClick: " + e);
                        }
                        buildRecyclerView(database);
                        alertDialog.dismiss();
                    }

                    private void dataAdding(String taskName, String taskDesc) {
                        ItemDAO itemDAO = database.getItemDAO();
                        ItemClass itemClass = new ItemClass();

                        itemClass.setTaskName(taskName);
                        itemClass.setTaskDesc(taskDesc);

                        itemDAO.insert(itemClass);
                        Toast.makeText(MainActivity.this, "Data Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                ItemDAO itemDAO = database.getItemDAO();
                ArrayList<ItemClass> items = new ArrayList<ItemClass>(itemDAO.getAllItems());

                progressBar.setVisibility(View.VISIBLE);

                if (!items.isEmpty()) {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.GONE);
                    data.setVisibility(View.VISIBLE);
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    data.setVisibility(View.GONE);
                }
                handler.postDelayed(this, 1);
            }
        };
        handler.postDelayed(r, 1000);
    }
    private void buildRecyclerView(APPDB database) {
        ItemDAO itemDAO = database.getItemDAO();
        ArrayList<ItemClass> items = new ArrayList<ItemClass>(itemDAO.getAllItems());

            try {
                Log.e("TAG", String.valueOf(items.size()));
                DataAdapter adapter = new DataAdapter(context, items);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
            } catch (Exception e) {
                Log.e("TAG", "buildRecycleView: " + e);
            }
        }

    }

