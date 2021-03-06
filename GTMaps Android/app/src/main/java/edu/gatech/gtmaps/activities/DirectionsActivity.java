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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.SearchObject;
import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Room;

import static edu.gatech.gtmaps.SearchObject.find;

public class DirectionsActivity extends AppCompatActivity {
    public String room_message;
    public String building_message;
    public String[] directions;
    public TextView instruction;
    public int ins_num = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        Intent intent = getIntent();
        DBHelper db = new DBHelper(this);


        room_message = intent.getStringExtra("room");
        TextView text = (TextView) findViewById(R.id.room_text);
        text.setText(room_message);

        building_message = intent.getStringExtra("building");
        TextView text2 = (TextView) findViewById(R.id.building_text);
        text2.setText(building_message);
        String building_id = "-1";

        //Looks for the bulding id in the database by comparing the name to the entries
        for (int i = 0; i < db.getAllBuildings().size(); i++) {
            if (db.getAllBuildings().get(i).getName().equalsIgnoreCase(building_message)) {
                building_id = db.getAllBuildings().get(i).getId();
                break;
            }
        }

//        Hallway entrance = db.getBuildingEntrances(building_id).get(0);
        Room destination = null;

        //looks for the room object by comparing the names in the database
        for (int i = 0; i < db.getRoomsPerBuilding(building_id).size(); i++) {
            if (db.getRoomsPerBuilding(building_id).get(i).getRoomName().equalsIgnoreCase(room_message)) {
                destination = db.getRoomsPerBuilding(building_id).get(i);
                break; //legacy code from when DB would keep populating and we'd need the first entry for a given building.
            }
        }
        Hallway entrance = null;
        List<Junction> junctions = db.getJunctionsPerBuilding(building_id);
        for (Junction j : junctions) {
            for (Hallway h : j.getHallways()) {
                if (h.getName().contains("entrance"));
                entrance = h;
                break;
            }
            break;
        }



        ImageView iv = (ImageView)findViewById(R.id.ivfloorplan);
        iv.setImageDrawable( getResources().getDrawable(R.drawable.ccbfloor));
//        Drawable d = getResources().getDrawable(R.drawable.ccbfloor, getTheme());

//        drawDirections(iv, d.getIntrinsicWidth() - 1, d.getIntrinsicHeight() - 1);
//        drawDirections(iv, someRoom);

        //Sets the text if room is found.(Room should always be found since
        //room search activity makes sure it's there first.
        if (building_id != "-1" && destination != null) {
            directions = get_directions(destination, entrance);
            instruction = (TextView) findViewById(R.id.instructions);
            instruction.setText(directions[ins_num - 1]);
        }
    }

    /**
     * Button for moving forward in the directions
     * @param view
     */
    public void next_ins(View view) {
        if (ins_num < directions.length) {
            ins_num++;
            instruction.setText(directions[ins_num - 1]);
        }
    }

    /**
     * Button for moving backward in the directions
     * @param view
     */
    public void prev_ins(View view) {
        if (ins_num > 1) {
            ins_num--;
            instruction.setText(directions[ins_num - 1]);
        }
    }

    /**
     * (Not fuly implemented) Draws on provided floorplan
     * @param iv
     * @param target
     * @param entrance
     */
    private void drawDirections(ImageView iv, Room target, BuildingSpace entrance) {
        //"Entrance" parameter should be passed in from the user entrance selection in the gallery
        LinkedList<BuildingSpace> route = SearchObject.find(target, entrance);

        for (BuildingSpace hallway : route) {
            drawDirection(iv, (Hallway) hallway);
        }
    }

    /**
     * Helper function for calling the searchObject class.
     * Also parses the returned string and places it into an array for easy viewing
     * @param dest
     * @param start
     * @return
     */
    public String[] get_directions(Room dest, BuildingSpace start) {
        LinkedList<BuildingSpace> rooms = SearchObject.find(dest,start);
        String directions = SearchObject.translate(rooms, dest);
        String[] directions_parsed = directions.split(".\n");
        return directions_parsed;


    }

    /**
     * (Not fully implemented) Function for drawing on the floor plan shown
     * @param iv
     * @param hallway
     */
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

    /**
     * Nav menu for traversing to the rest of the app
     * @param v
     */
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

    /**
     * legacy function for returning home when a home button was seperated from the nav menu.
     * @param view
     */
    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}