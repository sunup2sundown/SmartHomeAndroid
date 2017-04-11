package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 */

public class Sensor {
    String id;
    String name;
    String houseName;
    int value;

    public Sensor(){}

    public Sensor(String id, int value){
        this.id = id;
        this.value = value;
    }

    public int getValue(){
        return value;
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
