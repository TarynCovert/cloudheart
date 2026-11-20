package com.cloudheart.service;

import com.cloudheart.model.Heartbeat;
import com.cloudheart.model.HeartbeatObservation;
import com.cloudheart.repository.HeartbeatRepository;
import com.cloudheart.repository.ServiceInstanceRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HeartbeatService {
  private final HeartbeatRepository heartbeatRepository;
  private final ServiceRegistryService serviceRegistry;
  private final ServiceInstanceRepository instanceRepository;

  public HeartbeatService(
    HeartbeatRepository heartbeatRepository,
    ServiceRegistryService serviceRegistry,
    ServiceInstanceRepository instanceRepository
  ) {
    this.heartbeatRepository = heartbeatRepository;
    this.serviceRegistry = serviceRegistry;
    this.instanceRepository = instanceRepository;
  }

  public void registerHeartbeat(Heartbeat heartbeat) {

    if (heartbeat == null
      || heartbeat.serviceId() == null
      || heartbeat.serviceId().isBlank()
      || heartbeat.instanceId() == null
      || heartbeat.instanceId().isBlank()
      || heartbeat.timestamp() == null) {

      throw new IllegalArgumentException("Invalid heartbeat");
    }

    String serviceId = heartbeat.serviceId();
    if (serviceRegistry.getById(serviceId).isEmpty()) {
      throw new IllegalArgumentException("Service is not registered");
    }

    String instanceId = heartbeat.instanceId();
    if (instanceRepository.findById(serviceId, instanceId).isEmpty()) {
      throw new IllegalArgumentException("Instance is not registered");
    }

    HeartbeatObservation observation = new HeartbeatObservation(heartbeat, Instant.now());

    heartbeatRepository.saveLatest(observation);
  }
}