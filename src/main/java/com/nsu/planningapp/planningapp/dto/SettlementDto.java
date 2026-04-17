package com.nsu.planningapp.planningapp.dto;

public class SettlementDto{
    private final int id;
    private final String name;

    SettlementDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId(){ return id; }
    public String getName(){ return name; }
}

