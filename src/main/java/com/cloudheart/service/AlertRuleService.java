package com.cloudheart.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cloudheart.api.dto.CreateAlertRuleRequest;
import com.cloudheart.model.AlertRule;
import com.cloudheart.repository.AlertRuleRepository;
import com.cloudheart.validation.MetricDefinitions;


@Service
public class AlertRuleService {
  private final AlertRuleRepository alertRuleRepository;
  private final ServiceRegistryService serviceRegistry;

  public AlertRuleService(AlertRuleRepository alertRepository,
      ServiceRegistryService serviceRegistry) {
    this.serviceRegistry = serviceRegistry;
    this.alertRuleRepository = alertRepository;
  }

  public AlertRule create(CreateAlertRuleRequest request) {

    if (request == null
        || request.serviceId() == null
        || request.serviceId().isBlank()
        || request.metricName() == null
        || request.metricName().isBlank()
        || !Double.isFinite(request.threshold())
        || request.operator() == null
        || request.severity() == null) {

      throw new IllegalArgumentException("Invalid alert rule");
    }

    if (!MetricDefinitions.isSupportedMetric(request.metricName())) {
      throw new IllegalArgumentException(
        "Unsupported metric: " + request.metricName()
      );
    }

    String serviceId = request.serviceId();
    if (serviceRegistry.getById(serviceId).isEmpty()) {
      throw new IllegalArgumentException("Service is not registered");
    }


    String id = UUID.randomUUID().toString();

    AlertRule rule = new AlertRule(
      id,
      request.serviceId(),
      request.metricName(),
      request.threshold(),
      request.operator(),
      request.severity(),
      request.enabled()
    );

    alertRuleRepository.save(rule);

    return rule;
  }

  public List<AlertRule> queryAlertRulesByServiceId(String serviceId) {
    return alertRuleRepository.findByServiceId(serviceId);
  }

  public List<AlertRule> queryAllRules() {
    return alertRuleRepository.findAll();
  }
}
