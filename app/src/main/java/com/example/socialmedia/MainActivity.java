package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter adapter;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("hPost");
    private static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce,notifyreference;
    FirebaseDatabase database;
    Button btnmypost,btnbookmark;
    // Button btnshare;
    modelGeneral modelG;
    ImageView imgp,itemprofilepic;
    ViewPager pager;
    TextView itemusername;
    ImageView bellnotify;
    ImageButton btnshare;
    MainActivity obj;
    TextView displaynotifcount;
    CardView cardView;
    //int Natwar=0;

    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SocialMedia);
        setContentView(R.layout.activity_main);
        btnshare=findViewById(R.id.btnsharepost);
        displaynotifcount=findViewById(R.id.displynotifycount);
        obj=new MainActivity();

        bellnotify=findViewById(R.id.bellnotify);
        //onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        notifyreference = FirebaseDatabase.getInstance().getReference("notification");
        cardView=findViewById(R.id.pic);
        if(user==null){
            startActivity(new Intent(MainActivity.this,loginActivity.class));
            finish();
        }
        else{
            FirebaseDatabase.getInstance().getReference("profile").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userid=user.getUid();
                    if(snapshot.hasChild(userid)){

                        return;
                    }
                    else{
                        Intent intentet=new Intent(MainActivity.this,EditProfileActivity.class);
                        startActivity(intentet);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


   try{
       notifyreference.child("new").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int notifcount= (int) snapshot.getChildrenCount();
                if(notifcount>0) {
                    cardView.setVisibility(View.VISIBLE);
                    String count = String.valueOf(notifcount);
                    displaynotifcount.setText(count);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}catch (Exception e){}




        pager=findViewById(R.id.pager);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        arrayList = new ArrayList<modelGeneral>();
        adapter = new myAdapter(this, arrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        instance=this;
        // btnmypost=findViewById(R.id.bt);
        // btnbookmark=findViewById(R.id.btnmybookmark);
        database=FirebaseDatabase.getInstance();
        //databaseReference=database.getReference("healthPost");
        likesrefernce=database.getReference("likes");

        imgp=(ImageView)findViewById(R.id.img1);
        itemprofilepic=(ImageView)findViewById(R.id.itemprofilepic);
        itemusername=(TextView)findViewById(R.id.itemuserN);

        //pager.setAdapter(adapter);
        //setProfileInPost();
        Dynamiclink();
        // CategoryAdapter categoryAdapter=new CategoryAdapter(this,)
       /* btnmypost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyPost.class);
                startActivity(i);
            }
        });
        btnbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, BookMarkPost.class);
                startActivity(i);
            }
        });*/

        bellnotify.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                cardView.setVisibility(View.GONE);
                Intent intent=new Intent(MainActivity.this,Notify.class);
                startActivity(intent);
            }
        });

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    modelGeneral model = dataSnapshot.getValue(modelGeneral.class);
                    arrayList.add(model);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

/*        btnshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ptile=modelG.getTitle().toString().trim();
                String pbrief=modelG.getBrief().toString().trim();
               // String imgp=modelG.getUrlimage();

                BitmapDrawable bitmapDrawable=(BitmapDrawable)imgp.getDrawable();

                if(bitmapDrawable==null){
                    //post without image
                    shareTextOnly(ptile,pbrief);
                }
                else{
                    //post with the image
                    Bitmap bitmap=bitmapDrawable.getBitmap();
                    shareTextAndImage(ptile,pbrief,bitmap);
                }
            }
        });*/


        adapter.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                Intent intent = new Intent(MainActivity.this, descriptionActivity.class);
                // intent.putExtra("Arraylist",arrayList);
                intent.putExtra("title", data.getTitle().toString());
                intent.putExtra("Bdesc", data.getBrief().toString());
                intent.putExtra("im", data.getUrlimage());
                intent.putExtra("Ddesc",data.getDescription());
                intent.putExtra("postkey",data.getPid());
                intent.putExtra("blogid",data.getBlogerid());
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                startActivity(intent);
                finish();
            }

        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Intent in;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id1=item.getItemId();
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
                                Toast.makeText(MainActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                                Log.i("matching", "matching inside1 bro" +id1 );
                                in=new Intent(getBaseContext(),MainActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                startActivity(in);
                                finish();
                                break;
                            case R.id.action_schedules:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(MainActivity.this, "Post Selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(),newPost.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                startActivity(in);
                                finish();
                                break;
                            case R.id.action_music:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(MainActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(), profileActivity.class);
                                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                                startActivity(in);
                                finish();
                                break;
                        }
                        return true;
                    }
                });
    }



    private void shareTextAndImage(String title, String descrip, Bitmap bitmap) {
        String shareBody=title + "\n" + descrip;
        Uri uri=SavedImageToShare(bitmap);
        Intent shareintent=new Intent(Intent.ACTION_SEND);
        shareintent.putExtra(Intent.EXTRA_STREAM,uri);
        shareintent.putExtra(Intent.EXTRA_TEXT,shareBody);
        shareintent.putExtra(Intent.EXTRA_SUBJECT,"subject here");
        shareintent.setType("image/png");
        startActivity(Intent.createChooser(shareintent,"share via"));

    }

    private Uri SavedImageToShare(Bitmap bitmap) {
        File imagefolder=new File(this.getCacheDir(),"images");
        Uri uri=null;
        try{
            imagefolder.mkdirs();
            File file=new File(imagefolder,"shared_images");
            FileOutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(this,"com.example.socialmedia.fileprovider",file);


        }
        catch (Exception e){
            Toast.makeText(this,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void shareTextOnly(String title, String descrip) {
        String shareBody=title + "\n" + descrip;

        Intent shareintent=new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
        shareintent.putExtra(Intent.EXTRA_TEXT,shareBody);
        startActivity(Intent.createChooser(shareintent,"share via"));
    }
    @Override
    protected void onStart() {
        super.onStart();
       //  FirebaseUser user=mAuth.getCurrentUser();
       // FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


     /*   if(user==null){
            startActivity(new Intent(MainActivity.this,loginActivity.class));
            finish();
        }
        else{
            FirebaseDatabase.getInstance().getReference("profile").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String userid=user.getUid();
                    if(snapshot.hasChild(userid)){
                        return;
                    }
                    else{
                        Intent intentet=new Intent(MainActivity.this,EditProfileActivity.class);
                        startActivity(intentet);
                       finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }*/

        // Dynamiclink();
    }

    public void postN(View view) {
        Intent i = new Intent(MainActivity.this, newPost.class);
        startActivity(i);
        finish();
    }
    private void Dynamiclink() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
            @Override
            public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                Uri deeplink=null;
                if(pendingDynamicLinkData!=null){
                    deeplink=pendingDynamicLinkData.getLink();
                }


            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }



}