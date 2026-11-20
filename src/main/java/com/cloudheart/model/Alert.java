package com.cloudheart.model;

import java.time.Instant;

public record Alert(
    String id,
    String ruleId,
    String serviceId,
    String instanceId,
    AlertSeverity severity,
    String message,
    Instant triggeredAt,
    AlertStatus status
) {}
