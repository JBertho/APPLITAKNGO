package fr.takngo.application.Notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import fr.takngo.application.MyRequest;
import fr.takngo.application.R;

public class Notification{

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

    public static void createDriverNotification(Intent intent, PendingIntent pendingIntent, Context activityContext, Context packageContext){
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activityContext,"group_request")
                .setSmallIcon(R.drawable.second)
                .setContentTitle(activityContext.getString(R.string.notification_title))
                .setContentText(activityContext.getString(R.string.notification_content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(0xff00ff00, 300, 100)
                //l'intent qui va etre lancer quand le user va cliquer dessus
                .setContentIntent(pendingIntent)
                //supprime la notification une fois cliquée
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(packageContext);
        notificationManager.notify(21, builder.build());
    }

    public static void getDriverlessRoad(final Context context){
        final SharedPreferences settings = context.getSharedPreferences("user",Context.MODE_PRIVATE);
        String url ="https://takngo.fr:8080/api/road/freeRoad.php";
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        SharedPreferences.Editor editor = settings.edit();
                        try {
                            JSONArray array = new JSONArray(response);
                            editor.putBoolean("hasFreeRoad",array.length() > 0);
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("hasFreeRoad",false);
                editor.apply();
            }
        });

        MyRequest.getInstance(context).addToRequestQueue(request);
    }
}
