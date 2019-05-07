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
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (settings.getBoolean("hasFreeRoad",false)){

                    Notification.createDriverNotification(intentActivity,pendingIntent,MainActivity.this,packageContext);
                } else {
                    Log.d("notif2","pas de route");
                }
                timerHandler.postDelayed(this,60000);
            }
        };

        timerHandler.postDelayed(timerRunnable, 0);

        this.textView = (TextView)findViewById(R.id.response);

        //Creation du channel de notification
        Notification.createNotificationChannel(getString(R.string.channel_name),getString(R.string.channel_description),getSystemService(NotificationManager.class));

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
                                        Log.d("user", response);
                                        Log.d("user", user+"");
                                        Gson gson = new Gson();
                                        String json = gson.toJson(user);
                                        editor.putString("user",json);

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
        String url ="https://takngo.fr:8080/api/user/userRole.php?id="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int isOk = 0;
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length() ;i++ ){
                        if (array.get(i).toString().equals("ROLE_SERVICE") || array.get(i).toString().equals("ROLE_DRIVER")){
                            if (array.get(i).toString().equals("ROLE_SERVICE")){
                                isOk = isOk + 1;
                            }else if (array.get(i).toString().equals("ROLE_DRIVER")){
                                isOk = isOk + 2;
                            }
                        }
                    }
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
        MyRequest.getInstance(this.getApplicationContext()).addToRequestQueue(stringRequest);
    }

}
