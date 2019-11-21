package com.JosephCantrell.cameramap.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.JosephCantrell.cameramap.Presenter.CameraAppPresenter;
import com.JosephCantrell.cameramap.R;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {


    CameraAppPresenter presenter = new CameraAppPresenter(this);
    static final int REQUEST_TAKE_PHOTO = 1;
    String imageStoragePath;
    String TAG = "VIEW";
    private ImageView image;
    private File imageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.button_map) {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        } else
            presenter.receiveButtonInfo(v);
    }


    public void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = presenter.getOutputMediaFile();
        imageName = file;

        Log.i(TAG, "file info: " + file);

        if (file != null) {
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = presenter.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            //intent.putExtra("date", new Date());
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Picture Captured successfully", Toast.LENGTH_SHORT).show();
                // Launch back to the main activity
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                Location location = (Location) lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                double longitude = location.getLongitude();
                Log.i(TAG, "GPS Longitude: "+longitude);
                double latitude = location.getLatitude();
                Log.i(TAG, "GPS Latitude: "+latitude);
                geoTag(imageName.getAbsolutePath(),latitude, longitude);
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void geoTag(String filename, double latitude, double longitude){
        ExifInterface exif;

        try {

            exif = new ExifInterface(filename);

            exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, Location.convert(latitude, Location.FORMAT_SECONDS));
            exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, Location.convert(longitude, Location.FORMAT_SECONDS));


            if (latitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "N");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, "S");
            }

            if (longitude > 0) {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "E");
            } else {
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF, "W");
            }


            exif.saveAttributes();
            Log.i("ATTRIBUTES", "Saved attributes");
            if(TextUtils.isEmpty(exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE))){
                exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, "94/1,9/1,439667/10000");
                Toast.makeText(this,"Error occurred. Longitude set to default", Toast.LENGTH_SHORT);
                exif.saveAttributes();
            }
            if(TextUtils.isEmpty(exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE))){
                exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, "36/1,6/1,6929/10000");
                Toast.makeText(this,"Error occurred. Latitude set to default", Toast.LENGTH_SHORT);
                exif.saveAttributes();
            }
            Log.i(TAG,"long "+exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE));
            Log.i(TAG, "lat "+exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE));


        } catch (IOException e) {
            Log.e("PictureActivity", e.getLocalizedMessage());
        }
    }


}
