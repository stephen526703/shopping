package com.example.payment.config;

import com.example.common.events.PaymentProcessedEvent;
import com.example.common.events.PaymentReversedEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private Map<String,Object> baseProps(String bootstrapServers) {
        Map<String,Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, PaymentProcessedEvent> paymentProcessedPF(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new DefaultKafkaProducerFactory<>(baseProps(bootstrapServers));
    }

    @Bean
    public KafkaTemplate<String, PaymentProcessedEvent> paymentProcessedTemplate(
            ProducerFactory<String, PaymentProcessedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }

    @Bean
    public ProducerFactory<String, PaymentReversedEvent> paymentReversedPF(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new DefaultKafkaProducerFactory<>(baseProps(bootstrapServers));
    }

    @Bean
    public KafkaTemplate<String, PaymentReversedEvent> paymentReversedTemplate(
            ProducerFactory<String, PaymentReversedEvent> pf) {
        return new KafkaTemplate<>(pf);
    }
}
