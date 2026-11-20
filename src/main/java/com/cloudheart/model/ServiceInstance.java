
package com.cloudheart.model;

import java.time.Instant;

public record ServiceInstance(
    String id,
    String serviceId,
    String hostname,
    Instant registeredAt
) {}
