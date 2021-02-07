package com.innovation.socialmedia;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.socialmedia.R;
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
    TextView usenamedetail,countrydetail,citydetail,followerdetail,aboutbloger;
    RecyclerView recycledetail;
    myAdapter adapter2;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root1;
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce;
    FirebaseDatabase database;
    TextView itemfollowfdetail;
    Button showuserdetail;
    int mchk;

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
        mchk=0;
        appname.setVisibility(View.VISIBLE);
        search_.setVisibility(View.GONE);
        usenamedetail=findViewById(R.id.itemuserNfdetail);
        countrydetail=findViewById(R.id.itemuserCounfdetail);
        citydetail=findViewById(R.id.itemuserCfdetail);
        imgprofiledetail=findViewById(R.id.itemprofilepicdetail);
        aboutbloger=findViewById(R.id.aboutbloger);
        itemfollowfdetail=findViewById(R.id.itemfollowfdetail);
        showuserdetail=findViewById(R.id.showuserdetail);
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
       FirebaseUser myuser= FirebaseAuth.getInstance().getCurrentUser();
        String myuserida=myuser.getUid();

        root1 = FirebaseDatabase.getInstance().getReference("mypost").child(blogerid);
        DatabaseReference rootref = FirebaseDatabase.getInstance().getReference("hPost");
        DatabaseReference profref = FirebaseDatabase.getInstance().getReference("profile").child(blogerid);
        DatabaseReference followerref = FirebaseDatabase.getInstance().getReference("follower");

followerref.child(blogerid).addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        int followcount= (int) snapshot.getChildrenCount();
        itemfollowfdetail.setText(followcount+" following");

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        //databaseReference=database.getReference("healthPost");
        // likesrefernce = database.getReference("likes");
        profref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                aboutbloger.setText(snapshot.child("userdetail").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        showuserdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mchk==0){
                    showuserdetail.setText("Hide user detail");
                    aboutbloger.setVisibility(View.VISIBLE);
                    mchk=1;
                }
                else{
                    showuserdetail.setText("show user detail");
                    aboutbloger.setVisibility(View.GONE);
                    mchk=0;
                }
            }
        });

        root1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    final int[] fdch = {0};
                    if(fdch[0] ==0){
                    try {

                     rootref.child(dataSnapshot.getKey()).addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot snapshot1) {
                             if(fdch[0] ==0){
                             modelGeneral model = snapshot1.getValue(modelGeneral.class);
                             arrayList.add(0,model);
                             adapter2.notifyDataSetChanged();
                             fdch[0]=1;
                         }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError error) {

                         }
                     });


                    }catch (Exception e){}

                }

            }}



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
          //  fdch=1;


        });



        adapter2.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(followDetail.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc",data.getDescription());
                intent.putExtra("postkey",data.getPid());
                intent.putExtra("blogerid",data.getBlogerid());
                intent.putExtra("overview",data.getBrief().toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }

        });
    }}