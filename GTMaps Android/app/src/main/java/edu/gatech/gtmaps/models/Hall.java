package edu.gatech.gtmaps.models;

import java.util.List;

public class Hall implements BuildingSpace{

    private String name;
    private List<Room> roomsA;
    private List<Room> roomsB;
    private double length;
    private List<Junction> junctions; // add junctions1 and junctions2

    public Hall(String name, List<Room> roomsA, List<Room> roomsB, double length, List<Junction> junctions) {
        this.name = name;
        this.roomsA = roomsA;
        this.roomsB = roomsB;
        this.length = length;
        this.junctions = junctions;
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
