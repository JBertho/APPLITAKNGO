package fr.takngo.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fr.takngo.application.adapter.RoadAdapter;
import fr.takngo.application.entity.Road;

public class MyRoadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_road);


    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load(){
        MyRequest request = MyRequest.getInstance(this.getApplicationContext());
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/road/roadOfDriver.php?id=24";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Road> result= new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length() ;i++ ){
                                try {
                                    result.add(Road.RoadFromJson(array.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("road",result.size()+"");
                        setContent(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyRoadActivity.this, "Aucune course disponible",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    private void setContent(List<Road> roads){
        ListView lv_roads = (ListView)findViewById(R.id.lv_road);
        final RoadAdapter adapter = new RoadAdapter(MyRoadActivity.this,roads);
        lv_roads.setAdapter(adapter);

        lv_roads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyRoadActivity.this,RoadActivity.class);
                Road current = (Road) adapter.getItem(i);
                intent.putExtra("road",current);
                intent.putExtra("type","view");
                startActivity(intent);
            }
        });
    }

}
