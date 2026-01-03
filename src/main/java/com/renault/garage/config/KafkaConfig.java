package com.renault.garage.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration Kafka pour le microservice.
 * Active uniquement si Kafka est activé.
 */
@Configuration
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${app.kafka.topic.vehicle-created:vehicle-created-topic}")
    private String vehicleCreatedTopic;

    @Bean
    public NewTopic vehicleCreatedTopic() {
        return TopicBuilder.name(vehicleCreatedTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
