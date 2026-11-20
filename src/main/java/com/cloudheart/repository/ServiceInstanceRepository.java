package com.cloudheart.repository;

import java.util.List;
import java.util.Optional;

import com.cloudheart.model.ServiceInstance;

public interface ServiceInstanceRepository {

  boolean insertIfAbsent(ServiceInstance instance);

  Optional<ServiceInstance> findById(
      String serviceId,
      String instanceId
  );

  List<ServiceInstance> findAll();
}
