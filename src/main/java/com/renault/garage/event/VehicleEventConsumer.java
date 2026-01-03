package com.renault.garage.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Consumer d'événements pour les véhicules.
 * Consomme les événements de création de véhicules publiés sur Kafka.
 */
@Component
public class VehicleEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(VehicleEventConsumer.class);

    /**
     * Consomme les événements de création de véhicule.
     * 
     * @param event L'événement de création de véhicule
     */
    @KafkaListener(topics = "${app.kafka.topic.vehicle-created:vehicle-created-topic}",
            groupId = "${spring.kafka.consumer.group-id:renault-garage-group}",
            autoStartup = "${spring.kafka.enabled:false}")
    public void consumeVehicleCreatedEvent(VehicleCreatedEvent event) {
        logger.info("=== Événement VehicleCreated reçu ===");
        logger.info("  ID Véhicule: {}", event.getVehicleId());
        logger.info("  Marque: {}", event.getBrand());
        logger.info("  Modèle: {}", event.getModel());
        logger.info("  Année: {}", event.getManufacturingYear());
        logger.info("  Carburant: {}", event.getFuelType());
        logger.info("  Garage ID: {}", event.getGarageId());
        logger.info("  Garage: {}", event.getGarageName());
        logger.info("  Date création: {}", event.getCreatedAt());
        logger.info("=====================================");

        processVehicleCreatedEvent(event);
    }

    /**
     * Traitement métier de l'événement.
     */
    private void processVehicleCreatedEvent(VehicleCreatedEvent event) {
        // Exemple de traitement: log des véhicules électriques
        if (event.getFuelType() != null &&
                (event.getFuelType().name().equals("ELECTRIC") ||
                        event.getFuelType().name().equals("HYBRID"))) {
            logger.info(">>> Nouveau véhicule écologique enregistré: {} {} <<<",
                    event.getBrand(), event.getModel());
        }
    }
}
