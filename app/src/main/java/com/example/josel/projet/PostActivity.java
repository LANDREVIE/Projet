package com.example.josel.projet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostActivity extends AppCompatActivity {


    //projet 2 Test

    private int x=1024;
    private ImageButton mSelectImage;
    private EditText mPostTitle;
    private EditText mPostDesc;
    private Button mSubmitBtn;
    private Uri mImageUri = null;
    private static final int GALLERY_REQUEST = 1;
    private StorageReference mStorage;
    private DatabaseReference mDataBase;
    private ProgressDialog mProgress;
//José
    private Button mUploadBtn;
    private static final int CAMERA_REQUEST_CODE = 2;
//Fin José


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        System.out.println("PostActivity.onCreate"+ FirebaseInstanceId.getInstance().getToken());

//José
        mUploadBtn = (Button)findViewById(R.id.upload);
        mUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });
//Fin José

        mStorage = FirebaseStorage.getInstance().getReference();
        mDataBase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mPostDesc = (EditText) findViewById(R.id.descField);
        mPostTitle = (EditText) findViewById(R.id.titleField);

        mSelectImage = (ImageButton) findViewById(R.id.imageSelect);



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

    private void startPosting(){

        mProgress.setMessage("Posting to Blog ...");
        final String title_val = mPostTitle.getText().toString().trim();
        final String desc_val = mPostDesc.getText().toString().trim();

        if(!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null){
            mProgress.show();
            StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());

            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    @SuppressWarnings("VisibleForTests")
                    Long PicSize = taskSnapshot.getMetadata().getSizeBytes();
                    Float size = PicSize.floatValue()/x;
                    Float MegSize = size/x;
                    @SuppressWarnings("VisibleForTests")
                    String PicName = taskSnapshot.getMetadata().getName();
                    @SuppressWarnings("VisibleForTests")
                    Long PicDate = taskSnapshot.getMetadata().getCreationTimeMillis();

                    DatabaseReference newPost = mDataBase.push();

                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("date").setValue("test");
                    newPost.child("location").setValue("test");
                    newPost.child("image").setValue(downloadUrl.toString());

                    mProgress.dismiss();
                    Toast.makeText(PostActivity.this, "Téléchargement terminé ..", Toast.LENGTH_LONG).show();

                    Intent toy = new Intent(PostActivity.this, MainActivity.class);
                    startActivity(toy);
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mImageUri= data.getData();
//José

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            Glide.with(PostActivity.this).load(mImageUri).into(mSelectImage);
        }
//Fin José

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Glide.with(PostActivity.this).load(mImageUri).into(mSelectImage);
        }
    }
}

