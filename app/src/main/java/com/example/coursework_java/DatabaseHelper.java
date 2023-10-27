package com.example.coursework_java;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HikeDatabase";
    private static final int DATABASE_VERSION = 2;
    private Context context;
    // Table names
    public static final String TABLE_HIKE = "hike";
    public static final String TABLE_OBSERVATION = "observation";

    // Hike table columns
    public static final String HIKE_ID = "id";
    public static final String HIKE_NAME = "name";
    public static final String HIKE_DATE = "date";
    public static final String HIKE_DESCRIPTION = "description";
    public static final String HIKE_LOCATION = "location";
    public static final String HIKE_HAS_PARKING = "has_parking";

    public static final String HIKE_LENGTH = "length";
    public static final String HIKE_LEVEL = "level";

    // Observation table columns
    public static final String OBSERVATION_ID = "id";
    public static final String OBSERVATION_NAME = "name";
    public static final String OBSERVATION_IMAGE = "image";
    public static final String OBSERVATION_TIME = "time";
    public static final String OBSERVATION_DESCRIPTION = "description";
    public static final String HIKE_ID_FK = "hike_id"; // Foreign key for hike

    // Create hike table query
    private static final String CREATE_TABLE_HIKE = "CREATE TABLE " + TABLE_HIKE + "("
            + HIKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HIKE_NAME + " TEXT, "
            + HIKE_DATE + " TEXT, "
            + HIKE_DESCRIPTION + " TEXT, "
            + HIKE_HAS_PARKING + " INTEGER, "
            + HIKE_LOCATION + " TEXT, "
            + HIKE_LENGTH + " TEXT, "
            + HIKE_LEVEL + " TEXT"
            + ");";

    // Create observation table query
    private static final String CREATE_TABLE_OBSERVATION = "CREATE TABLE " + TABLE_OBSERVATION + "("
            + OBSERVATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + OBSERVATION_NAME + " TEXT, "
            + OBSERVATION_IMAGE + " BLOB, "
            + OBSERVATION_TIME + " TEXT, "
            + OBSERVATION_DESCRIPTION + " TEXT, "
            + HIKE_ID_FK + " INTEGER, "
            + "FOREIGN KEY (" + HIKE_ID_FK + ") REFERENCES " + TABLE_HIKE + " (" + HIKE_ID + ")"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HIKE);
        db.execSQL(CREATE_TABLE_OBSERVATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop tables if they exist and recreate them
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_OBSERVATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKE);
        onCreate(db);
    }

    public Hike getHike(int hikeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Hike hike = null;

        // Define the columns to retrieve
        String[] projection = {
                HIKE_ID,
                HIKE_NAME,
                HIKE_DATE,
                HIKE_DESCRIPTION,
                HIKE_LOCATION,
                HIKE_LENGTH,
                HIKE_LEVEL,
                HIKE_HAS_PARKING
        };

        // Define the WHERE clause to filter by hike ID
        String selection = HIKE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(hikeId) };

        Cursor cursor = db.query(
                TABLE_HIKE,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_NAME);
            int descColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_DESCRIPTION);
            int locationColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_LOCATION);
            int dateColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_DATE);
            int lengthColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_LENGTH);
            int levelColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_LEVEL);
            int hasParkingColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_HAS_PARKING);
            int hasIdColumnIndex = cursor.getColumnIndex(DatabaseHelper.HIKE_ID);

            do {
                String name = cursor.getString(nameColumnIndex);
                String desc = cursor.getString(descColumnIndex);
                String location = cursor.getString(locationColumnIndex);
                String date = cursor.getString(dateColumnIndex);
                String length = cursor.getString(lengthColumnIndex);
                String level = cursor.getString(levelColumnIndex);
                int hasParking = cursor.getInt(hasParkingColumnIndex);
                int key = cursor.getInt(hasIdColumnIndex);

                hike = new Hike(name, location, desc, date, level, length, hasParking);
                hike.setKey(key);
            } while (cursor.moveToNext());

            cursor.close();
        }

        db.close();
        return hike;
    }


    public boolean updateHike(int hikeId, String name, String date, String description, String location, String length, int hasParking, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(HIKE_NAME, name);
        values.put(HIKE_DATE, date);
        values.put(HIKE_DESCRIPTION, description);
        values.put(HIKE_LOCATION, location);
        values.put(HIKE_LENGTH, length);
        values.put(HIKE_LEVEL, level);
        values.put(HIKE_HAS_PARKING, hasParking);

        String whereClause = HIKE_ID + " = ?";
        String[] whereArgs = {String.valueOf(hikeId)};

        try {
            int rowsAffected = db.update(TABLE_HIKE, values, whereClause, whereArgs);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public boolean deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            int rowsDeleted = db.delete(TABLE_HIKE, null, null);
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    public long insertObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBSERVATION_NAME, observation.getName());
        values.put(OBSERVATION_DESCRIPTION, observation.getDesc());
        values.put(OBSERVATION_TIME, observation.getTime());
        byte[] imageByteArray = loadImageFromUri(observation.getImageUri());

        values.put(OBSERVATION_IMAGE, imageByteArray);

        values.put(HIKE_ID_FK, observation.getHikeId());

        long insertedRow = db.insert(TABLE_OBSERVATION, null, values);
        db.close();

        return insertedRow;
    }

    private byte[] loadImageFromUri(String imageUri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(Uri.parse(imageUri));
            if (inputStream != null) {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                int bytesRead;
                byte[] data = new byte[1024];

                while ((bytesRead = inputStream.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, bytesRead);
                }

                buffer.flush();
                return buffer.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public List<Observation> getObservationsForHike(int hikeId) {
        List<Observation> observationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_OBSERVATION +
                " WHERE " + HIKE_ID_FK + " = ?";

        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(hikeId)});

        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(OBSERVATION_NAME);
            int descIndex = cursor.getColumnIndex(OBSERVATION_DESCRIPTION);
            int timeIndex = cursor.getColumnIndex(OBSERVATION_TIME);
            int imageDataIndex = cursor.getColumnIndex(OBSERVATION_IMAGE);
            int keyIndex = cursor.getColumnIndex(OBSERVATION_ID);

            do {
                String name = cursor.getString(nameIndex);
                String desc = cursor.getString(descIndex);
                String time = cursor.getString(timeIndex);
                byte[] imageData = cursor.getBlob(imageDataIndex);
                int key = cursor.getInt(keyIndex);

                Observation observation = new Observation(name, desc, time, imageData);
                observation.setKey(key);
                observation.setHikeId(hikeId);
                observationList.add(observation);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return observationList;
    }

    public boolean updateObservation(Observation observation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OBSERVATION_NAME, observation.getName());
        values.put(OBSERVATION_DESCRIPTION, observation.getDesc());
        values.put(OBSERVATION_TIME, observation.getTime());

        byte[] imageByteArray = null;
        // Load the new image data from the image URI
        if (observation.getImageUri().length() > 0) {
            imageByteArray = loadImageFromUri(observation.getImageUri());
        }

        if (imageByteArray != null) {
            // Update the image data if it was successfully loaded
            values.put(OBSERVATION_IMAGE, imageByteArray);
        }

        String whereClause = OBSERVATION_ID + " = ?";
        String[] whereArgs = {String.valueOf(observation.getKey())};

        try {
            int rowsAffected = db.update(TABLE_OBSERVATION, values, whereClause, whereArgs);
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }


}
