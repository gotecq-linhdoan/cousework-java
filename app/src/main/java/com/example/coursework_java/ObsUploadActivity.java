package com.example.coursework_java;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.util.Calendar;

public class ObsUploadActivity extends AppCompatActivity {


    Button saveButton;
    TextView backAction;
    EditText obsName, obsTime, obsDesc;
    ImageView obsImage;
    int key;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_upload);

        saveButton = findViewById(R.id.saveButton);
        obsName = findViewById(R.id.obsName);
        obsTime = findViewById(R.id.obsTime);
        obsDesc = findViewById(R.id.obsDesc);
        obsImage = findViewById(R.id.obsImage);
        backAction = findViewById(R.id.uploadToDetail);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK){
                            Intent data = result.getData();
                            uri = data.getData();
                            obsImage.setImageURI(uri);
                        } else {
                            Toast.makeText(ObsUploadActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        obsImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        obsTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(ObsUploadActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                obsTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObsUploadActivity.this, DetailActivity.class).putExtra("Key", getIntent().getIntExtra("Key", 0));
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ObsUploadActivity.this);
                    builder.setCancelable(false);
                    builder.setView(R.layout.progress_layout);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    UploadObservation();
                }
            }
        });
    }

    private boolean validateFields() {
        String name = obsName.getText().toString();
        String time = obsTime.getText().toString();

        if (name.isEmpty()) {
            obsName.setError("Name is required");
            obsName.requestFocus();
            return false;
        }

        if (time.isEmpty()) {
            obsTime.setError("Time is required");
            obsTime.requestFocus();
            return false;
        }

        // Additional validation can be added here if needed

        return true;
    }

    public void UploadObservation() {
        String name = obsName.getText().toString();
        String desc = obsDesc.getText().toString();
        String time = obsTime.getText().toString();
        key = getIntent().getIntExtra("Key", 0);

        Observation observation = new Observation(name, desc, time, uri.toString());
        observation.setHikeId(key);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        long insertedRow = dbHelper.insertObservation(observation);

        if (insertedRow != -1) {
            Toast.makeText(ObsUploadActivity.this, "Observation added", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ObsUploadActivity.this, "Failed to add observation", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(getApplicationContext(), DetailActivity.class)
                .putExtra("Key", key));
        finish();
    }

}