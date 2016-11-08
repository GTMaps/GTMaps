package edu.gatech.gtmaps.models;

/**
 * Abstraction to ensure all "elements" of a building (hallway, room, etc.) can be polymorphisised
 * Created by Tim on 10/10/2016.
 */

public interface BuildingSpace {
    public String getId();

    public enum HallwaySide {
        SIDE_A,
        SIDE_B
    };
}
