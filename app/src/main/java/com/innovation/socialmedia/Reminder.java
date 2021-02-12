package com.innovation.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.socialmedia.R;
import com.innovation.socialmedia.Adapter.EventAdapter;
import com.innovation.socialmedia.Database.DatabaseClass;
import com.innovation.socialmedia.Database.EntityClass;


import java.util.List;

public class Reminder extends AppCompatActivity{
    ImageButton createEventbtn;
    EventAdapter eventAdapter;
    RecyclerView recyclerview;
    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        createEventbtn = findViewById(R.id.GoCreateEvent);
        recyclerview = findViewById(R.id.recyclerview);
        createEventbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Reminder.this,CreateEvent.class);
                startActivity(i);
            }
        });

        databaseClass = DatabaseClass.getDatabase(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();

    }

    private void setAdapter() {
        List<EntityClass> classList = databaseClass.EventDao().getAllData();
        eventAdapter = new EventAdapter(getApplicationContext(),classList);
        recyclerview.setAdapter(eventAdapter);
    }

}
