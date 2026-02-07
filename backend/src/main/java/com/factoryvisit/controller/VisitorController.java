package com.factoryvisit.controller;

import com.factoryvisit.dto.VisitorRegistrationRequest;
import com.factoryvisit.dto.VisitorRegistrationResponse;
import com.factoryvisit.service.EmailService;
import com.factoryvisit.service.QrCodeService;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    private final QrCodeService qrCodeService;
    private final EmailService emailService;

    public VisitorController(QrCodeService qrCodeService, EmailService emailService) {
        this.qrCodeService = qrCodeService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<VisitorRegistrationResponse> registerVisitor(
            @Valid @RequestBody VisitorRegistrationRequest request) {
        String payload = String.join("\n",
            "Factory Visit Registration",
            "---------------------------",
            "Name: " + request.getFullName(),
            "Email: " + request.getEmail(),
            "Phone: " + request.getPhone(),
            "Company: " + request.getCompanyName(),
            "Purpose: " + request.getPurposeOfVisit(),
            "Submitted At: " + Instant.now()
        );

        String qrBase64 = qrCodeService.generateQrCodeBase64(payload, 320);
        boolean emailSent = false;
        String message = "Registration received. QR code generated.";

        if (emailService.isMailConfigured()) {
            emailSent = emailService.sendQrEmail(request.getEmail(), request.getFullName(), qrBase64);
            message = emailSent
                    ? "Registration received. QR code generated and emailed."
                    : "Registration received. QR code generated; email delivery failed.";
        }

        return ResponseEntity.ok(new VisitorRegistrationResponse(message, qrBase64, emailSent));
    }
}
