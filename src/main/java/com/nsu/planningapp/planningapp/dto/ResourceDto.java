package com.nsu.planningapp.planningapp.dto;

//  NOT USED

public record ResourceDto(int id, String name, String unit){
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getUnit(){ return unit; }
}
