package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by M on 3/12/2017.
 */

public class User {
    private String name;
    private String sessionId;


    //getters & setters
    public String getName(){
        return name;
    }

    public void setName(){

    }

    public void setSession(String id){
        this.sessionId = id;
    }
}
