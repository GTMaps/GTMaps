package edu.gatech.gtmaps.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.models.Room;

public class RoomListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    String building = "";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_room_list);
        Intent intent = getIntent();
        DBHelper db = new DBHelper(this);
        building = intent.getStringExtra("building");
        String building_id = "-1";
        for (int i = 0; i < db.getAllBuildings().size(); i++) {
            if (db.getAllBuildings().get(i).getName().equalsIgnoreCase(building)) {
                building_id = db.getAllBuildings().get(i).getId();
                break;
            }
        }
        List<Room> list_of_rooms = db.getRoomsPerBuilding(building_id);
        ArrayList<String> names = new ArrayList<>();

        for (int i = 0; i < list_of_rooms.size(); i++) {
            names.add(list_of_rooms.get(i).getRoomName());
        }
        ListView lv = (ListView) findViewById(R.id.room_list);
        lv.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names));
        lv.setOnItemClickListener(this);




    }

    /**
     * A listener for selecting an item from the room list.
     * Checks the position and grabs the text out of it to send to the directions activity.
     * @param l
     * @param v
     * @param position
     * @param id
     */
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        //finds the item clicked and passes its' data into directions.
        String data = (String) l.getItemAtPosition(position);
        Intent intent = new Intent(this, DirectionsActivity.class);
        intent.putExtra("room", data);
        intent.putExtra("building", building);
        startActivity(intent);

    }
}
