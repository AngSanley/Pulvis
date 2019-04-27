package com.ta.hyah.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ta.hyah.MainActivity;
import com.ta.hyah.R;
import com.ta.hyah.constants.Constants;

import java.util.Random;

public class PushNotifications extends FirebaseMessagingService {
    String TAG = "PushNotifications";
    public PushNotifications() {
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0){
            Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null){
            String title = remoteMessage.getNotification().getTitle();
            String message = remoteMessage.getNotification().getBody();

            sendNotification(title, message);
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    public void sendNotification(String tittle, String message){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNELID = "com.ta.hyah.notif";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNELID, "Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNELID)
                .setContentTitle(tittle)
                .setContentText(message)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notifications)
                .setColor(getColor(R.color.colorPrimary));

        notificationManager.notify(new Random().nextInt(), builder.build());
    }
}
