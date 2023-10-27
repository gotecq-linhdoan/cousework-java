package com.example.coursework_java;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

public class ObsDetailActivity extends AppCompatActivity {

    TextView detailDesc, detailName, detailTime, backAction;
    ImageView detailImage;

    byte [] imageData;
    Bitmap imageBitmap;
    FloatingActionButton deleteButton, editButton;
    int key, hikeId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_obs_detail);
        detailDesc = findViewById(R.id.obsDetailDes);
        detailTime = findViewById(R.id.obsDetailTime);
        detailImage = findViewById(R.id.obsDetailImage);
        detailName = findViewById(R.id.obsDetailName);
        deleteButton = findViewById(R.id.deleteObsButton);
        editButton = findViewById(R.id.editObsButton);
        backAction = findViewById(R.id.backToDetail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            detailDesc.setText("Description: " + bundle.getString("Description"));
            detailName.setText(bundle.getString("Name"));
            detailTime.setText("Time: " + bundle.getString("Time"));
            key = getIntent().getIntExtra("Key", 0);
            hikeId = getIntent().getIntExtra("HikeId", 0);
            imageData = bundle.getByteArray("Image");
            imageBitmap = getImageFromBlob(imageData);
            if (imageBitmap != null) {
                detailImage.setImageBitmap(imageBitmap);
            }
        }

        backAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObsDetailActivity.this, DetailActivity.class).putExtra("Key", hikeId);
                startActivity(intent);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ObsDetailActivity.this, ObsUpdateActivity.class)
                        .putExtra("Name", detailName.getText().toString())
                        .putExtra("Description", detailDesc.getText().toString())
                        .putExtra("Time", detailTime.getText().toString())
                        .putExtra("Image", imageData)
                        .putExtra("HikeId", hikeId)
                        .putExtra("Key", key);
                startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase database = new DatabaseHelper(ObsDetailActivity.this).getWritableDatabase();
                String deleteQuery = "DELETE FROM " + DatabaseHelper.TABLE_OBSERVATION + " WHERE " + DatabaseHelper.OBSERVATION_ID + " = ?";

                // Execute the DELETE query with the specific hike ID
                database.execSQL(deleteQuery, new Object[]{key});
                database.close();

                Toast.makeText(ObsDetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(), DetailActivity.class).putExtra("Key", key));
                finish();
            }
        });
    }
    public Bitmap getImageFromBlob(byte[] blobData) {
        if (blobData != null) {
            return BitmapFactory.decodeByteArray(blobData, 0, blobData.length);
        }
        return null;
    }
}