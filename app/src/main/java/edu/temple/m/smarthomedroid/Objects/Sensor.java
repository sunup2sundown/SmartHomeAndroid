package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 */

public class Sensor {
    String session;
    int currentValue;
    String id;
    String name;
    String houseName;

    public Sensor(String sess,String house, String name, int status){
        this.houseName = house;
        this.currentValue = status;
        this.name = name;
        this.session= sess;
    }


    public int getValue(){
        return currentValue;
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
