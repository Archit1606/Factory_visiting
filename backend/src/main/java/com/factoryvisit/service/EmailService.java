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
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            String from = environment.getProperty("spring.mail.from");
            if (from == null || from.isBlank()) {
                from = environment.getProperty("spring.mail.username");
            }
            if (from != null && !from.isBlank()) {
                helper.setFrom(from);
            }
            helper.setSubject("Your Factory Visit QR Code");
            byte[] qrBytes = Base64.getDecoder().decode(qrBase64);
            String safeName = fullName == null || fullName.isBlank() ? "there" : fullName;
            String htmlBody = """
                    <div style=\"font-family: 'Segoe UI', Arial, sans-serif; color: #0f172a; line-height: 1.6;\">
                      <h2 style=\"margin: 0 0 12px;\">Hello %s,</h2>
                      <p style=\"margin: 0 0 12px;\">Thanks for registering your factory visit. Your QR code is below and attached for convenience.</p>
                      <div style=\"margin: 18px 0; padding: 16px; background: #f8fafc; border-radius: 12px; display: inline-block;\">
                        <img src=\"cid:qr-code\" alt=\"Factory visit QR code\" width=\"220\" height=\"220\" style=\"display: block;\" />
                      </div>
                      <p style=\"margin: 0;\">Please bring this QR code with you on the day of your visit.</p>
                    </div>
                    """.formatted(safeName);
            helper.setText(htmlBody, true);
            helper.addInline("qr-code", new org.springframework.core.io.ByteArrayResource(qrBytes), "image/png");
            helper.addAttachment("factory-visit-qr.png",
                    new org.springframework.core.io.ByteArrayResource(qrBytes),
                    "image/png");
            mailSender.send(message);
            return true;
        } catch (MessagingException | RuntimeException ex) {
            logger.warn("Unable to send registration email", ex);
            return false;
        }
    }
}
