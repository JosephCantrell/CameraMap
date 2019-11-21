package com.JosephCantrell.cameramap;

import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class PictureContent {
    static final List<PictureItem> ITEMS = new ArrayList<>();

    static AndroidExif exif = new AndroidExif();

    static int i;
    static String lat [];
    static String lon [];
    static String date [];
    static String lonRef[];
    static String latRef[];



    private static void addItem(PictureItem item){
        ITEMS.add(0, item);
    }

    public static void loadSavedImages(File dir) {
        ITEMS.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                init(dir);
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    loadImage(file);
                    Log.i("LOADSAVEDIMAGES","Found image: "+ file);
                }
            }
        }





    }

    private static void init(File dir){
        i = 0;
        File[] file = dir.listFiles();
        lat = new String[file.length];
        latRef = new String[file.length];
        lon = new String[file.length];
        lonRef = new String[file.length];
        date = new String[file.length];
    }

    public static void deleteSavedImages(File dir) {
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    file.delete();
                }
            }
        }
        ITEMS.clear();
    }

    private static String getDateFromUri(Uri uri){
        String[] split = uri.getPath().split("/");
        String fileName = split[split.length - 1];
        String fileNameNoExt = fileName.split("\\.")[0];
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = format.format(new Date(Long.parseLong(fileNameNoExt)));
        return dateString;
    }

    public static void loadImage(File file){

        PictureItem newItem = new PictureItem();
        newItem.uri = Uri.fromFile(file);
        newItem.date = exif.getDate(file.getAbsolutePath());
        newItem.latRef = exif.getLatRef(file.getAbsolutePath());
        newItem.lat = exif.getLat(file.getAbsolutePath());
        newItem.lonRef = exif.getLonRef(file.getAbsolutePath());
        newItem.lon = exif.getLon(file.getAbsolutePath());
        Log.i("LAT", "LAT: "+ newItem.lat);
        Log.i ("LAT REF", "LAT REF "+newItem.latRef);
        Log.i("Lon", "Lon: "+ newItem.lon);
        Log.i("LON REF", "LON REF " + newItem.lonRef);
        Log.i("Date","Date: "+newItem.date);
        lon[i] = newItem.lon;
        lonRef[i] = newItem.lonRef;
        lat[i] = newItem.lat;
        latRef[i] = newItem.latRef;
        date[i] = newItem.date;

        i++;
    }

    public static String[] getLat(){
        return lat;
    }

    public static String[] getLatRef() {
        return latRef;
    }

    public static String[] getLonRef(){
        return lonRef;
    }

    public static String[] getLon() {
        return lon;
    }

    public static String[] getDate() {
        return date;
    }
}
