package com.factoryvisit.controller;

import com.factoryvisit.dto.VisitorRegistrationRequest;
import com.factoryvisit.dto.VisitorRegistrationResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/visitors")
public class VisitorController {

    @PostMapping("/register")
    public ResponseEntity<VisitorRegistrationResponse> registerVisitor(
            @Valid @RequestBody VisitorRegistrationRequest request) {
        // Placeholder response; real registration logic added after database integration.
        return ResponseEntity.ok(new VisitorRegistrationResponse(
                "Registration received. QR code processing will follow."
        ));
    }
}
