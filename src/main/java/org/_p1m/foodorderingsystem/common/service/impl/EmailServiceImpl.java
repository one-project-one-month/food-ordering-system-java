package org._p1m.foodorderingsystem.common.service.impl;

import org._p1m.foodorderingsystem.common.event.EmailEvent;
import org._p1m.foodorderingsystem.common.service.EmailService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final ApplicationEventPublisher eventPublisher;

    public EmailServiceImpl(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void sendEmail(final String toEmail, final String subject, final String body) {
        this.eventPublisher.publishEvent(new EmailEvent(this, toEmail, subject, body));
    }
}
