package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class Peripheral {
    String name;
    String session;
    String id;
    String houseId;
    String type;
    public Peripheral(String name,String house,String session, String type){
        this.name=name;
        this.houseId=house;
        this.session=session;
        this.type=type;
    }
    public void setName(String name){
        this.name=name;
    }
    public void setType(String type){
        this.type=type;
    }
    public String getName(){
        return name;
    }
    public String getType(){
        return type;
    }
}
