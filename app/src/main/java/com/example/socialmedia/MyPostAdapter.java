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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyPostAdapter extends RecyclerView.Adapter<MyPostAdapter.myviewholder>{
    private ArrayList<modelGeneral> mlist;
    private Context context;
    modelGeneral model;
    Boolean likechec,bookmarkchecker;
    DatabaseReference likesref,postref;
    FirebaseDatabase database;




    public MyPostAdapter(Context context, ArrayList<modelGeneral> mlist){

        this.context=context;
        this.mlist=mlist;
    }
    ClickListener<modelGeneral> clickListener;

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.itemd,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.descrip.setText(mlist.get(position).getBrief());
        holder.title.setText(mlist.get(position).getTitle());
        Glide.with(context).load(mlist.get(position).getUrlimage()).into(holder.img);
        //String id=mlist.get(position).getPid();

        String postkey = mlist.get(position).getPid();


        FirebaseUser userlike = FirebaseAuth.getInstance().getCurrentUser();
        String curentUserId = userlike.getUid();
        database = FirebaseDatabase.getInstance();
        postref = database.getReference("hPost");
        likesref = database.getReference("likes");

        //final String postkey=postref.push().getKey();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("bookmark");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(curentUserId).hasChild(postkey)) {
                    holder.btnbookmark1.setImageResource(R.drawable.images);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.itemlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClick(mlist.get(position));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.mypostref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        holder.mypostref.child(curentUserId).child(postkey).removeValue();
                        holder.postref3.child(postkey).removeValue();
                        holder.bookmarkref.child(curentUserId).child(postkey).removeValue();
                        mlist.remove(position);
                       // notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        holder.setLikesbuttonStatus(postkey);

        holder.inc1.setOnClickListener(new View.OnClickListener() {

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


    }





    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public void setOnItemClickListener(ClickListener<modelGeneral> itemsClickListener) {
        this.clickListener= itemsClickListener;
    }

    class myviewholder extends RecyclerView.ViewHolder {

        ImageView img, img2;
        LinearLayout itemlayout;
        TextView title, title2, descrip, descrip2, likeddisplay;
        DatabaseReference likesref, bookmarkref, mypostref, postref3;
        FirebaseDatabase database;
        ImageButton btnbookmark1, btnbookmark;
        int likescount;

        Button inc1, delete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            database = FirebaseDatabase.getInstance();
            likesref = database.getReference("likes");
            bookmarkref = database.getReference("bookmark");
            delete = (Button) itemView.findViewById(R.id.delete1);
            mypostref = FirebaseDatabase.getInstance().getReference("mypost");
            postref3 = FirebaseDatabase.getInstance().getReference("hPost");

            img = (ImageView) itemView.findViewById(R.id.img11);
            title = (TextView) itemView.findViewById(R.id.title11);
            descrip = (TextView) itemView.findViewById(R.id.desc11);

            img2 = (ImageView) itemView.findViewById(R.id.img12);
            title2 = (TextView) itemView.findViewById(R.id.title12);
            descrip2 = (TextView) itemView.findViewById(R.id.desc12);
            inc1 = (Button) itemView.findViewById(R.id.inc1);
            likeddisplay = (TextView) itemView.findViewById(R.id.likedisplay);
            itemlayout = (LinearLayout) itemView.findViewById(R.id.ll1);
            btnbookmark1 = (ImageButton) itemView.findViewById(R.id.bm1);

        }

        public void setLikesbuttonStatus(String postkey) {
            inc1 = itemView.findViewById(R.id.inc1);
            likeddisplay = itemView.findViewById(R.id.likedisplay);
            likesref = FirebaseDatabase.getInstance().getReference("likes");
            FirebaseUser usera = FirebaseAuth.getInstance().getCurrentUser();
            String userida = usera.getUid();
            String likes = "likes";

            likesref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(postkey).hasChild(userida)) {
                        likescount = (int) snapshot.child(postkey).getChildrenCount();
                        likeddisplay.setText(Integer.toString(likescount) + likes);
                    } else {
                        likescount = (int) snapshot.child(postkey).getChildrenCount();
                        likeddisplay.setText(Integer.toString(likescount) + likes);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {


                }
            });


        }


    }}

