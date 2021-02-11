package com.innovation.socialmedia;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
<<<<<<< HEAD
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
=======
>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.example.socialmedia.R;

<<<<<<< HEAD
import java.util.Calendar;

=======
>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28
public class AlarmBrodcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        String text = bundle.getString("event");
        String date = bundle.getString("date") + " " + bundle.getString("time");
<<<<<<< HEAD
        int id = bundle.getInt("id");
=======
>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28

        //Click on Notification

        Intent intent1 = new Intent(context, NotificationMessage.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent1.putExtra("message", text);
        //Notification Builder
<<<<<<< HEAD
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (2*id+1), intent1, PendingIntent.FLAG_ONE_SHOT);
=======
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, PendingIntent.FLAG_ONE_SHOT);
>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, "notify_001");

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_layout);
        contentView.setImageViewResource(R.id.image, R.mipmap.ic_launcher);
<<<<<<< HEAD

        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, id, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, text+" "+id);
        contentView.setTextViewText(R.id.date, date);
        mBuilder.setSmallIcon(R.drawable.bell);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR;
=======
        PendingIntent pendingSwitchIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        contentView.setOnClickPendingIntent(R.id.flashButton, pendingSwitchIntent);
        contentView.setTextViewText(R.id.message, text);
        contentView.setTextViewText(R.id.date, date);
        mBuilder.setSmallIcon(R.drawable.ic_backspace_black_24dp);
        mBuilder.setAutoCancel(true);
        mBuilder.setOngoing(true);
        mBuilder.setPriority(Notification.PRIORITY_HIGH);
        mBuilder.setOnlyAlertOnce(true);
        mBuilder.build().flags = Notification.FLAG_NO_CLEAR | Notification.PRIORITY_HIGH;
>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28
        mBuilder.setContent(contentView);
        mBuilder.setContentIntent(pendingIntent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }

        Notification notification = mBuilder.build();
<<<<<<< HEAD
        notification.vibrate = new long[]{150, 300, 150, 400,150,600};
        notificationManager.notify(id, notification);
        try{

            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(context, som);
            toque.play();
        }
        catch(Exception e){}
=======
        notificationManager.notify(1, notification);

>>>>>>> 9f2ecbdfaf86dd8df4fe9b5465e94d6a441a2f28

    }
}