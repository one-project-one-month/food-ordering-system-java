package org._p1m.foodorderingsystem.common.listener;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org._p1m.foodorderingsystem.common.event.EmailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmailEventListener {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Async
    @EventListener
    public void handleEmailEvent(final EmailEvent event) {
        try {
            final MimeMessage mimeMessage = mailSender.createMimeMessage();
            final MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(event.getToEmail());
            helper.setSubject(event.getSubject());
            helper.setText(event.getBody(), true);

            mailSender.send(mimeMessage);
            log.info("MIME Email sent successfully to {}", event.getToEmail());
        } catch (Exception e) {
            log.error("Failed to send MIME email to {}", event.getToEmail(), e);
        }
    }
}
