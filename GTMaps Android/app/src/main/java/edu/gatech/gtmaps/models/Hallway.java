package edu.gatech.gtmaps.models;

import java.util.LinkedList;
import java.util.List;

public class Hallway implements BuildingSpace{

    private String name;
    private int floor;
    private Point end1;
    private Point end2;
    private List<Room> roomsA;
    private List<Room> roomsB;
    private double length;
    private double width;
    private List<Junction> junctions; // add junctions1 and junctions2

    public Hallway(String name, double length, double width, int floor, Point end1, Point end2) {
        this.name = name;
        this.roomsA = new LinkedList<Room>();
        this.roomsB = new LinkedList<Room>();
        this.length = length;
        this.width = width;
        this.junctions = new LinkedList<Junction>();
        this.floor = floor;
        this.end1 = end1;
        this.end2 = end2;
    }

    public Hallway(String name, LinkedList<Room> roomsA, LinkedList<Room> roomsB, double length, double width, List<Junction> junctions, int floor, Point end1, Point end2) {
        this(name, length, width, floor, end1, end2);
        this.roomsA = roomsA;
        this.roomsB = roomsB;
        this.junctions = junctions;
    }

    public String getName() {
        return name;
    }

    public List<Room> getRoomsA() { return roomsA; }

    public List<Room> getRoomsB() { return roomsB; }

    public double getLength() {
        return length;
    }

    public Point getEnd1() {
        return end1;
    }

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
     * @param side Character 'A' or 'B' determining which list to add to.
     * @param rooms A list of rooms which is added into the roomsA or roomsB list.
     */
    public void addRooms(char side, List<Room> rooms) throws IllegalAccessException {
        if (rooms.isEmpty()) { return; }
        switch (Character.toUpperCase(side)) {
            case 'A':
                this.roomsA.addAll(rooms);
            case 'B':
                this.roomsB.addAll(rooms);
            default:
                throw new IllegalArgumentException("Character " + side + " not supported.");
        }
    }

    public String toString() {
        String hallStr = "Hallway ";

        if (name != null) {
            hallStr += String.format("%s: ",name);
        }

        hallStr += "contains rooms ";

        //vStringify rooms on side A of the hallway
        for (Room room : roomsA) {
            hallStr += room.toString() + ", ";
        }
        hallStr = hallStr.substring(0, hallStr.length() - 2); //omit extra comma and space

        hallStr += " and ";

        // Stringify rooms on side B of the hallway
        for (Room room : roomsB) {
            hallStr += room.toString() + ", ";
        }
        hallStr = hallStr.substring(0, hallStr.length() - 2); //omit extra comma and space

        return hallStr;
    }

}
