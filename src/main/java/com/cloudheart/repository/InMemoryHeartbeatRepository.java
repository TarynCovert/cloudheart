
package com.cloudheart.repository;

import com.cloudheart.model.Heartbeat;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHeartbeatRepository
        implements HeartbeatRepository {

    private record InstanceKey(
        String serviceId,
        String instanceId
    ) {}

    private final Map<InstanceKey, Heartbeat> heartbeats =
            new ConcurrentHashMap<>();

    @Override
    public void saveLatest(Heartbeat heartbeat) {

        InstanceKey key = new InstanceKey(
            heartbeat.serviceId(),
            heartbeat.instanceId()
        );

        heartbeats.compute(key, (k, existing) -> {
            if (existing == null ||
                    heartbeat.timestamp().isAfter(existing.timestamp())) {
                return heartbeat;
            }

            return existing;
        });
    }

    @Override
    public Optional<Heartbeat> findLatest(
            String serviceId,
            String instanceId) {

        InstanceKey key = new InstanceKey(
            serviceId,
            instanceId
        );

        return Optional.ofNullable(heartbeats.get(key));
    }
}
