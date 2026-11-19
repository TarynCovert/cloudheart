package com.cloudheart.service;

import com.cloudheart.model.Metric;
import org.springframework.stereotype.Service;

@Service
public class MetricService {

    public void ingest(Metric metric) {
        System.out.println("Received metric from: " + metric.service());
    }
}