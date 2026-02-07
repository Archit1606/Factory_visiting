package com.factoryvisit.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final Environment environment;

    public EmailService(ObjectProvider<JavaMailSender> mailSenderProvider, Environment environment) {
        this.mailSender = mailSenderProvider.getIfAvailable();
        this.environment = environment;
    }

    public boolean isMailConfigured() {
        String host = environment.getProperty("spring.mail.host");
        return mailSender != null && host != null && !host.isBlank();
    }

    public boolean sendQrEmail(String to, String fullName, String qrBase64) {
        try {
            if (mailSender == null) {
                return false;
            }
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            String from = environment.getProperty("spring.mail.from");
            if (from != null && !from.isBlank()) {
                helper.setFrom(from);
            }
            helper.setSubject("Your Factory Visit QR Code");
            helper.setText(
                    "Hello " + fullName + ",\n\n" +
                            "Thanks for registering your factory visit. Your QR code is attached below.\n\n" +
                            "Please bring it with you on the day of your visit.",
                    false
            );
            byte[] qrBytes = Base64.getDecoder().decode(qrBase64);
            helper.addInline("qr-code", new org.springframework.core.io.ByteArrayResource(qrBytes), "image/png");
            mailSender.send(message);
            return true;
        } catch (MessagingException | RuntimeException ex) {
            logger.warn("Unable to send registration email", ex);
            return false;
        }
    }
}
