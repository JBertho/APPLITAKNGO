package fr.takngo.application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.takngo.application.Notification.Notification;
import fr.takngo.application.entity.User;


public class MainActivity extends AppCompatActivity {

    private EditText pseudo;
    private EditText password;
    private Button login;
    private Button register;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Handler timerHandler = new Handler();
        final Intent intentActivity = new Intent(this, MainActivity.class);
        final PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intentActivity, 0);
        final Context packageContext = this;
        Runnable timerRunnable = new Runnable(){

            @Override
            public void run() {
                Notification.getDriverlessRoad(MainActivity.this);
                SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (settings.getBoolean("hasFreeRoad",false)){

                    Notification.createDriverNotification(intentActivity,pendingIntent,MainActivity.this,packageContext);
                    timerHandler.postDelayed(this,600000);
                } else {
                    Log.d("notif","pas bon du tout");
                }
            }
        };

        timerHandler.postDelayed(timerRunnable, 600000);

        this.textView = (TextView)findViewById(R.id.response);

        //Creation du channel de notification
        Notification.createNotificationChannel(getString(R.string.channel_name),getString(R.string.channel_description),getSystemService(NotificationManager.class));

        /*
        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "group_request")
                .setSmallIcon(R.drawable.second)
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(21, builder.build()); */

        MyRequest request = MyRequest.getInstance(this.getApplicationContext());

        //get SharedPreferences
        final SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);

        // Instantiate the RequestQueue.
        final RequestQueue queue = request.getRequestQueue();




        this.pseudo =(EditText) findViewById(R.id.pseudo);
        this.password = (EditText) findViewById(R.id.password);
        this.login =(Button) findViewById(R.id.login);
        this.register = (Button) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pseudo.getText().toString().length() < 1 || password.getText().toString().length() < 1){
                    if (pseudo.getText().toString().length() < 1) {
                        pseudo.setError(getResources().getString(R.string.fields));
                    } else {
                        password.setError(getResources().getString(R.string.fields));
                    }
                } else {

                    String url ="https://takngo.fr:8080/api/user/CheckConnexion.php?email="+pseudo.getText().toString()+"&password="+HashMethods.encryptThisString(password.getText().toString());
                    // Add the request to the RequestQueue.
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    SharedPreferences.Editor editor = settings.edit();
                                    User user = null;
                                    try {
                                        JSONObject data = new JSONObject(response);
                                        user = User.UserFromJson(data);
                                        Gson gson = new Gson();
                                        String json = gson.toJson(user);
                                        editor.putString("user",json);
                                        //Log.d("user", user.getString("email"));
                                        editor.apply();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    checkRoles(user.getId());
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.wrong_ident),Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void checkRoles(int id){
        MyRequest request = MyRequest.getInstance(this.getApplicationContext());
        // Instantiate the RequestQueue.
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/user/userRole.php?id="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int isOk = 0;
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                try {
                    JSONArray array = new JSONArray(response);
                    Log.d("coucou",array.toString());
                    for (int i = 0; i < array.length() ;i++ ){
                        Log.d("coucou",array.get(i).toString());
                        if (array.get(i).toString().equals("ROLE_SERVICE") || array.get(i).toString().equals("ROLE_DRIVER")){
                            Log.d("coucou","suis la");
                            if (array.get(i).toString().equals("ROLE_SERVICE")){
                                isOk = isOk + 1;
                            }else if (array.get(i).toString().equals("ROLE_DRIVER")){
                                isOk = isOk + 2;
                            }
                        }
                    }
                    Log.d("coucou",isOk+"");
                    if (isOk >= 1 ){
                        intent.putExtra("role",isOk);
                        startActivity(intent);
                    }else{
                        Toast.makeText(MainActivity.this, getResources().getString(R.string.wrong_account),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.wrong_account),Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

}
