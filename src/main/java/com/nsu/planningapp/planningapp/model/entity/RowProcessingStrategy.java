package com.nsu.planningapp.planningapp.model.entity;

@Deprecated
@FunctionalInterface
public interface RowProcessingStrategy {
    void processRow(String[] parts, ImportExecutionContext context) throws Exception;
}
