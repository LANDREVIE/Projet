package com.example.josel.projet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostActivity extends AppCompatActivity {


    //projet 2 Test

    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;

    private Button mSubmitBtn;

    private Uri mImageUri = null;

    private static final int GALLERY_REQUEST = 1;

    private StorageReference mStorage;
    private DatabaseReference mDataBase;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Blog");

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);

        mPostTitle = (EditText) findViewById(R.id.titleField);
        mPostDesc = (EditText) findViewById(R.id.descField);

        mSubmitBtn = (Button) findViewById(R.id.submitBtn);

        mProgress = new ProgressDialog(this);
        mSelectImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });



        mSubmitBtn.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view){
                startPosting();


            }




        });}



    @SuppressWarnings("MissingPermission")
    private void startPosting(){



        mProgress.setMessage("Posting to Blog ...");
        mProgress.show();
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd_HH:mm:ss");
        final String currentDateandTime = sdf.format(new Date());

        LocationManager locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    Log.e("Latitude :", "" + location.getLatitude());
                    Log.e("Longitude :", "" + location.getLongitude());
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,10, locationListener);
        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        double latitudeDouble=0;
        double longitudeDouble=0;
        latitudeDouble = location.getLatitude();
        longitudeDouble = location.getLongitude();

        String StrLatitude=  String.valueOf(latitudeDouble);
        String StrLongitude = String.valueOf(longitudeDouble);
        final String StrPosition = "Latitude: "+StrLatitude+", Longitude: "+StrLongitude;



        // Debut du Tutoriel
        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null){
            StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost = mDataBase.push();
                    //DatabaseReference newPost = null;
                    //newPost = newPost.push();

                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("date").setValue(currentDateandTime);
                    newPost.child("location").setValue(StrPosition);
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();
                    Intent toy = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(toy);



                }
            });


        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            mImageUri = data.getData();

            mSelectImage.setImageURI(mImageUri);
        }
    }


}

