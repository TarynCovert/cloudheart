
package com.cloudheart.service;

import com.cloudheart.model.HeartbeatObservation;
import com.cloudheart.model.ServiceStatus;
import com.cloudheart.repository.HeartbeatRepository;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
public class LivenessService {

  private final HeartbeatRepository heartbeatRepository;
  private static final Duration TIMEOUT = Duration.ofSeconds(90);

  public LivenessService(HeartbeatRepository heartbeatRepository) {
    this.heartbeatRepository = heartbeatRepository;
  }

  public ServiceStatus evaluate(String serviceId, String instanceId) {
    Optional<HeartbeatObservation> latest = heartbeatRepository.findLatest(serviceId, instanceId);

    if (latest.isEmpty()) {
      return ServiceStatus.UNKNOWN;
    }

    Instant lastSeen = latest.get().receivedAt();
    Instant now = Instant.now();

    Duration elapsed = Duration.between(lastSeen, now);

    if (elapsed.compareTo(TIMEOUT) <= 0) {
      return ServiceStatus.HEALTHY;
    }

    return ServiceStatus.UNHEALTHY;
  }
}
