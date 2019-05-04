package fr.takngo.application.Product;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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

import fr.takngo.application.MenuActivity;
import fr.takngo.application.MyRequest;
import fr.takngo.application.R;
import fr.takngo.application.adapter.ProductAdapter;
import fr.takngo.application.entity.Product;
import fr.takngo.application.entity.Service;

public class ProductsActivity extends AppCompatActivity {

    ListView lv_products;
    FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);
        add = findViewById(R.id.b_products_add);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("resume","je apss la");
        load();
    }

    private void load(){

        MyRequest request = MyRequest.getInstance(this.getApplicationContext());
        final RequestQueue queue = request.getRequestQueue();
        final Service service = getIntent().getParcelableExtra("service");
        String url ="https://takngo.fr:8080/api/product/listByService.php?id=" + service.getId() ;

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductsActivity.this,CreateProduct.class);
                intent.putExtra("service",service);
                startActivity(intent);
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Product> result= new ArrayList<>();
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length() ;i++ ){
                                try {
                                    result.add(Product.ProductFromJSON(array.getJSONObject(i)));
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
                if (lv_products!= null){
                    lv_products.setVisibility(View.GONE);
                }
                Toast.makeText(ProductsActivity.this, "Aucune Produit existant",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    private void setContent(List<Product> services){
        lv_products = (ListView)findViewById(R.id.lv_products);
        TextView tv_name = (TextView)findViewById(R.id.tv_products_service_title);
        Service service = getIntent().getParcelableExtra("service");
        tv_name.setText(service.getName());

        final ProductAdapter adapter = new ProductAdapter(ProductsActivity.this,services);
        lv_products.setAdapter(adapter);

        lv_products.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductsActivity.this, ProductActivity.class);
                Product current = (Product) adapter.getItem(i);
                intent.putExtra("product",current);
                startActivity(intent);
            }
        });
    }
}
