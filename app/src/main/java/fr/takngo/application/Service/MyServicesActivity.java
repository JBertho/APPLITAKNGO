package fr.takngo.application.Service;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fr.takngo.application.MyRequest;
import fr.takngo.application.Product.ProductsActivity;
import fr.takngo.application.R;
import fr.takngo.application.adapter.ServiceAdapter;
import fr.takngo.application.entity.Service;
import fr.takngo.application.entity.User;

public class MyServicesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_services);
    }

    @Override
    protected void onResume() {
        super.onResume();
        load();
    }

    private void load(){
        SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);
        Gson gson = new Gson();
        String json = settings.getString("user","");
        User user = gson.fromJson(json,User.class);
        MyRequest request = MyRequest.getInstance(this.getApplicationContext());
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/Service/listByChief.php?id="+user.getId();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Service> result= new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length() ;i++ ){
                                try {
                                    result.add(Service.ServiceFromArray(array.getJSONObject(i)));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("coucou",result.size()+"");
                        setContent(result);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MyServicesActivity.this, getResources().getString(R.string.no_service),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void setContent(List<Service> services){
        ListView lv_services = (ListView)findViewById(R.id.lv_services);
        final ServiceAdapter adapter = new ServiceAdapter(MyServicesActivity.this,services);
        lv_services.setAdapter(adapter);

        lv_services.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MyServicesActivity.this, ProductsActivity.class);
                Service current = (Service) adapter.getItem(i);
                intent.putExtra("service",current);
                startActivity(intent);
            }
        });
    }




}
