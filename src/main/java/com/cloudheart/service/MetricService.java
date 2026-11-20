package com.cloudheart.service;

import org.springframework.stereotype.Service;

import java.util.List;

import com.cloudheart.model.Metric;
import com.cloudheart.repository.MetricRepository;
import com.cloudheart.validation.MetricValidator;

@Service
public class MetricService {

  private final MetricRepository metricRepository;
  private final ServiceRegistryService serviceRegistry;
  private final MetricValidator metricValidator;

  public MetricService(
      MetricRepository metricRepository,
      ServiceRegistryService serviceRegistry,
      MetricValidator metricValidator
    ) {
    this.metricRepository = metricRepository;
    this.serviceRegistry = serviceRegistry;
    this.metricValidator = metricValidator;
  }

  public void ingest(Metric metric) {

    metricValidator.validate(metric);

    String serviceId = metric.serviceId();
    if (serviceRegistry.getById(serviceId).isEmpty()) {
      throw new IllegalArgumentException("Service is not registered");
    }

    metricRepository.save(metric);
  }

  public List<Metric> queryMetricsByServiceId(String serviceId) {
    return metricRepository.findAllByServiceId(serviceId);
  }

  public List<Metric> queryMetricsByInstanceId(String serviceId, String instanceId) {
    return metricRepository.findAllByServiceAndInstanceId(serviceId, instanceId);
  }
}

