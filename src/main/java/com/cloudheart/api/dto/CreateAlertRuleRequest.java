package com.cloudheart.api.dto;

import com.cloudheart.model.ComparisonOperator;
import com.cloudheart.model.AlertSeverity;

public record CreateAlertRuleRequest(
    String serviceId,
    String metricName,
    double threshold,
    ComparisonOperator operator,
    AlertSeverity severity,
    boolean enabled
) {}