package com.example.coursework_java;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class UploadActivity extends AppCompatActivity {

    Button saveButton;

    TextView backAction;
    EditText hikeName, hikeLength, hikeDesc, hikeLocation, hikeDate;
    String hikeLevel;
    int hasParking;
    int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        saveButton = findViewById(R.id.saveButton);
        hikeName = findViewById(R.id.hikeName);
        hikeLength = findViewById(R.id.hikeLength);
        hikeDesc = findViewById(R.id.hikeDesc);
        hikeLocation = findViewById(R.id.hikeLocation);
        hikeDate = findViewById(R.id.hikeDate);
        backAction = findViewById(R.id.uploadToHome);

        hikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Spinner hikeLevelSpinner = findViewById(R.id.hikeLevel);
        hikeLevelSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                hikeLevel = hikeLevelSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                hikeLevel = "Easy";
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                uploadData();
            }
        });

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create a date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Set the selected date to the EditText
                hikeDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year); // Format the date as needed
            }
        }, year, month, day);

        // Show the date picker dialog
        datePickerDialog.show();
    }
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        int id = view.getId();
        if(id == R.id.parkingAvailable) {
            if(checked) {
                hasParking = 1;
            }
        } else {
            if(checked) {
                hasParking = 0;
            }
        }
    }
    public void uploadData () {
        String name = hikeName.getText().toString();
        String desc = hikeDesc.getText().toString();
        String location = hikeLocation.getText().toString();
        String length = hikeLength.getText().toString();
        String date = hikeDate.getText().toString();

        Hike hike = new Hike(name, location, desc, date, hikeLevel, length, hasParking);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.HIKE_NAME, hike.getHikeName());
        values.put(DatabaseHelper.HIKE_DESCRIPTION, hike.getHikeDesc());
        values.put(DatabaseHelper.HIKE_LOCATION, hike.getHikeLocation());
        values.put(DatabaseHelper.HIKE_DATE, hike.getHikeDate());
        values.put(DatabaseHelper.HIKE_LENGTH, hike.getHikeLength());
        values.put(DatabaseHelper.HIKE_HAS_PARKING, hike.getHasParking());
        values.put(DatabaseHelper.HIKE_LEVEL, hike.getHikeLevel());


        long newRowId = database.insert(DatabaseHelper.TABLE_HIKE, null, values);

        dbHelper.close();

        if (newRowId != -1) {
            Toast.makeText(UploadActivity.this, "Hike saved with ID: " + newRowId, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(UploadActivity.this, "Error saving hike", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}