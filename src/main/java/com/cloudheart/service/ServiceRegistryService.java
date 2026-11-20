
package com.cloudheart.service;

import com.cloudheart.model.MonitoredService;
import com.cloudheart.api.dto.RegisterServiceRequest;
import com.cloudheart.repository.ServiceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServiceRegistryService {

    private final ServiceRepository repository;

    public ServiceRegistryService(ServiceRepository repository) {
        this.repository = repository;
    }

    public MonitoredService register(RegisterServiceRequest request) {
      if (request == null
          || request.name() == null
          || request.name().isBlank()
          || request.environment() == null
          || request.environment().isBlank()
          || request.region() == null
          || request.region().isBlank()) {

        throw new IllegalArgumentException(
          "Name, environment, and region are required"
        );
      }

      String id = UUID.randomUUID().toString();

      MonitoredService service = new MonitoredService(
        id,
        request.name(),
        request.description(),
        request.environment(),
        request.region()
      );

      if (!repository.insertIfAbsent(service)) {
        throw new IllegalStateException(
            "Service already registered or ID already exists"
        );
      }

    return service;
  }

  public Optional<MonitoredService> getById(String id) {
    return repository.findById(id);
  }

  public List<MonitoredService> getAll() {
    return repository.findAll();
  }
}
