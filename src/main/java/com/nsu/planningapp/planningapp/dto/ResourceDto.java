package com.nsu.planningapp.planningapp.dto;

public class ResourceDto{
    private final int id;
    private final String name;
    //private id unit;

    ResourceDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId(){ return id; }
    public String getName(){ return name; }
    //public id getUnit(){ return unit; }
}
