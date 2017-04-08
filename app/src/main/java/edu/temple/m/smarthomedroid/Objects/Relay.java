package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 *
 * Edited: 3/27/2017 10:45am by Matt White
 */

public class Relay implements Peripheral {
    String id;
    String name;
    String houseName;
    int currentValue;

    public Relay(){}

    public Relay(String id, String name, int status){
        this.id = id;
        this.currentValue = status;
        this.name = name;
    }

    public void switchOn(){
        currentValue = 1;
    }
    public void switchOff(){
        currentValue = 0;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setHouseName(String houseName){
        this.houseName = houseName;
    }

    public boolean getStatus(){
        if(currentValue == 1){
            return true;
        }
        else{
            return false;
        }
    }

    public String getName(){
        return this.name;
    }

    public String getHouseName(){
        return this.houseName;
    }

}
