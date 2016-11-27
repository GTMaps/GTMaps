package edu.gatech.gtmaps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.drawable.Drawable;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.BuildingSpace.HallwaySide;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Room;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

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
    public static final String SIDE_A = "SIDE_A";
    public static final String SIDE_B = "SIDE_B";

    public static final String HALLWAYS_TABLE_NAME = "Hallways";
    public static final String HALLWAYS_BUILDING_ID = "b_uid";
    public static final String HALLWAYS_FLOOR_ID = "f_id";
    public static final String HALLWAYS_HALLWAY_ID = "h_uid";
    public static final String HALLWAYS_HALLWAY_NAME = "h_name";
    public static final String HALLWAYS_END1_X = "end1_x";
    public static final String HALLWAYS_END1_Y = "end1_y";
    public static final String HALLWAYS_END2_X = "end2_x";
    public static final String HALLWAYS_END2_Y = "end2_y";
    public static final String HALLWAYS_LENGTH = "length";
    public static final String HALLWAYS_WIDTH = "width";

    public static final String JUNCTIONS_TABLE_NAME = "Junctions";
    public static final String JUNCTIONS_BUILDING = "b_uid";
    public static final String JUNCTIONS_HALLWAY_1 = "h_uid1";
    public static final String JUNCTIONS_HALLWAY_2 = "h_uid2";
    public static final String JUNCTIONS_COORDINATE_X = "h_coordinate_x";
    public static final String JUNCTIONS_COORDINATE_Y = "h_coordinate_y";

    public static final String FLOORS_TABLE_NAME = "Floors";
    public static final String FLOORS_ID = "id";
    public static final String FLOORS_BUILDING_ID = "b_uid";
    public static final String FLOORS_URL = "url";

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
                    + BUILDINGS_ID + " TEXT PRIMARY KEY NOT NULL,"
                    + BUILDINGS_NAME + " TEXT,"
                    + BUILDINGS_URL + " TEXT"

                    + ")";
        db.execSQL(CREATE_BUILDINGS_TABLE);

        String CREATE_HALLWAYS_TABLE = "CREATE TABLE " + HALLWAYS_TABLE_NAME + "("
                + HALLWAYS_BUILDING_ID + " TEXT NOT NULL,"
                + HALLWAYS_FLOOR_ID + " TEXT NOT NULL,"
                + HALLWAYS_HALLWAY_ID + " TEXT NOT NULL,"
                + HALLWAYS_HALLWAY_NAME + " TEXT,"

                + HALLWAYS_END1_X + " REAL,"
                + HALLWAYS_END1_Y + " REAL,"
                + HALLWAYS_END2_X + " REAL,"
                + HALLWAYS_END2_Y + " REAL,"
                + HALLWAYS_LENGTH+ " REAL,"
                + HALLWAYS_WIDTH+ " REAL," +

                //Primary Key = B_uid,F_id,H_uid
                "PRIMARY KEY(" + HALLWAYS_BUILDING_ID + ", " + HALLWAYS_FLOOR_ID + ", " + HALLWAYS_HALLWAY_ID +")," +

                // Foreign Keys = B_uid
                "FOREIGN KEY(" + HALLWAYS_BUILDING_ID + ")"
                + " REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")"

                +")";
        db.execSQL(CREATE_HALLWAYS_TABLE);

        String CREATE_ROOMS_TABLE = "CREATE TABLE " + ROOMS_TABLE_NAME + "("
                + ROOMS_BUILDING_ID + " TEXT NOT NULL,"
                + ROOMS_FLOOR_ID + " TEXT NOT NULL,"
                + ROOMS_ROOM_ID + " TEXT NOT NULL,"
                + ROOMS_ROOM_NAME + " TEXT,"

                + ROOMS_HALLWAY_ID + " TEXT NOT NULL,"
                + ROOMS_HALLWAY_SIDE + " TEXT NOT NULL," // boolean(small int)???
                + ROOMS_CENTER_X + " REAL," // why nullable???
                + ROOMS_CENTER_Y + " REAL,"
                + ROOMS_DOOR_X + " REAL,"
                + ROOMS_DOOR_Y + " REAL," +

                //Primary Key = B_uid,F_id,door_x,door_y -> Yvonne changed it to room_id bec
                // instances like staircase without a door are causing problems
                "PRIMARY KEY(" + ROOMS_ROOM_ID + ")," +

                // Foreign Keys = B_uid, H_uid
                "FOREIGN KEY(" + ROOMS_BUILDING_ID + ")"
                        + " REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")," +
                "FOREIGN KEY(" + ROOMS_HALLWAY_ID + ")"
                        + " REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")"

                +")";
        db.execSQL(CREATE_ROOMS_TABLE);

        String CREATE_JUNCTIONS_TABLE = "CREATE TABLE " + JUNCTIONS_TABLE_NAME + "("
                + JUNCTIONS_BUILDING + " TEXT NOT NULL,"
                + JUNCTIONS_HALLWAY_1 + " TEXT NOT NULL,"
                + JUNCTIONS_HALLWAY_2 + " TEXT NOT NULL,"
                + JUNCTIONS_COORDINATE_X + " REAL,"
                + JUNCTIONS_COORDINATE_Y + " REAL," +

                //Primary Key = B_uid, H_uid1, H_uid2
                "PRIMARY KEY(" + JUNCTIONS_BUILDING + ", " + JUNCTIONS_HALLWAY_1 + ", " + JUNCTIONS_HALLWAY_2 + ")," +

                // Foreign Keys = H_uid1, H_uid2
                "FOREIGN KEY(" + JUNCTIONS_BUILDING + ")"
                + " REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")," +

                "FOREIGN KEY(" + JUNCTIONS_HALLWAY_1 + ")"
                + " REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")," +

                "FOREIGN KEY(" + JUNCTIONS_HALLWAY_2 + ")"
                + " REFERENCES " + HALLWAYS_TABLE_NAME + "(" + HALLWAYS_HALLWAY_ID + ")"

                + ")";

        db.execSQL(CREATE_JUNCTIONS_TABLE);

        String CREATE_FLOORS_TABLE = "CREATE TABLE " + FLOORS_TABLE_NAME + "("
                + FLOORS_ID + " TEXT NOT NULL,"
                + FLOORS_BUILDING_ID + " TEXT NOT NULL,"
                + FLOORS_URL + " TEXT NOT NULL," +

                // Primary Key = B_uid, F_id
                "PRIMARY KEY(" + FLOORS_BUILDING_ID + ", " + FLOORS_ID + ")," +

                // Foreign Key = B_uid
                "FOREIGN KEY(" + FLOORS_BUILDING_ID + ")"
                + " REFERENCES " + BUILDINGS_TABLE_NAME + "(" + BUILDINGS_ID + ")"

                + ")";

        db.execSQL(CREATE_FLOORS_TABLE);
    }

    public void populateData() {
        SQLiteDatabase db = getWritableDatabase();

        Map<String, List> buildings = null;
        Yaml yaml = new Yaml();
        try {
            InputStream is = DBHelper.class.getClassLoader().getResourceAsStream("building_structure.yaml");
            buildings = (Map<String, List>) yaml.load(is);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        populateBuildings(buildings);
    }

    private void populateBuildings(Map<String, List> buildings) {
        for (Map.Entry<String, List> buildingEntry : buildings.entrySet()) {
            String buildingName = buildingEntry.getKey();
            String buildingId = nextId(BUILDINGS_TABLE_NAME);
            List<Map> buildingAttributes = buildingEntry.getValue();
            insertBuilding(buildingId, buildingName, (String) buildingAttributes.get(0).get("url"));

            List<Double> entrance = (List) buildingAttributes.get(1).get("entrance");
            List<Map> floors = (List) buildingAttributes.get(2).get("floors");

            if (floors == null) {
                continue;
            }

            Map<Point, List<String>> junctionMap = new HashMap<>();
            for (Map<String, List> floor : floors) {
                for (Map.Entry<String, List> floorEntry : floor.entrySet()) {
                    String floorId = floorEntry.getKey();
                    List<Map> floorAttributes = floorEntry.getValue();

                    String url = floorAttributes.get(0).get("url").toString();
                    insertFloor(floorId, buildingId, url);

                    List<Map> hallways = (List) floorAttributes.get(1).get("hallways");
                    populateHallways(hallways, buildingId, floorId, junctionMap);
                }
            }

            populateJunctions(junctionMap, buildingId);
        }
    }

    private void populateJunctions(Map<Point, List<String>> junctionMap, String buildingId) {
        for (Map.Entry<Point, List<String>> junction : junctionMap.entrySet()) {
            Point point = junction.getKey();
            List<String> hallways = junction.getValue();
            for (int i = 0; i < hallways.size() - 1; i++) {
                for (int j = i + 1; j < hallways.size(); j++) {
                    String hallway1 = hallways.get(i);
                    String hallway2 = hallways.get(j);
                    insertJunction(buildingId, hallway1, hallway2, point.getX(), point.getY());
                }
            }
        }
    }

    private void populateHallways(List<Map> hallways, String buildingId, String floorId, Map<Point, List<String>> junctionMap) {
        for (Map<String, List> hallway : hallways) {
            for (Map.Entry<String, List> hallwayEntry : hallway.entrySet()) {
                String hallwayName = hallwayEntry.getKey();
                String hallwayId = nextId(HALLWAYS_TABLE_NAME);
                List<Map> hallwayAttributes = hallwayEntry.getValue();

                List<Double> end1 = (List) hallwayAttributes.get(0).get("end1");
                List<Double> end2 = (List) hallwayAttributes.get(1).get("end2");
                insertHallway(hallwayId, hallwayName, buildingId, floorId,
                        end1.get(0).floatValue(), end1.get(1).floatValue(),
                        end2.get(0).floatValue(), end2.get(1).floatValue(), 0, 0);

                Point end1_point = new Point(end1.get(0).floatValue(), end1.get(1).floatValue());
                List<String> connectingHallways;
                if (junctionMap.containsKey(end1_point)) {
                    connectingHallways = junctionMap.get(end1_point);
                }  else {
                    connectingHallways = new ArrayList<>();
                }
                connectingHallways.add(hallwayId);
                junctionMap.put(end1_point, connectingHallways);

                Point end2_point = new Point(end2.get(0).floatValue(), end2.get(1).floatValue());
                if (junctionMap.containsKey(end2_point)) {
                    connectingHallways = junctionMap.get(end2_point);
                }  else {
                    connectingHallways = new ArrayList<>();
                }
                connectingHallways.add(hallwayId);
                junctionMap.put(end2_point, connectingHallways);

                List<Map> sideA = (List) hallwayAttributes.get(2).get("side a");
                List<Map> sideB = (List) hallwayAttributes.get(3).get("side b");
                populateRooms(sideA, buildingId, floorId, hallwayId, SIDE_A);
                populateRooms(sideB, buildingId, floorId, hallwayId, SIDE_B);
            }
        }
    }

    private void populateRooms(List<Map> rooms, String buildingId, String floorId,
                               String hallwayId, String side) {
        if (rooms == null) {
            return;
        }
        for (Map<Object, List> room : rooms) {
            for (Map.Entry<Object, List> roomEntry : room.entrySet()) {
                String roomName = roomEntry.getKey().toString();
                String roomId = nextId(ROOMS_TABLE_NAME);
                List<List> coords = roomEntry.getValue();

                float center_x = 0, center_y = 0;
                for (int i = 0; i < 4; i++) {
                    List<Double> point = coords.get(i);
                    center_x += point.get(0).floatValue();
                    center_y += point.get(1).floatValue();
                }
                center_x /= 4;
                center_y /= 4;

                List<Double> door = coords.get(4);
                float door_x = door.get(0).floatValue();
                float door_y = door.get(1).floatValue();

                insertRoom(roomId, roomName, buildingId, floorId, hallwayId, side,
                        center_x, center_y, door_x, door_y);
            }
        }
    }

    private String nextId(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        String buildings_query = "SELECT COUNT(*) FROM " + tableName;

        /* Create DB cursor to iterate over query results */
        Cursor cursor =  db.rawQuery(buildings_query, null);
        cursor.moveToFirst();
        return parseInt(cursor.getString(0)) + "";
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
        return insertHallway(hallway_id, hallway_name, building_id, floor_id, 0, 0, 0, 0, 0, 0);
    }

    public boolean insertHallway(String hallway_id, String hallway_name, String building_id,
                                 String floor_id, float end1_x, float end1_y, float end2_x, float end2_y,
                                 float length, float width)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(HALLWAYS_HALLWAY_ID, hallway_id);
        contentValues.put(HALLWAYS_HALLWAY_NAME, hallway_name);

        contentValues.put(HALLWAYS_BUILDING_ID, building_id);
        contentValues.put(HALLWAYS_FLOOR_ID, floor_id);
        contentValues.put(HALLWAYS_HALLWAY_ID, hallway_id);
        contentValues.put(HALLWAYS_END1_X, end1_x);
        contentValues.put(HALLWAYS_END1_Y, end1_y);
        contentValues.put(HALLWAYS_END2_X, end2_x);
        contentValues.put(HALLWAYS_END2_Y, end2_y);
        contentValues.put(HALLWAYS_LENGTH, length);
        contentValues.put(HALLWAYS_WIDTH, width);

        return db.insert(HALLWAYS_TABLE_NAME, null, contentValues) != -1;
    }

    public boolean insertRoom(String room_id, String room_name, String building_id,
                              String floor_id, String hallway_id, String hallway_side,
                              float center_x, float center_y, float door_x, float door_y)
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
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ROOMS_ROOM_ID, room.getRoomId());
        values.put(ROOMS_BUILDING_ID, room.getBuildingId());
        values.put(ROOMS_FLOOR_ID, room.getFloorId());
        values.put(ROOMS_HALLWAY_ID, room.getHallwayId());
        values.put(ROOMS_HALLWAY_SIDE, room.getHallwaySide().toString());
        values.put(ROOMS_ROOM_NAME, room.getRoomName());

        Point door = room.getDoor();
        values.put(ROOMS_DOOR_X, door.getX());
        values.put(ROOMS_DOOR_Y, door.getY());

        Point center = room.getCenter();
        values.put(ROOMS_CENTER_X, center.getX());
        values.put(ROOMS_CENTER_Y, center.getY());

        return db.insert(ROOMS_TABLE_NAME, null, values) != -1;
    }

    public boolean insertJunction(String building_id, String hallway_id1, String hallway_id2, float x, float y)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(JUNCTIONS_BUILDING, building_id);
        contentValues.put(JUNCTIONS_HALLWAY_1, hallway_id1);
        contentValues.put(JUNCTIONS_HALLWAY_2, hallway_id2);
        contentValues.put(JUNCTIONS_COORDINATE_X, x);
        contentValues.put(JUNCTIONS_COORDINATE_Y, y);

        return db.insert(JUNCTIONS_TABLE_NAME, null, contentValues) != -1;
    }

    public boolean insertFloor(String floor_id, String building_id, String url) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(FLOORS_ID, floor_id);
        contentValues.put(FLOORS_BUILDING_ID, building_id);
        contentValues.put(FLOORS_URL, url);

        return db.insert(FLOORS_TABLE_NAME, null, contentValues) != -1;
    }

    /***************************  SQL Select Queries (DB "Getters") *******************************/

//    /* If no parameters provided, mock out response - Temporary patch */
//    public List<Building> mockAllBuildings(){
//        this.insertBuilding("building_0","College of Computing", "coc.jpg");
//        this.insertBuilding("building_1","Klaus Advanced Computing Building","klaus.jpg");
//        this.insertBuilding("building_2","Student Center", "student_center.jpg");
//
//        List<Building> buildings = getAllBuildings();
//
//        return buildings;
//    }
//
//    /* run this after mocking buildingss */
//    public List<Hallway> mockHallwaysPerFloor(){
//        this.insertHallway("hallway_0","entrance1","building_0","floor_0");
//        this.insertHallway("hallway_1","hallway1","building_0","floor_0");
//        this.insertHallway("hallway_2","entrance2","building_0","floor_0");
//
//        List<Hallway> hallways = getHallwaysPerFloor("building_0","floor_0");
//        return hallways;
//    }
//
//    /* If no parameters provided, mock out response - Temporary patch */
//    /* run this after mocking buildings */
//    public List<Room> mockRoomsPerFloor(){
//        this.insertRoom("room_0", "211", "building_0", "floor_2", "hallway_0", "SIDE_A", 0, 0, 1, 1);
//        this.insertRoom("room_1", "212", "building_0", "floor_2", "hallway_0", "SIDE_B", 2, 4, 5, 6);
//        this.insertRoom("room_2", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 1);
//        this.insertRoom("room_3", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 2);
//
//        List<Room> rooms = getRoomsPerFloor("building_0","floor_1");
//        /* should return room_2 and room_3 */
//        return rooms;
//    }

    /* returns a list of all buildings in the DB */
    public List<Building> getAllBuildings(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Building> building_list = new ArrayList<>();

        /* Select buildings query */
        String buildings_query = "SELECT * FROM " + BUILDINGS_TABLE_NAME;

        /* Create DB cursor to iterate over query results */
        Cursor cursor =  db.rawQuery(buildings_query, null);
        cursor.moveToFirst();

        /* Map columns to POJO object */
        while (!cursor.isAfterLast())
        {
            Building building = new Building();

            building.setBuildingId(cursor.getString(cursor.getColumnIndex(BUILDINGS_ID)));
            building.setBuildingName(cursor.getString(cursor.getColumnIndex(BUILDINGS_NAME)));
            building.setBuildingImage(cursor.getString(cursor.getColumnIndex(BUILDINGS_URL)));

            building_list.add(building);
            cursor.moveToNext();
        }

        cursor.close();
        return building_list;
    }

    /* Should return all hallways in a building which are marked as entrance */
    public List<Hallway> getBuildingEntrances(String building_id){
        SQLiteDatabase db = this.getReadableDatabase();

        List<Hallway> entrance_list = new ArrayList<>();

        /* Select buildings query */
        String entrances_query = "SELECT * FROM " + HALLWAYS_TABLE_NAME +
                                " WHERE " + HALLWAYS_HALLWAY_NAME + " LIKE '%entrance%'"
                                + " AND " + BUILDINGS_ID + " equals '" + building_id + "'";

        /* Create DB cursor to iterate over query results */
        Cursor cursor =  db.rawQuery(entrances_query, null);
        cursor.moveToFirst();

        /* Map columns to POJO object */
        while (!cursor.isAfterLast())
        {
            Hallway entrance = new Hallway();

            entrance.setBuildingId(cursor.getString(0));
            entrance.setFloorId(cursor.getString(1));
            entrance.setHallwayId(cursor.getString(2));
            entrance.setHallwayName(cursor.getString(3));

            entrance_list.add(entrance);
            cursor.moveToNext();
        }

        cursor.close();
        return entrance_list;
    }

    public List<Junction> getJunctionsPerBuilding(String building_id) {
        ArrayList<Junction> junction_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        List<Hallway> hallways = getHallwaysPerBuilding(building_id);

        Map<String, Hallway> idToHallways = new HashMap<>();
        for (Hallway h : hallways) {
            idToHallways.put(h.getId(), h);
        }

        String query = "SELECT *" +
                " FROM " + JUNCTIONS_TABLE_NAME +
                " WHERE " + JUNCTIONS_BUILDING + "='" + building_id + "'";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            String h_uid1 = cursor.getString(cursor.getColumnIndex(JUNCTIONS_HALLWAY_1));
            String h_uid2 = cursor.getString(cursor.getColumnIndex(JUNCTIONS_HALLWAY_2));
            Point point = new Point(parseFloat(cursor.getString(cursor.getColumnIndex(JUNCTIONS_COORDINATE_X))),
                    parseFloat(cursor.getString(cursor.getColumnIndex(JUNCTIONS_COORDINATE_Y))));
            List<Hallway> members = new ArrayList<>(2);

            Hallway hallway1 = idToHallways.get(h_uid1);
            Hallway hallway2 = idToHallways.get(h_uid2);
            members.add(hallway1);
            members.add(hallway2);

            Junction junction = new Junction(members, point);
            junction_list.add(junction);
            hallway1.addJunction(junction);
            hallway2.addJunction(junction);
            cursor.moveToNext();
        }

        cursor.close();
        return junction_list;
    }

    /* Returns all the rooms on a particular building's floor */
    public List<Room> getRoomsPerFloor(String building_id, String floor_id)
    {
        List<Room> room_list = new ArrayList<>();
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

        /* Map columns to POJO object */
        while (!cursor.isAfterLast())
        {
            Room room = new Room();

            room.setRoomId(cursor.getString(cursor.getColumnIndex(ROOMS_ROOM_ID)));
            room.setBuildingId(cursor.getString(cursor.getColumnIndex(ROOMS_BUILDING_ID)));
            room.setFloorId(cursor.getString(cursor.getColumnIndex(ROOMS_FLOOR_ID)));
            room.setRoomName(cursor.getString(cursor.getColumnIndex(ROOMS_ROOM_NAME)));
            room.setHallwayId(cursor.getString(cursor.getColumnIndex(ROOMS_HALLWAY_ID)));
            room.setHallwaySide((HallwaySide.valueOf(cursor.getString(cursor.getColumnIndex(ROOMS_HALLWAY_SIDE)))));

            Float center_x = parseFloat(cursor.getString(cursor.getColumnIndex(ROOMS_CENTER_X)));
            Float center_y = parseFloat(cursor.getString(cursor.getColumnIndex(ROOMS_CENTER_Y)));
            Point center = new Point(center_x,center_y);
            room.setCenter(center);

            Float door_x = parseFloat(cursor.getString(cursor.getColumnIndex(ROOMS_DOOR_X)));
            Float door_y = parseFloat(cursor.getString(cursor.getColumnIndex(ROOMS_DOOR_Y)));
            Point door = new Point(door_x,door_y);
            room.setDoor(door);

            room_list.add(room);
            cursor.moveToNext();
        }

        cursor.close();
        return room_list;
    }

    public List<Hallway> getHallwaysPerFloor(String building_id, String floor_id) {
        List<Hallway> hallway_list = new ArrayList<>();
        String hallway_query = "";

        SQLiteDatabase db = this.getReadableDatabase();

        /* Select rooms query */
        hallway_query = "SELECT *" +
                " FROM " + HALLWAYS_TABLE_NAME +
                " WHERE " + HALLWAYS_BUILDING_ID + "='" + building_id + "'";

        /* Add floor parameter to query if specified */
        if (floor_id != null) {
            hallway_query += " AND " + HALLWAYS_FLOOR_ID + "='" + floor_id + "'";
        }

        /* Create DB cursor to iterate over query results */
        Cursor cursor = db.rawQuery(hallway_query, null);
        cursor.moveToFirst();

        Map<String, List<Room>> hallwayNSideToRoom = new HashMap<>();
        List<Room> rooms = getRoomsPerBuilding(building_id);
        for (Room room : rooms) {
            String key = room.getHallwayId() + room.getHallwaySide();
            List<Room> roomsOnSide;
            if (hallwayNSideToRoom.containsKey(key)) {
                roomsOnSide = hallwayNSideToRoom.get(key);
            } else {
                roomsOnSide = new ArrayList<>();
            }
            roomsOnSide.add(room);

            hallwayNSideToRoom.put(key, roomsOnSide);
        }

        /* Map columns to POJO object */
        while (!cursor.isAfterLast()) {
            Hallway hallway = new Hallway();

            hallway.setHallwayId(cursor.getString(cursor.getColumnIndex(HALLWAYS_HALLWAY_ID)));
            hallway.setBuildingId(cursor.getString(cursor.getColumnIndex(HALLWAYS_BUILDING_ID)));
            hallway.setFloorId(cursor.getString(cursor.getColumnIndex(HALLWAYS_FLOOR_ID)));
            hallway.setHallwayName(cursor.getString(cursor.getColumnIndex(HALLWAYS_HALLWAY_NAME)));
            hallway.setEnd1(new Point(parseFloat(cursor.getString(cursor.getColumnIndex(HALLWAYS_END1_X))),
                    parseFloat(cursor.getString(cursor.getColumnIndex(HALLWAYS_END1_Y)))));
            hallway.setEnd2(new Point(parseFloat(cursor.getString(cursor.getColumnIndex(HALLWAYS_END2_X))),
                    parseFloat(cursor.getString(cursor.getColumnIndex(HALLWAYS_END2_Y)))));

            hallway.setRoomsA(hallwayNSideToRoom.get(hallway.getId()+HallwaySide.SIDE_A));
            hallway.setRoomsB(hallwayNSideToRoom.get(hallway.getId()+HallwaySide.SIDE_B));

            hallway_list.add(hallway);
            cursor.moveToNext();
        }

        cursor.close();

        return hallway_list;
    }

    /* Returns all the rooms in a particular building */
    public List<Room> getRoomsPerBuilding(String building_id) {
        return getRoomsPerFloor(building_id, null);
    }

    public List<Hallway> getHallwaysPerBuilding(String building_id) {
        return getHallwaysPerFloor(building_id, null);
    }

    /* Returns all picture uris for all supported buildings */
    public ArrayList<String> getAllBuildingUrls() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> urls = new ArrayList<>();

        String query = "SELECT " + BUILDINGS_URL
                + " FROM " + BUILDINGS_TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            urls.add(cursor.getString(0));
            cursor.moveToNext();
        }

        cursor.close();
        return urls;
    }

    public int getBuildingUrl(String building_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        /* Select rooms query */
        String query = "SELECT " + BUILDINGS_URL +
                " FROM " + BUILDINGS_TABLE_NAME +
                " WHERE " + BUILDINGS_ID + "='" + building_id + "'";

        Cursor cursor =  db.rawQuery(query, null);
        cursor.moveToFirst();
        String url = cursor.getString(0);
        int id = getResId(url, Drawable.class);
        cursor.close();
        return id;
    }

    public static int getResId(String resName, Class<?> c) {

        try {
            Field idField = c.getDeclaredField(resName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public String getFloorUrl(String building_id, String floor_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        /* Select rooms query */
        String query = "SELECT " + FLOORS_URL +
                " FROM " + FLOORS_TABLE_NAME +
                " WHERE " + FLOORS_BUILDING_ID + "='" + building_id + "'" +
                " AND " + FLOORS_ID + "='" + floor_id + "'";

        Cursor cursor =  db.rawQuery(query, null);
        cursor.moveToFirst();
        String url = cursor.getString(0);
        cursor.close();
        return url;
    }
}

