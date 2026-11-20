
package com.cloudheart.repository;

import com.cloudheart.model.AlertRule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAlertRuleRepository
        implements AlertRuleRepository {

    private final Map<String, AlertRule> rules =
            new ConcurrentHashMap<>();

    @Override
    public void save(AlertRule rule) {
        rules.put(rule.id(), rule);
    }

    @Override
    public Optional<AlertRule> findById(String id) {
        return Optional.ofNullable(rules.get(id));
    }

    @Override
    public List<AlertRule> findByServiceId(String serviceId) {
        return rules.values().stream()
                .filter(rule -> rule.serviceId().equals(serviceId))
                .toList();
    }

    @Override
    public List<AlertRule> findAll() {
        return List.copyOf(rules.values());
    }
}
