package com.example.socialmedia;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import androidx.cardview.widget.CardView;
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


public class myAdapterNotify extends RecyclerView.Adapter<myAdapterNotify.myviewholder>{
    private ArrayList<modelGeneral> mlist;
    private Context context;
    modelGeneral model;
   // Boolean likechec,bookmarkchecker=false,followerchecker=false;
  //  DatabaseReference likesref,postref,profilereference,followerreference,followedreference;
    FirebaseDatabase database;


    public myAdapterNotify(Context context, ArrayList<modelGeneral> mlist){

        this.context=context;
        this.mlist=mlist;
    }
    ClickListener<modelGeneral> clickListener;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemnotify,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {


           try {
               holder.titlenotify.setText(mlist.get(position).getTitle());
               Glide.with(context).load(mlist.get(position).getUrlimage()).into(holder.postpicnotify);
               //String id=mlist.get(position).getPid();

           }catch (Exception e){}

        String postkey = mlist.get(position).getPid();
        String idbloger = mlist.get(position).getBlogerid();

        FirebaseUser userlike = FirebaseAuth.getInstance().getCurrentUser();
        String curentUserId = userlike.getUid();
        database = FirebaseDatabase.getInstance();
        DatabaseReference profileref=FirebaseDatabase.getInstance().getReference("profile");
        profileref.child(idbloger).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String imgurl = snapshot.child("imgUrlP").getValue().toString();
                    Glide.with(context).load(imgurl).into(holder.blogpicnotify);
                }catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.llnotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mlist.get(position));
            }
        });

        holder.notififyreferece.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try{
                Boolean check= (Boolean) snapshot.child(mlist.get(position).getBlogerid()).child(postkey).getValue();
                Log.e(TAG, String.valueOf(check));

                if(check==false){
                   holder.cardViewNotify.setCardBackgroundColor(Color.WHITE);

                }}catch (Exception e){}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setOnItemClickListener(ClickListener<modelGeneral> itemsClickListener) {
        this.clickListener= itemsClickListener;
    }

    class myviewholder extends RecyclerView.ViewHolder{
      ImageView blogpicnotify,postpicnotify;
      TextView titlenotify;
      LinearLayout llnotify;
      DatabaseReference notififyreferece;
        FirebaseAuth user;
        CardView cardViewNotify;


        public myviewholder(@NonNull View itemView) {
            super(itemView);

          blogpicnotify=itemView.findViewById(R.id.blogerpicnotify);
          postpicnotify=itemView.findViewById(R.id.postnotify);
          titlenotify=itemView.findViewById(R.id.titlenotify);
          llnotify=itemView.findViewById(R.id.llnotify);
           user=FirebaseAuth.getInstance();
          String userid=user.getUid();
          cardViewNotify=itemView.findViewById(R.id.carViewnotify);
          cardViewNotify.setCardBackgroundColor(Integer.parseInt("#AED6F1"));
        notififyreferece=FirebaseDatabase.getInstance().getReference("notification").child("old").child(userid);
        }





}}

