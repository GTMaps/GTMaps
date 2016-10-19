package edu.gatech.gtmaps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.gatech.gtmaps.R;

public class DirectionsActivity extends AppCompatActivity {
    public String room_message;
    public String building_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        Intent intent = getIntent();

        room_message = intent.getStringExtra("room");
        TextView text = (TextView) findViewById(R.id.room_text);
        text.setText(room_message);

        building_message = intent.getStringExtra("building");
        TextView text2 = (TextView) findViewById(R.id.building_text);
        text2.setText(building_message);



        ImageView iv = (ImageView)findViewById(R.id.ivfloorplan);
        iv.setImageResource(R.drawable.ccbfloor);
    }

    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}