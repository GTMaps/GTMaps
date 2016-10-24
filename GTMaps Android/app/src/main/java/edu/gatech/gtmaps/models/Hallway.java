package edu.gatech.gtmaps.models;

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

    public Hallway(String name, List<Room> roomsA, List<Room> roomsB, double length, double width, List<Junction> junctions, int floor, Point end1, Point end2) {
        this.name = name;
        this.roomsA = roomsA;
        this.roomsB = roomsB;
        this.length = length;
        this.width = width;
        this.junctions = junctions;
        this.floor = floor;
        this.end1 = end1;
        this.end2 = end2;
    }

    public String getName() {
        return name;
    }

    public List<Room> getRoomsA() {
        return roomsA;
    }

    public List<Room> getRoomsB() {
        return roomsB;
    }

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
