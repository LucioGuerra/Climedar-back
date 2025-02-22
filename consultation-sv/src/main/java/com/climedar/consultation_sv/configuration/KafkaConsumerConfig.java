package com.climedar.consultation_sv.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${KAFKA_URL}")
    private String kafkaUrl;

    @Value("${KAFKA_USER}")
    private String kafkaUser;

    @Value("${KAFKA_PASSWORD}")
    private String kafkaPassword;

    @Value("${KAFKA_TRUSTSTORE_LOCATION}")
    private String truststoreLocation;

    @Value("${KAFKA_TRUSTSTORE_PASSWORD}")
    private String truststorePassword;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaUrl);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "consultation-sv");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        config.put(JsonDeserializer.TYPE_MAPPINGS, "com.climedar.payment_sv.external.event.published.ConfirmedPayEvent:com.climedar.consultation_sv.external.event.received.ConfirmedPayEvent, " +
                "com.climedar.doctor_sv.external.event.published.ShiftCanceledEvent:com.climedar.consultation_sv.external.event.received.ShiftCanceledEvent");

        config.put("security.protocol", "SASL_SSL");
        config.put("sasl.mechanism", "PLAIN");
        config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
                + kafkaUser + "\" password=\"" + kafkaPassword + "\";");
        config.put("ssl.truststore.location", truststoreLocation);
        config.put("ssl.truststore.password", truststorePassword);

        return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
