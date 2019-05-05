package fr.takngo.application.Product;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


import fr.takngo.application.MenuActivity;
import fr.takngo.application.MyRequest;
import fr.takngo.application.R;
import fr.takngo.application.Service.MyServicesActivity;
import fr.takngo.application.entity.Product;

public class EditProductActivity extends AppCompatActivity {
    Button find,validate,cancel;
    ImageView images;
    Product product;
    EditText image,name,description,price;
    String picture;
    Product savedProduct;
    int modif = 0;

    private static int GALLERY_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        ContextCompat.checkSelfPermission(EditProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);

        product = (Product)getIntent().getParcelableExtra("product");
        find = findViewById(R.id.b_modifProduct_pict);
        images = findViewById(R.id.iv_modifProduct_image);
        validate = findViewById(R.id.b_editProduct_validate);
        cancel = findViewById(R.id.b_editProduct_cancel);

        name = findViewById(R.id.et_modifProduct_name);
        description = findViewById(R.id.et_modifProduct_description);
        price = findViewById(R.id.et_modifProduct_price);

        name.setText(product.getName());
        description.setText(product.getDescription());
        price.setText(product.getPrice()+"");

        savedProduct = product;


    }

    @Override
    protected void onResume() {
        super.onResume();

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an Intent with action as ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK);
                // Sets the type as image/*. This ensures only components of type image are selected
                intent.setType("image/*");
                //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                // Launching the Intent
                startActivityForResult(intent,GALLERY_REQUEST);
            }
        });

        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setName(name.getText().toString());
                product.setDescription(description.getText().toString());
                product.setPrice((Float.parseFloat(price.getText().toString())));
                if (picture != null){
                    product.setPict(picture);
                    modif = 1;
                }else   {
                    picture = "none";
                    modif = 0;
                }
                edit();
                Intent intent = new Intent(EditProductActivity.this, MyServicesActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case 1:
                    //data.getData return the content URI for the selected Image
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Get the cursor
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();
                    //Get the column index of MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Gets the String value in the column
                    String imgDecodableString = cursor.getString(columnIndex);
                    //Log.d("image",imgDecodableString);
                    cursor.close();
                    // Set the Image in ImageView after decoding the String
                    Bitmap bm = BitmapFactory.decodeFile(imgDecodableString);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 75, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.NO_WRAP);
                    Log.d("image",encodedImage);

                    images.setImageBitmap(bm);
                    images.buildDrawingCache();
                    Bitmap bmp = images.getDrawingCache();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.JPEG, 70, stream);
                    byte[] byteFormat = stream.toByteArray();
                    // get the base 64 string
                    picture = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                    Log.d("image",picture);
                    break;
            }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void edit(){
        MyRequest request = MyRequest.getInstance(EditProductActivity.this);
        final RequestQueue queue = request.getRequestQueue();
        String url ="https://takngo.fr:8080/api/product/updateProduct.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Intent intent = new Intent(EditProductActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parameters = new HashMap<String, String>();
                parameters.put("id",savedProduct.getId()+"");
                parameters.put("pict", picture);
                parameters.put("name",savedProduct.getName());
                parameters.put("description",savedProduct.getDescription());
                parameters.put("price",savedProduct.getPrice()+"");
                parameters.put("modif",modif+"");
                parameters.put("service",savedProduct.getService()+"");
                return parameters;
            }
        };
        queue.add(stringRequest);
    }
}
