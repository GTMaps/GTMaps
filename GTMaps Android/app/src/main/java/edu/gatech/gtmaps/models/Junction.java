package edu.gatech.gtmaps.models;

import java.util.List;

public class Junction implements BuildingSpace {

    private List<Hallway> hallways;
    private Point coordinate;

    public Junction(List<Hallway> hallways, Point coordinate) {
        this.hallways = hallways;
        this.coordinate = coordinate;
    }

    public List<Hallway> getHallways() {
        return hallways;
    }

    public Point getCoordinate() {
        return coordinate;
    }

    public String toString() {
        String hallStr = "Connection between hallways ";
        for (Hallway hallway : hallways) {
            hallStr += hallway.toString() + ", ";
        }
        return hallStr.substring(0, hallStr.length() - 2);
    }

    @Override
    public String getName() {
        return this.toString();
    }
}
