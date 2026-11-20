
package com.cloudheart.repository;

import com.cloudheart.model.ServiceInstance;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryServiceInstanceRepository
        implements ServiceInstanceRepository {

    private record InstanceKey(
        String serviceId,
        String instanceId
    ) {}

    private final ConcurrentHashMap<InstanceKey, ServiceInstance>
            instances = new ConcurrentHashMap<>();

    @Override
    public boolean insertIfAbsent(ServiceInstance instance) {

        InstanceKey key = new InstanceKey(
            instance.serviceId(),
            instance.id()
        );

        return instances.putIfAbsent(key, instance) == null;
    }

    @Override
    public Optional<ServiceInstance> findById(
            String serviceId,
            String instanceId) {

        InstanceKey key = new InstanceKey(
            serviceId,
            instanceId
        );

        return Optional.ofNullable(instances.get(key));
    }

    @Override
    public List<ServiceInstance> findAll() {
        return List.copyOf(instances.values());
    }
}
