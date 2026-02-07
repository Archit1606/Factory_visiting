package com.factoryvisit.dto;

public class VisitorRegistrationResponse {

    private String message;
    private String qrCodeBase64;
    private boolean emailSent;

    public VisitorRegistrationResponse(String message, String qrCodeBase64, boolean emailSent) {
        this.message = message;
        this.qrCodeBase64 = qrCodeBase64;
        this.emailSent = emailSent;
    }

    public String getMessage() {
        return message;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public boolean isEmailSent() {
        return emailSent;
    }
}
