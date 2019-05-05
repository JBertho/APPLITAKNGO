package fr.takngo.application.Road;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import fr.takngo.application.MyRequest;
import fr.takngo.application.R;
import fr.takngo.application.entity.Road;
import fr.takngo.application.entity.User;

public class RoadActivity extends AppCompatActivity {

    TextView start,end,appointment,distance,user,phone ;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);

        SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = settings.getString("user","");
        final User myUser = gson.fromJson(json,User.class);

        final Road road =(Road)getIntent().getParcelableExtra("road");

        start = (TextView)findViewById(R.id.tv_viewRoad_start);
        end = (TextView)findViewById(R.id.tv_viewRoad_end);
        appointment = (TextView)findViewById(R.id.tv_viewRoad_appointment);
        distance = (TextView)findViewById(R.id.tv_viewRoad_distance);
        user = (TextView)findViewById(R.id.tv_viewRoad_userName);
        submit = (Button)findViewById(R.id.b_viewRoad_submit);

        start.setText(road.getStart_street());
        end.setText(road.getEnd_start());
        appointment.setText(road.getAppointment());
        distance.setText(road.getDistance()+"");
        setClientInfo(road);

        if (getIntent().getStringExtra("type").equals("free")) {
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MyRequest request = MyRequest.getInstance(RoadActivity.this);
                    final RequestQueue queue = request.getRequestQueue();
                    String url = "https://takngo.fr:8080/api/road/setDriver.php?rId=" + road.getId() + "&uId="+myUser.getId();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Intent intent = new Intent(RoadActivity.this, FreeRoadActivity.class);
                                    Toast.makeText(RoadActivity.this, getResources().getString(R.string.reservation_msg), Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RoadActivity.this, getResources().getString(R.string.wrong_ident), Toast.LENGTH_SHORT).show();
                        }
                    });
                    queue.add(stringRequest);
                }
            });
        }else if (getIntent().getStringExtra("type").equals("view")){
            ViewGroup layout = (ViewGroup) submit.getParent();
            layout.removeView(submit);
            submit.setVisibility(View.GONE);

        }
    }

    public void setClientInfo(Road road){
        user = (TextView)findViewById(R.id.tv_viewRoad_userName);
        phone = (TextView)findViewById(R.id.tv_viewRoad_phone);

        MyRequest request = MyRequest.getInstance(RoadActivity.this);
        final RequestQueue queue = request.getRequestQueue();
        String url = "https://takngo.fr:8080/api/road/getClientName.php?id=" + road.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject array = new JSONObject(response);
                            user.setText(array.getString("lastname")+" "+array.getString("name"));
                            phone.setText(array.getString("phone"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RoadActivity.this, getResources().getString(R.string.wrong_ident), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);


    }

}
