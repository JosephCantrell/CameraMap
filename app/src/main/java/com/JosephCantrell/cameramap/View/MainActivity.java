package com.JosephCantrell.cameramap.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.JosephCantrell.cameramap.ItemFragment;
import com.JosephCantrell.cameramap.MyItemRecyclerViewAdapter;
import com.JosephCantrell.cameramap.PictureItem;
import com.JosephCantrell.cameramap.Presenter.CameraAppPresenter;
import com.JosephCantrell.cameramap.R;

import static com.JosephCantrell.cameramap.PictureContent.loadSavedImages;
import static com.JosephCantrell.cameramap.PictureContent.loadImage;
import static com.JosephCantrell.cameramap.PictureContent.deleteSavedImages;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class MainActivity extends AppCompatActivity
        implements ItemFragment.OnListFragmentInteractionListener{


    CameraAppPresenter presenter = new CameraAppPresenter(this);
    static final int REQUEST_TAKE_PHOTO = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    String imageStoragePath;
    String TAG = "VIEW";
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //if(mAdapter == null){
            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment);
            //Log.i(TAG, "Frag: " + currentFragment );
            recyclerView = (RecyclerView) currentFragment.getView();
            mAdapter = ((RecyclerView) currentFragment.getView()).getAdapter();
            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                    DividerItemDecoration.VERTICAL);
            recyclerView.addItemDecoration(dividerItemDecoration);
            recyclerView.setAdapter(mAdapter);
        //}
    }

    protected void onResume(){
        super.onResume();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loadSavedImages(presenter.getDir());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public void onClick(View v){
        presenter.receiveButtonInfo(v);
    }


    public void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File file = presenter.getOutputMediaFile();

        Log.i(TAG,"file info: " + file);

        if(file != null){
            imageStoragePath = file.getAbsolutePath();
        }

        Uri fileUri = presenter.getOutputMediaFileUri(getApplicationContext(), file);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            //intent.putExtra("date", new Date());
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Picture Captured successfully", Toast.LENGTH_SHORT).show();
                // Launch back to the main activity
            }
            else if(resultCode == RESULT_CANCELED){
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onListFragmentInteraction(PictureItem item){
        // Item in the gallery has been clicked on.
    }


}
