package com.JosephCantrell.cameramap.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhotoDbHelper extends SQLiteOpenHelper {

    public PhotoDbHelper(Context context) {
        super(context, PhotoContract.DB_NAME, null, PhotoContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " +
                PhotoContract.PhotoEntry.TABLE + " " +                       // Table's name
                "(" +                           // The columns in the table
                PhotoContract.PhotoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PhotoContract.PhotoEntry.COL_PHOTO_NAME + " TEXT NOT NULL," +
                PhotoContract.PhotoEntry.COL_PHOTO_LAT + " DOUBLE NOT NULL, " +
                PhotoContract.PhotoEntry.COL_PHOTO_LONG + " DOUBLE NOT NULL, " +
                PhotoContract.PhotoEntry.COL_PHOTO_TIME + " DATE NOT NULL )";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PhotoContract.PhotoEntry.TABLE);
        onCreate(db);
    }
}