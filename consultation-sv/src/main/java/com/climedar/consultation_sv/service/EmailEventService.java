package com.climedar.consultation_sv.service;

import com.climedar.consultation_sv.entity.Consultation;
import com.climedar.consultation_sv.external.event.published.NotificationSendEvent;
import com.climedar.consultation_sv.external.model.patient.Patient;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
public class EmailEventService {


    public NotificationSendEvent createConsultationNotificationEvent(Consultation consultation, Patient patient) {
        NotificationSendEvent notificationSendEvent = new NotificationSendEvent();
        notificationSendEvent.setTo(patient.getEmail());
        notificationSendEvent.setSubject("Consultation Notification");
        notificationSendEvent.setMessage("Thank you.");
        return notificationSendEvent;
    }
}
