
package com.cloudheart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cloudheart.model.Alert;
import com.cloudheart.model.AlertStatus;
import com.cloudheart.model.RuleViolation;
import com.cloudheart.repository.AlertRepository;

@Service
public class AlertManagementService {
  private final AlertRepository alertRepository;

  public AlertManagementService(AlertRepository alertRepository) {
    this.alertRepository = alertRepository;
  }

  public List<Alert> processViolations(List<RuleViolation> violations) {
    List<Alert> alertsAdded = new ArrayList<>();

    for (RuleViolation violation : violations) {

      String id = UUID.randomUUID().toString();

      String message = String.format(
        "%s triggered an alert: observed %.2f, threshold %.2f",
        violation.metricName(),
        violation.observedValue(),
        violation.threshold()
      );

      Alert alert = new Alert(
        id,
        violation.ruleId(),
        violation.serviceId(),
        violation.instanceId(),
        violation.severity(),
        message,
        violation.timestamp(),
        AlertStatus.ACTIVE
      );

      if (alertRepository.insertIfNoOpenAlert(alert)) {
        alertsAdded.add(alert);
      }
    }

    return alertsAdded;
  }

  public Alert findById(String id) {
    Optional<Alert> result = alertRepository.findById(id);

    if (result.isEmpty()) {
      throw new IllegalArgumentException("Alert not found");
    }

    return result.get();
  }

  public void acknowledge(String id) {
    Alert alert = findById(id);

    if (alert.status() == AlertStatus.ACKNOWLEDGED) {
      return;
    }

    if (alert.status() != AlertStatus.ACTIVE) {
      throw new IllegalStateException(
        "Only active alerts can be acknowledged"
      );
    }

    Alert updated = withStatus(
      alert,
      AlertStatus.ACKNOWLEDGED
    );

    if (!alertRepository.updateIfStatus(updated, AlertStatus.ACTIVE)) {
      throw new IllegalStateException(
        "Alert status changed concurrently"
      );
    }
  }

  public void resolve(String id) {
    Alert alert = findById(id);

    if (alert.status() == AlertStatus.RESOLVED) {
      return;
    }

    Alert updated = withStatus(
      alert,
      AlertStatus.RESOLVED
    );

    if (!alertRepository.updateIfStatus(updated, alert.status())) {
      throw new IllegalStateException(
        "Alert status changed concurrently"
      );
    }
  }

  public List<Alert> findByServiceId(String serviceId) {
    return alertRepository.findByServiceId(serviceId);
  }

  public List<Alert>findByServiceAndInstanceId(
    String serviceId,
    String instanceId) {

    return alertRepository.findByServiceAndInstanceId(
      serviceId,
      instanceId
    );
  }

  public List<Alert> findAll() {
    return alertRepository.findAll();
  }

  private Alert withStatus(Alert alert, AlertStatus status) {
    return new Alert(
      alert.id(),
      alert.ruleId(),
      alert.serviceId(),
      alert.instanceId(),
      alert.severity(),
      alert.message(),
      alert.triggeredAt(),
      status
    );
  }
}
