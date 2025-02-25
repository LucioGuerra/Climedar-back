package com.climedar.payment_sv.external.event.published;

import lombok.Data;

import java.io.File;

@Data
public class NotificationSendEvent {
    private String to;
    private String subject;
    private String message;
    private File pdfFile;
}
