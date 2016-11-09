package edu.gatech.gtmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.BuildingSpace.HallwaySide;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Room;

import static java.lang.Float.parseFloat;

public class DBHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "GTMapsDB.db";
        public static final String BUILDINGS_TABLE_NAME = "Buildings";
        public static final String BUILDINGS_ID = "id";
        public static final String BUILDINGS_NAME = "name";
        public static final String BUILDINGS_URL = "url";

        public static final String ROOMS_TABLE_NAME = "Rooms";
        public static final String ROOMS_BUILDING_ID = "b_uid";
        public static final String ROOMS_FLOOR_ID = "f_id";
        public static final String ROOMS_ROOM_ID = "r_uid";
        public static final String ROOMS_ROOM_NAME = "r_name";
        public static final String ROOMS_HALLWAY_ID = "h_uid";
        public static final String ROOMS_HALLWAY_SIDE = "h_side";
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

            String CREATE_BUILDINGS_TABLE =
                    "CREATE TABLE " + BUILDINGS_TABLE_NAME + "("
                        + BUILDINGS_ID + " INTEGER PRIMARY KEY NOT NULL,"
                        + BUILDINGS_NAME + " TEXT,"
                        + BUILDINGS_URL + " TEXT "

                        + ")";
            db.execSQL(CREATE_BUILDINGS_TABLE);

            String CREATE_HALLWAYS_TABLE = "CREATE TABLE " + HALLWAYS_TABLE_NAME + "("
                    + HALLWAYS_BUILDING_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_FLOOR_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_HALLWAY_ID + " INTEGER NOT NULL,"
                    + HALLWAYS_HALLWAY_NAME + " TEXT,"

                    + HALLWAYS_CENTER_X + " REAL," // ?? same question as room table
                    + HALLWAYS_CENTER_Y + " REAL,"
                    + HALLWAYS_LENGTH+ " REAL,"
                    + HALLWAYS_WIDTH+ " REAL," +

                    //Primary Key = B_uid,F_id,H_uid
                    "PRIMARY KEY(" + HALLWAYS_BUILDING_ID + ", " + HALLWAYS_FLOOR_ID + ", " + HALLWAYS_HALLWAY_ID +")," +

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
                    + ROOMS_CENTER_X + " REAL," // why nullable???
                    + ROOMS_CENTER_Y + " REAL,"
                    + ROOMS_DOOR_X + " REAL,"
                    + ROOMS_DOOR_Y + " REAL," +

                    //Primary Key = B_uid,F_id,door_x,door_y
                    "PRIMARY KEY(" + ROOMS_BUILDING_ID + ", " + ROOMS_FLOOR_ID + "," + ROOMS_DOOR_X + "," + ROOMS_DOOR_Y + ")," +

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
            db.execSQL("DROP TABLE IF EXISTS " + BUILDINGS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + ROOMS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HALLWAYS_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + JUNCTIONS_TABLE_NAME);

            onCreate(db);
        }


        /***************************  SQL Insert Functions ****************************************/
        public boolean insertBuilding(String building_id, String building_name, String building_url)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(BUILDINGS_ID, building_id);
            contentValues.put(BUILDINGS_NAME, building_name);
            contentValues.put(BUILDINGS_URL, building_url);

            return db.insert(BUILDINGS_TABLE_NAME, null, contentValues) != -1;
        }

        public boolean insertHallway(String hallway_id, String hallway_name, String building_id, String floor_id)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(HALLWAYS_HALLWAY_ID, hallway_id);
            contentValues.put(HALLWAYS_HALLWAY_NAME, hallway_name);

            contentValues.put(HALLWAYS_BUILDING_ID, building_id);
            contentValues.put(HALLWAYS_FLOOR_ID, floor_id);
            contentValues.put(HALLWAYS_HALLWAY_ID, hallway_id);

            return db.insert(HALLWAYS_TABLE_NAME, null, contentValues) != -1;
        }

        public boolean insertRoom(String room_id, String room_name, String building_id, String floor_id, String hallway_id, String hallway_side, float center_x, float center_y, double door_x, double door_y)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ROOMS_ROOM_ID, room_id);
            contentValues.put(ROOMS_ROOM_NAME, room_name);

            contentValues.put(ROOMS_BUILDING_ID, building_id);
            contentValues.put(ROOMS_FLOOR_ID, floor_id);
            contentValues.put(ROOMS_HALLWAY_ID, hallway_id);
            contentValues.put(ROOMS_HALLWAY_SIDE, hallway_side);

            contentValues.put(ROOMS_CENTER_X, center_x);
            contentValues.put(ROOMS_CENTER_Y, center_y);
            contentValues.put(ROOMS_DOOR_X, door_x);
            contentValues.put(ROOMS_DOOR_Y, door_y);

            return db.insert(ROOMS_TABLE_NAME, null, contentValues) != -1;
        }

        public boolean insertRoom(Room room){
            //TODO: incomplete
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(ROOMS_ROOM_ID, room.getRoomId());
            values.put(ROOMS_BUILDING_ID, room.getBuildingId());
            values.put(ROOMS_FLOOR_ID, room.getFloorId());
            values.put(ROOMS_HALLWAY_ID, room.getHallwayId());
            //values.put(ROOMS_HALLWAY_SIDE);
            values.put(ROOMS_ROOM_NAME, room.getRoomName());

            return db.insert(ROOMS_TABLE_NAME, null, values) != -1;
        }

        public boolean insertJunction(String hallway_id1, String hallway_id2)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(JUNCTIONS_HALLWAY_1, hallway_id1);
            contentValues.put(JUNCTIONS_HALLWAY_2, hallway_id2);

            return db.insert(JUNCTIONS_TABLE_NAME, null, contentValues) != -1;
        }

    /***************************  SQL Select Queries (DB "Getters") *******************************/

        /* Returns all the rooms on a particular building's floor */
        public ArrayList<Room> getRoomsPerFloor(String building_id, String floor_id)
        {
            ArrayList<Room> room_list = new ArrayList<>();
            String room_query = "";

            SQLiteDatabase db = this.getReadableDatabase();

            /* Select rooms query */
            room_query = "SELECT *" +
                        " FROM " + ROOMS_TABLE_NAME +
                        " WHERE " + ROOMS_BUILDING_ID + "='" + building_id + "'";

            /* Add floor parameter to query if specified */
            if (floor_id != null) {
                room_query += " AND " + ROOMS_FLOOR_ID + "='" + floor_id + "'";
            }

            /* Create DB cursor to iterate over query results */
            Cursor cursor =  db.rawQuery(room_query, null);
            cursor.moveToFirst();

            Cursor c = db.rawQuery("Select count(*) from " + ROOMS_TABLE_NAME, null);
            c.moveToFirst();

            /* Map columns to POJO object */
            while (!cursor.isAfterLast())
            {
                Room room = new Room();

                /* TODO: consider making a column enum to decrease chance of human error*/
                room.setBuildingId(cursor.getString(0));
                room.setFloorId(cursor.getString(1));
                room.setRoomId(cursor.getString(2));
                room.setRoomName(cursor.getString(3));
                room.setHallwayId(cursor.getString(4));
                room.setHallwaySide((HallwaySide.valueOf(cursor.getString(5))));

                Float center_x = parseFloat(cursor.getString(6));
                Float center_y = parseFloat(cursor.getString(7));
                Point center = new Point(center_x,center_y);
                room.setCenter(center);

                Float door_x = parseFloat(cursor.getString(8));
                Float door_y = parseFloat(cursor.getString(9));
                Point door = new Point(door_x,door_y);
                room.setDoor(door);

                room_list.add(room);
                cursor.moveToNext();
            }
            return room_list;
        }

    /* Returns all the rooms in a particular building */
    public ArrayList<Room> getRoomsPerBuilding(String building_id) {
        ArrayList<Room> room_list = new ArrayList<>();
        room_list = getRoomsPerFloor(building_id, null);
        return room_list;
    }
}

