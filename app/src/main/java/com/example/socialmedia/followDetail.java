package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class followDetail extends MainActivity {
    ImageView imgprofiledetail;
    TextView usenamedetail,countrydetail,citydetail,followerdetail;
    RecyclerView recycledetail;
    myAdapter adapter2;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root1;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_follow_detail);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_follow_detail, null);
        dynamicContent.addView(wizard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        appname.setVisibility(View.VISIBLE);
        search_.setVisibility(View.GONE);
        usenamedetail=findViewById(R.id.itemuserNfdetail);
        countrydetail=findViewById(R.id.itemuserCounfdetail);
        citydetail=findViewById(R.id.itemuserCfdetail);
        imgprofiledetail=findViewById(R.id.itemprofilepicdetail);


        Intent in = getIntent();
        String blogerid=in.getStringExtra("blogerid");
        String usernamed=in.getStringExtra("usernamed");
        String cityd=in.getStringExtra("cityd");
        String countryd=in.getStringExtra("countryd");
        String urld=in.getStringExtra("urld");
        usenamedetail.setText(usernamed);
        countrydetail.setText(countryd);
        citydetail.setText(cityd);
        Glide.with(getApplicationContext()).load(urld).into(imgprofiledetail);

        recycledetail = (RecyclerView) findViewById(R.id.recycleviewprofiledetail);
        arrayList = new ArrayList<modelGeneral>();
        adapter2 = new myAdapter(this, arrayList);
        recycledetail.setHasFixedSize(true);
        recycledetail.setLayoutManager(new LinearLayoutManager(this));
        recycledetail.setAdapter(adapter2);
        // instance = this;


        // database = FirebaseDatabase.getInstance();
      //  FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
       // String myuserida=myuser.getUid();
        root1 = FirebaseDatabase.getInstance().getReference("mypost").child(blogerid);
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference("hPost");
        //databaseReference=database.getReference("healthPost");
        // likesrefernce = database.getReference("likes");

        root1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {

                     rootref.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot1) {
                             modelGeneral model = snapshot1.getValue(modelGeneral.class);
                             arrayList.add(0,model);
                             adapter2.notifyDataSetChanged();


                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });


                    }catch (Exception e){}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter2.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(followDetail.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc", data.getDescription());

                startActivity(intent);
            }

        });
    }}