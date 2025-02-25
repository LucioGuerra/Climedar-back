package com.climedar.notification_sv.event;

import lombok.Data;

import java.io.File;

@Data
public class NotificationSendEvent {

    private String to;
    private String subject;
    private String message;
    private byte[] pdfFile;

    public NotificationSendEvent() {
    }

    public NotificationSendEvent(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
