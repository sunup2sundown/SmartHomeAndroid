package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 */

public class Sensor {
    String id;
    String name;
    int value;

    public Sensor(){}

    public Sensor(String id, int value){
        this.id = id;
        this.value = value;
    }
}
