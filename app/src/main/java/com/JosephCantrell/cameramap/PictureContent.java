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



    private static void addItem(PictureItem item){
        ITEMS.add(0, item);
    }

    public static void loadSavedImages(File dir) {
        ITEMS.clear();
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    loadImage(file);
                    Log.i("LOADSAVEDIMAGES","Found image: "+ file);
                }
            }
        }
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
        newItem.lat = exif.getLat(file.getAbsolutePath());
        newItem.lon = exif.getLon(file.getAbsolutePath());
        //Log.i("LOAD IMAGES", "Date: " + newItem.date);
        //Log.i("LOAD IMAGES", "lat: "+ newItem.lat);
        //Log.i("LOAD IMAGES", "long: " + newItem.lon);
    }
}
