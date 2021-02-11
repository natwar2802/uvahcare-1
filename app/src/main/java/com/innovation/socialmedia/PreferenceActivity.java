package com.innovation.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferenceActivity extends AppCompatActivity {
    ImageView btnNextMain;
    Button updatePreference;
    ArrayList<String> temp=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_preference);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        updatePreference=findViewById(R.id.updatePreference);
        LinearLayout l=findViewById(R.id.defense);
        FirebaseUser userpreference1= FirebaseAuth.getInstance().getCurrentUser();
        String idpreference1=userpreference1.getUid();
        DatabaseReference prefeference1= FirebaseDatabase.getInstance().getReference("profile").child(idpreference1).child("userPreference");
        updatePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(temp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Select Preferences before Proceeding...", Toast.LENGTH_LONG).show();
                }
                else {
                    prefeference1.setValue(temp);
                    Intent i = new Intent(PreferenceActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        });
        try{
        prefeference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                 temp= (ArrayList<String>) snapshot.getValue();


                for(int i=0;i<l.getChildCount();i++)
                {
                    LinearLayout l1=(LinearLayout)(l.getChildAt(i));
                    for(int j=0;j<l1.getChildCount();j++)
                    {
                        CardView c=(CardView)(l1.getChildAt(j));
                        TextView t = ((TextView) c.getChildAt(0));
                        String k=t.getText().toString();
                        try{
                        if(temp.contains(k)){
                            c.setCardBackgroundColor(Color.parseColor("#B388FF"));

                        }}catch (Exception e){}
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}catch (Exception e){}

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

        if(temp.contains(k)){
            c.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
            temp.remove(k);

        }
        else {
            c.setCardBackgroundColor(Color.parseColor("#B388FF"));
            temp.add(k);
        }}
    }
