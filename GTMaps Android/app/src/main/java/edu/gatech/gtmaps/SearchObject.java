package edu.gatech.gtmaps;

import java.util.LinkedList;
import java.util.List;

import edu.gatech.gtmaps.models.BuildingSpace;
import edu.gatech.gtmaps.models.Hallway;
import edu.gatech.gtmaps.models.Junction;
import edu.gatech.gtmaps.models.Point;
import edu.gatech.gtmaps.models.Vec;
import edu.gatech.gtmaps.models.Room;

/**
 * Static search class for the application
 * Created by Tim on 10/3/2016.
 */
public class SearchObject {

    /**
     * Performs breadth first search (to be adapted to Greedy Best-First) across all the halls/rooms
     * to find target room.
     * @param target The desired room to navigate to.
     * @param start The starting hallway.
     * @return LinkedList of building spaces [hallway,Junction,Junction...Junction] to navigate from start to target.
     */
    public static LinkedList<BuildingSpace> find(Room target, BuildingSpace start) {
        //Initialize datastructures/loop variables
        LinkedList<BuildingSpace> ret = new LinkedList<>();
        LinkedList<BuildingSpace> visited = new LinkedList<>();
        LinkedList<Node> frontier = new LinkedList<>();
        boolean found = false;
        Node currNode = new Node(start, null, null);

        while (!found) {
            visited.add(currNode.h);

            for (Junction j :( (Hallway) currNode.h).getJunctions()) {
                List<Hallway> hallways = j.getHallways();
                for (int i = 0; i < hallways.size(); i++) {
                    if (!hallways.get(i).getName().equals(currNode.h.getId())) {
                        frontier.push(new Node(hallways.get(i), currNode, j));
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
            ret.push((currNode.j != null) ? currNode.j : currNode.h);
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
        Hallway thisHall = (Hallway) dir.get(0);
        Hallway nextHall = (Hallway) dir.get(0);
        for (int i = 1; i < dir.size(); i++) {
            BuildingSpace node = dir.get(i);
            for (Hallway h : ((Junction) node).getHallways()) {
                if (!h.equals(thisHall)) {
                    nextHall = h;
                }
            }

            Point thisEnd = thisHall.getEnd1();
            Point nextEnd = nextHall.getEnd1();
            if (!(thisEnd.getX() == nextEnd.getX() || thisEnd.getY() == nextEnd.getY())) {
                String direction = (isLeftTurn(thisHall.getEnd1(),thisHall.getEnd2(),nextHall.getEnd1(),nextHall.getEnd2())) ? "left" : "right"; //replace true with math logic to figure out side hall is on
                sb.append("Turn ");
                sb.append(direction);
                sb.append(" at end of hallway ");
                sb.append(thisHall.getName());
                sb.append(" onto ");
                sb.append(nextHall.getName());
                sb.append(".\n");
            }
            thisHall = nextHall;
        }
        sb.append("Desired room will be on this hallway (");
        sb.append(thisHall.getName());
        String hall_side = (true) ? "right" : "left"; //replace true with math logic to figure out side room is on
        sb.append(") on the ");
        sb.append(hall_side);
        sb.append(" of the hall.\n");
        return sb.toString();
    }

    private static class Node {
        private BuildingSpace h;
        private Junction j;
        private Node p;
        private Node(BuildingSpace thisHallway, Node previousHallway, Junction connector) {
            h = thisHallway;
            p = previousHallway;
            j = connector;
        }
    }
    //let a, b belong to thishall and c,d belong to nexthall
    public static boolean isLeftTurn(Point a, Point b, Point c, Point d) {
        Point connectingP1 =a, connectingP2 = c;
        double ac = a.d(a,c);
        double ad = a.d(a,d);
        double bc = b.d(b,c);
        double bd = b.d(b,d);
        double min = ac;
        if(ad < min) {
            min = ad;
            connectingP1 = a;
            connectingP2 = c;
        }
        if(bc < min) {
            min = bc;
            connectingP1 = b;
            connectingP2 = c;
        }
        if(bd < min) {
            min = bd;
            connectingP1 = b;
            connectingP2 = d;
        }

        Point startingP1 = (connectingP1==b) ? a:b;
        Point endP2 = (connectingP2==d) ? c:d;
        Vec thisHallVec = new Vec(startingP1, connectingP1);
        Vec nextHallVec = new Vec(connectingP2,endP2);
        return nextHallVec.isLeftTurn(thisHallVec);
    }

}