package com.ohgiraffers.team3backendadmin.infrastructure.kafka.config;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.PerformancePointSnapshotEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
public class PerformancePointKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.performance-point-group-id:team3-admin-performance-point}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, PerformancePointSnapshotEvent> performancePointSnapshotConsumerFactory() {
        JsonDeserializer<PerformancePointSnapshotEvent> deserializer =
            new JsonDeserializer<>(PerformancePointSnapshotEvent.class);
        deserializer.ignoreTypeHeaders();
        deserializer.addTrustedPackages(
            "com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto",
            "com.ohgiraffers.team3backendhr.infrastructure.kafka.dto"
        );
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PerformancePointSnapshotEvent>
    performancePointSnapshotKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PerformancePointSnapshotEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(performancePointSnapshotConsumerFactory());
        return factory;
    }

    private Map<String, Object> consumerConfig() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return config;
    }
}
