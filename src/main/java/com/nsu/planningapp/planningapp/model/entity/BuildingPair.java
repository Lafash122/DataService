package com.nsu.planningapp.planningapp.model.entity;

public class BuildingPair {
    private final String settlementName;
    private final String blueprintName;

    public BuildingPair(String settlementName, String blueprintName) {
        this.settlementName = settlementName;
        this.blueprintName = blueprintName;
    }

    public String getBlueprintName() {
        return blueprintName;
    }
    public String getSettlementName() {
        return settlementName;
    }

    @Override
    public String toString() {
            return settlementName + ";" + blueprintName;
    }
}
