package org._p1m.foodorderingsystem.features.payment.controller;

import java.util.Map;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentRequestDTO;
import org._p1m.foodorderingsystem.features.payment.dto.PaymentResponseDTO;
import org._p1m.foodorderingsystem.features.payment.dto.UploadPaymentPictureRequest;
import org._p1m.foodorderingsystem.features.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;

@RestController
@RequestMapping("${api.base.path}/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPayment(@Valid @RequestBody PaymentRequestDTO paymentRequestDTO) {
        PaymentResponseDTO createdPayment = paymentService.createPayment(paymentRequestDTO);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.CREATED.value())
                .message("Payment created successfully.")
                .data(createdPayment)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }
    
    @PostMapping(
    	    value = "/{paymentId}/payment-picture",
    	    consumes = MediaType.MULTIPART_FORM_DATA_VALUE
	)
	@Operation(
	    summary = "Upload payment screenshot",
	    description = "Uploads screenshot for the payment to verify.",
	    requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
	        description = "Multipart form with image file",
	        required = true,
	        content = @Content(
	            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
	            schema = @Schema(implementation = UploadPaymentPictureRequest.class)
	        )
	    ),
	    responses = {
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "File uploaded successfully"),
	        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Failed to upload file")
	    }
	)
	public ResponseEntity<?> uploadPaymentPicture(
	    @Parameter(description = "Payment ID", required = true)
	    @PathVariable("paymentId") final Long paymentId,

	    @Parameter(hidden = true)
	    @RequestParam("file") final MultipartFile file
	) {
	    try {
	        final String fileUrl = this.paymentService.uploadPaymentPicture(paymentId, file);
	        return ResponseEntity.ok(Map.of("url", fileUrl));
	    } catch (Exception e) {
	        return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
	    }
	}
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponseDTO paymentResponseDTO = paymentService.getPaymentById(id);
        ApiResponse apiResponse = ApiResponse.builder()
                .success(1)
                .code(HttpStatus.OK.value())
                .message("Payment retrieved successfully.")
                .data(paymentResponseDTO)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}