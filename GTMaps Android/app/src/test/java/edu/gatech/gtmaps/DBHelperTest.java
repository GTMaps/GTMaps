package edu.gatech.gtmaps;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;

import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Room;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(RobolectricTestRunner.class)
public class DBHelperTest {
    private DBHelper dbHelper;

    @Test
    public void testOnCreate() {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        dbHelper = new DBHelper(context.getApplicationContext());
        assertEquals("GTMapsDB.db", dbHelper.getDatabaseName());
    }

    @Test
    public void testGetRoomsPerFloor() {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        dbHelper = new DBHelper(context.getApplicationContext());

        dbHelper.insertBuilding("building_0", "CoC", "building_url");
        dbHelper.insertHallway("hallway_0", "h0", "building_0", "floor_2");
        dbHelper.insertHallway("hallway_1", "h1", "building_0", "floor_1");
        assertTrue(dbHelper.insertRoom("room_0", "211", "building_0", "floor_2", "hallway_0", "SIDE_A", 0, 0, 1, 1));
        assertTrue(dbHelper.insertRoom("room_1", "212", "building_0", "floor_2", "hallway_0", "SIDE_B", 2, 4, 5, 6));
        assertTrue(dbHelper.insertRoom("room_2", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 1));
        assertTrue(dbHelper.insertRoom("room_3", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 2)); // Same room different door

        ArrayList<Room> actualRoomsOnFloor1 = dbHelper.getRoomsPerFloor("building_0", "floor_2");

        assertEquals(2, actualRoomsOnFloor1.size());
        for (Room r : actualRoomsOnFloor1) {
            if (r.getRoomId().equals("room_0")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_2", "hallway_0", BuildingSpace.HallwaySide.SIDE_A, "room_0", "211", new Point(0, 0), new Point(1, 1)), r);
            } else if (r.getRoomId().equals("room_1")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_2", "hallway_0", BuildingSpace.HallwaySide.SIDE_B, "room_1", "212", new Point(2, 4), new Point(5, 6)), r
                );
            } else {
                fail(r.toString() + " shouldn't be on floor 2");
            }
        }

        ArrayList<Room> actualRoomsOnFloor2 = dbHelper.getRoomsPerFloor("building_0", "floor_1");
        assertEquals(2, actualRoomsOnFloor2.size());
        for (Room r : actualRoomsOnFloor2) {
            if (r.getRoomId().equals("room_2")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_1", "hallway_1", BuildingSpace.HallwaySide.SIDE_B, "room_2", "112", new Point(2, 3), new Point(5, 1)), r
                );
            } else if (r.getRoomId().equals("room_3")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_1", "hallway_1", BuildingSpace.HallwaySide.SIDE_B, "room_3", "112", new Point(2, 3), new Point(5, 2)), r
                );
            } else {
                fail(r.toString() + " shouldn't be on floor 1");
            }
        }
    }

    @Test
    public void testGetRoomsPerBuilding() {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        dbHelper = new DBHelper(context.getApplicationContext());

        dbHelper.insertBuilding("building_0", "CoC", "building_url");
        dbHelper.insertHallway("hallway_0", "h0", "building_0", "floor_2");
        dbHelper.insertHallway("hallway_1", "h1", "building_0", "floor_1");
        assertTrue(dbHelper.insertRoom("room_0", "211", "building_0", "floor_2", "hallway_0", "SIDE_A", 0, 0, 1, 1));
        assertTrue(dbHelper.insertRoom("room_1", "212", "building_0", "floor_2", "hallway_0", "SIDE_B", 2, 4, 5, 6));
        assertTrue(dbHelper.insertRoom("room_2", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 1));
        assertTrue(dbHelper.insertRoom("room_3", "112", "building_0", "floor_1", "hallway_1", "SIDE_B", 2, 3, 5, 2)); // Same room different door

        ArrayList<Room> actual = dbHelper.getRoomsPerBuilding("building_0");

        assertEquals(4, actual.size());
        for (Room r : actual) {
            if (r.getRoomId().equals("room_0")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_2", "hallway_0", BuildingSpace.HallwaySide.SIDE_A, "room_0", "211", new Point(0, 0), new Point(1, 1)), r);
            } else if (r.getRoomId().equals("room_1")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_2", "hallway_0", BuildingSpace.HallwaySide.SIDE_B, "room_1", "212", new Point(2, 4), new Point(5, 6)), r
                );
            } else if (r.getRoomId().equals("room_2")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_1", "hallway_1", BuildingSpace.HallwaySide.SIDE_B, "room_2", "112", new Point(2, 3), new Point(5, 1)), r
                );
            } else if (r.getRoomId().equals("room_3")) {
                assertRoomsEqual(
                        new Room("building_0", "floor_1", "hallway_1", BuildingSpace.HallwaySide.SIDE_B, "room_3", "112", new Point(2, 3), new Point(5, 2)), r
                );
            } else {
                fail();
            }
        }
    }

    private void assertRoomsEqual(Room expected, Room actual) {
        assertEquals(expected.getBuildingId(), actual.getBuildingId());
        assertEquals(expected.getFloorId(), actual.getFloorId());
        assertEquals(expected.getHallwayId(), actual.getHallwayId());
        assertEquals(expected.getHallwaySide(), actual.getHallwaySide());
        assertEquals(expected.getRoomId(), actual.getRoomId());
        assertEquals(expected.getRoomName(), actual.getRoomName());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getDoor(), actual.getDoor());
    }

}
