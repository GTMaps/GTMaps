package edu.gatech.gtmaps;

import java.util.LinkedList;
import edu.gatech.gtmaps.models.BuildingSpace;
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

    /**
     * Performs breadth first search (to be adapted to Greedy Best-First across all the halls/rooms
     * to find target room.
     * @param target The desired room to navigate to.
     * @param start The starting hallway.
     * @return LinkedList of building spaces to navigate from start ot target.
     */
    public LinkedList<BuildingSpace> find(Room target, BuildingSpace start) {
        //Initialize datastructres/loop variables
        LinkedList<BuildingSpace> ret = new LinkedList<>();
        LinkedList<BuildingSpace> visited = new LinkedList<>();
        boolean found = false;
        Node currNode = new Node(start, null);

        while (!found) {
            if (currNode.h == start) {
                found = true;
            }
        }
        return ret;
    }

    private class Node {
        private BuildingSpace h;
        private BuildingSpace p;
        private Node(BuildingSpace thisHallway, BuildingSpace previousHallway) {
            h = thisHallway;
            p = previousHallway;
        }
    }
}
