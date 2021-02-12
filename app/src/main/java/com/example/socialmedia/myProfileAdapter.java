package com.example.socialmedia;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class myProfileAdapter extends RecyclerView.Adapter<myProfileAdapter.myviewholder>{
    private ArrayList<modelProfile> mlistProfile;
    private Context context;
    modelProfile modelprofile;
    Boolean likechec,bookmarkchecker;
    DatabaseReference likesref,postref;
    FirebaseDatabase database;
    public boolean followchecker=false,check;
    public int ch=0;




    public myProfileAdapter(Context context, ArrayList<modelProfile> mlistProfile){

        this.context=context;
        this.mlistProfile=mlistProfile;
    }
    ClickListener<modelProfile> clickListener;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemfollow,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.usernamef.setText(mlistProfile.get(position).getUsernameP());
        String city=mlistProfile.get(position).getCityP();
        if(city.length()>8)
            city=city.substring(0,5)+"...";
        holder.cityf.setText(city);
        String country=mlistProfile.get(position).getCountryP();
        if(country.length()>8)
            country=country.substring(0,5)+"...";
        holder.countryf.setText(country);
        Glide.with(context).load(mlistProfile.get(position).getImgUrlP()).into(holder.imgProfilef);
        //String id=mlist.get(position).getPid();
       // String postkey = mlistProfile.get(position).getPid();

        String blogerid=mlistProfile.get(position).getId();
        FirebaseUser userfollow= FirebaseAuth.getInstance().getCurrentUser();
        String curentUserIdfollow = userfollow.getUid();
        database = FirebaseDatabase.getInstance();
    //   DatabaseReference followerreference=database.getReference("follower").child(curentUserIdfollow);

        //=findViewById(R.id.following);
        DatabaseReference followreference= FirebaseDatabase.getInstance().getReference("follower");
        DatabaseReference followedreference= FirebaseDatabase.getInstance().getReference("followed");

        FirebaseUser usera=FirebaseAuth.getInstance().getCurrentUser();
        String userida=usera.getUid();


        followreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int followcount=(int)snapshot.child(blogerid).getChildrenCount();
                holder.followerdisplay.setText(Integer.toString(followcount)+" following");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {


            }
        });


        //final String postkey=postref.push().getKey();
        /*
      try{  followerreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}
      catch (Exception e){
          Log.e(TAG,e.getMessage());
      }*/
        holder.itemlayoutf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mlistProfile.get(position));
            }
        });

        followreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                // if(userida.equals(idbloger)){
                // holder.btnfollow.setVisibility(View.GONE);
                // }
                // else{
                //if(check==true){
                if (snapshot.child(blogerid).hasChild(userida)) {
                    // followreference.child(idbloger).child(curentUserId).removeValue();
                    holder.btnfollowf.setText("unfollow");
                    //holder.btnbookmark.setImageResource(R.drawable.imagesb);
                    // followchecker=false;
                     }
                 else {
                    // followreference.child(idbloger).child(curentUserId).setValue(true);
                    holder.btnfollowf.setText("follow");
                    // holder.btnbookmark.setImageResource(R.drawable.images);
                    // followchecker=false;

                }
            }
            //String mobno=databaseReference.Auythecation(mobno);

            // databaseReference.child(ImageUploadId).setValue(imageUploadInfo);


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

try{
    holder.btnfollowf.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            check=true;

            followreference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    //bookmarkchecker=(Boolean)snapshot.child(curentUserId).hasChild(postkey);
                    if(check==true){
                        if(snapshot.child(blogerid).hasChild(userida)){

                            followreference.child(blogerid).child(userida).removeValue();
                           followedreference.child(userida).child(blogerid).removeValue();
                           if(ch==0) {
                               for(modelProfile model:mlistProfile)
                               {
                                   if(model.getId().equals(blogerid))
                                   {
                                       mlistProfile.remove(model);
                                       notifyDataSetChanged();
                                       break;
                                   }
                               }
                           }
                          //  holder.btnfollowf.setText("follow");
                            //holder.btnbookmark.setImageResource(R.drawable.imagesb);
                            check=false;
                        }
                        else{
                            followreference.child(blogerid).child(userida).setValue(true);
                            followedreference.child(userida).child(blogerid).setValue(true);

                            holder.btnfollowf.setText("Unfollow");
                            // holder.btnbookmark.setImageResource(R.drawable.images);
                            check=false;
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

}catch (Exception e){
    Log.e(TAG,e.getMessage());
}


    }





    @Override
    public int getItemCount() {
        return mlistProfile.size();
    }

    public void setOnItemClickListener(ClickListener<modelProfile> itemsClickListener) {
        this.clickListener= itemsClickListener;
    }


    class myviewholder extends RecyclerView.ViewHolder{
        ImageView imgProfilef;
        Button btnfollow,btnfollowf;
        LinearLayout itemlayoutf;
        TextView usernamef,cityf,countryf;
        TextView followerdisplay;
        int followercount;
        TextView itemfollower;
        DatabaseReference followersref;

        //@SuppressLint("WrongViewCast")
        public myviewholder(@NonNull View itemView) {
            super(itemView);
             followersref=FirebaseDatabase.getInstance().getReference().child("follower");
            imgProfilef=(ImageView) itemView.findViewById(R.id.itemprofilepicf);
            usernamef=(TextView) itemView.findViewById(R.id.itemuserNf);
            cityf=(TextView) itemView.findViewById(R.id.itemuserCf);
            countryf=(TextView) itemView.findViewById(R.id.itemuserCounf);
            btnfollow=(Button) itemView.findViewById(R.id.btnfollowf);
            followerdisplay=(TextView) itemView.findViewById(R.id.itemfollowf);
            itemlayoutf=(LinearLayout)itemView.findViewById(R.id.llf);
           // itemfollower=(TextView) itemView.findViewById(R.id.itemfollowf);
            btnfollowf=itemView.findViewById(R.id.btnfollowf);

        }
    }



}


