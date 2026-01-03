package com.renault.garage.event;

import com.renault.garage.entity.Vehicle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Publisher d'événements pour les véhicules.
 * Publie un événement Kafka lors de la création d'un nouveau véhicule.
 */
@Slf4j
@Component
public class VehicleEventPublisher {

    private final Optional<KafkaTemplate<String, VehicleCreatedEvent>> kafkaTemplate;

    @Value("${app.kafka.topic.vehicle-created:vehicle-created-topic}")
    private String topicName;

    @Value("${spring.kafka.enabled:false}")
    private boolean kafkaEnabled;

    @Autowired
    public VehicleEventPublisher(Optional<KafkaTemplate<String, VehicleCreatedEvent>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Publie un événement de création de véhicule.
     */
    public void publishVehicleCreated(Vehicle vehicle) {
        if (!kafkaEnabled || kafkaTemplate.isEmpty()) {
            log.info("Kafka désactivé - Événement non publié pour le véhicule: {} {} (ID: {})",
                    vehicle.getBrand(), vehicle.getModel(), vehicle.getId());
            return;
        }

        VehicleCreatedEvent event = new VehicleCreatedEvent(
                vehicle.getId(),
                vehicle.getBrand(),
                vehicle.getModel(),
                vehicle.getManufacturingYear(),
                vehicle.getFuelType(),
                vehicle.getGarage() != null ? vehicle.getGarage().getId() : null,
                vehicle.getGarage() != null ? vehicle.getGarage().getName() : null);

        try {
            CompletableFuture<SendResult<String, VehicleCreatedEvent>> future = kafkaTemplate.get().send(topicName,
                    String.valueOf(vehicle.getId()), event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Événement VehicleCreated publié avec succès: {}", event);
                } else {
                    log.error("Erreur lors de la publication de l'événement VehicleCreated: {}", event, ex);
                }
            });
        } catch (Exception e) {
            log.error("Exception lors de la publication de l'événement VehicleCreated: {}", event, e);
        }
    }
}
