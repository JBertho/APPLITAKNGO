package fr.takngo.application;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.google.gson.Gson;

import java.util.Arrays;

import fr.takngo.application.entity.User;

public class ProfilActivity extends AppCompatActivity {

    TextView name,lname,email,phone,address,birthday,postalCode;
    de.hdodenhof.circleimageview.CircleImageView profile_picture;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //On récupère le user qui est stocké dans les preferences
        SharedPreferences settings = getSharedPreferences("user",MODE_PRIVATE);

        Gson gson = new Gson();
        String json = settings.getString("user","");

        User user = gson.fromJson(json,User.class);

        this.profile_picture = findViewById(R.id.profile_image);


        String[] extension = user.getProfile_pict().split("profile.");

        //Log.d("ext", Arrays.toString(extension));

        String imgUrl = "https://takngo.fr/images/user/"+user.getId()+"/profile."+extension[extension.length-1];


        ImageRequest imageRequest = new ImageRequest(
                imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        profile_picture.setImageBitmap(response);
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


        this.name = (TextView) findViewById(R.id.user_name);
        this.name.setText(user.getName());

        this.lname = (TextView) findViewById(R.id.user_lname);
        this.lname.setText(user.getLastname());

        this.email = (TextView) findViewById(R.id.user_email);
        this.email.setText(user.getEmail());

        this.birthday = (TextView) findViewById(R.id.user_birthday);
        this.birthday.setText(user.getBirthday());

        this.address = (TextView) findViewById(R.id.user_address);
        this.address.setText(user.getAddress());

        this.phone = (TextView) findViewById(R.id.user_phone);
        this.phone.setText(user.getPhone());

        this.postalCode = (TextView) findViewById(R.id.user_postalCode);
        this.postalCode.setText(user.getPostal_code());

    }
}
