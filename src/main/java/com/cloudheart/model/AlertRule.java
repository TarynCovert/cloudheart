package com.cloudheart.model;

public record AlertRule(
  String id,
  String serviceId,
  String metricName,
  double threshold,
  ComparisonOperator operator,
  AlertSeverity severity,
  boolean enabled
) {}
