package com.ohgiraffers.team3backendadmin.infrastructure.kafka.config;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceItemStandardSnapshotEvent;
import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.MaintenanceLogSnapshotEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class MaintenanceReferenceKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public ProducerFactory<String, MaintenanceLogSnapshotEvent> maintenanceLogSnapshotProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, MaintenanceLogSnapshotEvent> maintenanceLogSnapshotKafkaTemplate() {
        return new KafkaTemplate<>(maintenanceLogSnapshotProducerFactory());
    }

    @Bean
    public ProducerFactory<String, MaintenanceItemStandardSnapshotEvent> maintenanceItemStandardSnapshotProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        config.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, MaintenanceItemStandardSnapshotEvent> maintenanceItemStandardSnapshotKafkaTemplate() {
        return new KafkaTemplate<>(maintenanceItemStandardSnapshotProducerFactory());
    }
}
