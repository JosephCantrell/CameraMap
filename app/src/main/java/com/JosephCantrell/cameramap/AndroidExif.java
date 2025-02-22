package com.JosephCantrell.cameramap;

import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidExif{

    String imagefile ="/sdcard/DCIM/Camera/myphoto.jpg";
    ImageView image;
    TextView Exif;

    /** Called when the activity is first created. */
    //@Override
    public void onCreate(Bundle savedInstanceState) {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        //image = (ImageView)findViewById(R.id.image);
        //Exif = (TextView)findViewById(R.id.exif);
        //ImageView image = (ImageView)findViewById(R.id.image);

        Bitmap bm = BitmapFactory.decodeFile(imagefile);
        image.setImageBitmap(bm);

        Exif.setText(ReadExif(imagefile));
    }

    public String getLat(String file){
        try{
            ExifInterface exifInterface = new ExifInterface(file);
            return exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        }
        catch(IOException e){
            return "False";
        }
    }

    public String getLatRef(String file){
        try{
            ExifInterface exifInterface = new ExifInterface(file);
            return exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
        }
        catch(IOException e){
            return "False";
        }
    }


    public String getLon(String file){
        try{
            ExifInterface exifInterface = new ExifInterface(file);
            return exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        }
        catch(IOException e){
            return "False";
        }
    }

    public String getLonRef(String file){
        try{
            ExifInterface exifInterface = new ExifInterface(file);
            return exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
        }
        catch(IOException e){
            return "False";
        }
    }

    public String getDate(String file){
        try{
            ExifInterface exifInterface = new ExifInterface(file);
            return exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
        }
        catch(IOException e){
            return "False";
        }
    }

    String ReadExif(String file){
        String exif="Exif: " + file;
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            exif += "\nIMAGE_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            exif += "\nIMAGE_WIDTH: " + exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            exif += "\n DATETIME: " + exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            exif += "\n TAG_MAKE: " + exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            exif += "\n TAG_MODEL: " + exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            exif += "\n TAG_ORIENTATION: " + exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            exif += "\n TAG_WHITE_BALANCE: " + exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            exif += "\n TAG_FOCAL_LENGTH: " + exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            exif += "\n TAG_FLASH: " + exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            exif += "\nGPS related:";
            exif += "\n TAG_GPS_DATESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            exif += "\n TAG_GPS_TIMESTAMP: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            exif += "\n TAG_GPS_LATITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            exif += "\n TAG_GPS_LATITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            exif += "\n TAG_GPS_LONGITUDE: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            exif += "\n TAG_GPS_LONGITUDE_REF: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            exif += "\n TAG_GPS_PROCESSING_METHOD: " + exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

            //Toast.makeText(AndroidExifActivity.this,
            //        "finished",
            //        Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            //Toast.makeText(AndroidExifActivity.this,
            //        e.toString(),
            //        Toast.LENGTH_LONG).show();
        }

        return exif;
    }

}