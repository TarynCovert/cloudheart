
package com.cloudheart.model;

import java.time.Instant;

public record RuleViolation(
    String ruleId,
    String serviceId,
    String instanceId,
    String metricName,
    double observedValue,
    double threshold,
    AlertSeverity severity,
    Instant timestamp
) {}
