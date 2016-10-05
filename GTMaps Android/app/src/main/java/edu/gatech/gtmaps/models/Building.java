package edu.gatech.gtmaps.models;

import java.util.List;

public abstract class Building {

    private String name;
    private List<Hall> halls;
    private List<Room> rooms; // not sure if necessary since "halls" contain all the rooms.
                              // but will need this list when we choose room from a building (drop down list)

    public Building(String name, List<Hall> halls) {
        this.name = name;
        this.halls = halls;
    }

    public String getName() {
        return name;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String toString() {
        return name;
    }

}
