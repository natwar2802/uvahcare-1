package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity<user> extends AppCompatActivity {
    RecyclerView recyclerView;
    myAdapter adapter;
    ArrayList<modelGeneral> arrayList;
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("hPost");
    public static MainActivity instance;
    FirebaseAuth mAuth;
    DatabaseReference likesrefernce,notifyreference,profilereference;
    FirebaseDatabase database;
    Button btnmypost,btnbookmark;
    // Button btnshare;
    modelGeneral modelG;
    ImageView imgp,itemprofilepic;
    ViewPager pager;
    TextView itemusername;
    ImageView bellnotify;
    ImageButton btnshare;
   // MainActivity obj;
    TextView displaynotifcount;
    SearchView search_;
    TextView appname;
    CardView cardView;
    int seench=0;
   // EditText searchbar;
    //int Natwar=0;
    int mainch=0;
    //FirebaseUser user;
    private BottomNavigationView toolbar2;
    ProgressBar progressBarmain;

    private static final String TAG = "xyz";



    //@SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_SocialMedia);
        setContentView(R.layout.activity_main);
        instance=this;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



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

                        Intent intentet = new Intent(MainActivity.this, EditProfileActivity.class);
                        startActivity(intentet);
                        finish();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


// clear FLAG_TRANSLUCENT_STATUS flag:

        appname=(TextView)findViewById(R.id.appname);
        appname.setVisibility(View.GONE);

        cardView=findViewById(R.id.pic);
       // btnshare=findViewById(R.id.btnsharepost);
        search_=findViewById(R.id.search_);
        search_.setVisibility(View.VISIBLE);
       // searchbar=findViewById(R.id.search_bar);
        displaynotifcount=findViewById(R.id.displynotifycount);
        //obj=new MainActivity();
        progressBarmain=findViewById(R.id.progressbarmain);

        String sharelinktext="https://healthappinnovation.page.link";

        bellnotify=findViewById(R.id.bellnotify);

         if(splashScreen.chsplash==0){
        progressBarmain.setVisibility(View.VISIBLE);}

        splashScreen.chsplash=1;
        //onStart();


        notifyreference = FirebaseDatabase.getInstance().getReference("notification");
        profilereference = FirebaseDatabase.getInstance().getReference("profile");







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

        toolbar2 = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        //toolbar2.inflateMenu(R.menu.menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.my_statusbar_color));
        }

        //pager.setAdapter(adapter);
        //setProfileInPost();

        Dynamiclink();
        Query myTopPostsQuery = database.getReference("hPost")
                .orderByChild("postscore").limitToLast(100);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(mainch==0) {

                    FirebaseUser use = FirebaseAuth.getInstance().getCurrentUser();
                    if(use!=null) {
                        String useid = use.getUid();


                    profilereference.child(useid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            arrayList.clear();
                            int count = 0;
                            String preseenpost;
                            int count1 = 0,userpostch=0;
                            ArrayList<String> userp = (ArrayList<String>) snapshot.child("userPreference").getValue();

                            try{ preseenpost = snapshot.child("prevseenpost").getValue().toString();}
                            catch (Exception e){
                                preseenpost="xyz";
                            }


                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    try{
                                    modelGeneral model = dataSnapshot1.getValue(modelGeneral.class);
                                    String postprefe = model.getPreference().toString();
                                    if (postprefe.equals(preseenpost)) {
                                        arrayList.add(0, model);
                                        adapter.notifyDataSetChanged();
                                        count1++;
                                        count++;
                                    } else if (userp.contains(postprefe)) {
                                        arrayList.add(count1, model);
                                        adapter.notifyDataSetChanged();
                                        count++;
                                    } else if (userpostch == 0 && model.getBlogerid().equals(useid)) {

                                        arrayList.add(count1, model);
                                        adapter.notifyDataSetChanged();
                                        count1++;
                                        userpostch = 1;
                                    } else {
                                        arrayList.add(count, model);
                                        adapter.notifyDataSetChanged();
                                    }
                                    progressBarmain.setVisibility(View.GONE);


                                }catch (Exception e){}}
                         //   }catch (Exception e){}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mainch=1;
                }

            }}

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

        try{
            notifyreference.child("new").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int sum=0;
                    for (DataSnapshot dataSnapshot : snapshot.getChildren())
                        sum=sum+(int)dataSnapshot.getChildrenCount();
                    int notifcount = sum;
                    if (notifcount > 0) {
                        cardView.setVisibility(View.VISIBLE);
                        String count = String.valueOf(notifcount);
                        displaynotifcount.setText(count);
                    }


                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });}catch (Exception e){}

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
    search_.setOnCloseListener(new SearchView.OnCloseListener() {
        @Override
        public boolean onClose() {
            Intent intent=new Intent(MainActivity.this,MainActivity.class);
            startActivity(intent);
            return false;
        }
    });
    search_.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            search_.onActionViewExpanded();
        }
    });

        search_.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processearch(s);
                return false;
            }


        });

        bellnotify.setOnClickListener(new View.OnClickListener() {



            @Override
            public void onClick(View v) {
                displaynotifcount.setText("");

                cardView.setVisibility(View.GONE);
                Intent intent=new Intent(MainActivity.this,Notify.class);
                startActivity(intent);
            }
        });






     /*   root.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (mainch == 0) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        modelGeneral model = dataSnapshot.getValue(modelGeneral.class);
                        arrayList.add(model);
                        adapter.notifyDataSetChanged();
                    }

                }
                mainch=1;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


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
      //  DatabaseReference profileref1=FirebaseDatabase.getInstance().getReference("profile").child(user.getUid()).child("prevseenpost");
      //  DatabaseReference seencount=FirebaseDatabase.getInstance().getReference("seencount");
        DatabaseReference postref=FirebaseDatabase.getInstance().getReference("hPost");



        adapter.setOnItemClickListener(new ClickListener<modelGeneral>() {
            @Override
            public void onItemClick(modelGeneral data) {
                final int[] checkerdes = {0};
                 postref.addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot2) {
                         if(checkerdes[0] ==0){
                         if(snapshot2.hasChild(data.getPid())){
                             Intent intent = new Intent(MainActivity.this, descriptionActivity.class);
                             // intent.putExtra("Arraylist",arrayList);
                             intent.putExtra("title", data.getTitle().toString());
                             intent.putExtra("Bdesc", data.getBrief().toString());
                             intent.putExtra("im", data.getUrlimage());
                             intent.putExtra("Ddesc", data.getDescription());
                             intent.putExtra("postkey", data.getPid());
                             intent.putExtra("blogerid", data.getBlogerid());
                             intent.putExtra("overview",data.getBrief().toString());
                             intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                             startActivity(intent);

                         }
                         else{
                             arrayList.remove(data);
                             adapter.notifyDataSetChanged();
                             Toast.makeText(getApplicationContext(),"This Post is Deleted by blogger",Toast.LENGTH_SHORT).show();

                         }
                             checkerdes[0] =1;
                     }

                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


         /*       final int[] seenpostch = {0};
                profileref1.setValue(data.getPreference());
               postref.child(data.getPid()).addValueEventListener(new ValueEventListener() {

                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       if(seenpostch[0] ==0){
                       Long count= (Long) snapshot.child("seencount").getValue();
                       count+=1;
                       postref.child(data.getPid()).child("seencount").setValue(count);
                           seenpostch[0]=1;

                       }

                   }


                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });*/

                // finish();
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
                               // MenuItem item1=findViewById(R.id.action_favorites);
                              //  MenuItem item2=findViewById(R.id.action_schedules);
                              //  MenuItem item3=findViewById(R.id.action_music);
                                toolbar2.getMenu().findItem(R.id.action_favorites).setChecked(true);
                                toolbar2.getMenu().findItem(R.id.action_schedules).setChecked(false);
                                toolbar2.getMenu().findItem(R.id.action_music).setChecked(false);



                              //  item1.setChecked(true);
                               // item2.setChecked(false);
                                //item3.setChecked(false);
                                Log.i("matching", "matching inside1 bro" +id1 );
                                in=new Intent(getBaseContext(),MainActivity.class);
                                startActivity(in);
                                //finish();
                                break;
                            case R.id.action_schedules:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(MainActivity.this, "Post Selected", Toast.LENGTH_SHORT).show();
                               /* MenuItem item4=findViewById(R.id.action_favorites);
                                MenuItem item5=findViewById(R.id.action_schedules);
                                MenuItem item6=findViewById(R.id.action_music);
                                item5.setChecked(true);
                                item4.setChecked(false);
                                item6.setChecked(false);*/
                                toolbar2.getMenu().findItem(R.id.action_favorites).setChecked(false);
                                toolbar2.getMenu().findItem(R.id.action_schedules).setChecked(true);
                                toolbar2.getMenu().findItem(R.id.action_music).setChecked(false);

                                in=new Intent(getBaseContext(),newPost.class);
                                startActivity(in);
                                //  finish();
                                break;
                            case R.id.action_music:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(MainActivity.this, "Profile selected", Toast.LENGTH_SHORT).show();
                               /* MenuItem item7=findViewById(R.id.action_favorites);
                                MenuItem item8=findViewById(R.id.action_schedules);
                                MenuItem item9=findViewById(R.id.action_music);
                                item9.setChecked(true);
                                item7.setChecked(false);
                                item8.setChecked(false);*/
                                toolbar2.getMenu().findItem(R.id.action_favorites).setChecked(false);
                                toolbar2.getMenu().findItem(R.id.action_schedules).setChecked(false);
                                toolbar2.getMenu().findItem(R.id.action_music).setChecked(true);

                                in=new Intent(getBaseContext(), profileActivity.class);
                                startActivity(in);
                                //finish();
                                break;
<<<<<<< HEAD:app/src/main/java/com/innovation/socialmedia/MainActivity.java
                           /* case R.id.action_reminder:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(MainActivity.this, "Reminder selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(),Reminder.class);
                                startActivity(in);
                                break;*/
                            default:
                                break;
=======
>>>>>>> parent of bcfbd8f (changes):app/src/main/java/com/example/socialmedia/MainActivity.java
                        }
                        return true;
                    }
                });
    }

  public static MainActivity callMain(){
        return instance;
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
       
           // SearchView searchView=(SearchView)item.getActionView();
            


           


        private void processearch(String s) {
          //  FirebaseRecyclerOptions<modelGeneral> options=new FirebaseRecyclerOptions.Builder<modelGeneral>().setQuery(FirebaseDatabase.getInstance().getReference().child("hPost").orderByChild("title").startAt(s).endAt(s+"\uf8ff"),modelGeneral.class).build();


           // recyclerView.setAdapter(adapter);
            arrayList.clear();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

            Query query = reference.child("hPost").orderByChild("title").startAt(s).endAt(s+"\uf8ff");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {

                        for (DataSnapshot issue : dataSnapshot.getChildren()) {
                            // do with your result
                            arrayList.add(0,issue.getValue(modelGeneral.class));
                            adapter.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


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
                   // deeplink.toString();


                    String s= deeplink.toString();
                    Log.e(TAG,s);
                    Toast.makeText(getApplicationContext(),"Welcome in UVAH Care",Toast.LENGTH_SHORT).show();

                    int i;
                    int count=0;
                    for(i=s.length()-1;i>=0;i--)
                    {
                        char c=s.charAt(i);
                        if(c=='/'){
                            count++;
                            if(count==2)
                            break;}
                    }
                    String id=s.substring(i+1,s.length()-1);
                    Log.e(TAG,id);
                   DatabaseReference postref=FirebaseDatabase.getInstance().getReference("hPost");

                    try {
                        postref.child(id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                modelGeneral data = snapshot.getValue(modelGeneral.class);
                                Intent intent = new Intent(MainActivity.this, descriptionActivity.class);
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }catch (Exception e){}

                    //Intent intent=new Intent(MainActivity.this,profileActivity .class);
                    // startActivity(intent);
                }

                 //    Intent intent=new Intent(MainActivity.this,profileActivity.class);
                 // startActivity(intent);
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}