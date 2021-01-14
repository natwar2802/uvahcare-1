package com.example.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;


public class myAdapter extends RecyclerView.Adapter<myAdapter.myviewholder>{
    private ArrayList<modelGeneral> mlist;
    private Context context;
    modelGeneral model;
    Boolean likechec,bookmarkchecker=false,followerchecker=false;
    DatabaseReference likesref,postref,profilereference,followerreference,followedreference;
    FirebaseDatabase database;
    public int ch=0;
    public int bookmarkch=0;




    public myAdapter(Context context, ArrayList<modelGeneral> mlist){

        this.context=context;
        this.mlist=mlist;
    }
    ClickListener<modelGeneral> clickListener;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.descrip.setText(mlist.get(position).getBrief());
        holder.title.setText(mlist.get(position).getTitle());
        Glide.with(context).load(mlist.get(position).getUrlimage()).into(holder.img);
        //String id=mlist.get(position).getPid();

        String postkey=mlist.get(position).getPid();
        String idbloger=mlist.get(position).getBlogerid();




        FirebaseUser userlike= FirebaseAuth.getInstance().getCurrentUser();
        String curentUserId= userlike.getUid();
        database=FirebaseDatabase.getInstance();
        profilereference = database.getReference("profile");
        postref=database.getReference("hPost");
        likesref=database.getReference("likes");
        database=FirebaseDatabase.getInstance();
        followerreference=database.getReference("follower");
        followedreference=database.getReference("followed");

       /* profilereference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.itemusername.setText(snapshot.child(idbloger).child("usernameP").getValue().toString());
                String url=snapshot.child(idbloger).child("imgUrlP").getValue().toString();
                Glide.with(context.getApplicationContext()).load(url).into(holder.itemprofilepic);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

       if(ch==1){
            holder.btndel.setVisibility(View.VISIBLE);
            holder.btndel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.mypostref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            holder.mypostref.child(curentUserId).child(postkey).removeValue();
                            holder.postref3.child(postkey).removeValue();
                            holder.bookmarkref.child(curentUserId).child(postkey).removeValue();
                            holder.likesref.child(postkey).removeValue();
                            holder.referencerate.child(postkey).removeValue();

                            mlist.remove(position);
                            holder.followerefernce1.child(curentUserId).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                    for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                                        try{
                                        holder.notifyreference1.child(dataSnapshot.getKey()).child(postkey).removeValue();}
                                        catch (Exception e){}
                                        try {
                                            holder.notifyreference2.child(dataSnapshot.getKey()).child(postkey).removeValue();
                                        }catch (Exception e){}

                                    }

                                    // adapter2.notifyDataSetChanged();
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                    // notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });

        }

        DatabaseReference reportreference=FirebaseDatabase.getInstance().getReference("report");

        holder.mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reportreference.child(postkey).child(curentUserId).setValue(true);
            }
        });

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("bookmark");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(curentUserId).hasChild(postkey)) {
                    holder.btnbookmark.setImageResource(R.drawable.images);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //final String postkey=postref.push().getKey();

try{
        followerreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                if(curentUserId.equals(idbloger)){
                     holder.btnfollow.setVisibility(View.GONE);
                }
                else{
                   // if(followerchecker.equals(true)){
                        if(snapshot.child(idbloger).hasChild(curentUserId)){
                            // followreference.child(idbloger).child(curentUserId).removeValue();
                           holder.btnfollow.setText("unfollow");
                            //holder.btnfollow.setImageResource(R.drawable.unfollowicon);
                            // followchecker=false;
                        }
                        else{
                            // followreference.child(idbloger).child(curentUserId).setValue(true);
                           holder.btnfollow.setText("follow");
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

        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mlist.get(position));
            }
        });
        //  Glide.with(holder.img2.getContext()).load(model.getUrlimage()).into(holder.img2);

        holder.setLikesbuttonStatus(postkey);

        holder.inc.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(context.getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
                likechec =true;
                holder.likesref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        if(likechec.equals(true)){
                            if(snapshot.child(postkey).hasChild(curentUserId)){
                                holder.likesref.child(postkey).child(curentUserId).removeValue();
                                likechec=false;
                            }
                            else{
                                holder.likesref.child(postkey).child(curentUserId).setValue(true);
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



        holder.btnbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bookmarkchecker=true;

                holder.bookmarkref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String bmkey= holder.bookmarkref.child(postkey).push().getKey();
                        String bmtitle=mlist.get(position).getTitle();
                        String bmbrief=mlist.get(position).getBrief();
                        String bmdis=mlist.get(position).getDescription();
                        String bmuri=mlist.get(position).getUrlimage();
                        String bmpostid=mlist.get(position).getPid();
                        String blogerid=mlist.get(position).getBlogerid();
                        String prefrence="prefernce";
                        long data=System.currentTimeMillis();
                        long rateno=0;
                        float rating=0;
                        int claps=0;

                        modelGeneral info = new modelGeneral(bmtitle,bmbrief,bmuri,bmdis,bmpostid,blogerid,prefrence,data,rateno,rating,claps);
                        //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);

                        if(bookmarkchecker.equals(true)){
                            if(snapshot.child(curentUserId).hasChild(postkey)){
                                
                                holder.bookmarkref.child(curentUserId).child(postkey).removeValue();
                                holder.btnbookmark.setImageResource(R.drawable.imagesb);
                                bookmarkchecker=false;
                            }
                            else{

                                holder.bookmarkref.child(curentUserId).child(postkey).setValue(true);
                                holder.btnbookmark.setImageResource(R.drawable.images);
                                bookmarkchecker=false;
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


       holder.hbtnsharepost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BitmapDrawable bitmapDrawable=(BitmapDrawable)holder.img.getDrawable();

                if(bitmapDrawable==null){
                    //post without image
                    shareTextOnly(holder.title,holder.descrip);
                }
                else{
                    //post with the image
                    Bitmap bitmap=bitmapDrawable.getBitmap();
                    shareTextAndImage(holder.title,holder.descrip,bitmap);
                }
            }


        });



        holder.btnfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                followerchecker=true;

                followerreference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                        if(followerchecker.equals(true)){
                            if(snapshot.child(idbloger).hasChild(curentUserId)){
                                 holder.notifyreference1.addValueEventListener(new ValueEventListener() {
                                     @Override
                                     public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                         if(snapshot1.child(curentUserId).hasChild(idbloger)){
                                         holder.notifyreference1.child(curentUserId).child(idbloger).removeValue();
                                         holder.notifyreference2.child(curentUserId).child(idbloger).removeValue();
                                         }
                                     }

                                     @Override
                                     public void onCancelled(@NonNull DatabaseError error) {

                                     }
                                 });


                                followerreference.child(idbloger).child(curentUserId).removeValue();
                                followedreference.child(curentUserId).child(idbloger).removeValue();
                                holder.btnfollow.setText("follow");
                                //holder.btnfollow.setImageResource(R.drawable.followicon);
                                followerchecker=false;
                            }
                            else{
                                followerreference.child(idbloger).child(curentUserId).setValue(true);
                                followedreference.child(curentUserId).child(idbloger).setValue(true);

                               holder.btnfollow.setText("Unfollow");
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
        try{
        profilereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                modelProfile modelp=snapshot.child(idbloger).getValue(modelProfile.class);

                    String userp=modelp.getUsernameP();
                    String url=modelp.getImgUrlP();

                    holder.itemusername.setText(userp);
                    // String url=snapshot.child(idbloger).child("imgUrlP").getValue().toString();
                    Glide.with(context.getApplicationContext()).load(url).into(holder.itemprofilepic);
                }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}catch (Exception e){}
/*        holder.btnupdateProfile.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              holder.setProfile();
          }
      });*/

       // float frate=ratingpop.getRating();

      //  holder.referencerate.child(postkey).child(curentUserId).setValue(frate);

        holder.referencerate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(postkey)) {
                    int n = (int) snapshot.child(postkey).getChildrenCount()-1;
                   // if(snapshot.child(postkey).child("sum").get)
                 /*   if (snapshot.child(postkey).child("sum").getValue().getClass().getName().equals("java.lang.Double")){
                    Double sum1 = (Double) snapshot.child(postkey).child("sum").getValue();
                    double sum=sum1.doubleValue();
                    double avrate=sum/n;
                    String avr=Double.toString(avrate);

                   holder.displayrate.setText(avr);
                    }*/

                      try{  Long sum1= (Long) snapshot.child(postkey).child("sum").getValue();
                       double sum=sum1.doubleValue();
                        float avrate=((float) sum)/n;
                        String avr=Float.toString(avrate);
                        holder.displayrate.setText(avr);
                      }catch (Exception e){}


                }
                else{
            // holder.displayrate.setText(0);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void shareTextAndImage(TextView title, TextView descrip, Bitmap bitmap) {
        String shareBody=title + "\n" + descrip;
        Uri uri=SavedImageToShare(bitmap);
        Intent shareintent=new Intent(Intent.ACTION_SEND);
        shareintent.putExtra(Intent.EXTRA_STREAM,uri);
        shareintent.putExtra(Intent.EXTRA_TEXT,shareBody);
        shareintent.putExtra(Intent.EXTRA_SUBJECT,"subject here");
        shareintent.setType("image/png");
        context.startActivity(Intent.createChooser(shareintent,"share via"));

    }

    private Uri SavedImageToShare(Bitmap bitmap) {
        File imagefolder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try{
            imagefolder.mkdirs();
            File file=new File(imagefolder,"shared_images");
            FileOutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context,"com.example.socialmedia.fileprovider",file);


        }
        catch (Exception e){
            Toast.makeText(context,""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    private void shareTextOnly(TextView title, TextView descrip) {
        String shareBody=title + "\n" + descrip;

        Intent shareintent=new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_SUBJECT,"Subject here");
        shareintent.putExtra(Intent.EXTRA_TEXT,shareBody);
        context.startActivity(Intent.createChooser(shareintent,"share via"));
    }


    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setOnItemClickListener(ClickListener<modelGeneral> itemsClickListener) {
        this.clickListener= itemsClickListener;
    }

    class myviewholder extends RecyclerView.ViewHolder{

        ImageView img,img2;
        LinearLayout itemlayout;
        TextView title,title2,descrip,descrip2,likeddisplay;
        DatabaseReference likesref,bookmarkref,profileref, referencerate,mypostref,postref3,followerefernce1,notifyreference1,notifyreference2;
        FirebaseDatabase database;
        ImageButton btnbookmark,mImageButton,inc,hbtnsharepost;
        int likescount;
        Button btnupdateProfile,btnfollow;

        TextView username,usercountry,usercity;
        ImageView profilepic;

        ImageView itemprofilepic;
        TextView itemusername,displayclap;
        Button btnrating;
        TextView displayrate;
        Button btndel;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            mImageButton= (ImageButton) itemView.findViewById(R.id.imageButton);

           // mRatingBar=itemView.findViewById(R.id.rating);
            //mRatingBar.setRating(5);
            database=FirebaseDatabase.getInstance();
            likesref=database.getReference("likes");
            bookmarkref=database.getReference("bookmark");
            displayrate=itemView.findViewById(R.id.displayrate);

            img=(ImageView) itemView.findViewById(R.id.img1);
            title=(TextView) itemView.findViewById(R.id.title1);
            descrip=(TextView) itemView.findViewById(R.id.desc1);

            img2=(ImageView) itemView.findViewById(R.id.img12);
            title2=(TextView) itemView.findViewById(R.id.title12);
            descrip2=(TextView) itemView.findViewById(R.id.desc12);
            inc=(ImageButton) itemView.findViewById(R.id.inc);
           hbtnsharepost=(ImageButton) itemView.findViewById(R.id.btnsharepost);
            likeddisplay=(TextView) itemView.findViewById(R.id.displayclap);
            itemlayout=(LinearLayout)itemView.findViewById(R.id.ll);
            btnbookmark=(ImageButton) itemView.findViewById(R.id.bm);
            btnupdateProfile= (Button)itemView.findViewById(R.id.updateProfile);
            itemprofilepic=(ImageView)itemView.findViewById(R.id.itemprofilepic);
            itemusername=(TextView)itemView.findViewById(R.id.itemuserN);
            btnfollow=itemView.findViewById(R.id.btnfollow);
            referencerate= FirebaseDatabase.getInstance().getReference("rating");
            btndel=itemView.findViewById(R.id.btndel);

            displayclap=itemView.findViewById(R.id.displayclap);

            mypostref = FirebaseDatabase.getInstance().getReference("mypost");
            postref3 = FirebaseDatabase.getInstance().getReference("hPost");
            notifyreference1 =  FirebaseDatabase.getInstance().getReference("notification").child("old");
            notifyreference2 =  FirebaseDatabase.getInstance().getReference("notification").child("new");

            followerefernce1 = FirebaseDatabase.getInstance().getReference("follower");



        }

        public void setLikesbuttonStatus(String postkey){
            inc=itemView.findViewById(R.id.inc);
            likesref= FirebaseDatabase.getInstance().getReference("likes");
            FirebaseUser usera=FirebaseAuth.getInstance().getCurrentUser();
            String userida=usera.getUid();

            likesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    likescount=(int)snapshot.child(postkey).getChildrenCount();
                    postref3.child(postkey).child("claps").setValue(likescount);
                    displayclap.setText(Integer.toString(likescount));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        }
        private void showPopupMenu(View view,int position) {
            // inflate menu
            PopupMenu popup = new PopupMenu(view.getContext(),view );
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.popupmenu, popup.getMenu());
            popup.setOnMenuItemClickListener(new MyMenuItemClickListener(position));
            popup.show();
        }
        public void setProfile(){
            username=itemView.findViewById(R.id.userName);
            usercity=itemView.findViewById(R.id.tcity);
            usercountry=itemView.findViewById(R.id.tcountry);
            profilepic=itemView.findViewById(R.id.profilepic);
            profileref=  FirebaseDatabase.getInstance().getReference("profile");
            FirebaseUser pusera=FirebaseAuth.getInstance().getCurrentUser();
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
                    Glide.with(context).load(url).into(profilepic);
                    // likescount=(int)snapshot.child(postkey).getChildrenCount();
                    //  likeddisplay.setText(Integer.toString(likescount)+likes);



                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });

        }


    }
   /* public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }*/



}

