
package com.cloudheart.repository;

import com.cloudheart.model.Heartbeat;
import java.util.Optional;

public interface HeartbeatRepository {

    void saveLatest(Heartbeat heartbeat);

    Optional<Heartbeat> findLatest(
        String serviceId,
        String instanceId
    );
}
