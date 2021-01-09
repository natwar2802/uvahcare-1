package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class profileActivity extends MainActivity {

    Button btninfollow;
    TextView btnmypostp,editp,following;
    TextView btnbookmarkp,gotopreference,gotofollower,gotofollowed;
    DatabaseReference profileref,followreference;
    TextView username,usercountry,usercity;
    ImageView profilepic;
    int followcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_profile);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_profile, null);
        dynamicContent.addView(wizard);
        editp=findViewById(R.id.editprofile);
        btnmypostp=findViewById(R.id.btnmypostp);
        btnbookmarkp=findViewById(R.id.btnmybookmarkp);
       // btninfollow=findViewById(R.id.btninfollow);
        username=findViewById(R.id.userName);
        usercity=findViewById(R.id.tcity);
        usercountry=findViewById(R.id.tcountry);
        profilepic=findViewById(R.id.profilepic);
        gotopreference=findViewById(R.id.gotopreference);
        gotofollower=findViewById(R.id.gotofollower);
        gotofollowed=findViewById(R.id.gotofollowed);
        setProfile();
        setfollowStatus();

        editp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, EditProfileActivity.class);
                startActivity(i);
            }
        });
        btnmypostp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, MyPost.class);
                startActivity(i);
            }
        });
        btnbookmarkp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, BookMarkPost.class);
                startActivity(i);
            }
        });
        gotopreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, PreferenceActivity.class);
                startActivity(i);
            }
        });
        gotofollower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, followActivity.class);
                startActivity(i);
            }
        });
        gotofollowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, followedBloggerActivity.class);
                startActivity(i);
            }
        });

    }

    public void setProfile(){
        username=findViewById(R.id.userName);
        usercity=findViewById(R.id.tcity);
        usercountry=findViewById(R.id.tcountry);
        profilepic=findViewById(R.id.imageView1);
        profileref=  FirebaseDatabase.getInstance().getReference("profile");
        FirebaseUser pusera= FirebaseAuth.getInstance().getCurrentUser();
        String puserida=pusera.getUid();
        // String likes="likes";

        profileref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String usern=snapshot.child(puserida).child("usernameP").getValue().toString();
                username.setText(usern);
                String userc=snapshot.child(puserida).child("cityP").getValue().toString();
                usercity.setText(userc);
                String usercoun=snapshot.child(puserida).child("countryP").getValue().toString();
                usercountry.setText(usercoun);
                String url=snapshot.child(puserida).child("imgUrlP").getValue().toString();
                Glide.with(getApplicationContext()).load(url).into(profilepic);
                // likescount=(int)snapshot.child(postkey).getChildrenCount();
                //  likeddisplay.setText(Integer.toString(likescount)+likes);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }
    public void setfollowStatus(){
        following=findViewById(R.id.following);
        followreference= FirebaseDatabase.getInstance().getReference("follower");
        FirebaseUser usera=FirebaseAuth.getInstance().getCurrentUser();
        String userida=usera.getUid();


        followreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                followcount=(int)snapshot.child(userida).getChildrenCount();
                following.setText(Integer.toString(followcount)+" following");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

    }
}
