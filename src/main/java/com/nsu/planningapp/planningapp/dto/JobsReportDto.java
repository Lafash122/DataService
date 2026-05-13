package com.nsu.planningapp.planningapp.dto;

public record JobsReportDto(int totalJobs, int totalHigherEduJobs) {
    public int getTotalJobs(){ return totalJobs; }
    public int getTotalHigherEduJobs(){ return totalHigherEduJobs; }
}
