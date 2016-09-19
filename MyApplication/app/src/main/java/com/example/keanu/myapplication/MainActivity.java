package com.example.keanu.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, BUILDINGS);
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.Building_Choice);
        textView.setAdapter(adapter);

        ImageView iv = (ImageView)findViewById(R.id.logoiv);
        iv.setImageResource(R.drawable.gtmaps);
    }

    private static final String[] BUILDINGS = new String[] {
            "College of Computing", "Howey","CULC", "Van Leer"
    };

    public void findBuilding(View view) {
        Intent intent = new Intent(this, RoomChooseActivity.class);
        TextView text = (TextView) findViewById(R.id.Building_Choice);
        String message = text.getText().toString();
        intent.putExtra("building", message);
        startActivity(intent);
    }
}

