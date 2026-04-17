package com.nsu.planningapp.planningapp.dto;

public class BuildingBlueprintDto{
    private final int id;
    private final String name;
    private final String type;

    BuildingBlueprintDto(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    //private int serviceLife;
    //private int workdayNumber;
    //private float dailyWaterConsumption;
    //private float dailyEnergyConsumption;

    // Getters
    public int getId(){ return id; }
    public String getName(){ return name; }
    public String getType() {return type; }
    //public int getServiceLife(){ return serviceLife; }
    //public int getWorkdayNumber(){ return workdayNumber; }
    //public float getDailyWaterConsumption(){ return dailyWaterConsumption; }
    //public float getDailyEnergyConsumption(){ return dailyEnergyConsumption; }
}
