package org._p1m.foodorderingsystem.features.forgotPassword.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org._p1m.foodorderingsystem.common.service.EmailService;
import org._p1m.foodorderingsystem.common.util.ServerUtil;
import org._p1m.foodorderingsystem.features.forgotPassword.service.ForgotPasswordService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

//import static org._p1m.foodorderingsystem.common.storage.LocalStorageService.log;


@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordServiceImpl implements ForgotPasswordService {

    private final JavaMailSender javaMailSender;

    private final ServerUtil serverUtil;
    private final StringRedisTemplate redisTemplate;

    private static final long EXPIRATION_MINUTES = 15;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String fromMail;


    @Override
    public void sendResetCode(String email) {
        String resetCode = serverUtil.generateNumericCode(6);
        redisTemplate.opsForValue().set("forgot-password:" + email, resetCode, EXPIRATION_MINUTES, TimeUnit.MINUTES);

        try {
            sendEmail(email , resetCode);
        } catch (MessagingException | IOException e){
//            log.warn("Email likely sent but timed out waiting for SMTP response: {}", email, e);
//            log.error("Email sending failed to {}", email, e);
//            throw new RuntimeException("Failed to send reset code.");
        }

    }

    private void sendEmail(String email, String resetCode) throws MessagingException , IOException{
        String userName = email.split("@")[0];
        String htmlTemplate = serverUtil.loadTemplate("templates/mailTemplates/resetMail.html");
        String htmlContent =htmlTemplate
                .replace("{{username}}" , userName)
                .replace("{{code}}" , resetCode);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true , "UTF-8");

        helper.setTo(email);
        helper.setFrom(fromMail);
        helper.setSubject("Your FoodOrderingSystem Password Reset Code");

        helper.setText(htmlContent , true);
        helper.addInline("logoImage", new ClassPathResource("templates/logo/logo.png"));

//        javaMailSender.send(message);
        this.emailService.sendEmail(email, "Your FoodOrderingSystem Password Reset Code", htmlContent);
    }

    @Override
    public boolean verifyResetCode(String email, long code) {
        String key = "forgot-password:" + email;
        String storedCode = redisTemplate.opsForValue().get(key);
        boolean valid = storedCode != null && storedCode.equals(String.valueOf(code));
        if (valid) redisTemplate.delete(key);
        return valid;
    }
}
