package org._p1m.foodorderingsystem.common.event;

import org.springframework.context.ApplicationEvent;

public class EmailEvent extends ApplicationEvent {

    private final String toEmail;
    private final String subject;
    private final String body;

    public EmailEvent(final Object source, final String toEmail, final String subject, final String body) {
        super(source);
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public String getBody() {
        return body;
    }
}