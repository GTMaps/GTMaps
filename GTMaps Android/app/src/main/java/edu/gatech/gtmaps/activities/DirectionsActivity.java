package edu.gatech.gtmaps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.actions, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent1;

                if (item.getItemId() == R.id.go_to_building) {
                    intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent1);
                    return true;
                } else if(item.getItemId() == R.id.choose_room) {
                    intent1 = new Intent(getApplicationContext(), RoomChooseActivity.class);
                    String message = getIntent().getStringExtra("building");
                    intent1.putExtra("building", message);
                    startActivity(intent1);
                    return true;
                }
                return false;
            }
        });
        popup.show();
    }

    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}