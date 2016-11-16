package edu.gatech.gtmaps.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

<<<<<<< HEAD
import java.util.Arrays;
=======
import java.util.LinkedList;
>>>>>>> origin/master

import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.SearchObject;
import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Room;

import static edu.gatech.gtmaps.SearchObject.find;

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

//        Drawable d = getResources().getDrawable(R.drawable.ccbfloor, getTheme());

//        drawDirections(iv, d.getIntrinsicWidth() - 1, d.getIntrinsicHeight() - 1);
//        drawDirections(iv, someRoom);
    }

    private void drawDirections(ImageView iv, Room target, Building building) {
        BuildingSpace entrance = building.getEntrance();
        LinkedList<BuildingSpace> route = SearchObject.find(target, entrance);

        for (BuildingSpace hallway : route) {
            drawDirection(iv, (Hallway) hallway);
        }
    }

    private void drawDirection(ImageView iv, Hallway hallway) {
        // TODO: update floorplan corresponding to the current floor
        Bitmap floorplan = BitmapFactory.decodeResource(getResources(), R.drawable.ccbfloor);
        Bitmap direction = Bitmap.createBitmap(floorplan.getWidth(), floorplan.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(direction);
        canvas.drawBitmap(floorplan, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        canvas.drawLine(
                hallway.getEnd1().getX(),
                hallway.getEnd1().getY(),
                hallway.getEnd2().getX(),
                hallway.getEnd2().getY(),
                paint);
        iv.setImageDrawable(new BitmapDrawable(getResources(), direction));
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