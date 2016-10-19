package edu.gatech.gtmaps.models;

import java.util.List;

public abstract class Building {

    private String name;
    private List<Hallway> hallways;
    private List<Room> rooms; // not sure if necessary since "hallways" contain all the rooms.
                              // but will need this list when we choose room from a building (drop down list)

    public Building(String name, List<Hallway> hallways) {
        this.name = name;
        this.hallways = hallways;
    }

    public String getName() {
        return name;
    }

    public List<Hallway> getHallways() {
        return hallways;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public String toString() {
        return name;
    }

}
