package edu.gatech.gtmaps;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

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
     * Performs Greedy Best-First search across all the halls/rooms, prioritizing on hallway length.
     * to find target room.
     * @param target The desired room to navigate to.
     * @param start The starting hallway.
     * @return LinkedList of building spaces [hallway,Junction,Junction...Junction] to navigate from start to target.
     */
    public static LinkedList<BuildingSpace> find(Room target, BuildingSpace start) {
        //Initialize datastructures/loop variables
        LinkedList<BuildingSpace> ret = new LinkedList<>();
        LinkedList<BuildingSpace> visited = new LinkedList<>();
        //LinkedList<Node> frontier = new LinkedList<>();
        PriorityQueue<Node> frontier = new PriorityQueue<>(11, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                Hallway h1 = (Hallway) o1.h;
                Hallway h2 = (Hallway) o2.h;
                Point h1_end1 = h1.getEnd1();
                Point h1_end2 = h1.getEnd2();
                Point h2_end1 = h2.getEnd1();
                Point h2_end2 = h2.getEnd2();
                double dist1,dist2;
                double dx = h1_end1.getX() - h1_end2.getX();
                double dy = h1_end1.getY() - h1_end2.getY();
                dist1 = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                dx = h2_end1.getX() - h2_end2.getX();
                dy = h2_end1.getY() - h2_end2.getY();
                dist2 = Math.sqrt(Math.pow(dx,2) + Math.pow(dy,2));
                return (int) -(dist1 - dist2);
            }
        });
        boolean found = false;
        Node currNode = new Node(start, null, null);

        while (!found) {
            visited.add(currNode.h);

            for (Junction j :( (Hallway) currNode.h).getJunctions()) {
                List<Hallway> hallways = j.getHallways();
                for (int i = 0; i < hallways.size(); i++) {
                    if (!hallways.get(i).getName().equals(currNode.h.getId())) {
                        //frontier.addLast(new Node(hallways.get(i), currNode, j));
                        frontier.add(new Node(hallways.get(i), currNode, j));
                    }
                }
            }

            if (!frontier.isEmpty()) {
                //Node next = frontier.pop();
                Node next = frontier.poll();
                while (visited.contains(next.h) & !frontier.isEmpty()) {
                    //next = frontier.pop();
                    next = frontier.poll();
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
    public static String translate(LinkedList<BuildingSpace> dir, Room goalRoom) {
        StringBuilder sb = new StringBuilder();
        LinkedList<Hallway> halls = new LinkedList<>();
        Hallway thisHall = (Hallway) dir.get(0);
        Hallway nextHall = (Hallway) dir.get(0);
        halls.add(thisHall);

        int direction_number = 0;

        for (int i = 1; i < dir.size(); i++) {
            BuildingSpace node = dir.get(i);
            for (Hallway h : ((Junction) node).getHallways()) {
                if (!h.equals(thisHall)) {
                    nextHall = h;
                }
            }

            Point thisEnd = thisHall.getEnd1();
//            Point nextEnd = nextHall.getEnd1();

            if (!oneHall(thisHall,nextHall)) {
                String direction = (isLeftTurn(thisHall.getEnd1(),thisHall.getEnd2(),nextHall.getEnd1(),nextHall.getEnd2())) ? "left" : "right";

                direction_number+=1;
                sb.append(direction_number);
                sb.append(". ");

                sb.append("Turn ");
                sb.append(direction);
                Point p = connectingPoints(thisHall.getEnd1(), thisHall.getEnd2(), nextHall.getEnd1(), nextHall.getEnd2())[0];
                //(direction.equalsIgnoreCase("left") && p.equals(thisEnd)) || direction.equalsIgnoreCase("right") && !p.equals(thisEnd) ? thisHall.getRoomsA() : thisHall.getRoomsB();
                List<Room> roomSide;
                if ((direction.equalsIgnoreCase("left") && p.equals(thisEnd)) || (direction.equalsIgnoreCase("right") && !p.equals(thisEnd))) {
                    roomSide = (thisHall.getRoomsA().size() > 0) ? thisHall.getRoomsA() : thisHall.getRoomsB();
                } else {
                    roomSide = (thisHall.getRoomsB().size() > 0) ? thisHall.getRoomsB() : thisHall.getRoomsA();
                }
                if (roomSide.size() < 1) {
                    sb.append(" down the hall at the next junction.\n");
                } else {
                    sb.append(" after room ");
                    Room lastRoom = (p.equals(thisEnd)) ? roomSide.get(0) : roomSide.get(roomSide.size() - 1);
                    sb.append(lastRoom.getRoomName());
                    sb.append(".\n");
                }
                //sb.append(thisHall.getName());
                //sb.append(" onto ");
                //sb.append(nextHall.getName());
                //sb.append(".\n");
            }
            thisHall = nextHall;
            halls.add(thisHall);
        }
        Hallway lastHall = halls.getLast();
        Hallway penultimate = halls.get(halls.size() - 2);

        sb.append("Room ");
        sb.append(goalRoom.getRoomName());
        sb.append(" will be on this hallway ");
        //sb.append(lastHall.getName());

        String hall_side = (isLeftRoom(lastHall.getEnd1(), lastHall.getEnd2(), penultimate.getEnd1(), penultimate.getEnd2(),goalRoom.getDoor())) ? "left" : "right";
        sb.append("on the ");
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
        Point[] p = connectingPoints(a,b,c,d);
        Point connectingP1 =p[0], connectingP2 = p[1];

        Point startingP1 = (connectingP1==b) ? a:b;
        Point endP2 = (connectingP2==d) ? c:d;
        Vec thisHallVec = new Vec(startingP1, connectingP1);
        Vec nextHallVec = new Vec(connectingP2,endP2);
        return !nextHallVec.isLeftTurn(thisHallVec);
    }

    /**
     * Determines if the room is on the left side of the hall.
     * @param lasthallEnd1 Side one of the last hallway.
     * @param lasthallEnd2 Side two of the last hallway.
     * @param prevhallEnd1 Side one of the penultimate hallway.
     * @param prevhallEnd2 Side two of the penultimate hallway.
     * @param roomDoor The point representing the doorway of the room.
     * @return True if the room is on the left of the hall, false otherwise.
     */
    private static boolean isLeftRoom(Point lasthallEnd1, Point lasthallEnd2, Point prevhallEnd1, Point prevhallEnd2, Point roomDoor) {
        Point[] p = connectingPoints(lasthallEnd1,lasthallEnd2,prevhallEnd1,prevhallEnd2);
        Point entrySide =p[0], otherSide = p[1];

        int diffX = (int) (entrySide.getX() - otherSide.getX());
        boolean ret = true;
        if (diffX < 0) {
            int roomYhallY = (int) (roomDoor.getY() - entrySide.getY());
            ret = (roomYhallY > 0);
        } else if (diffX > 0) {
            int roomYhallY = (int) (roomDoor.getY() - entrySide.getY());
            ret = (roomYhallY < 0);
        } else {
            int diffY = (int) (entrySide.getY() - otherSide.getY());
            int roomXhallX = (int) (roomDoor.getX() - entrySide.getX());
            ret = (diffY < 0) ? (roomXhallX > 0) : (roomXhallX < 0);
        }
        return !ret;
    }

    /**
     * Determines which to points are the connecting points between hallways.
     * @param a End 1 of hallway 1 (Point).
     * @param b End 2 of hallway 1 (Point).
     * @param c End 1 of hallway 2 (Point).
     * @param d End 2 of hallway 2 (Point).
     * @return Point array containing [Hallway 1 Connecting End, Hallway 2 Connecting End]
     */
    private static Point[] connectingPoints(Point a, Point b, Point c, Point d) {
        Point connectingP1 =a, connectingP2 = c;
        double ac = a.d(a,c);
        double ad = a.d(a,d);
        double bc = b.d(b,c);
        double bd = b.d(b,d);
        double min = ac;
        if(ad < min) {
            min = ad;
            connectingP1 = a;
            connectingP2 = d;
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
        Point[] ret = {connectingP1, connectingP2};
        return ret;
    }

    /**
     * Uses dot product to determine which ends of two hallways are closest.
     * @param h1 The first hallway with ends 1 and 2
     * @param h2 The second hallway with ends 1 or 2
     * @return Point[] containing [h1.(end 1 or 2), h2.(end 1 or 2)] depending on which two are closest.
     */
    private static boolean oneHall(Hallway h1, Hallway h2) {
        Point[] p = connectingPoints(h1.getEnd1(), h1.getEnd2(), h2.getEnd1(), h2.getEnd2());
        Point opposite1 = (h1.getEnd1().equals(p[0])) ? h1.getEnd2() : h1.getEnd1();
        Point opposite2 = (h2.getEnd1().equals(p[1])) ? h2.getEnd2() : h2.getEnd1();
        return (opposite1.getX() == opposite2.getX() || opposite1.getY() == opposite2.getY());
    }
}