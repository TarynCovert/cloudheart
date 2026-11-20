
package com.cloudheart.repository;

import com.cloudheart.model.MonitoredService;

import java.util.List;
import java.util.Optional;

public interface ServiceRepository {

  boolean insertIfAbsent(MonitoredService service);

  Optional<MonitoredService> findById(String id);

  List<MonitoredService> findAll();
}
