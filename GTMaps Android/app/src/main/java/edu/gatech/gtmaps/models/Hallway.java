package edu.gatech.gtmaps.models;

import java.util.LinkedList;
import java.util.List;

public class Hallway implements BuildingSpace{

    private String id;
    private String building_id;
    private String floor_id;

    private String name;
    private Point end1;
    private Point end2;
    private List<Room> roomsA;
    private List<Room> roomsB;
    private double length;
    private double width;
    private List<Junction> junctions; // add junctions1 and junctions2

    public Hallway(String id, String building_id, String floor_id, String name, double length, double width, Point end1, Point end2) {
        this.id = id;
        this.building_id = building_id;
        this.floor_id = floor_id;

        this.name = name;
        this.roomsA = new LinkedList<Room>();
        this.roomsB = new LinkedList<Room>();
        this.length = length;
        this.width = width;
        this.junctions = new LinkedList<Junction>();
        this.end1 = end1;
        this.end2 = end2;
    }

    public Hallway(String id, String building_id, String floor_id, String name,
                   LinkedList<Room> roomsA, LinkedList<Room> roomsB, double length, double width,
                   List<Junction> junctions, Point end1, Point end2) {

        this(id, building_id, floor_id, name, length, width, end1, end2);
        this.roomsA = roomsA;
        this.roomsB = roomsB;
        this.junctions = junctions;
    }

    /* getters */
    public String getName() {
        return name;
    }

    public String getId() { return id;}

    public List<Room> getRoomsA() { return roomsA; }

    public List<Room> getRoomsB() { return roomsB; }

    public double getLength() {
        return length;
    }

    public Point getEnd1() { return end1; }

    public Point getEnd2() {
        return end2;
    }

    public List<Junction> getJunctions() {
        return junctions;
    }

    public void addJunction(Junction junction) {
        junctions.add(junction);
    }



    /**
     * Adds a set of rooms to one side of this hallway.
     * @param side Enum side 'A' or 'B' determining which list to add to.
     * @param rooms A list of rooms which is added into the roomsA or roomsB list.
     */
    public void addRooms(HallwaySide side, List<Room> rooms) throws IllegalArgumentException {
        if (rooms.isEmpty()) { return; }
        switch (side) {
            case SIDE_A:
                this.roomsA.addAll(rooms);
            case SIDE_B:
                this.roomsB.addAll(rooms);
            default:
                throw new IllegalArgumentException("Character " + side + " not supported.");
        }
    }

    public String toString() {
        String hallway_str = "Hallway ";

        if (name != null) {
            hallway_str += String.format("%s: ",name);
        }
        hallway_str += "contains rooms ";

        // Stringify rooms on side A of the hallway
        for (Room room : roomsA) {
            hallway_str += room.toString() + ", ";
        }
        hallway_str = hallway_str.substring(0, hallway_str.length() - 2); //omit extra comma and space

        hallway_str += " and ";

        // Stringify rooms on side B of the hallway
        for (Room room : roomsB) {
            hallway_str += room.toString() + ", ";
        }
        hallway_str = hallway_str.substring(0, hallway_str.length() - 2); //omit extra comma and space

        return hallway_str;
    }

}
