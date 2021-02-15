package com.innovation.socialmedia;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.socialmedia.R;
import com.innovation.socialmedia.Adapter.EventAdapter;
import com.innovation.socialmedia.Database.DatabaseClass;
import com.innovation.socialmedia.Database.EntityClass;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Reminder extends AppCompatActivity {
   ImageButton GoCreateEvent;
    EventAdapter eventAdapter;
    RecyclerView recyclerview;
    DatabaseClass databaseClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        GoCreateEvent = findViewById(R.id.GoCreateEvent);
        GoCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Reminder.this, CreateEvent.class);
                startActivity(intent);
            }
        });
        recyclerview = findViewById(R.id.recyclerview);
        databaseClass = DatabaseClass.getDatabase(getApplicationContext());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();

    }

    private void setAdapter() {
        List<EntityClass> classList = databaseClass.EventDao().getAllData();
        eventAdapter = new EventAdapter(getApplicationContext(), classList);
        recyclerview.setAdapter(eventAdapter);
        String currentdate = new SimpleDateFormat("dd-MM-yyyy").format(new Date());
        int i=0;
        while (i<classList.size()){
            String eventd=classList.get(i).getEventStartdate();
            int check=currentdate.compareTo(eventd);
            if(check>0){

                classList.remove(i);
            }
            else{
                i++;
            }

        }
        Collections.reverse(classList);
        eventAdapter.notifyDataSetChanged();
    }
}