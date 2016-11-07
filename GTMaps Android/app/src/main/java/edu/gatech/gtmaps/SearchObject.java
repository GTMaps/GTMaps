package edu.gatech.gtmaps;

import java.util.LinkedList;
import java.util.List;

import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Room;

/**
 * Static search class for the application
 * Created by Tim on 10/3/2016.
 */
public class SearchObject {

    /**
     * Performs breadth first search (to be adapted to Greedy Best-First across all the halls/rooms
     * to find target room.
     * @param target The desired room to navigate to.
     * @param start The starting hallway.
     * @return LinkedList of building spaces to navigate from start to target.
     */
    public static LinkedList<BuildingSpace> find(Room target, BuildingSpace start) {
        //Initialize datastructures/loop variables
        LinkedList<BuildingSpace> ret = new LinkedList<>();
        LinkedList<BuildingSpace> visited = new LinkedList<>();
        LinkedList<Node> frontier = new LinkedList<>();
        boolean found = false;
        Node currNode = new Node(start, null);

        while (!found) {
            visited.add(currNode.h);

            for (Junction j :( (Hallway) currNode.h).getJunctions()) {
                List<Hallway> hallways = j.getHallways();
                for (int i = 0; i < hallways.size(); i++) {
                    if (!hallways.get(i).getName().equals(currNode.h.getName())) {
                        frontier.push(new Node(hallways.get(i), currNode));
                    }
                }
            }

            if (!frontier.isEmpty()) {
                Node next = frontier.pop();
                while (visited.contains(next.h) & !frontier.isEmpty()) {
                    next = frontier.pop();
                }
                currNode = next;
            }
            else {
                visited = new LinkedList<>();
            }

            if (((Hallway) currNode.h).getRoomsA().contains(target) || ((Hallway) currNode.h).getRoomsB().contains(target)) {
                found = true;
            }
        }

        while (currNode != null) {
            ret.push(currNode.h);
            currNode = currNode.p;
        }
        return ret;
    }

    /**
     * Plaintext translation of a list of building spaces. Currently incomplete.
     * @param dir LinkedList of BuildingSpace objects which constitute a path to be translated.
     * @return String representing the directions to navigate from the start of dir to the end.
     */
    public static String translate(LinkedList<BuildingSpace> dir) {
        StringBuilder sb = new StringBuilder();
        for (BuildingSpace node : dir) {
            sb.append(node.getClass());
            sb.append(": ");
            sb.append(node.getName());
            sb.append("\n");
        }
        return sb.toString();
    }

    private static class Node {
        private BuildingSpace h;
        private Node p;
        private Node(BuildingSpace thisHallway, Node previousHallway) {
            h = thisHallway;
            p = previousHallway;
        }
    }
}