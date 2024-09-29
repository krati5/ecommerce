package org.example.paymentservice.services;

import org.example.paymentservice.dtos.EmailNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final String FROM_EMAIL = "support@ekart.com";


    public void sendEmail(String fromEmail, String recipientEmail, String subject, String body, String topic){
        // Create email notification message
//        fromEmail="kratiy.5@gmail.com";
        recipientEmail="kratiy.5@gmail.com";
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setTo(recipientEmail);
        emailNotification.setFrom(fromEmail);
        emailNotification.setSubject(subject);
        emailNotification.setBody(body);

        // Send email notification to Kafka topic
        kafkaTemplate.send(topic, emailNotification);
    }

    public void sendEmail(String recipientEmail, String subject, String body, String topic){
        sendEmail(FROM_EMAIL, recipientEmail, subject, body, topic);
    }
}
