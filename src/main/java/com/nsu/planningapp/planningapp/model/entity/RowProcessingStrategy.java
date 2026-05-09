package com.nsu.planningapp.planningapp.model.entity;

@FunctionalInterface
public interface ProcessingStrategy {
    void process(String[] parts, ImportExecutionContext context) throws Exception;
}
