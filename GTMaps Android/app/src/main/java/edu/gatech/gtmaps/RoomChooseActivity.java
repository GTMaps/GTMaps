package edu.gatech.gtmaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView iv = (ImageView)findViewById(R.id.ivbuilding);
        iv.setImageResource(R.drawable.coc);
    }

    private static final String[] ROOMS = new String[]{
            "301", "302", "303", "017"
    };

    public void roomSearch(View view) {
        Intent intent = new Intent(this, DirectionsActivity.class);
        TextView text = (TextView) findViewById(R.id.room_choice);
        String new_message = message + " Room " + text.getText().toString();
        intent.putExtra("room", new_message);
        startActivity(intent);
    }
}