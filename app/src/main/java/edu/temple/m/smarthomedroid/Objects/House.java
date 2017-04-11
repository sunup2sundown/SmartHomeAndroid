package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 *
 * Edited: 3/27/2017 10:45am by Matt White
 */

public class House {
    String id;
    String name;

    public House(){}

    public House(String id, String name){
        this.id = id;
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

}
