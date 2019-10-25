package com.JosephCantrell.cameramap.Presenter;

import android.Manifest;
import android.content.Context;
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

import java.io.File;

public class CameraAppPresenter extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int WRITE_PERMISSION_CODE = 101;
    private static final int READ_PERMISSION_CODE = 102;

    private int permissionCount;

    private MainActivity view;
    private CameraModel model;

    private static final String TAG = "Camera Presenter";

    public CameraAppPresenter(MainActivity view){
        this.view = view;
        this.model = new CameraModel();
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
                checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, WRITE_PERMISSION_CODE);
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_CODE);
                checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
                if(permissionCount == 3)
                    view.captureImage();
                break;

            }
            case R.id.button_map:{
                model.getAllPhotos();
                break;
            }
            default:
                break;
        }
    }

    public void checkPermission(String permission, int requestCode){
        if(ContextCompat.checkSelfPermission(view,permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(view, new String[] {permission}, requestCode);
        }
        else{
            //Toast.makeText(view, "Permission already grated", Toast.LENGTH_SHORT).show();
            Log.i(TAG,permission + " already granted");
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
