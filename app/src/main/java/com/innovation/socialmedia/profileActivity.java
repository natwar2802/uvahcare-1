package com.innovation.socialmedia;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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

public class profileActivity extends MainActivity {

    Button btninfollow;
    TextView btnmypostp,following;
    TextView btnbookmarkp,gotopreference,gotofollower,gotofollowed;
    CardView btnbookmarkpcard,gotopreferencecard,gotofollowercard,gotofollowedcard,btnmypostpcard;
    DatabaseReference profileref,followreference;
    TextView username,usercountry,usercity;
    ImageView profilepic,editp;
    int followcount;
    ImageButton logout;
    TextView userdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_profile);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_profile, null);
        dynamicContent.addView(wizard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        SearchView search_=findViewById(R.id.search_);
        search_.setVisibility(View.GONE);
        appname.setVisibility(View.VISIBLE);
// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        editp=findViewById(R.id.editprofile);
        logout=findViewById(R.id.logout);
        btnmypostp=findViewById(R.id.btnmypostp);
        btnmypostpcard=findViewById(R.id.btnmypostpcard);
        btnbookmarkp=findViewById(R.id.btnmybookmarkp);
        btnbookmarkpcard=findViewById(R.id.btnmybookmarkpcard);
       // btninfollow=findViewById(R.id.btninfollow);
        username=findViewById(R.id.userName);
        usercity=findViewById(R.id.tcity);
        usercountry=findViewById(R.id.tcountry);
        gotopreferencecard=findViewById(R.id.gotopreferencecard);
        gotopreference=findViewById(R.id.gotopreference);
        gotofollowercard=findViewById(R.id.gotofollowercard1);
        gotofollower=findViewById(R.id.gotofollower);
        gotofollowedcard=findViewById(R.id.gotofollowedcard);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent =new Intent(profileActivity.this,loginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnmypostpcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, MyPost.class);

                startActivity(i);

            }
        });
        btnbookmarkpcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, BookMarkPost.class);
                startActivity(i);
            }
        });
        gotopreferencecard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, PreferenceActivity.class);
                startActivity(i);
            }
        });
        gotofollowercard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profileActivity.this, followActivity.class);
                startActivity(i);
            }
        });
        gotofollowedcard.setOnClickListener(new View.OnClickListener() {
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
                String detail=snapshot.child(puserida).child("userdetail").getValue().toString();
//             userdetail.setText(detail);
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
