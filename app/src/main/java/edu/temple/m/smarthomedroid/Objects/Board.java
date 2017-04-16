package edu.temple.m.smarthomedroid.Objects;

/**
 * Created by Jhang Myong Ja on 4/15/2017.
 */

public class Board {
    private String name;
    private String houseName;

    public String getName() {
        return name;
    }

    public Board(String name, String houseName){
        this.name = name;
        this.houseName = houseName;
    }
}
