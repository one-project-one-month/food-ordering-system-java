package org._p1m.foodorderingsystem.common.service;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
}
