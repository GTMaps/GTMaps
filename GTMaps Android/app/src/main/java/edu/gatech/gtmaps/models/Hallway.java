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

    public Hallway(){
        this.id = null;
        this.building_id = null;
        this.floor_id = null;

        this.name = "";
        this.roomsA = null;
        this.roomsB = null;
        this.length = 0;
        this.width = 0;
        this.junctions = null;
        this.end1 = null;
        this.end2 = null;
    }

    public Hallway(String id, String building_id, String floor_id, String name,
                   LinkedList<Room> roomsA, LinkedList<Room> roomsB, double length, double width,
                   List<Junction> junctions, Point end1, Point end2) {

        this(id, building_id, floor_id, name, length, width, end1, end2);
        this.roomsA = roomsA;
        this.roomsB = roomsB;
        this.junctions = junctions;
    }

    /* Setters */
    public void setHallwayName(String name) {this.name = name;}

    public void setHallwayId(String id){this.id = id;}

    public void setBuildingId(String building_id){
        this.building_id = building_id;
    }

    public void setFloorId(String floor_id){
        this.floor_id = floor_id;
    }

    public void setLength(Double length){
        this.length = length;
    }

    public void setWidth(Double width){
        this.width = width;
    }

    public void setRoomsA(){
        //sql call to get rooms A and assign it
    }

    public void setRoomsB(){
        //sql call to get rooms B and assign it
    }

    public void setJunctions(){
        //sql call to set junctions
    }

    public void end1(){
        //sql call to get end1
    }

    public void end2(){
        //sql call to get end2
    }

    /* getters */
    public String getName() {
        return name;
    }

    public String getId() { return id;}

    //TODO: add sql queries to initially poll for RoomsA and RoomsB from DB
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
                break;
            case SIDE_B:
                this.roomsB.addAll(rooms);
                break;
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
