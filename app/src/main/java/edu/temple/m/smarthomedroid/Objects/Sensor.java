package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 */

public class Sensor implements Peripheral {
    String id;
    String name;
    String houseName;
    int value;

    public Sensor(){}

    public Sensor(String id, String name, int value){
        this.id = id;
        this.name = name;
        this.value = value;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHouseName(String houseName){
        this.houseName = houseName;
    }

    public String getName(){return name;}

    public String getHouseName(){
        return houseName;
    }
    public int getValue(){
        return value;
    }
}
