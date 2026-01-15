package com.JobPortalAuthService.Producer;



import java.util.List;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.JobPortalAuthService.Repo.OutboxRepository;
/*
@Service
public class UserEventProducer {

    private static final String TOPIC = "user-created-topic";

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;

    
    private final OutboxRepository outboxRepository;

    public UserEventProducer(KafkaTemplate<String, UserCreatedEvent> kafkaTemplate, OutboxRepository outboxRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
    }

//    public void sendUserCreatedEvent(UserCreatedEvent event) {
//        kafkaTemplate.send(TOPIC, event);
//   }
    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() {

        List<OutboxEvent> events =
                outboxRepository.findTop20ByStatus("NEW");

        for (OutboxEvent event : events) {
            try {
                kafkaTemplate.send(TOPIC, event.getPayload());
                event.setStatus("SENT");
            } catch (Exception ex) {
                event.setStatus("FAILED");
            }
            outboxRepository.save(event);
        }
    }
    
    */
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class UserEventProducer {

    private static final String TOPIC = "user-created-topic";

    private final KafkaTemplate<String, UserCreatedEvent> kafkaTemplate;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    public UserEventProducer(
            KafkaTemplate<String, UserCreatedEvent> kafkaTemplate,
            OutboxRepository outboxRepository,
            ObjectMapper objectMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.outboxRepository = outboxRepository;
        this.objectMapper = objectMapper;
    }

    @Scheduled(fixedDelay = 5000)
    public void publishOutboxEvents() {

        List<OutboxEvent> events =
                outboxRepository.findTop20ByStatus("NEW");

        for (OutboxEvent outbox : events) {
            try {
                // ✅ Convert JSON string → POJO
                UserCreatedEvent event =
                        objectMapper.readValue(
                                outbox.getPayload(),
                                UserCreatedEvent.class
                        );

                kafkaTemplate.send(TOPIC, event);

                outbox.setStatus("SENT");

            } catch (Exception ex) {
                outbox.setStatus("FAILED");
            }

            outboxRepository.save(outbox);
        }
    }
}


