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

public class Notify extends MainActivity{

    RecyclerView recyclerViewnotify;
    myAdapterNotify adapter2;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root2,root1,root3;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce;
    FirebaseDatabase database;
    int checknotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_notify);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_notify, null);
        dynamicContent.addView(wizard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        appname.setVisibility(View.VISIBLE);
        search_.setVisibility(View.GONE);
        recyclerViewnotify = (RecyclerView) findViewById(R.id.recycleViewnotify);
        arrayList = new ArrayList<modelGeneral>();
        adapter2 = new myAdapterNotify(this, arrayList);
        recyclerViewnotify.setHasFixedSize(true);
        recyclerViewnotify.setLayoutManager(new LinearLayoutManager(this));
        checknotify=0;
        // instance = this;

        // database = FirebaseDatabase.getInstance();
        FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
        String myuserida1=myuser.getUid();


        root2= FirebaseDatabase.getInstance().getReference("notification").child("old").child(myuserida1);
        root3= FirebaseDatabase.getInstance().getReference("notification").child("new").child(myuserida1);

        root1= FirebaseDatabase.getInstance().getReference("hPost");


        //databaseReference=database.getReference("healthPost");
        // likesrefernce = database.getReference("likes");

       /* root2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                root2.child(arrayList.get(po))
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
        // checknotify=1;

        root3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        // modelGeneral model = dataSnapshot.getValue(modelGeneral.class);
                        //arrayList.add(model);
                        String postid=dataSnapshot1.getKey();
                        root2.child(dataSnapshot.getKey()).child(postid).setValue(true);
                    }

                    // arrayList.add(snapshot.getValue(modelGeneral.class));
                  /*  root1.child(postid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                           // arrayList.add(snapshot.getValue(modelGeneral.class));
                            adapter2.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });*/


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        root3.removeValue();


        root2.addValueEventListener(new ValueEventListener() {

                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(checknotify==0) {
                                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                                                        // modelGeneral model = dataSnapshot.getValue(modelGeneral.class);
                                                        //arrayList.add(model);
                                                        String postid = dataSnapshot1.getKey();
                                                        root1.child(postid).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                                                Boolean chek = arrayList.contains(snapshot1.getValue(modelGeneral.class));
                                                                if (chek == false) {
                                                                    arrayList.add(0,snapshot1.getValue(modelGeneral.class));

                                                                    // adapter2.notifyDataSetChanged();
                                                                }
                                                                adapter2.notifyDataSetChanged();

                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                            }
                                                        });


                                                    }}

                                                checknotify = 1;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    }

        );
        adapter2.notifyDataSetChanged();

        recyclerViewnotify.setAdapter(adapter2);



        adapter2.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(Notify.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);

                root2.child(data.blogerid).child(data.pid).setValue(false);

                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc",data.getDescription());
                intent.putExtra("postkey",data.getPid());
                intent.putExtra("blogerid",data.getBlogerid());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }

        });
    }
}