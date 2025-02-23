package com.climedar.notification_sv.event;

import lombok.Data;

@Data
public class NotificationSendEvent {

    private String to;
    private String subject;
    private String message;

    public NotificationSendEvent(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }
}
