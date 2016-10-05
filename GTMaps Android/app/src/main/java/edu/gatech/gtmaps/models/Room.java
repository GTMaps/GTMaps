package edu.gatech.gtmaps.models;

public class Room {

    private Building building;
    private Hall hall; // not in the python file. might be necessary???
    private String number;

    public Room(String number, Building building, Hall hall) {
        this.building = building;
        this.hall = hall;
        this.number = number;
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
            Room other_room = (Room) other;
            return building.equals(other_room.getBuilding())
                    && number.equals(other_room.getNumber());
        }
        return false;
    }

    public String toString() {
        return "Room number: " + number;
    }

}
