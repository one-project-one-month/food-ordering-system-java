package org._p1m.foodorderingsystem.common.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org._p1m.foodorderingsystem.common.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ServerUtil {


    private final StringRedisTemplate redisTemplate;
    private final JavaMailSender javaMailSender;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Value("${jwt.secret}")
    private String SecretKey;
    private final long Access_Token_ExpireTime = 1000 * 60 * 10;

    public String getSecretKey() {
        return SecretKey;
    }

    public long getExpireTime() {
        return Access_Token_ExpireTime;
    }


    public String generateNumericCode(int length) {
        StringBuilder code = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // generates a digit from 0 to 9
            code.append(digit);
        }

        return code.toString();
    }
    public void sendCodeToEmail(final String email , final long EXPIRATION_MINUTES , final String templateName , final String redisPrefix ) {
        String resetCode = generateNumericCode(6);
        redisTemplate.opsForValue().set(redisPrefix  + email, resetCode, EXPIRATION_MINUTES, TimeUnit.MINUTES);
        try {
            sendEmail(email , resetCode , templateName);
        } catch (MessagingException | IOException e){
            e.printStackTrace();
        }
    }

    private void sendEmail(String email, String resetCode , String templateName) throws MessagingException , IOException{
        String userName = email.split("@")[0];
        String htmlTemplate = loadTemplate("templates/mailTemplates/"+ templateName +".html");
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


    public String loadTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();

        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("role", roles);
//        SecretKey key = Keys.hmacShaKeyFor(SecretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+Access_Token_ExpireTime))
                .signWith(Keys.hmacShaKeyFor(SecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        long refresh_Token_ExpireTime = 1000 * 60 * 720;
        String roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
        claims.put("role", roles);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ refresh_Token_ExpireTime))
                .signWith(Keys.hmacShaKeyFor(SecretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }



}
