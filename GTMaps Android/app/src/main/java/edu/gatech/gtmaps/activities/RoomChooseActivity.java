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

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.models.Building;

public class RoomChooseActivity extends AppCompatActivity {
public String message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_choose);
        Intent intent = getIntent();

        message = intent.getStringExtra("building");
        TextView text = (TextView) findViewById(R.id.building_text);
        text.setText(message);

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

    private static final String[] ROOMS = new String[]{
            "301", "302", "303", "017"
    };

    private Building find_building(String name) {
      //  ArrayList<Building> listOfBuildings = DBHelper.getAllBuildings();
        return null;
    }

    public void roomSearch(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        TextView text = (TextView) findViewById(R.id.room_choice);
        if (Arrays.asList(ROOMS).contains(text.getText().toString())) {
            String new_message = " Room " + text.getText().toString();
            intent.putExtra("room", new_message);
            intent.putExtra("building", message);
        }
        startActivity(intent);
    }
    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}