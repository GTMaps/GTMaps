package edu.gatech.gtmaps;

import java.util.LinkedList;
import edu.gatech.gtmaps.models.Building;
import edu.gatech.gtmaps.models.Room;
import edu.gatech.gtmaps.models.Hall;

/**
 * Singleton search object for the application
 * Created by Tim on 10/3/2016.
 */
public class SearchObject {
    private static SearchObject ourInstance = new SearchObject();

    public static SearchObject getInstance() {
        return ourInstance;
    }

    private SearchObject() {
    }

    public LinkedList<Room> find(String roomNumber, Building b) {
        LinkedList<Room> ret = new LinkedList();
        return ret;
    }

    private class Node {
        private Hall h;
        private Hall p;
        private Node(Hall thisHallway, Hall previousHallway) {
            h = thisHallway;
            p = previousHallway;
        }
    }
}
