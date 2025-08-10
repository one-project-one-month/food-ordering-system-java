package org._p1m.foodorderingsystem.features.mail;

import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {

    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequestDto request) {
        this.emailService.sendEmail(request.getTo(), request.getSubject(), request.getHtmlContent());
        return ResponseEntity.ok("Email is being sent to " + request.getTo());
    }
}
