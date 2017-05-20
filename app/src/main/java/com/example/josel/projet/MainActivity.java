package com.example.josel.projet;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static com.example.josel.projet.R.id.post_image;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView mBlogList;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mBlogList = (RecyclerView) findViewById(R.id.blog_list);
        mBlogList.setHasFixedSize(true);
        mBlogList.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(

                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mDatabase

        ){
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position){
                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setDate(model.getDate());
                viewHolder.setLocation(model.getLocation());
                viewHolder.setImage(getApplicationContext(), model.getImage());
            }
        };

        mBlogList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

//Titre
        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.post_title);
            post_title.setText(title);
        }
//Fin Titre

//Image
        public void setImage(Context ctx, String image){
            ImageView post_image =  (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
//Fin Image

//Description
        public void setDesc(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }
//Fin Description

//Date
        public void setDate(String date){
            TextView post_date =  (TextView)mView.findViewById(R.id.post_date);
            post_date.setText(date);
        }
//Fin Date

//Location
        public void setLocation(String location){
            TextView post_location =  (TextView)mView.findViewById(R.id.post_location);
            post_location.setText(location);
        }
//Fin Location


    }



//Bouton vers Post Activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
//Fin Bouton vers Post Activity
}
