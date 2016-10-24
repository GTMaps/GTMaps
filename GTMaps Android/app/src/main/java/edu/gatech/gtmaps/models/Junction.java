package edu.gatech.gtmaps.models;

import java.util.List;

public class Junction {

    private List<Hall> halls;

    public Junction(List<Hall> halls) {
        this.halls = halls;
    }

    public List<Hall> getHalls() {
        return halls;
    }

    public String toString() {
        String hallStr = "Connection between halls ";
        for (Hall hall : halls) {
            hallStr += hall.toString() + ", ";
        }
        return hallStr.substring(0, hallStr.length() - 2);
    }

}
