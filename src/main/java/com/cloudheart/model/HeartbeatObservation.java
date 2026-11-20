
package com.cloudheart.model;

import java.time.Instant;

public record HeartbeatObservation(
    Heartbeat heartbeat,
    Instant receivedAt
) {}
