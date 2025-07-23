package org._p1m.foodorderingsystem.features.payment.controller;

import jakarta.validation.Valid;
import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentDTO;
import org._p1m.foodorderingsystem.features.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPayment(@Valid @RequestBody PaymentDTO paymentDTO) {
        PaymentDTO createdPayment = paymentService.createPayment(paymentDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Payment created successfully.")
                .data(createdPayment)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentById(@PathVariable Long id) {
        PaymentDTO paymentDTO = paymentService.getPaymentById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Payment retrieved successfully.")
                .data(paymentDTO)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
