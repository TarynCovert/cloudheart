
package com.cloudheart.repository;

import com.cloudheart.model.Alert;
import com.cloudheart.model.AlertStatus;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryAlertRepository implements AlertRepository {

    private final ConcurrentHashMap<String, Alert> alerts =
            new ConcurrentHashMap<>();

    @Override
    public void save(Alert alert) {
        alerts.put(alert.id(), alert);
    }

    @Override
    public Optional<Alert> findById(String id) {
        return Optional.ofNullable(alerts.get(id));
    }

    @Override
    public List<Alert> findByServiceId(String serviceId) {
        return alerts.values().stream()
                .filter(alert ->
                    alert.serviceId().equals(serviceId))
                .toList();
    }

    @Override
    public List<Alert> findByServiceAndInstanceId(
            String serviceId,
            String instanceId) {

        return alerts.values().stream()
                .filter(alert ->
                    alert.serviceId().equals(serviceId)
                    && alert.instanceId().equals(instanceId))
                .toList();
    }

    @Override
    public List<Alert> findAll() {
        return List.copyOf(alerts.values());
    }

    @Override
    public synchronized boolean insertIfNoOpenAlert(Alert alert) {

        if (alerts.containsKey(alert.id())) {
            return false;
        }

        boolean alreadyOpen = alerts.values().stream()
                .anyMatch(existing ->
                    existing.ruleId().equals(alert.ruleId())
                    && existing.serviceId().equals(alert.serviceId())
                    && existing.instanceId().equals(alert.instanceId())
                    && existing.status() != AlertStatus.RESOLVED
                );

        if (alreadyOpen) {
            return false;
        }

        alerts.put(alert.id(), alert);
        return true;
    }

    @Override
    public synchronized boolean updateIfStatus(
            Alert updated,
            AlertStatus expectedStatus) {

        Alert existing = alerts.get(updated.id());

        if (existing == null
                || existing.status() != expectedStatus) {
            return false;
        }

        alerts.put(updated.id(), updated);
        return true;
    }
}
