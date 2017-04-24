package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class Board {
    private String name;
    private String houseName;
    private String session;

    public String getName() {
        return name;
    }

    public Board(String name, String houseName, String session){
        this.name = name;
        this.houseName = houseName;
        this.session = session;
    }
    public void setname(String name){
        this.name=name;
    }
}
