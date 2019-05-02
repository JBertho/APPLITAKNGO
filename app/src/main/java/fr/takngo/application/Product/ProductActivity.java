package fr.takngo.application.Product;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import fr.takngo.application.R;
import fr.takngo.application.entity.Product;

public class ProductActivity extends AppCompatActivity {

    TextView name,description,price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Product product = (Product) getIntent().getParcelableExtra("product");
        name = findViewById(R.id.tv_viewProduct_name);
        description = findViewById(R.id.tv_viewProduct_description);
        price = findViewById(R.id.tv_viewProduct_price);

        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice()+ " €");

    }
}
