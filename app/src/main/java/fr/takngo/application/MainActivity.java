package fr.takngo.application;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {

    private EditText pseudo;
    private EditText password;
    private Button login;
    private Button register;
    private TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.textView = (TextView)findViewById(R.id.response);

        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        String url ="https://takngo.fr:8080";

        // Request a string response from the provided URL.
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        String res = "Response is: "+ response.substring(0,15);
                        textView.setText(res);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });



        this.pseudo =(EditText) findViewById(R.id.pseudo);
        this.password = (EditText) findViewById(R.id.password);
        this.login =(Button) findViewById(R.id.login);
        this.register = (Button) findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the request to the RequestQueue.
                queue.add(stringRequest);
                if (pseudo.getText().toString().length() < 1 || password.getText().toString().length() < 1){
                    if (pseudo.getText().toString().length() < 1) {
                        pseudo.setError(getResources().getString(R.string.fields));
                    } else {
                        password.setError(getResources().getString(R.string.fields));
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
