package com.cloudheart.model;

import java.time.Instant;

public record Metric(
    String serviceId,
    String instanceId,
    String name,
    double value,
    String unit,
    Instant timestamp
) {}
