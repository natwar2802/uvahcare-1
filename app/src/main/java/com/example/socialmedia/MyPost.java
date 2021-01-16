package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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


public class MyPost extends MainActivity {

    RecyclerView recyclerView2;
    myAdapter adapter2;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root1,root2;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_post);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_my_post, null);
        dynamicContent.addView(wizard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        recyclerView2 = (RecyclerView) findViewById(R.id.recycleView2);
        arrayList = new ArrayList<modelGeneral>();
        adapter2 = new myAdapter(this, arrayList);
        adapter2.ch=1;
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView2.setAdapter(adapter2);
       // instance = this;

       // database = FirebaseDatabase.getInstance();
        FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
        String myuserida=myuser.getUid();
        root1 = FirebaseDatabase.getInstance().getReference("mypost").child(myuserida);
        root2 = FirebaseDatabase.getInstance().getReference("hPost");

        //databaseReference=database.getReference("healthPost");
       // likesrefernce = database.getReference("likes");

        root1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    root2.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            modelGeneral model = snapshot1.getValue(modelGeneral.class);
                            arrayList.add(model);
                            adapter2.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                   // recyclerView2.setAdapter(adapter2);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        recyclerView2.setAdapter(adapter2);

        adapter2.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(MyPost.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc",data.getDescription());
                intent.putExtra("postkey",data.getPid());
                intent.putExtra("blogid",data.getBlogerid());
                startActivity(intent);
            }

        });
    }}