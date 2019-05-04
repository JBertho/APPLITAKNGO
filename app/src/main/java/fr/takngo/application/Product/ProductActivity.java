package fr.takngo.application.Product;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.takngo.application.MainActivity;
import fr.takngo.application.MenuActivity;
import fr.takngo.application.MyRequest;
import fr.takngo.application.R;
import fr.takngo.application.entity.Product;

public class ProductActivity extends AppCompatActivity {

    TextView name,description,price;
    Button modif,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    protected void onResume() {
        super.onResume();

        final Product product = (Product) getIntent().getParcelableExtra("product");
        name = findViewById(R.id.tv_viewProduct_name);
        description = findViewById(R.id.tv_viewProduct_description);
        price = findViewById(R.id.tv_viewProduct_price);
        modif = findViewById(R.id.b_viewProduct_modif);
        delete = findViewById(R.id.b_viewProduct_delete);

        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice()+ " €");

        modif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProductActivity.this,EditProductActivity.class);
                i.putExtra("product",product);
                startActivity(i);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(product.getId());

            }
        });
    }
    private void delete(int id){
        MyRequest request = MyRequest.getInstance(ProductActivity.this);
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/product/dropProduct.php?id="+id;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ProductActivity.this,"Produit supprimé",Toast.LENGTH_SHORT).show();
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProductActivity.this,"Produit inexistant",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

    }

}
