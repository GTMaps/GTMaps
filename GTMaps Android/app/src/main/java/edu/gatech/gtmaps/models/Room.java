package edu.gatech.gtmaps.models;

public class Room implements BuildingSpace {

    private String building_id;
    private String floor_id;
    private String hallway_id;
    private HallwaySide hallway_side;

    private String room_id;
    private Point center;
    private Point door;
    private String room_name;
    private String room_number;

    public Room(String building_id, String floor_id, String hallway_id, HallwaySide hallway_side,
                String room_id, String room_name, String room_number, Point center, Point door) {

        this.building_id = building_id;
        this.floor_id = floor_id;
        this.hallway_id = hallway_id;
        this.hallway_side = hallway_side;

        this.room_id = room_id;
        this.room_name = room_name;
        this.room_number = room_number;

        this.center = center;
        this.door = door;
    }

    /* Default constructor.  Values left as null to fast-fail on the issue of missing required values. */
    public Room(){
        this.building_id = null;
        this.floor_id = null;
        this.hallway_id = null;
        this.hallway_side = null;

        this.room_id = null;
        this.room_name = "";
        this.room_number = "";

        this.center = null;
        this.door = null;
    }

    /* setters */
    public void setBuildingId(String building_id){ this.building_id = building_id; }

    public void setFloorId(String floor_id) { this.floor_id = floor_id; }

    public void setRoomId(String room_id) { this.room_id = room_id; }

    public void setRoomName(String room_name) { this.room_name = room_name; }


    public void setHallwayId(String hallway_id) { this.hallway_id = hallway_id; }

    public void setHallwaySide(HallwaySide hallway_side) { this.hallway_side = hallway_side; }

    public void setDoor(Point door) { this.door = door; }

    public void setCenter(Point center) { this.center = center; }


    /* getters */

    public String getBuildingId() {
        return building_id;
    }

    public String getFloorId(){
        return floor_id;
    }

    public String getRoomId() {
        return room_id;
    }

    /* for contract with "Building Space" superclass */
    public String getId() { return room_id; }

    public String getRoomName() { return room_name; }

    public String getRoomNumber() { return room_number; }

    public String getHallwayId() {
        return hallway_id;
    }

    public HallwaySide getHallwaySide() { return this.hallway_side; }

    public Point getDoor(){
        return door;
    }

    public Point getCenter(){
        return center;
    }

    /* Util */
    public boolean equals(Object other) {
        if (other instanceof Room) {
            Room other_room = (Room) other;
            return building_id.equals(other_room.getBuildingId())
                    && room_id.equals(other_room.getRoomId());
        }
        return false;
    }

    public String toString() {
        return "Room: " + room_number + " (" + room_name + ")";
    }

}
