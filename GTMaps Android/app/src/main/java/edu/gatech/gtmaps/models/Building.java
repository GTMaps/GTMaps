package edu.gatech.gtmaps.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.gatech.gtmaps.DBHelper;

public class Building {

    private String name;
    private String building_id;
    private String image_url;
    private List<Room> rooms;
    private List<Hallway> entrances;
    private DBHelper dbHelper;

    public Building(){
        this.building_id = null;
        this.name = null;
        this.image_url = "default_building.png";
        this.entrances = null;
    }

    public Building(String building_id, String name, String image_url) {
        this.building_id = building_id;
        this.name = name;
        this.image_url = image_url;
    }

    public void setBuildingId(String building_id){
        this.building_id = building_id;
    }

    public void setBuildingName(String building_name){
        this.name = building_name;
    }

    public String getId() {return this.building_id;}

    //TODO return R.drawable resource id instead of image_url
    public void setBuildingImage(String image_url){
        this.image_url = image_url;
    }

    public String getName() {
        return name;
}

    public List<Hallway> getEntrances() {
        return dbHelper.getBuildingEntrances(building_id);
    }

    public List<Room> getRooms() {
        return dbHelper.getRoomsPerBuilding(building_id);
    }

    public String toString() {
        return name;
    }
}
