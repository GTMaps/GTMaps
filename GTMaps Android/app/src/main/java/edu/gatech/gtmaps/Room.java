package edu.gatech.gtmaps;

public class Room {

    private String number;
    private Building building;
    private Hall hall; // not in the python file. might be necessary???

    public Room(String number, Building building, Hall hall) {
        this.number = number;
        this.building = building;
        this.hall = hall;
    }

    public String getNumber() {
        return number;
    }

    public Building getBuilding() {
        return building;
    }

    public Hall getHall() {
        return hall;
    }

    public boolean equals(Object other) {
        if (other instanceof Room) {
            Room room = (Room) other;
            return building.equals(room.getBuilding()) && number.equals(room.getNumber());
        }
        return false;
    }

    public String toString() {
        return "Room number: " + number;
    }

}
