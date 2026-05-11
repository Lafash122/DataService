package com.nsu.planningapp.planningapp.model.entity;

public record BuildingPair(String settlementName, String blueprintName) {
    @Override
    public String toString() {
        return settlementName + ";" + blueprintName;
    }
}
