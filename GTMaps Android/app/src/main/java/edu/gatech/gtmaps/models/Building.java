package edu.gatech.gtmaps.models;

import java.util.List;

public abstract class Building {

    private String name;
    private List<Hallway> hallways;
    private List<Room> rooms;
    private Hallway entrance;

    public Building(String name, List<Hallway> halls, List<Room> rooms, Hallway entrance) {
        this.name = name;
        this.hallways = halls;
        this.rooms = rooms;
        this.entrance = entrance;
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

    public Hallway getEntrance() {
        return entrance;
    }

    public String toString() {
        return name;
    }

}
