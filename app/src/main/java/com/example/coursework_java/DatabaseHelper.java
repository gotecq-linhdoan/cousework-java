package com.example.coursework_java;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HikeDatabase";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_HIKES = "hikes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESC = "description";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_LENGTH = "length";
    public static final String COLUMN_HAS_PARKING = "has_parking";
    public static final String COLUMN_OBSERVATIONS_JSON = "observations_json";

    private static final String DATABASE_CREATE = "create table "
            + TABLE_HIKES + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_NAME
            + " text not null, " + COLUMN_DESC + " text, "
            + COLUMN_LOCATION + " text, " + COLUMN_DATE + " text, "
            + COLUMN_LENGTH + " text, " + COLUMN_HAS_PARKING + " integer, "
            + COLUMN_LEVEL + " text, "
            + COLUMN_OBSERVATIONS_JSON + " text);";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HIKES);
        onCreate(db);
    }

    public void deleteAllHikes() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_HIKES, null, null);
        db.close();
    }

    public void updateHike(int id, String name, String desc, String location, String date, String length, int hasParking, String level) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESC, desc);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_LENGTH, length);
        values.put(COLUMN_HAS_PARKING, hasParking);
        values.put(COLUMN_LEVEL, level);
        String[] whereArgs = {String.valueOf(id)};
        db.update(TABLE_HIKES, values, COLUMN_ID + "=?", whereArgs);
        db.close();
    }
}
