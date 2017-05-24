package com.example.josel.projet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;

public class PostActivity extends AppCompatActivity {

    // modif test  !
    private ImageView imageView;

    private EditText mPosteTitle;

    private EditText mPostDesc;

    private Uri mImageUri = null;

    public Button mSubmitBtn;

    private StorageReference mStorage;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
    }

//Bouton to MainActivity
    public void init() {
        mSubmitBtn = (Button)findViewById(R.id.submitBtn);
        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toy = new Intent(PostActivity.this, MainActivity.class);
                startActivity(toy);
            }
        });
    }
//Fin Bouton to MainActivity
}

