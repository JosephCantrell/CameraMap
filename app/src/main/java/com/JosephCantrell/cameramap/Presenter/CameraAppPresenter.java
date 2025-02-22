package com.JosephCantrell.cameramap.Presenter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.JosephCantrell.cameramap.Model.CameraModel;
import com.JosephCantrell.cameramap.R;
import com.JosephCantrell.cameramap.View.MainActivity;
import com.JosephCantrell.cameramap.View.MapsActivity;

import java.io.File;

public class CameraAppPresenter extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_PERMISSION_CODE = 101;
    private static final int READ_PERMISSION_CODE = 102;
    private static final int ACCESS_FINE_LOCATION_CODE = 103;

    private int permissionCount;

    private MainActivity view;
    private MapsActivity mapView;
    private CameraModel model;

    private static final String TAG = "Camera Presenter";

    public CameraAppPresenter(MainActivity view){
        this.view = view;
        this.model = new CameraModel();
        this.mapView = new MapsActivity();
    }

    public void onCreate(){
        model = new CameraModel();
        permissionCount = 0;
    }

    public File getOutputMediaFile(){
        return model.getOutputMediaFile();
    }

    public Uri getOutputMediaFileUri(Context context, File file){
        return model.getOutputMediaFileUri(context,file);
    }

    public File getDir(){
        return model.getStorageDirectory();
    }

    public void getAllPhotos(){
        model.getAllPhotos();
    }

    public void receiveButtonInfo(View v){
        switch (v.getId()){
            case R.id.button_take_photo: {
                checkPermission();
                if(permissionCount == 4)
                    view.captureImage();
                break;

            }
            case R.id.button_map:{
                break;
            }
            default:
                break;
        }
    }

    public void checkPermission(){
        permissionCount = 0;
        if(ContextCompat.checkSelfPermission(view,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(view, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_CODE);
        }
        else{
            //Toast.makeText(view, "Permission already grated", Toast.LENGTH_SHORT).show();
            Log.i(TAG,Manifest.permission.WRITE_EXTERNAL_STORAGE + " already granted");
            permissionCount++;
        }

        if(ContextCompat.checkSelfPermission(view,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(view, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
        }
        else{
            //Toast.makeText(view, "Permission already grated", Toast.LENGTH_SHORT).show();
            Log.i(TAG,Manifest.permission.READ_EXTERNAL_STORAGE + " already granted");
            permissionCount++;
        }

        if(ContextCompat.checkSelfPermission(view,Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(view, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else{
            //Toast.makeText(view, "Permission already grated", Toast.LENGTH_SHORT).show();
            Log.i(TAG,Manifest.permission.CAMERA+ " already granted");
            permissionCount++;
        }

        if(ContextCompat.checkSelfPermission(view,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(view, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, ACCESS_FINE_LOCATION_CODE);
        }
        else{
            //Toast.makeText(view, "Permission already grated", Toast.LENGTH_SHORT).show();
            Log.i(TAG,Manifest.permission.ACCESS_FINE_LOCATION + " already granted");
            permissionCount++;
        }
    }

    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(view,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(view,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == WRITE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(view,
                        "Write to Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(view,
                        "Write to Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(view,
                        "Read Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(view,
                        "Read Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
