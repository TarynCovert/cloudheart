package com.cloudheart.model;

import java.time.Instant;

public record Heartbeat(
  String serviceId,
  String instanceId,
  Instant timestamp
){}
