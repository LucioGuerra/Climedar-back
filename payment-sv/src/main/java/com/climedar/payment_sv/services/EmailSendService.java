package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.external.event.published.NotificationSendEvent;
import com.climedar.payment_sv.external.model.Consultation;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailSendService {



    public NotificationSendEvent createPaidNotificationEvent(Payment payment, File pdfFile) {
        NotificationSendEvent notificationSendEvent = new NotificationSendEvent();
        //notificationSendEvent.setTo(patient.getEmail());
        notificationSendEvent.setSubject("Consultation Notification");
        notificationSendEvent.setMessage("Thank you.");
        return notificationSendEvent;
    }
}
