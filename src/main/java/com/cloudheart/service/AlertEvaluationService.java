
package com.cloudheart.service;

import com.cloudheart.model.AlertRule;
import com.cloudheart.model.ComparisonOperator;
import com.cloudheart.model.Metric;
import com.cloudheart.model.RuleViolation;
import com.cloudheart.repository.AlertRuleRepository;
import com.cloudheart.validation.MetricValidator;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class AlertEvaluationService {
  private final AlertRuleRepository ruleRepository;
  private final MetricValidator metricValidator;

  public AlertEvaluationService(AlertRuleRepository ruleRepository,
    MetricValidator metricValidator
  ) {
    this.ruleRepository = ruleRepository;
    this.metricValidator = metricValidator;
  }

  public List<RuleViolation> evaluate(Metric metric) {
    metricValidator.validate(metric);

    List<RuleViolation> results = new ArrayList<>();
    List<AlertRule> ruleList = ruleRepository.findByServiceId(metric.serviceId());


    for (AlertRule rule : ruleList) {
      if (!rule.enabled()) {
        continue;
      }

      if (!rule.metricName().equals(metric.name())) {
        continue;
      }

      boolean triggered = match(rule.operator(), metric.value(), rule.threshold());

      if (triggered) {
        RuleViolation violation =
          new RuleViolation(
            rule.id(),
            metric.serviceId(),
            metric.instanceId(),
            metric.name(),
            metric.value(),
            rule.threshold(),
            rule.severity(),
            metric.timestamp()
          );

        results.add (violation);
      }
    }

    return results;
  }


  private boolean match(ComparisonOperator operator, double value, double threshold) {

    return switch (operator) {
      case GREATER_THAN -> value > threshold;
      case GREATER_THAN_OR_EQUAL -> value >= threshold;
      case LESS_THAN -> value < threshold;
      case LESS_THAN_OR_EQUAL -> value <= threshold;
    };
  }
}



