package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by M on 4/23/2017.
 */

public class Camera {
    String id;
    String name;
    String houseName;
    String session;
    final String TAG = "Camera Object";

    public Camera(){

    }
    public Camera(String sess,String house, String name){
        this.houseName = house;
        this.name = name;
        this.session= sess;
    }

    // Peripheral interface functions
    public void setName(String name){
        this.name = name;
    }

    public void setHouseName(String houseName){
        this.houseName = houseName;
    }

    public String getName(){
        return this.name;
    }

    public String getHouseName(){
        return this.houseName;
    }
}
