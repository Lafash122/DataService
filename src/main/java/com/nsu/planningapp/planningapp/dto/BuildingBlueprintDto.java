package com.nsu.planningapp.planningapp.dto;

record BuildingBlueprintDto(int id, String name, int serviceLife,
                            int numberOfWorkdays, float dailyWaterConsumption,
                            float dailyEnergyConsumption, String blueprintType){
    // Getters
    public int getId(){ return id; }
    public String getName(){ return name; }
    public int getServiceLife(){ return serviceLife; }
    public int getWorkdayNumber(){ return numberOfWorkdays; }
    public float getDailyWaterConsumption(){ return dailyWaterConsumption; }
    public float getDailyEnergyConsumption(){ return dailyEnergyConsumption; }
    public String getType() {return blueprintType; }
}
