package com.example.coursework_java;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.net.Uri;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

public class ObsUpdateActivity extends AppCompatActivity {


    Button updateButton;
    EditText obsName, obsTime, obsDesc;
    TextView backAction;
    ImageView obsImage;
    int key, hikeId;
    Uri uri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_update);

        updateButton = findViewById(R.id.updateObsButton);
        obsName = findViewById(R.id.updateObsName);
        obsTime = findViewById(R.id.updateObsTime);
        obsImage = findViewById(R.id.updateObsImage);
        obsDesc = findViewById(R.id.updateObsDesc);
        backAction = findViewById(R.id.updateToDetail);
        Bundle bundle = getIntent().getExtras();

        if(bundle != null) {
            obsName.setText(bundle.getString("Name"));
            obsTime.setText(bundle.getString("Time").split(": ")[1]);
            obsDesc.setText(bundle.getString("Description").split(": ")[1]);
            obsImage.setImageBitmap(getImageFromBlob(bundle.getByteArray("Image")));
            key = getIntent().getIntExtra("Key", 0);
            hikeId = getIntent().getIntExtra("HikeId", 0);
        }

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
                            Toast.makeText(ObsUpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObsUpdateActivity.this, DetailActivity.class).putExtra("Key", hikeId);
                startActivity(intent);
            }
        });
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

                TimePickerDialog timePickerDialog = new TimePickerDialog(ObsUpdateActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                obsTime.setText(hourOfDay + ":" + minute);
                            }
                        }, hour, minute, false);
                timePickerDialog.show();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ObsUpdateActivity.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                UpdateObservation();
            }
        });
    }
    public Bitmap getImageFromBlob(byte[] blobData) {
        if (blobData != null) {
            return BitmapFactory.decodeByteArray(blobData, 0, blobData.length);
        }
        return null;
    }

    public void UpdateObservation() {
        String name = obsName.getText().toString();
        String desc = obsDesc.getText().toString();
        String time = obsTime.getText().toString();

        Observation observation = new Observation(name, desc, time, uri != null ? uri.toString() : "");
        observation.setKey(key);
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        boolean success = dbHelper.updateObservation(observation);

        if (success) {
            Toast.makeText(ObsUpdateActivity.this, "Observation updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ObsUpdateActivity.this, "Error updating observation", Toast.LENGTH_SHORT).show();
        }

        startActivity(new Intent(getApplicationContext(), DetailActivity.class)
                .putExtra("Key", hikeId));
        finish();
    }
}
