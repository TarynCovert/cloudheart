
package com.cloudheart.validation;

import com.cloudheart.model.Metric;
import org.springframework.stereotype.Component;

@Component
public class MetricValidator {

  public void validate(Metric metric) {

    if (metric == null
        || metric.serviceId() == null
        || metric.serviceId().isBlank()
        || metric.instanceId() == null
        || metric.instanceId().isBlank()
        || metric.name() == null
        || metric.name().isBlank()
        || metric.unit() == null
        || metric.unit().isBlank()
        || metric.timestamp() == null
        || !Double.isFinite(metric.value())) {

      throw new IllegalArgumentException("Invalid metric");
    }

    if (!MetricDefinitions.isSupportedMetric(metric.name())) {
      throw new IllegalArgumentException(
        "Unsupported metric: " + metric.name()
      );
    }

    if (!MetricDefinitions.isValidUnit(
        metric.name(), metric.unit())) {

      throw new IllegalArgumentException(
        "Invalid unit for metric: " + metric.name()
      );
    }
  }
}
