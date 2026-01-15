package com.JobPortalNotificationService.Config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.JobPortalNotificationService.DTOS.JobEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
@EnableKafka
public class KafkaConfig {

	@Bean
	public ConsumerFactory<String, JobEvent> consumerFactory() {

	    JsonDeserializer<JobEvent> deserializer =
	            new JsonDeserializer<>(JobEvent.class);

	    // Let Spring’s ObjectMapper handle JavaTimeModule
	    deserializer.addTrustedPackages("*");

	    Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9093");
	    props.put(ConsumerConfig.GROUP_ID_CONFIG, "notification-service-group");
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

	    return new DefaultKafkaConsumerFactory<>(
	            props,
	            new StringDeserializer(),
	            deserializer
	    );
	}


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, JobEvent>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, JobEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
