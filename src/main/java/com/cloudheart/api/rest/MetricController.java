package com.cloudheart.api.rest;

import com.cloudheart.model.Metric;
import com.cloudheart.service.MetricService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {

  private final MetricService metricService;

  public MetricController(MetricService metricService) {
    this. metricService = metricService;
  }

  @PostMapping
  public ResponseEntity<Void> ingest(@RequestBody Metric metric) {
    metricService.ingest(metric);
    return ResponseEntity.accepted().build();
  }

}
