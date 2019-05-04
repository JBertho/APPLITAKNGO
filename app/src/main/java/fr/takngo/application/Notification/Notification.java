package fr.takngo.application.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class Notification {

    public static void createNotificationChannel(CharSequence name, String description, NotificationManager notificationManager){
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("group_request", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            notificationManager.createNotificationChannel(channel);
        }
    }

    public static void createNotification(){

    }
}
