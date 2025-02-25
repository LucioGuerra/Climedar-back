package com.climedar.payment_sv.services;

import com.climedar.payment_sv.entity.payment.Payment;
import com.climedar.payment_sv.external.event.published.NotificationSendEvent;
import com.climedar.payment_sv.external.model.Consultation;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class EmailSendService {



    public NotificationSendEvent createPaidNotificationEvent(Payment payment, Consultation consultation, byte[] pdf) {
        NotificationSendEvent notificationSendEvent = new NotificationSendEvent();
        notificationSendEvent.setTo(consultation.getPatient().getEmail());
        notificationSendEvent.setSubject("Notificación de Consulta");
        notificationSendEvent.setMessage("Gracias por su pago. Adjunto encontrará el documento en PDF correspondiente a su consulta.");
        notificationSendEvent.setPdfFile(pdf);

        return notificationSendEvent;
    }
}
