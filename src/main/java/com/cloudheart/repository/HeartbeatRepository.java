
package com.cloudheart.repository;

import com.cloudheart.model.HeartbeatObservation;

import java.util.Optional;

public interface HeartbeatRepository {

  void saveLatest(HeartbeatObservation observation);

  Optional<HeartbeatObservation> findLatest(
    String serviceId,
    String instanceId
  );
}
