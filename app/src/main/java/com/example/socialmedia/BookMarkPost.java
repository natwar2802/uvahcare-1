package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BookMarkPost extends AppCompatActivity {
    RecyclerView recyclerView3;
    myAdapter adapter2;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root2;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce;
    FirebaseDatabase database;
    int bookch=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book_mark_post);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_book_mark_post, null);
        dynamicContent.addView(wizard);
        recyclerView3 = (RecyclerView) findViewById(R.id.recycleView3);
        arrayList = new ArrayList<modelGeneral>();
        adapter2 = new myAdapter(this, arrayList);
        recyclerView3.setHasFixedSize(true);
        recyclerView3.setLayoutManager(new LinearLayoutManager(this));
        recyclerView3.setAdapter(adapter2);
        // instance = this;

        // database = FirebaseDatabase.getInstance();
        FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
        String myuserida1=myuser.getUid();


        root2= FirebaseDatabase.getInstance().getReference("bookmark").child(myuserida1);
        //databaseReference=database.getReference("healthPost");
        // likesrefernce = database.getReference("likes");
  try{
       root2.addValueEventListener(new ValueEventListener() {

           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (bookch == 0) {
                   for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   modelGeneral model = dataSnapshot.getValue(modelGeneral.class);
                   arrayList.add(model);

               }

               adapter2.notifyDataSetChanged();
           }
           bookch=1;
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });}catch (Exception e){}



        adapter2.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(BookMarkPost.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc", data.getDescription());

                startActivity(intent);
            }

        });
    }}