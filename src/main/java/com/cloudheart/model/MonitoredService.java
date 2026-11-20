package com.cloudheart.model;

public record MonitoredService(
  String id,
  String name,
  String description,
  String environment,
  String region
) {}


