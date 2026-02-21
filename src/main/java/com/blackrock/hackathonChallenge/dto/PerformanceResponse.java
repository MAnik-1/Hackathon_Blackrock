package com.blackrock.hackathonChallenge.dto;

public class PerformanceResponse {

    private long executionTimeMs;
    private long memoryUsedMb;
    private int activeThreads;

    public PerformanceResponse(long executionTimeMs,
                               long memoryUsedMb,
                               int activeThreads) {
        this.executionTimeMs = executionTimeMs;
        this.memoryUsedMb = memoryUsedMb;
        this.activeThreads = activeThreads;
    }

    public long getExecutionTimeMs() { return executionTimeMs; }
    public long getMemoryUsedMb() { return memoryUsedMb; }
    public int getActiveThreads() { return activeThreads; }
}
