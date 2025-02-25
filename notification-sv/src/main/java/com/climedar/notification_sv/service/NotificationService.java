package com.climedar.notification_sv.service;

import com.climedar.notification_sv.event.NotificationSendEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.eclipse.angus.mail.iap.ByteArray;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class NotificationService {

    private final JavaMailSender javaMailSender;


    @KafkaListener(topics = "notification", groupId = "notification-group")
    public void sendEmail(NotificationSendEvent event) throws MessagingException {
        if (event.getPdfFile() == null) {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(event.getTo());
            mailMessage.setSubject(event.getSubject());
            mailMessage.setText(event.getMessage());
            javaMailSender.send(mailMessage);
        } else {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(event.getTo());
            helper.setSubject(event.getSubject());
            helper.setText(event.getMessage(), false);
            helper.addAttachment("recibo-de-pago.pdf", new ByteArrayResource(event.getPdfFile()));
            javaMailSender.send(message);
        }
    }



}
