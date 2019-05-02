package fr.takngo.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button service,course,profil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        service = findViewById(R.id.b_menu_service);
        course = findViewById(R.id.b_menu_road);
        profil = findViewById(R.id.b_menu_profil);

        int role = getIntent().getIntExtra("role",3);
        setButton(role,course,2,RoadMenuActivity.class);
        setButton(role,profil,0,ProfilActivity.class);
        setButton(role,service,1,RoadMenuActivity.class);

    }

    private void setButton(int role, Button button, int id, final Class activity){
        if (role == 3 || role == id || id == 0){
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MenuActivity.this,activity);
                    startActivity(i);
                }
            });
        }else if (role == (3-id)){
            button.setVisibility(View.GONE);
        }

    }


}
