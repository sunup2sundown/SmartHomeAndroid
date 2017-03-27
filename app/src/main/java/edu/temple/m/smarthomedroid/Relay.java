package edu.temple.m.smarthomedroid;

/**
 * Created by Jhang Myong Ja on 3/25/2017.
 */

public class Relay {
    public static final boolean ON = true;
    public static final boolean OFF = false;
    String id;
    boolean status;

    public Relay(String id, boolean status){
        this.id = id;
        this.status = status;
    }

    public void switchOn(){
        status = ON;
    }
    public void switchOff(){
        status = OFF;
    }
}
