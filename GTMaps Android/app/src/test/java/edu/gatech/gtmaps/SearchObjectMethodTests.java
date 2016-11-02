package edu.gatech.gtmaps;

import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Room;

import static org.junit.Assert.assertEquals;

/**
 * Tim's unit tests for SearchObject
 * Created by Tim on 10/31/2016.
 */

public class SearchObjectMethodTests {

    Hallway simpleEntry;
    Room goalRoom;

    @Before
    public void setup() {
        Room leftRoom = new Room("1", null, 0, new Point(120,100), new Point(235,90) , 'A');
        Room rightRoom = new Room("2", null, 0, new Point(350,80), new Point(320,80), 'B');
        Hallway simpleEntry = new Hallway("H1", 335, 85, 0, new Point(275,45), new Point(275,385));
        LinkedList temp = new LinkedList();
        temp.add(leftRoom);
        simpleEntry.addRooms(leftRoom.getSide(), temp);
        temp.clear();
        temp.add(rightRoom);
        simpleEntry.addRooms(rightRoom.getSide(), temp);
        Hallway simpleHallway = new Hallway("H1", 335, 85, 0, new Point(275,45), new Point(275,385));
        temp.clear();
        temp.add(simpleEntry);
        temp.add(simpleHallway);
        Junction simpleJunction = new Junction(temp);
        temp = null;
        goalRoom = rightRoom;
    }

    @Test
    public void simpleSearch_returnsCorrect() {
        LinkedList<BuildingSpace> directions = SearchObject.find(goalRoom, simpleEntry);
    }
}