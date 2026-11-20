
package com.cloudheart.repository;

import com.cloudheart.model.AlertRule;

import java.util.List;
import java.util.Optional;

public interface AlertRuleRepository {

  void save(AlertRule rule);

  Optional<AlertRule> findById(String id);

  List<AlertRule> findByServiceId(String serviceId);

  List<AlertRule> findAll();
}
