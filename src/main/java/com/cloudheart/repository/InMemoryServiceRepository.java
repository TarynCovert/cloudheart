
package com.cloudheart.repository;

import com.cloudheart.model.MonitoredService;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryServiceRepository implements ServiceRepository {

    private final Map<String, MonitoredService> services =
            new ConcurrentHashMap<>();

    @Override
    public synchronized boolean insertIfAbsent(MonitoredService service) {

        if (services.containsKey(service.id())) {
            return false;
        }

        for (MonitoredService existing : services.values()) {
            if (existing.name().equalsIgnoreCase(service.name())
                    && existing.environment().equalsIgnoreCase(service.environment())
                    && existing.region().equalsIgnoreCase(service.region())) {
                return false;
            }
        }

        services.put(service.id(), service);
        return true;
    }

    @Override
    public Optional<MonitoredService> findById(String id) {
        return Optional.ofNullable(services.get(id));
    }

    @Override
    public List<MonitoredService> findAll() {
        return List.copyOf(services.values());
    }
}
