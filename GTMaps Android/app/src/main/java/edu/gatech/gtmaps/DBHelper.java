package edu.gatech.gtmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

    public class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "GTMapsDB.db";
        public static final String BUILDINGS_TABLE_NAME = "Buildings";
        public static final String BUILDINGS_ID = "id";
        public static final String BUILDINGS_NAME = "name";

        public static final String ROOMS_TABLE_NAME = "Rooms";
        public static final String ROOMS_BUILDING_ID = "b_uid";
        public static final String ROOMS_FLOOR_ID = "f_id";
        public static final String ROOMS_HALLWAY_ID = "h_uid";
        public static final String ROOMS_HALLWAY_SIDE = "h_side";
        public static final String ROOMS_ROOM_ID = "r_uid";
        public static final String ROOMS_ROOM_NAME = "r_name";
        public static final String ROOMS_CENTER_X = "center_x";
        public static final String ROOMS_CENTER_Y = "center_y";
        public static final String ROOMS_DOOR_X = "door_x"; // How to deal with multiple doors???
        public static final String ROOMS_DOOR_Y = "door_y";

        public static final String HALLWAYS_TABLE_NAME = "Hallways";
        public static final String HALLWAYS_BUILDING_ID = "b_uid";
        public static final String HALLWAYS_FLOOR_ID = "f_id";
        public static final String HALLWAYS_HALLWAY_ID = "h_uid";
        public static final String HALLWAYS_HALLWAY_NAME = "h_name";
        public static final String HALLWAYS_CENTER_X = "center_x";
        public static final String HALLWAYS_CENTER_Y = "center_y";
        public static final String HALLWAYS_LENGTH = "length";
        public static final String HALLWAYS_WIDTH = "width";

        public static final String JUNCTIONS_TABLE_NAME = "Junctions";
        public static final String JUNCTIONS_HALLWAY_1 = "h_uid1";
        public static final String JUNCTIONS_HALLWAY_2 = "h_uid2";
        public static final String JUNCTIONS_COORDINATE_X = "h_coordinate_x";
        public static final String JUNCTIONS_COORDINATE_Y = "h_coordinate_y";
        private HashMap hp;

        public DBHelper(Context context)
        {
            super(context, DATABASE_NAME , null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            //TODO CONSIDER USING STRING FORMATTING INSTEAD OF A BUNCH OF PLUS SIGNS

            // TODO Auto-generated method stub
            String CREATE_BUILDINGS_TABLE =
                    "CREATE TABLE " + BUILDINGS_TABLE_NAME + "("
                        + BUILDINGS_ID + " INTEGER PRIMARY KEY NOT NULL,"
                        + BUILDINGS_NAME + " TEXT"
                        + ")";
            db.execSQL(CREATE_BUILDINGS_TABLE);

            String CREATE_HALLWAYS_TABLE = "CREATE TABLE " + HALLWAYS_TABLE_NAME + "("
                    + HALLWAYS_BUILDING_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_FLOOR_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_HALLWAY_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_HALLWAY_NAME + " TEXT,"

                    + HALLWAYS_CENTER_X + " INTEGER," // ?? same question as room table
                    + HALLWAYS_CENTER_Y + " INTEGER,"
                    + HALLWAYS_LENGTH+ " INTEGER,"
                    + HALLWAYS_WIDTH+ " INTEGER," +

                    //Primary Key = B_uid,F_id,H_uid
                    "PRIMARY KEY(" + HALLWAYS_BUILDING_ID + " " + HALLWAYS_FLOOR_ID + " " + HALLWAYS_HALLWAY_ID +")," +

                    // Foreign Keys = B_uid
                    "FOREIGN KEY(" + HALLWAYS_BUILDING_ID + ")"
                    + "REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")"

                    +")";
            db.execSQL(CREATE_HALLWAYS_TABLE);

            String CREATE_ROOMS_TABLE = "CREATE TABLE " + ROOMS_TABLE_NAME + "("
                    + ROOMS_BUILDING_ID + " INTEGER NOT NULL,"
                    + ROOMS_FLOOR_ID + " INTEGER NOT NULL,"
                    + ROOMS_ROOM_ID + " INTEGER NOT NULL,"
                    + ROOMS_ROOM_NAME + " TEXT,"

                    + ROOMS_HALLWAY_ID + " INTEGER NOT NULL,"
                    + ROOMS_HALLWAY_SIDE + " TEXT NOT NULL," // boolean(small int)???
                    + ROOMS_CENTER_X + " INTEGER," // why nullable???
                    + ROOMS_CENTER_Y + " INTEGER,"
                    + ROOMS_DOOR_X + " INTEGER,"
                    + ROOMS_DOOR_Y + " INTEGER," +

                    //Primary Key = B_uid,F_id,door
                    "PRIMARY KEY(" + ROOMS_BUILDING_ID + ROOMS_FLOOR_ID + ROOMS_DOOR_X + ROOMS_DOOR_Y + ")," +

                    // Foreign Keys = B_uid, H_uid
                    "FOREIGN KEY(" + ROOMS_BUILDING_ID + ")"
                            + "REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")," +
                    "FOREIGN KEY(" + ROOMS_HALLWAY_ID + ")"
                            + "REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")"

                    +")";
            db.execSQL(CREATE_ROOMS_TABLE);

            String CREATE_JUNCTIONS_TABLE = "CREATE TABLE " + JUNCTIONS_TABLE_NAME + "("
                    + JUNCTIONS_HALLWAY_1 + " INTEGER,"
                    + JUNCTIONS_HALLWAY_2 + " INTEGER,"
                    + JUNCTIONS_COORDINATE_X + " INTEGER,"
                    + JUNCTIONS_COORDINATE_Y + " INTEGER," +

                    //Primary Key = H_uid1, H_uid2
                    "PRIMARY KEY(" + JUNCTIONS_HALLWAY_1 + ", " + JUNCTIONS_HALLWAY_2 + ")," +

                    // Foreign Keys = H_uid1, H_uid2
                    "FOREIGN KEY(" + JUNCTIONS_HALLWAY_1 + ")"
                    + "REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")," +

                    "FOREIGN KEY(" + JUNCTIONS_HALLWAY_2 + ")"
                    + "REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")"

                    + ")";

            db.execSQL(CREATE_JUNCTIONS_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // TODO Auto-generated method stub
            db.execSQL("DROP TABLE IF EXISTS " + BUILDINGS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ROOMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HALLWAYS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + JUNCTIONS_TABLE_NAME);

            onCreate(db);
        }

        // #################### EVERYTHING BELOW THIS IS EXAMPLE CODE #######################################

        public boolean insertContact  (String name, String phone, String email, String street,String place)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("street", street);
            contentValues.put("place", place);
            db.insert("contacts", null, contentValues);
            return true;
        }

        public Cursor getData(int id){
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
            return res;
        }

        public int numberOfRows(){
            SQLiteDatabase db = this.getReadableDatabase();
            int numRows = (int) DatabaseUtils.queryNumEntries(db, ROOMS_TABLE_NAME);
            return numRows;
        }

        public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("phone", phone);
            contentValues.put("email", email);
            contentValues.put("street", street);
            contentValues.put("place", place);
            db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
            return true;
        }

        public Integer deleteContact (Integer id)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete("contacts",
                    "id = ? ",
                    new String[] { Integer.toString(id) });
        }

        public ArrayList<String> getAllContacts()
        {
            ArrayList<String> array_list = new ArrayList<String>();

            //hp = new HashMap();
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from contacts", null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(ROOMS_ROOM_ID)));
                res.moveToNext();
            }
            return array_list;
        }
    }

