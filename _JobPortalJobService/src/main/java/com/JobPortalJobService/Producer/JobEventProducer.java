package com.JobPortalJobService.Producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service 
public class JobEventProducer {
	
	public static final String TOPIC = "job-notification";
	public static final String CHAT_TOPIC = "chat-schedule";
	
	
	public final KafkaTemplate<String,JobEvent> kafkaTemplate;
	
	public JobEventProducer(KafkaTemplate<String, JobEvent> kafkaTemplate) {
		this.kafkaTemplate = kafkaTemplate;
	}
	
	public void publish(JobEvent event) {
		kafkaTemplate.send(TOPIC,event);
		
		
	}
    public void publishToChat(JobEvent event) {
        kafkaTemplate.send(CHAT_TOPIC, event);
    }
	

}
