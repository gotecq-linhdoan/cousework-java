package com.example.coursework_java;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView detailName, detailLocation,  detailDesc, detailDate, detailLength, detailLevel, detailHasParking, backAction;
    FloatingActionButton deleteButton, editButton, addObsButton;
    List<Observation> observationList;
    Hike hike;
    RecyclerView recyclerView;
    ObservationAdapter observationAdapter;
    int key, hasParking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailName = findViewById(R.id.hikeNameDetail);
        detailLocation = findViewById(R.id.hikeDetailLocation);
        detailDesc = findViewById(R.id.hikeDescDetail);
        detailDate = findViewById(R.id.hikeDateDetail);
        detailLength = findViewById(R.id.hikeLengthDetail);
        detailLevel = findViewById(R.id.hikeLevelDetail);
        detailHasParking = findViewById(R.id.hikeParkingDetail);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);
        addObsButton = findViewById(R.id.addObsButton);
        recyclerView = findViewById(R.id.recyclerObsView);
        backAction = findViewById(R.id.backToHome);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(DetailActivity.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        observationList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            key = getIntent().getIntExtra("Key", 0);
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            hike = dbHelper.getHike(key);
            observationList = dbHelper.getObservationsForHike(key);
            detailName.setText(hike.getHikeName());
            detailLocation.setText("Location: " + hike.getHikeLocation());
            detailDesc.setText("Description: " + hike.getHikeDesc());
            detailDate.setText("Date of the Hike: " + hike.getHikeDate());
            detailLength.setText("Hike Length: " + hike.getHikeLength() + "m");
            detailLevel.setText("Hike level: " + hike.getHikeLevel());
            hasParking = hike.getHasParking();
            detailHasParking.setText("Parking Status: " + (hasParking == 1 ? "Available" : "Not Available"));
        }

        observationAdapter = new ObservationAdapter(DetailActivity.this, observationList);
        recyclerView.setAdapter(observationAdapter);
        observationAdapter.notifyDataSetChanged();

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = new DatabaseHelper(DetailActivity.this).getWritableDatabase();
                String deleteQuery = "DELETE FROM " + DatabaseHelper.TABLE_HIKE + " WHERE " + DatabaseHelper.HIKE_ID + " = ?";

                // Execute the DELETE query with the specific hike ID
                database.execSQL(deleteQuery, new Object[]{key});
                database.close();

                Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Name", detailName.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Date", detailDate.getText().toString())
                        .putExtra("Location", detailLocation.getText().toString())
                        .putExtra("Length", detailLength.getText().toString())
                        .putExtra("Level", detailLevel.getText().toString())
                        .putExtra("Parking", hasParking)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
        addObsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailActivity.this, ObsUploadActivity.class)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
    }
}