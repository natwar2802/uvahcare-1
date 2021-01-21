package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static android.widget.Toast.LENGTH_SHORT;
import static androidx.core.content.ContextCompat.getSystemService;

public class descriptionActivity extends MainActivity {

    RatingBar ratingpop,btnratedesc;
    TextView ratingBardescription;
    Button btnsubmitrate,cancel_rate;
    CardView popupcard;
    float avrating;
    int ch=0,ch1=0;
    private String TAG;
    ImageView profileimg;
    TextView userNff;
    Button btnfollowff;
    Boolean followerchecker=false,likechec=false;
    ImageButton inc2,btnshare2,btnshared,bmd;
    TextView displayclap2;
    TextView rate_text;
    LinearLayout user_feedback;
    ScrollView scrollView;
    boolean bookmarkcheckerd=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_description);
        LinearLayout dynamicContent;
        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        View wizard = getLayoutInflater().inflate(R.layout.activity_description, null);
        dynamicContent.addView(wizard);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        appname.setVisibility(View.VISIBLE);
        search_.setVisibility(View.GONE);
        ratingBardescription=findViewById(R.id.ratingdescription);
        rate_text=findViewById(R.id.rate_text);
        user_feedback=findViewById(R.id.user_feedback);
        btnratedesc=findViewById(R.id.ratedesc);
        scrollView=findViewById(R.id.scrollView);
        btnsubmitrate=findViewById(R.id.submitrating);
        popupcard=findViewById(R.id.popupcard);
        btnshared= findViewById(R.id.btnsharepostd);
        ratingpop=findViewById(R.id.ratingpop);
        userNff=findViewById(R.id.itemuserNff);
        btnfollowff=findViewById(R.id.btnfollowff);
        inc2=findViewById(R.id.inc2);
        btnshare2=findViewById(R.id.btnsharepost2);
        displayclap2=findViewById(R.id.displayclap2);
        bmd = findViewById(R.id.bmd);
        DatabaseReference followerreference=database.getReference("follower");
        DatabaseReference followedreference=database.getReference("followed");
        DatabaseReference bookmarkref = database.getReference("bookmark");


        cancel_rate=findViewById(R.id.cancel_rate);

        btnratedesc.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                popupcard.setVisibility(View.VISIBLE);
                ratingpop.setRating(btnratedesc.getRating());
                btnratedesc.setVisibility(View.GONE);
                rate_text.setVisibility(View.GONE);
            }
        });




        TextView txt= (TextView) findViewById(R.id.title12);
        Intent in = getIntent();
        txt.setText(in.getStringExtra("title"));
        ImageView imj=(ImageView) findViewById(R.id.img12);
        String url=in.getStringExtra("im");
        TextView txt11= (TextView) findViewById(R.id.desc12);
        txt11.setText(in.getStringExtra("Ddesc"));
        String postkey=in.getStringExtra("postkey");
        DatabaseReference likesref= FirebaseDatabase.getInstance().getReference("likes");
        DatabaseReference postref3= FirebaseDatabase.getInstance().getReference("hPost");
        FirebaseUser usera=FirebaseAuth.getInstance().getCurrentUser();
        String userida=usera.getUid();
        profileimg=findViewById(R.id.itemprofilepicff);
        FirebaseUser cuser= FirebaseAuth.getInstance().getCurrentUser();
        String cid=cuser.getUid();
       // CardView cardforpopup=findViewById(R.id.cardforpopup);
        Glide.with(imj.getContext()).load(url).into(imj);
        bmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookmarkcheckerd=true;

                bookmarkref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(bookmarkcheckerd==true){
                            if(snapshot.child(userida).hasChild(postkey)){
                                bookmarkref.child(userida).child(postkey).removeValue();
                                bmd.setImageResource(R.drawable.imagesb);
                                bookmarkcheckerd=false;
                            }
                            else{
                                bookmarkref.child(userida).child(postkey).setValue(true);
                                bmd.setImageResource(R.drawable.images);
                                bookmarkcheckerd=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
      //  float fratindesc=ratingBardescription.getRating();


        String blogerid=in.getStringExtra("blogerid");

        DatabaseReference refrate= FirebaseDatabase.getInstance().getReference("rating");
        DatabaseReference hpost= FirebaseDatabase.getInstance().getReference("hPost");
        DatabaseReference profileref= FirebaseDatabase.getInstance().getReference("profile");
        DatabaseReference notifyreference1 =  FirebaseDatabase.getInstance().getReference("notification").child("old");
        DatabaseReference notifyreference2 =  FirebaseDatabase.getInstance().getReference("notification").child("new");

        profileref.child(blogerid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String purl= (String) snapshot.child("imgUrlP").getValue();
                String name= (String) snapshot.child("usernameP").getValue();

               userNff.setText(name);
                Glide.with(profileimg.getContext()).load(purl).into(profileimg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });






        try{
        refrate.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ch==0){
                if(snapshot.hasChild(postkey)) {

                        int n = (int) snapshot.child(postkey).getChildrenCount() - 1;

                        // if(snapshot.child(postkey).child("sum").get)
                        //try{
                        // Double sum = (Double) snapshot.child(postkey).child("sum").getValue();
                     //   double sum = Double.parseDouble(snapshot.child(postkey).child("sum").getValue());
                  //  a.getClass().getName()

                   /* if (snapshot.child(postkey).child("sum").getValue().getClass().getName().equals("java.lang.Double")) {
                        double sum = (double) snapshot.child(postkey).child("sum").getValue();
                        Double avrate = sum / n;
                        float avr = avrate.floatValue();
                        ratingBardescription.setRating(avr);

                    }*/

                      try{
                          final int[] chekscore = {0};
                          Long sum = (Long) snapshot.child(postkey).child("sum").getValue();
                          hpost.child(postkey).child("ratesum").setValue(sum);
                        // double d = sum.doubleValue();
                        float avrate =((float) sum)/ n;
                          ratingBardescription.setText(Float.toString(avrate));
                          hpost.child(postkey).child("rating").setValue(avrate);
                         // hpost.child(postkey).child("postscore").setValue(10);
                          hpost.addValueEventListener(new ValueEventListener() {
                              @Override
                              public void onDataChange(@NonNull DataSnapshot snapshot) {
                                  if(chekscore[0] ==0){
                                  long totalno=snapshot.getChildrenCount();
                                  Long claps= (Long) snapshot.child(postkey).child("claps").getValue();
                                  Long seencount= (Long) snapshot.child(postkey).child("seencount").getValue();
                                  Long datetime= (Long) snapshot.child(postkey).child("datetime").getValue();
                                  Long postno= (Long) snapshot.child(postkey).child("postno").getValue();
                                  double score;
                                  String sumstr=sum.toString();
                                  score=((double) claps)/(seencount+1)+(avrate/10)+((double) sumstr.length())/10+((double)postno)/totalno;
                                  hpost.child(postkey).child("postscore").setValue(score);

                              }
                              chekscore[0] =1;}

                              @Override
                              public void onCancelled(@NonNull DatabaseError error) {

                              }
                          });



                          // float avr = avrate.floatValue();

                      }catch (Exception e){}








                  //}
                  /* catch (Exception e){
                       Long sum = (Long)snapshot.child(postkey).child("sum").getValue();
                       Double avrate=((double) sum)/n;

                       float avr=avrate.floatValue();
                       ratingBardescription.setRating(avr);
                   }*/

                }
                else{
                    // holder.displayrate.setText(0);

                }
                    ch=1;
            }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }catch (Exception e){
            Log.e(TAG,e.getMessage());

        }



        try{
            followerreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                    if(cid.equals(blogerid)){
                        btnfollowff.setVisibility(View.GONE);
                    }
                    else{
                        // if(followerchecker.equals(true)){
                        if(snapshot.child(blogerid).hasChild(cid)){
                            // followreference.child(idbloger).child(curentUserId).removeValue();
                            btnfollowff.setText("unfollow");
                            //holder.btnfollow.setImageResource(R.drawable.unfollowicon);
                            // followchecker=false;
                        }
                        else{
                            // followreference.child(idbloger).child(curentUserId).setValue(true);
                            btnfollowff.setText("follow");
                            //  holder.btnfollow.setImageResource(R.drawable.followicon);
                            // followchecker=false;
                        }
                    }
                }
                //String mobno=databaseReference.Auythecation(mobno);

                // databaseReference.child(ImageUploadId).setValue(imageUploadInfo);


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}catch (Exception e){}

try {
    btnfollowff.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            followerchecker = true;

            followerreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                    if (followerchecker.equals(true)) {
                        if (snapshot.child(blogerid).hasChild(cid)) {
                            notifyreference1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    if (snapshot1.child(cid).hasChild(blogerid)) {
                                        notifyreference1.child(cid).child(blogerid).removeValue();
                                        notifyreference2.child(cid).child(blogerid).removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                            followerreference.child(blogerid).child(cid).removeValue();
                            followedreference.child(cid).child(blogerid).removeValue();
                            btnfollowff.setText("follow");
                            //holder.btnfollow.setImageResource(R.drawable.followicon);
                            followerchecker = false;
                        } else {
                            followerreference.child(blogerid).child(cid).setValue(true);
                            followedreference.child(cid).child(blogerid).setValue(true);

                            btnfollowff.setText("Unfollow");
                            // holder.btnfollow.setImageResource(R.drawable.unfollowicon);
                            followerchecker = false;
                        }
                    }
                    //String mobno=databaseReference.Auythecation(mobno);

                    // databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });
}catch (Exception e){}


        //  rateuser.child(postkey).child(cid).setValue(fratindesc);
        refrate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(postkey).hasChild(cid)){
                    btnratedesc.setVisibility(View.GONE);
                    rate_text.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnratedesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // btnsubmitrate.setVisibility(View.VISIBLE);
                popupcard.setVisibility(View.VISIBLE);

            }
        });

        cancel_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupcard.setVisibility(View.GONE);
            }
        });
        btnsubmitrate.setOnClickListener(new View.OnClickListener() {

          //   try {
            @Override
            public void onClick(View v) {
                ch1=0;
                ch=0;
                float frate=(float)ratingpop.getRating();



              refrate.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot snapshot) {
                      if(ch1==0){

                     if (snapshot.hasChild(postkey)) {

                         int n = (int) snapshot.child(postkey).getChildrenCount();
                       /*  if (snapshot.child(postkey).child("sum").getValue().getClass().getName().equals("java.lang.Double")){

                             double sum = (double) snapshot.child(postkey).child("sum").getValue();
                         // float sum = (float) snapshot.child(postkey).child("sum").getValue();
                         float fsum = (float)sum;

                         fsum = fsum + frate;
                         float avrate =((float) fsum )/ n;
                         ratingBardescription.setRating(avrate);
                         refrate.child(postkey).child(cid).setValue(frate);
                         refrate.child(postkey).child("sum").setValue(fsum);
                         }*/
       //   try{
                             Long sum1 = (Long) snapshot.child(postkey).child("sum").getValue();
                             // float sum = (float) snapshot.child(postkey).child("sum").getValue();
                             double sum=sum1.doubleValue();
                             float fsum = (float)sum;

                             fsum = fsum + frate;
                             float avrate =((float) fsum )/ n;
                           //  ratingBardescription.setText(Float.toString(avrate));
                             refrate.child(postkey).child(cid).setValue(frate);
                             refrate.child(postkey).child("sum").setValue(fsum);




        //  }catch (Exception e){}


                     }
                     else {
                       //  float sum = frate;
                         //float avrate = frate;
                         try {
                             ratingBardescription.setText(Float.toString(frate));
                             refrate.child(postkey).child(cid).setValue(frate);
                             refrate.child(postkey).child("sum").setValue(frate);
                         }catch (Exception e){}


                     }

                  }
                  ch1=1;
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError error) {

                  }
              });

              popupcard.setVisibility(View.GONE);
            }
        }

             );

       // inc=itemView.findViewById(R.id.inc);


        likesref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int likescount=(int)snapshot.child(postkey).getChildrenCount();
                displayclap2.setText(Integer.toString(likescount));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });

        inc2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                likechec =true;
                likesref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(likechec.equals(true)){
                            if(snapshot.child(postkey).hasChild(cid)){
                                likesref.child(postkey).child(cid).removeValue();
                                likechec=false;
                            }
                            else{
                                likesref.child(postkey).child(cid).setValue(true);
                                likechec=false;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });


        btnshare2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLink link = FirebaseDynamicLinks.getInstance()
                        .createDynamicLink()
                        .setLink(Uri.parse("https://"+postkey+"/"))
                        .setDomainUriPrefix("https://healthappinnovation.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.socialmedia").build())

                        .buildDynamicLink();

                Uri dynamic=link.getUri();

                Log.e("link","hello"+link.getUri());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link.getUri().toString());
                startActivity(Intent.createChooser(intent, "Share Link"));

                //  Uri domain=mlist.get(position).getPid().to;
                //  taskSnapshot.getDownloadUrl()

                //  BitmapDrawable bitmapDrawable=(BitmapDrawable)holder.img.getDrawable();

                //  onShareClicked();

            }


        });
        btnshared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DynamicLink link = FirebaseDynamicLinks.getInstance()
                        .createDynamicLink()
                        .setLink(Uri.parse("https://"+postkey+"/"))
                        .setDomainUriPrefix("https://healthappinnovation.page.link")
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder("com.example.socialmedia").build())

                        .buildDynamicLink();

                Uri dynamic=link.getUri();

                Log.e("link","hello"+link.getUri());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, link.getUri().toString());
                startActivity(Intent.createChooser(intent, "Share Link"));

            }


        });

        btnfollowff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                followerchecker=true;

                followerreference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                        if(followerchecker.equals(true)){
                            if(snapshot.child(blogerid).hasChild(cid)){
                                notifyreference1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        if(snapshot1.child(cid).hasChild(blogerid)){
                                            notifyreference1.child(cid).child(blogerid).removeValue();
                                            notifyreference2.child(cid).child(blogerid).removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                                followerreference.child(blogerid).child(cid).removeValue();
                                followedreference.child(cid).child(blogerid).removeValue();
                                btnfollowff.setText("follow");
                                //holder.btnfollow.setImageResource(R.drawable.followicon);
                                followerchecker=false;
                            }
                            else{
                                followerreference.child(blogerid).child(cid).setValue(true);
                                followedreference.child(cid).child(blogerid).setValue(true);

                                btnfollowff.setText("Unfollow");
                                // holder.btnfollow.setImageResource(R.drawable.unfollowicon);
                                followerchecker=false;
                            }
                        }
                        //String mobno=databaseReference.Auythecation(mobno);

                        // databaseReference.child(ImageUploadId).setValue(imageUploadInfo);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });



    }




}