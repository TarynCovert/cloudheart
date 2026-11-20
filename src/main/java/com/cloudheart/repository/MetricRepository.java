package com.cloudheart.repository;

import com.cloudheart.model.Metric;

import java.util.List;

public interface MetricRepository {
  void save(Metric metric);

  //Finds all metric data for a particular service
  List<Metric> findAllByServiceId(String serviceId);

  //Finds all metric data for a particular service
  List<Metric> findAllByServiceAndInstanceId(String serviceId, String InstanceId);
}
