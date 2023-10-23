package com.example.coursework_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

public class DetailActivity extends AppCompatActivity {

    TextView detailName, detailLocation,  detailDesc, detailDate, detailLength, detailLevel, detailHasParking;
    FloatingActionButton deleteButton, editButton;
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

        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            detailName.setText(bundle.getString("Name"));
            detailLocation.setText("Location: " + bundle.getString("Location"));
            detailDesc.setText(bundle.getString("Description"));
            detailDate.setText("Date of the Hike: " + bundle.getString("Date"));
            detailLength.setText("Hike Length: " + bundle.getString("Length") + "m");
            detailLevel.setText("Hike level: " + bundle.getString("Level"));
            hasParking = getIntent().getIntExtra("Parking", 0);
            detailHasParking.setText("Parking Status: " + (hasParking == 1 ? "Available" : "Not Available"));
            key = getIntent().getIntExtra("Key", 0);
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = new DatabaseHelper(DetailActivity.this).getWritableDatabase();
                String deleteQuery = "DELETE FROM " + DatabaseHelper.TABLE_HIKES + " WHERE " + DatabaseHelper.COLUMN_ID + " = ?";

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
    }
}