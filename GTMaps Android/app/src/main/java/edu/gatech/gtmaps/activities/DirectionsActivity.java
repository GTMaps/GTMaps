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

        Drawable d = getResources().getDrawable(R.drawable.ccbfloor, getTheme());
        drawDirection(iv, 0, 0, d.getIntrinsicWidth() - 1, d.getIntrinsicHeight() - 1);
    }

    private void drawDirection(ImageView iv, int startx, int starty, int stopx, int stopy) {
        Bitmap floorplan = BitmapFactory.decodeResource(getResources(), R.drawable.ccbfloor);
        Bitmap direction = Bitmap.createBitmap(floorplan.getWidth(), floorplan.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(direction);
        canvas.drawBitmap(floorplan, 0, 0, null);

        Paint paint = new Paint();
        paint.setColor(Color.RED);
        paint.setStrokeWidth(5);
        canvas.drawLine(startx, starty, stopx, stopy, paint);
        iv.setImageDrawable(new BitmapDrawable(getResources(), direction));
    }

    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}