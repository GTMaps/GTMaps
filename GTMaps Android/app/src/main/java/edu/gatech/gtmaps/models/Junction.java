package edu.gatech.gtmaps.models;

import java.util.List;

public class Junction {

    private List<Hallway> hallways;

    public Junction(List<Hallway> hallways) {
        this.hallways = hallways;
        for (Hallway hallway : hallways) {
            hallway.addJunction(this);
        }
    }

    public List<Hallway> getHallways() {
        return hallways;
    }

    public String toString() {
        String hallStr = "Connection between hallways ";
        for (Hallway hallway : hallways) {
            hallStr += hallway.toString() + ", ";
        }
        return hallStr.substring(0, hallStr.length() - 2);
    }

}
