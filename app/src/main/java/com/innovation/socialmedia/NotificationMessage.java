package com.innovation.socialmedia;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmedia.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NotificationMessage extends AppCompatActivity {
    TextView textView,tv_notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_message);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation22);

        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Intent in;
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        int id1=item.getItemId();
                        switch (item.getItemId()) {

                            case R.id.action_favorites:
                                Toast.makeText(NotificationMessage.this, "Home Selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(),MainActivity.class);
                                startActivity(in);
                                break;
                            case R.id.action_schedules:
                                Toast.makeText(NotificationMessage.this, "Post Selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(),newPost.class);
                                startActivity(in);
                                break;
                            case R.id.action_music:
                                Toast.makeText(NotificationMessage.this, "Profile selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(), profileActivity.class);
                                startActivity(in);
                                break;
                            case R.id.action_reminder:
                                Log.i("matching", "matching inside1 bro" + id1);
                                Toast.makeText(NotificationMessage.this, "Reminder selected", Toast.LENGTH_SHORT).show();
                                in=new Intent(getBaseContext(),Reminder.class);
                                startActivity(in);
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
        // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        textView = findViewById(R.id.tv_message);
        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString("message"));
        tv_notes = findViewById(R.id.tv_notes);
        tv_notes.setText(bundle.getString("RemNotes"));
    }
}