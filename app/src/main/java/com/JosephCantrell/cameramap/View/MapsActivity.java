package com.JosephCantrell.cameramap.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.JosephCantrell.cameramap.AndroidExif;
import com.JosephCantrell.cameramap.PictureContent;
import com.JosephCantrell.cameramap.PictureItem;
import com.JosephCantrell.cameramap.Presenter.CameraAppPresenter;
import com.JosephCantrell.cameramap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.view.ViewGroup.LayoutParams;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    static final List<PictureItem> ITEMS = new ArrayList<>();

    private static int i;

    private static String[] lat;
    private static String[] lon;
    private static String[] date;
    private static String[] lonRef;
    private static String[] latRef;
    private static String[] ID;
    private static Uri[] URIs;

    private LinearLayout markerConstraintLayout;

    static AndroidExif exif = new AndroidExif();



    private CameraAppPresenter presenter;

    public MapsActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        i = 0;

        markerConstraintLayout = findViewById(R.id.LLMap);


        final File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/CameraMap");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(mediaStorageDir);
            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    protected void onResume(){
        super.onResume();
   }

    private static void init(File dir){
        File[] file = dir.listFiles();
        lat = new String[file.length];
        latRef = new String[file.length];
        lon = new String[file.length];
        lonRef = new String[file.length];
        date = new String[file.length];
        ID = new String[file.length];
        URIs = new Uri[file.length];
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //Log.i("Lon.Length", "Lon.Length: "+lon.length);

        mMap.setOnMarkerClickListener(this);

        for(int j = 0; j < lon.length; j++)
        {
            Double la = 0.0;
            Double lo = 0.0;

            Log.i("PRINTING J","J: "+j);

            Log.i("PRINT LAT[J]","LAT at j: " + lat[j]);
            Log.i("PRINT LON[J]","LON at j: " + lon[j]);


            if(!lat[j].isEmpty()) {
                la = getInfoFromGPS(lat[j]);
                if(latRef[j].equals("S"))
                {
                    la = -1*la;
                }
                if (!lon[j].isEmpty()) {
                    lo = getInfoFromGPS(lon[j]);
                    if(lonRef[j].equals("W"))
                    {
                        lo = -1*lo;
                    }
                    LatLng temp = new LatLng(la,lo);
                    mMap.addMarker(new MarkerOptions().position(temp).title(ID[j]));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(temp));
                    CameraPosition cp = new CameraPosition.Builder().target(temp).zoom(17).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cp));
                    //mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
                }
            }
        }

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private double getInfoFromGPS(String string){
        String[] split = string.split(",");
        double returnVal = 0;
        for(int i = 0; i < 3;i++){
            String[] numbers;
            numbers = split[i].split("/");
            double value;
            if(i==0) {
                value = Double.valueOf(numbers[0]) / Double.valueOf(numbers[1]);    // Return the hour value in the RA form
                returnVal = value;        // Start to set up the return value
                //Log.i("INFOFROMGPS", "Hours: "+ returnVal);
            }
            if(i==1){
                value = Double.valueOf(numbers[0])/60;  // Get the Minutes amount from the RA format given by the photo
                returnVal = returnVal + value;
                //Log.i("INFO FROM PHOTO" , "Minutes: "+ value + " Hours+minutes: "+returnVal);
            }
            if(i==2){
                value = Double.valueOf(numbers[0]) / Double.valueOf(numbers[1]);    // Get the value into decimal form : 236347/10000
                Double temp = value / 3600;             // Calculate the seconds amount from the RA format given by the photo
                returnVal = (returnVal + temp);
                //Log.i("INFO FROM PHOTO", "Seconds: "+ temp + " Finished: "+ returnVal);
            }
        }

        return returnVal;
    }


    private static void addItem(PictureItem item){
        ITEMS.add(0, item);
    }

    public static void loadSavedImages(File dir) {
        ITEMS.clear();
        init(dir);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            for (File file : files) {

                String absolutePath = file.getAbsolutePath();
                String extension = absolutePath.substring(absolutePath.lastIndexOf("."));
                if (extension.equals(".jpg")) {
                    loadImage(file);
                    //Log.i("LOADSAVEDIMAGES","Found image: "+ file);
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


    public static void loadImage(File file){

        PictureItem newItem = new PictureItem();
        URIs[i] = Uri.fromFile(file);
        Log.i("I VALUE", "VALUE: "+ i);
        ID[i] = String.valueOf(i);
        date[i] = exif.getDate(file.getAbsolutePath());
        latRef[i] = exif.getLatRef(file.getAbsolutePath());
        lat[i] = exif.getLat(file.getAbsolutePath());
        if(TextUtils.isEmpty(lat[i])){
            lat[i] = "36/1,6/1,6929/10000";
        }
        lonRef[i] = exif.getLonRef(file.getAbsolutePath());
        lon[i] = exif.getLon(file.getAbsolutePath());
        if(TextUtils.isEmpty(lon[i])){
            lon[i] = "94/1,9/1,439667/10000";
        }
        Log.i("LAT", "LAT: "+ lat[i]);
        Log.i ("LAT REF", "LAT REF "+latRef[i]);
        Log.i("Lon", "Lon: "+ lon[i]);
        Log.i("LON REF", "LON REF " + lonRef[i]);
        Log.i("Date","Date: "+date[i]);
        i++;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        final TextView pathname;
        final TextView dateCreated;
        final TextView Longitude;
        final TextView Latitude;
        final ImageView photo;



        int ID = Integer.parseInt(marker.getTitle());
        LayoutInflater layoutInflater = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View customView = layoutInflater.inflate(R.layout.popup_photo, null);
        final PopupWindow markerDisplay;
        markerDisplay = new PopupWindow(customView, 1000 , 2500);
        markerDisplay.setFocusable(true);
        markerDisplay.showAtLocation(markerConstraintLayout, Gravity.CENTER, 0, 0);

        pathname = (TextView) customView.findViewById(R.id.PathName);
        dateCreated = (TextView)customView.findViewById(R.id.dateCreated);
        Longitude = (TextView) customView.findViewById(R.id.Longitude);
        Latitude = (TextView) customView.findViewById(R.id.Latitude);
        photo = (ImageView) customView.findViewById(R.id.ImageViewPhoto);

        pathname.setText(URIs[ID].toString());
        dateCreated.setText(date[ID]);
        Longitude.setText(lon[ID]);
        Latitude.setText(lat[ID]);

        photo.setImageURI(URIs[ID]);

        return false;
    }
}
