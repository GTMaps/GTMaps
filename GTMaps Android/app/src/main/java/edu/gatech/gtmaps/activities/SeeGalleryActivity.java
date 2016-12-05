package edu.gatech.gtmaps.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;
import edu.gatech.gtmaps.models.Building;

public class SeeGalleryActivity extends AppCompatActivity {
    public String message;
    public DBHelper db = new DBHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //not fully implemented. Shows pictures available in database at the moment.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.see_gallery);
        Intent intent = getIntent();
        GridView gridView = (GridView) findViewById(R.id.gallerygrid);
        gridView.setAdapter(new GridAdapter(this));

    }


    public void home(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void showPopup(View v) {
        //functionality for "choose another room" is taken out because it doesn't make sense for this activity.
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
                } /*else if (item.getItemId() == R.id.choose_room) {
                    intent1 = new Intent(getApplicationContext(), RoomChooseActivity.class);
                    String message = getIntent().getStringExtra("building");
                    intent1.putExtra("building", message);
                    startActivity(intent1);
                    return true;
                }*/
                return false;
            }
        });
        popup.show();
    }





    private final class GridAdapter extends BaseAdapter { //class to try and make pictures in galelry show up in a grid with text. Does not fully work.
        private final ArrayList<GridItem> items = new ArrayList<>();
        private final LayoutInflater inflater;


        public GridAdapter(Context context) {
            inflater = LayoutInflater.from(context);
            List<Building> buildings = db.getAllBuildings();
            for (int i = 0; i < 3; i++) {
                String url = db.getBuildingUrl(buildings.get(i).getId());
                items.add(new GridItem(buildings.get(i).getName(), getResources().getIdentifier(url, "drawable", getPackageName())));
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public GridItem getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return items.get(i).drawableId;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            ImageView picture;
            TextView name;

            if (v == null) {
                v = inflater.inflate(R.layout.grid_layoutitem, viewGroup, false);
                v.setTag(R.id.picture, v.findViewById(R.id.picture));
                v.setTag(R.id.text, v.findViewById(R.id.text));
            }

            picture = (ImageView) v.getTag(R.id.picture);
            name = (TextView) v.getTag(R.id.text);

            GridItem item = getItem(i);

            picture.setImageResource(item.drawableId);
            name.setText(item.name);

            return v;
        }


        private class GridItem {
            public final String name;
            public final int drawableId;

            GridItem(String name, int drawableId) {
                this.name = name;
                this.drawableId = drawableId;

            }
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}