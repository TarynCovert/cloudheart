
package com.cloudheart.validation;

import java.util.Map;

public final class MetricDefinitions {

    private static final Map<String, String> UNITS = Map.of(
      "cpu_usage", "percent",
      "memory_usage", "percent",
      "response_time", "ms",
      "error_count", "count"
    );

    private MetricDefinitions() {}

    public static boolean isSupportedMetric(String metricName) {
      return UNITS.containsKey(metricName);
    }

    public static boolean isValidUnit(String metricName, String unit) {
      return unit != null && unit.equals(UNITS.get(metricName));
    }
}
