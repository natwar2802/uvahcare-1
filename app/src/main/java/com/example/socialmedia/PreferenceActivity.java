package com.example.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PreferenceActivity extends AppCompatActivity {
    Button btnNextMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        btnNextMain=findViewById(R.id.nexttoMain);
        btnNextMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PreferenceActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
       LinearLayout l=findViewById(R.id.defense);
        for(int i=0;i<l.getChildCount();i++)
        {
            LinearLayout l1=(LinearLayout)(l.getChildAt(i));
            for(int j=0;j<l1.getChildCount();j++)
            {
                CardView c=(CardView)(l1.getChildAt(j));
                TextView t = ((TextView) c.getChildAt(0));
                String k=t.getText().toString();
                DatabaseReference prefeference= FirebaseDatabase.getInstance().getReference("preference");
                FirebaseUser userpreference= FirebaseAuth.getInstance().getCurrentUser();
                String idpreference=userpreference.getUid();
                prefeference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(k).hasChild(idpreference)){
                            c.setCardBackgroundColor(Color.parseColor("#B388FF"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        }
        for(int i=0;i<l.getChildCount();i++)
        {
            LinearLayout l1=(LinearLayout)(l.getChildAt(i));
            for(int j=0;j<l1.getChildCount();j++)
            {
                CardView c=(CardView)(l1.getChildAt(j));
                TextView t = ((TextView) c.getChildAt(0));
                t.setTextSize(20);
            }
        }

    }
    public void f(View view)
    {
        CardView c=(CardView)findViewById(view.getId());
        TextView t = ((TextView) c.getChildAt(0));
        String k=t.getText().toString();
        //List<String> l2=MainActivity.getInstance().pref;

        DatabaseReference prefeference= FirebaseDatabase.getInstance().getReference("preference");
        FirebaseUser userpreference= FirebaseAuth.getInstance().getCurrentUser();
        String idpreference=userpreference.getUid();
        prefeference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(k).hasChild(idpreference)){
                    c.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                    prefeference.child(k).child(idpreference).removeValue();
                }
                else{
                    c.setCardBackgroundColor(Color.parseColor("#B388FF"));
                    prefeference.child(k).child(idpreference).setValue(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}