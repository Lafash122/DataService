package com.nsu.planningapp.planningapp.dto;

public record BuildingDto(int id, String settlementName, String blueprintName, String blueprintType) {
    public int getId(){ return id; }
    public String getSettlementName(){ return settlementName; }
    public String getBlueprintName(){ return blueprintName; }
    public String getBlueprintType(){ return blueprintType; }
}
