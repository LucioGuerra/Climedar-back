package com.climedar.notification_sv.service;

import com.climedar.notification_sv.event.NotificationSendEvent;
import lombok.AllArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;


    @KafkaListener(topics = "notification", groupId = "notification-group")
    public void sendEmail(NotificationSendEvent event) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(event.getTo());
        mailMessage.setSubject(event.getSubject());
        mailMessage.setText(event.getMessage());
        javaMailSender.send(mailMessage);
    }

}
