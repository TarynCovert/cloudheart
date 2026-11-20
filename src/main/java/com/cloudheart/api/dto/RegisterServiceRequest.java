package com.cloudheart.api.dto;

public record RegisterServiceRequest (
  String name,
  String description,
  String environment,
  String region
) {}
