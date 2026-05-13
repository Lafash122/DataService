package com.nsu.planningapp.planningapp.dto;

public record TransportBlueprintDto(int id, String name, String blueprintType) {
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getBlueprintType() {return blueprintType; }
}
