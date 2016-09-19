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

public class DirectionsActivity extends AppCompatActivity {
    public String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.directions);
        Intent intent = getIntent();

        message = intent.getStringExtra("room");
        TextView text = (TextView) findViewById(R.id.room_text);
        text.setText(message);


        ImageView iv = (ImageView)findViewById(R.id.ivfloorplan);
        iv.setImageResource(R.drawable.ccbfloor);
    }
}