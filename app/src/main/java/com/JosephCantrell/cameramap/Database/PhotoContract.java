package com.JosephCantrell.cameramap.Database;

import android.provider.BaseColumns;

public class PhotoContract {
    public static final String DB_NAME = "com.JosephCantrell.cameramap.Database";
    public static final int DB_VERSION = 1;

    public class PhotoEntry implements BaseColumns {
        public static final String TABLE = "photos";
        public static final String COL_PHOTO_NAME = "name";
        public static final String COL_PHOTO_LAT = " latitude";
        public static final String COL_PHOTO_LONG = "longitude";
        public static final String COL_PHOTO_TIME = "time";
    }
}