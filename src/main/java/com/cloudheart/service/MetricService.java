package com.cloudheart.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.cloudheart.model.Metric;
import com.cloudheart.model.RuleViolation;
import com.cloudheart.repository.MetricRepository;
import com.cloudheart.validation.MetricValidator;

@Service
public class MetricService {

  private final MetricRepository metricRepository;
  private final ServiceRegistryService serviceRegistry;
  private final MetricValidator metricValidator;
  private final AlertEvaluationService alertEvaluationService;
  private final AlertManagementService alertManagementService;

  public MetricService(
      MetricRepository metricRepository,
      ServiceRegistryService serviceRegistry,
      MetricValidator metricValidator,
      AlertEvaluationService evaluateAlert,
      AlertManagementService managementAlert
    ) {
    this.metricRepository = metricRepository;
    this.serviceRegistry = serviceRegistry;
    this.metricValidator = metricValidator;
    this.alertEvaluationService = evaluateAlert;
    this.alertManagementService = managementAlert;
  }

  public void ingest(Metric metric) {

    metricValidator.validate(metric);

    String serviceId = metric.serviceId();
    if (serviceRegistry.getById(serviceId).isEmpty()) {
      throw new IllegalArgumentException("Service is not registered");
    }

    metricRepository.save(metric);

    List<RuleViolation> violations = alertEvaluationService.evaluate(metric);
    alertManagementService.processViolations(violations);
  }

  public List<Metric> queryMetricsByServiceId(String serviceId) {
    return metricRepository.findAllByServiceId(serviceId);
  }

  public List<Metric> queryMetricsByInstanceId(String serviceId, String instanceId) {
    return metricRepository.findAllByServiceAndInstanceId(serviceId, instanceId);
  }
}

