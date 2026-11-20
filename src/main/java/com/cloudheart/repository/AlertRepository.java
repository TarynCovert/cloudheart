
package com.cloudheart.repository;

import com.cloudheart.model.Alert;
import com.cloudheart.model.AlertStatus;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
  void save(Alert alert);

  Optional<Alert> findById(String id);

  List<Alert> findByServiceId(String serviceId);
  List<Alert> findByServiceAndInstanceId(
    String serviceId,
    String instanceId
  );
  List<Alert> findAll();

  boolean insertIfNoOpenAlert(Alert alert);

  boolean updateIfStatus(
    Alert updated,
    AlertStatus expectedStatus
  );
}
