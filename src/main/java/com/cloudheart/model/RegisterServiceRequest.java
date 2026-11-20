package com.cloudheart.model;

public record RegisterServiceRequest(
    String name,
    String description,
    String environment,
    String region
) {}
