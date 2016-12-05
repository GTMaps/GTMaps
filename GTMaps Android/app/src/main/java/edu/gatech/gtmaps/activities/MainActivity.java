package edu.gatech.gtmaps.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

import edu.gatech.gtmaps.DBHelper;
import edu.gatech.gtmaps.R;

public class MainActivity extends AppCompatActivity {
    //A test array for populating the auto-fill
    private static final String[] BUILDINGS = new String[] {
            "College of Computing", "Howey","CULC", "Van Leer"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DBHelper db = new DBHelper(this);
        if (db.getAllBuildings().size() == 0) {
            db.populateData();
        }

        // TO FIX LEGACY DB ISSUES
        // comment out above if-block and uncomment out below line
        // to resolve potential db errors from legacy builds
        ///db.onUpgrade(db.getReadableDatabase(), 0, 1);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Below works with the drop down list when you start typing
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, BUILDINGS); //populates the list with the BUILDIGNS array
        AutoCompleteTextView textView = (AutoCompleteTextView)
                findViewById(R.id.Building_Choice);
        textView.setAdapter(adapter);
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
        ImageView iv = (ImageView)findViewById(R.id.logoiv);
        iv.setImageResource(R.drawable.gtmaps);

    }
    /**
     * Button that will open the gllery activty
     * @param view
     */
    public void seeGallery(View view) {
        Intent intent = new Intent(this, SeeGalleryActivity.class);
        startActivity(intent);
    }


    /**
     * Button that will pass in the building choice if found in the buildings array
     * @param view
     */
    public void findBuilding(View view) {
        Intent intent = new Intent(this, RoomChooseActivity.class);
        TextView text = (TextView) findViewById(R.id.Building_Choice);
        if (Arrays.asList(BUILDINGS).contains(text.getText().toString())) {
            String message = text.getText().toString();
            intent.putExtra("building", message);
            startActivity(intent);
        }
    }

    /**
     * Hides the keyboard if tapped outside of the keyboard or if enter is pressed
     * @param view
     */
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

