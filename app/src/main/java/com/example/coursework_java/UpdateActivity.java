package com.example.coursework_java;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {
    Button updateButton;
    EditText hikeName, hikeLength, hikeDesc, hikeLocation, hikeDate;
    Spinner hikeSpinner;
    String hikeLevel;
    RadioGroup parkingStatus;
    int year, month, day, key, parkingInt, hasParking;
    int levelPositon = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        hikeName = findViewById(R.id.updateHikeName);
        hikeLength = findViewById(R.id.updateHikeLength);
        hikeDesc = findViewById(R.id.updateHikeDesc);
        hikeLocation = findViewById(R.id.updateHikeLocation);
        hikeDate = findViewById(R.id.updateHikeDate);
        hikeSpinner = findViewById(R.id.updateHikeLevel);
        parkingStatus = findViewById(R.id.parkingStatus);

        hikeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            hikeName.setText(bundle.getString("Name"));
            hikeLength.setText(bundle.getString("Length").split(": ")[1].replace("m", ""));
            hikeDesc.setText(bundle.getString("Description"));
            hikeLocation.setText(bundle.getString("Location").split(": ")[1]);
            hikeDate.setText(bundle.getString("Date").split(": ")[1]);

            parkingInt = getIntent().getIntExtra("Parking", 0);

            String stringLevel = bundle.getString("Level").split(": ")[1];
            if(stringLevel == "Easy") {
                levelPositon = 0;
            } else if (stringLevel == "Medium") {
                levelPositon = 1;
            } else {
                levelPositon = 2;
            }
            hikeSpinner.setSelection(levelPositon);
            if(parkingInt == 1) {
                parkingStatus.check(R.id.parkingAvailable);
            } else {
                parkingStatus.check(R.id.noParkingAvailable);
            }
            key = getIntent().getIntExtra("Key", 0);

        }

        hikeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                hikeLevel = hikeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                hikeLevel = "Easy";
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                updateHike();
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

    public void updateHike () {
        String name = hikeName.getText().toString();
        String desc = hikeDesc.getText().toString();
        String location = hikeLocation.getText().toString();
        String length = hikeLength.getText().toString();
        String date = hikeDate.getText().toString();

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.updateHike(key, name, desc, location, date, length, hasParking, hikeLevel);

        Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
        // Return to the MainActivity or perform any other desired actions
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }
}
