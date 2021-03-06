package edu.gatech.gtmaps.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.Room;

public class RoomChooseActivity extends AppCompatActivity {
public String message;
    private static String[] ROOMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_choose);
        Intent intent = getIntent();
        DBHelper db = new DBHelper(this);

        String building_id = "-1";


        message = intent.getStringExtra("building");
        TextView text = (TextView) findViewById(R.id.building_text);
        text.setText(message);

        for (int i = 0; i < db.getAllBuildings().size(); i++) {
            if (db.getAllBuildings().get(i).getName().equalsIgnoreCase(message)) {
                building_id = db.getAllBuildings().get(i).getId();
                break;
            }
        }
        //Drop down list is populated from the database for the specified building.
        List<Room> list_of_rooms = db.getRoomsPerBuilding(building_id);
        ROOMS = new String[list_of_rooms.size()];
        for (int i = 0; i < list_of_rooms.size(); i++) {
            ROOMS[i] = list_of_rooms.get(i).getRoomName();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, ROOMS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.room_choice);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });

        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
            }
        });
        textView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        ImageView iv = (ImageView)findViewById(R.id.ivbuilding);
        iv.setImageResource(R.drawable.coc);
    }

    /**
     * Passes room and building text into the directions activity
     * @param view
     */
    public void roomSearch(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        TextView text = (TextView) findViewById(R.id.room_choice);
        if (Arrays.asList(ROOMS).contains(text.getText().toString())) {
            String new_message = text.getText().toString();
            intent.putExtra("room", new_message);
            intent.putExtra("building", message);
        }
        startActivity(intent);
    }

    /**
     * Function for navigating back to the home menu
     * @param view
     */
    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Nav menu for returning home or to coosing another room
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
     * Button for bringing user to see the entire room list.
     * @param view
     */
    public void see_list(View view) {
        Intent intent = new Intent(this, RoomListActivity.class);
        intent.putExtra("building", message);
        startActivity(intent);
    }

    /**
     * Functionality for makind the keyboard disappear when clicking outside the keyboard.
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}