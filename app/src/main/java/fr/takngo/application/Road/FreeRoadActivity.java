package fr.takngo.application.Road;

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

import fr.takngo.application.MyRequest;
import fr.takngo.application.R;
import fr.takngo.application.adapter.RoadAdapter;
import fr.takngo.application.entity.Road;

public class FreeRoadActivity extends AppCompatActivity {


    private ListView lv_roads;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_road);

        lv_roads = (ListView)findViewById(R.id.lv_road);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load(){
         List<Road> result= new ArrayList<>();
        
        MyRequest request = MyRequest.getInstance(this.getApplicationContext());
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/road/freeRoad.php";
        
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
                            Log.d("cnul",result.size()+"");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        setContent(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(lv_roads != null){
                    lv_roads.setVisibility(View.GONE);
                }
                Toast.makeText(FreeRoadActivity.this, getResources().getString(R.string.no_course),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    private void setContent(List<Road> roads){

        final RoadAdapter adapter = new RoadAdapter(FreeRoadActivity.this,roads);
        lv_roads.setAdapter(adapter);

        lv_roads.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(FreeRoadActivity.this,RoadActivity.class);
                Road current = (Road) adapter.getItem(i);
                intent.putExtra("road",current);
                intent.putExtra("type","free");
                startActivity(intent);
            }
        });
    }

}
