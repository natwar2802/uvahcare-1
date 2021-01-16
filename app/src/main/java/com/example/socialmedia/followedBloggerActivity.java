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


public class followedBloggerActivity extends MainActivity {

    RecyclerView recyclerViewfollowed;
    myProfileAdapter adapterProfile;
    ArrayList<modelProfile> arrayListProfile;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference followedereference,profilereference;
    FirebaseDatabase database;
    int ch=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_followed_blogger);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_followed_blogger, null);
        dynamicContent.addView(wizard);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        recyclerViewfollowed = (RecyclerView) findViewById(R.id.recycleViewfollowed);
        arrayListProfile = new ArrayList<modelProfile>();
        adapterProfile = new myProfileAdapter(this, arrayListProfile);
        recyclerViewfollowed.setHasFixedSize(true);
        recyclerViewfollowed.setLayoutManager(new LinearLayoutManager(this));
        adapterProfile.followchecker=true;
        recyclerViewfollowed.setAdapter(adapterProfile);
        // instance = this;


        // database = FirebaseDatabase.getInstance();
        FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
        String myuserida=myuser.getUid();
        followedereference= FirebaseDatabase.getInstance().getReference("followed").child(myuserida);
        profilereference=FirebaseDatabase.getInstance().getReference("profile");
        //databaseReference=database.getReference("healthPost");
        // likesrefernce = database.getReference("likes");
        followedereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ch==0){
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    // modelProfile model = dataSnapshot.getValue();
                    // arrayListProfile.add(model);
                    String id=dataSnapshot.getKey();

                    profilereference.child(id).addValueEventListener(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot1) {
                            String username=snapshot1.child("usernameP").getValue().toString();
                            String usercountry=snapshot1.child("countryP").getValue().toString();
                            String usercity=snapshot1.child("cityP").getValue().toString();
                            String imgUrl=snapshot1.child("imgUrlP").getValue().toString();
                            String aboutme=snapshot1.child("userdetail").getValue().toString();
                            ArrayList<String> userPreference = new ArrayList<String>();
                            userPreference.add("");
                            String prevseenpost="";
                            modelProfile modelp=new modelProfile(username,usercountry,usercity,imgUrl,id,aboutme,userPreference,prevseenpost);
                            if(arrayListProfile.contains(modelp)){
                                arrayListProfile.remove(modelp);
                            }
                            else {
                                arrayListProfile.add(modelp);
                            }
                            adapterProfile.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
                adapterProfile.notifyDataSetChanged();
            }
            ch=1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapterProfile.setOnItemClickListener(new ClickListener<modelProfile>() {
            @Override
            public void onItemClick(modelProfile data) {
                Intent intent = new Intent(followedBloggerActivity.this, followDetail.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("usernamed", data.getUsernameP().toString());
                intent.putExtra("cityd", data.getCityP().toString());
                intent.putExtra("urld", data.getImgUrlP());
                intent.putExtra("countryd", data.getCountryP());
                intent.putExtra("blogerid",data.getId().toString());

                startActivity(intent);
            }

        });
    }}