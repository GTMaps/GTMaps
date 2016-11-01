package edu.gatech.gtmaps.models;

public class Room implements BuildingSpace {

    private Building building;
    private Hallway hall;
    private int floor;
    private Point center;
    private Point door;
    private char side;
    private String number;

    public Room(String number, Hallway hall, int floor, Point center, Point door, char side) {
        //this.building = building;
        this.hall = hall;
        this.number = number;
        this.floor = floor;
        this.center = center;
        this.door = door;
        this.side = side;
        this.number = number;
    }

    public void setHall(Hallway h) {
        this.hall = h;
    }

    public String getNumber() {
        return number;
    }

    public Building getBuilding() {
        return building;
    }

    public Hallway getHall() {
        return hall;
    }

    public char getSide() { return this.side; }

    public boolean equals(Object other) {
        if (other instanceof Room) {
            Room other_room = (Room) other;
            return building.equals(other_room.getBuilding())
                    && number.equals(other_room.getNumber());
        }
        return false;
    }

    public String getName() {return number;}

    public String toString() {
        return "Room number: " + number;
    }

}
