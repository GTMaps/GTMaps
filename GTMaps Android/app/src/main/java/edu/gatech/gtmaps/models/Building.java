package edu.gatech.gtmaps.models;

import java.util.List;

public abstract class Building {

    private String name;
    private List<Hall> halls;
    private List<Room> rooms;
    private Hall entrance;

    public Building(String name, List<Hall> halls, List<Room> rooms, Hall entrance) {
        this.name = name;
        this.halls = halls;
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

    public Hall getEntrance() {
        return entrance;
    }

    public String toString() {
        return name;
    }

}
