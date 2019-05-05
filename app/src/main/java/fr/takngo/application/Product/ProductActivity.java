package fr.takngo.application.Product;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

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
    ImageView pict;
    Button modif,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    protected void onResume() {
        super.onResume();

        pict = findViewById(R.id.iv_viewProduct_pict);

        final Product product = (Product) getIntent().getParcelableExtra("product");
        Gson gson = new Gson();
        String[] extension = product.getPict().split(product.getService()+"/");
        Log.d("images",extension[extension.length-1]);
        String imgUrl = "https://takngo.fr/images/product/"+product.getService()+"/"+extension[extension.length-1];

        ImageRequest imageRequest = new ImageRequest(
                imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        pict.setImageBitmap(response);
                    }
                },
                0,
                0,
                null,
                Bitmap.Config.RGB_565, //Image decode configuration
                new Response.ErrorListener() { // Error listener
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Do something with error response
                        error.printStackTrace();
                    }
                }
        );

        MyRequest.getInstance(this.getApplicationContext()).addToRequestQueue(imageRequest);


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
