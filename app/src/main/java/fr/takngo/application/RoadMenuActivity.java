package fr.takngo.application;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoadMenuActivity extends AppCompatActivity {

    Button free,mine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road_menu);

        free = findViewById(R.id.b_roadMenu_free);
        mine = findViewById(R.id.b_roadMenu_mine);

        free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RoadMenuActivity.this,FreeRoadActivity.class);
                startActivity(i);
            }
        });

        mine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RoadMenuActivity.this,MyRoadActivity.class);
                startActivity(i);
            }
        });
    }
}
