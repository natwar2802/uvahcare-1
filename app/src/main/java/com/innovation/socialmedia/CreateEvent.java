package com.innovation.socialmedia;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.socialmedia.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.innovation.socialmedia.Database.DatabaseClass;
import com.innovation.socialmedia.Database.EntityClass;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CreateEvent extends AppCompatActivity {
    private static final String TAG = "";
    Button btn_time1 ,btn_datestart ,btn_done;
    ImageButton btn_record;
    EditText editext_message,edittext_Notes;
    String timeTonotify1;
    DatabaseClass databaseClass;
    int NotifCount;
    public static int ch_activity=0;
    DatabaseReference countref;
    String RemTitle,RemNotes="";
    Bitmap RemBitmap;
    //ImageView imageViewReminder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        btn_record = findViewById(R.id.btn_record);
        editext_message = findViewById(R.id.edittext_message);
        btn_time1 = findViewById(R.id.btn_time1);
       // imageViewReminder = findViewById(R.id.imgviewReminder);
        //btn_time2 = findViewById(R.id.btn_time2);
        edittext_Notes = findViewById(R.id.edittext_Notes);
        if(ch_activity==1) {
            Intent in = getIntent();
            RemTitle = in.getStringExtra("ReminderTitle");
            editext_message.setText(RemTitle);
            RemNotes = in.getStringExtra("ReminderNotes");
            edittext_Notes.setText(RemNotes);
            //byte[] byteArray = in.getByteArrayExtra("image");
            //Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //RemBitmap = bmp;
            //imageViewReminder.setImageBitmap(RemBitmap);
        }
        btn_datestart = findViewById(R.id.btn_datestart);
        //btn_dateend = findViewById(R.id.btn_dateend);
        btn_done = findViewById(R.id.btn_done);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        countref=FirebaseDatabase.getInstance().getReference("NotifCount").child(user.getUid());
        final int[] ch = {0};
        countref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(ch[0] ==0)
                {
                    String s=snapshot.getValue().toString();
                   NotifCount =Integer.parseInt(s);
                    ch[0] =1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordSpeech();
            }
        });
        btn_time1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime1();
            }
        });
        /*btn_time2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectTime2();
            }
        });*/
        btn_datestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateStart();
            }
        });
        /*btn_dateend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateEnd();
            }
        });*/
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();

            }
        });

        databaseClass = DatabaseClass.getDatabase(getApplicationContext());
       // ch_activity=0;

    }


    private void submit() {
        String text = editext_message.getText().toString().trim();
        RemNotes = edittext_Notes.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Please Enter or record the text", Toast.LENGTH_SHORT).show();
        } else {
            if (btn_time1.getText().toString().equals("Select Time") || btn_datestart.getText().toString().equals("Select Date")) {
                Toast.makeText(this, "Please select date and time", Toast.LENGTH_SHORT).show();
            } else {
                EntityClass entityClass = new EntityClass();
                String value = (editext_message.getText().toString().trim());
                String dateStart = (btn_datestart.getText().toString().trim());
                //String dateEnd  = (btn_dateend.getText().toString().trim());
                String time1 = (btn_time1.getText().toString().trim());
                //String time2 = (btn_time2.getText().toString().trim());
                entityClass.setEventStartdate(dateStart);
                entityClass.setNotes(RemNotes);
                entityClass.setEventname(value);
                entityClass.setEventtime1(time1);
                //entityClass.setEventtime2(time2);
                //entityClass.setEventEnddate(dateEnd);
                databaseClass.EventDao().insertAll(entityClass);
                int i= NotifCount;

                i=setAlarmMain(i,value,timeTonotify1,dateStart);
               // i=setAlarmMain(i,value,timeTonotify2,dateStart,dateEnd);
            }
        }
    }
    public int setAlarmMain(int i,String value,String time, String dateStart)
    {
        int j=i;
        String Startdateandtime;
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        Date DateStart;
        //Date DateEnd;
        Startdateandtime = dateStart + " " + time;
        //Enddateandtime = dateEnd + " " + time;
        try {
            DateStart = formatter.parse(Startdateandtime);
           // DateEnd = formatter.parse(Enddateandtime);
            long t1=DateStart.getTime();
            //long t2=DateEnd.getTime();

           // while(t1<=t2) {
                //if(t1>=System.currentTimeMillis()) {
                    setAlarm(value, t1, dateStart, time, j);
                    j++;
                //}
               // t1+=24*60*60*1000;

           // }
        }catch (ParseException e){e.printStackTrace();}
        countref.setValue(j);
        return j;
    }

    private void selectTime1() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify1 = i + ":" + i1;
                btn_time1.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }
    /*private void selectTime2() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                timeTonotify2 = i + ":" + i1;
                btn_time2.setText(FormatTime(i, i1));
            }
        }, hour, minute, false);
        timePickerDialog.show();

    }*/

    private void selectDateStart() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String days="";
                if(day/10==0)
                    days+="0"+day;
                else
                    days+=day;
                int mth=month+1;
                String months="";
                if(mth<=9)
                        months+="0"+mth;
                else
                    months+=mth;
                btn_datestart.setText(days + "-" + (months) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }
    /*private void selectDateEnd() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btn_dateend.setText(day + "-" + (month + 1) + "-" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }*/

    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }


    private void recordSpeech() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, "en-US");
        try {

            startActivityForResult(intent, 1);
        } catch (Exception e) {
            Toast.makeText(this, "Your device does not support Speech recognizer", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                editext_message.setText(text.get(0));
            }
        }

    }

    private void setAlarm(String text, long time_milli,String date, String time, int i) {
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getApplicationContext(), AlarmBrodcast.class);
        intent.putExtra("event", text);
        intent.putExtra("time", date);
        intent.putExtra("date", time);
        intent.putExtra("id", i);
        intent.putExtra("RemNotes",RemNotes);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), i, intent, PendingIntent.FLAG_ONE_SHOT);
        am.set(AlarmManager.RTC_WAKEUP, time_milli+0, pendingIntent);
        finish();

    }
}