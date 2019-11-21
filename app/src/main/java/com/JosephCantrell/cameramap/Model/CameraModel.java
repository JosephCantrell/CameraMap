package com.JosephCantrell.cameramap.Model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.JosephCantrell.cameramap.R;
import com.JosephCantrell.cameramap.View.MainActivity;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class CameraModel extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    String TAG = "CAMERA MODEL";

    static final String EXTENSION[] = new String[]{
            ".jpg"
    };

    private Uri file;

    public void takePhoto() {
        //dispatchTakePictureIntent();
    }

    public File getStorageDirectory(){
        //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "/CameraMap");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/CameraMap");

        return mediaStorageDir;
    }


    static final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            for(final String ext : EXTENSION) {
                if (name.endsWith("." + ext)) {
                    return true;
                }
            }
            return false;
        }
    };

    public void getAllPhotos(){
        File dir = getStorageDirectory();
        File[] fileList = dir.listFiles();
        for(int i = 0; i < fileList.length; i++)
        {
            Log.d(TAG, "Filename: "+ dir + "/" + fileList[i].getName());

            File imageFile = new File(dir + "/" + fileList[i].getName());
                if(imageFile.exists()){
                    Bitmap myBM = BitmapFactory.decodeFile(imageFile.getAbsolutePath());    // Getting the bitmap for the file

                    //ImageView myImage = (ImageView) findViewById(R.id.item_image_view);

                    //myImage.setImageBitmap(myBM);
                }
        }
    }

    public File getOutputMediaFile(){

        File mediaStorageDir = getStorageDirectory();

        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdir()){
                Log.i(TAG, "Failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "CameraMap_" + timeStamp + ".jpg");
        Log.i(TAG,"MediaFile: " + mediaFile);
        return mediaFile;

    }

    public static Uri getOutputMediaFileUri(Context context, File file){
        return FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
    }
}
