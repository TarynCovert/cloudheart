
package com.cloudheart.repository;

import com.cloudheart.model.HeartbeatObservation;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryHeartbeatRepository
        implements HeartbeatRepository {

    private record InstanceKey(
        String serviceId,
        String instanceId
    ) {}

    private final ConcurrentHashMap<InstanceKey, HeartbeatObservation>
            observations = new ConcurrentHashMap<>();

    @Override
    public void saveLatest(HeartbeatObservation observation) {

        InstanceKey key = new InstanceKey(
            observation.heartbeat().serviceId(),
            observation.heartbeat().instanceId()
        );

        observations.compute(key, (k, existing) -> {
            if (existing == null ||
                    observation.receivedAt()
                        .isAfter(existing.receivedAt())) {
                return observation;
            }

            return existing;
        });
    }

    @Override
    public Optional<HeartbeatObservation> findLatest(
            String serviceId,
            String instanceId) {

        return Optional.ofNullable(
            observations.get(
                new InstanceKey(serviceId, instanceId)
            )
        );
    }
}
