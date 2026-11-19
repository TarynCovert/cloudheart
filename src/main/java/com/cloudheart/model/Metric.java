package com.cloudheart.model;

public record Metric(
    String service,
    double cpuUsage,
    double memoryUsage,
    long responseTime
) {}
