package com.anshu.digicraft;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Build;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "default_fcm_channel";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = "FCM Message";
        String body = "";

        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            body = remoteMessage.getNotification().getBody();
        } else if (!remoteMessage.getData().isEmpty()) {
            body = remoteMessage.getData().toString();
        }

        showNotification(title, body);
        updateMainActivity(title, body);

    }

    @Override
    public void onNewToken( String token) {
        super.onNewToken(token);
        Log.d("FCM_SERVICE", "New token: " + token);
        // TODO: send token to server
    }

    private void showNotification(String title, String message) {
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel =
                    new NotificationChannel(CHANNEL_ID, "FCM Notifications",
                            NotificationManager.IMPORTANCE_DEFAULT);
            nm.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true);

        nm.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void updateMainActivity(String title, String body) {
        String combined = "Title: " + title + "\nMessage: " + body;


        Intent intent = new Intent("fcmmessage");
        intent.putExtra("message", combined);
        sendBroadcast(intent);
    }



}
