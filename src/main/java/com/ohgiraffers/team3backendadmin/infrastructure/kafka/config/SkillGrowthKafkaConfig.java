package com.ohgiraffers.team3backendadmin.infrastructure.kafka.config;

import com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto.SkillGrowthCalculatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class SkillGrowthKafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id:team3-admin-skill-growth}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, SkillGrowthCalculatedEvent> skillGrowthCalculatedConsumerFactory() {
        JsonDeserializer<SkillGrowthCalculatedEvent> deserializer =
            new JsonDeserializer<>(SkillGrowthCalculatedEvent.class);
        deserializer.ignoreTypeHeaders();
        deserializer.addTrustedPackages(
            "com.ohgiraffers.team3backendadmin.infrastructure.kafka.dto",
            "com.ohgiraffers.team3backendbatch.infrastructure.kafka.dto"
        );
        return new DefaultKafkaConsumerFactory<>(consumerConfig(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SkillGrowthCalculatedEvent>
    skillGrowthCalculatedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SkillGrowthCalculatedEvent> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(skillGrowthCalculatedConsumerFactory());
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
