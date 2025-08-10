package org._p1m.foodorderingsystem.config.exceptions;

import java.nio.file.AccessDeniedException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org._p1m.foodorderingsystem.config.response.dto.ApiResponse;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;

/**
 * Global exception handler for centralized exception management across the application.
 * Provides tailored responses for various exception types to ensure consistent and user-friendly error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles IllegalArgumentExceptions, typically thrown when method arguments are invalid or inappropriate.
     *
     * @param ex      the IllegalArgumentException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse> handleIllegalArgumentException(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), "Invalid argument provided.", request);
    }

    /**
     * Handles EntityNotFoundException, thrown when an entity cannot be located in the database.
     *
     * @param ex      the EntityNotFoundException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse> handleEntityNotFoundException(EntityNotFoundException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), "Entity not found.", request);
    }

    /**
     * Handles DuplicateEntityException, thrown when attempting to create an entity that already exists.
     *
     * @param ex      the DuplicateEntityException encountered.
     * @param request the current HTTP request.
     * @return @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ApiResponse> handleDuplicateEntityException(DuplicateEntityException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), "Duplicate entity detected.", request);
    }

    /**
     * Handles ConstraintViolationException, which is typically thrown when validation of input data fails.
     * This method gathers detailed information about the specific validation violations, including the fields
     * and the associated error messages, and constructs a tailored error response.
     *
     * @param ex      the ConstraintViolationException encountered, containing details about the validation errors.
     * @param request the current HTTP request that triggered the exception, used for logging and response context.
     * @return a ResponseEntity containing a standardized ApiResponse with an HTTP status of UNPROCESSABLE_ENTITY
     *         (422) and a message containing the detailed validation error information.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        StringBuilder violationMessages = new StringBuilder();

        ex.getConstraintViolations().forEach(violation -> {
            violationMessages.append(violation.getPropertyPath().toString())
                    .append(": ")
                    .append(violation.getMessage())
                    .append("; ");
        });

        if (violationMessages.isEmpty()) {
            violationMessages.append("Validation failed with no specific violations.");
        }

        return buildErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY,
                "Validation failed",
                violationMessages.toString(),
                request
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<Map<String, String>> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> errorMap = new HashMap<>();
            errorMap.put("field", error.getField());
            errorMap.put("message", error.getDefaultMessage());
            errors.add(errorMap);
        });

        HttpServletRequest httpServletRequest = ((HttpServletRequest) request.resolveReference(WebRequest.REFERENCE_REQUEST));
        assert httpServletRequest != null;
        ApiResponse errorResponse = ApiResponse.builder()
                .success(0)
                .code(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message("Validation failed")
                .data(errors)
                .meta(Map.of(
                        "method", httpServletRequest.getMethod(),
                        "endpoint", httpServletRequest.getRequestURI()
                ))
                .build();

        return new ResponseEntity<>(errorResponse, headers, status);
    }

    /**
     * Handles UnauthorizedException, typically thrown when a request is made by an unauthenticated or unauthorized user.
     *
     * @param ex      the UnauthorizedException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse with an HTTP 401 (Unauthorized) status.
     */
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), "Unauthorized", request);
    }

    /**
     * Handles UnauthorizedException, typically thrown when a request is made by an unauthenticated or unauthorized user.
     *
     * @param ex      the TokenExpiredException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse with an HTTP 401 (Unauthorized) status.
     */
    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiResponse> handleTokenExpiredException(TokenExpiredException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GONE, ex.getMessage(), "Token Expired", request);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ApiResponse> handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GONE, ex.getMessage(), "Token Expired", request);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ApiResponse> handleJwtException(JwtException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.GONE, ex.getMessage(), "Token Expired", request);
    }

    @ExceptionHandler(EntityDeletionException.class)
    public ResponseEntity<ApiResponse> handleEntityDeletionException(EntityDeletionException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage(), "Entity Deletion", request);
    }

    /**
     * Handles all uncaught exceptions, providing a generic error response.
     *
     * @param ex      the Exception encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleGlobalException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), "An unexpected error occurred.", request);
    }
    
    /**
     * Overrides the default handling of HttpMessageNotReadableException.
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  @NotNull HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request.resolveReference(WebRequest.REFERENCE_REQUEST));
        assert httpServletRequest != null;

        ApiResponse errorResponse = ApiResponse.builder()
                .success(0)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Malformed JSON request")
                .data(ex.getLocalizedMessage())
                .meta(Map.of(
                        "method", httpServletRequest.getMethod(),
                        "endpoint", httpServletRequest.getRequestURI()
                ))
                .build();

        return new ResponseEntity<>(errorResponse, headers, status);
    }
//    
    /**
     * **OVERRIDE** handleHttpRequestMethodNotSupported from ResponseEntityExceptionHandler
     * Handles HttpRequestMethodNotSupportedException, providing a custom error response.
     *
     * @param ex      the HttpRequestMethodNotSupportedException encountered.
     * @param headers the HTTP headers.
     * @param status  the HTTP status.
     * @param request the current WebRequest.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         @NotNull HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request.resolveReference(WebRequest.REFERENCE_REQUEST));
        assert httpServletRequest != null;

        ApiResponse errorResponse = ApiResponse.builder()
                .success(0)
                .code(HttpStatus.METHOD_NOT_ALLOWED.value())
                .message("HTTP method not allowed")
                .data(ex.getMessage())
                .meta(Map.of(
                        "method", httpServletRequest.getMethod(),
                        "endpoint", httpServletRequest.getRequestURI()
                ))
                .build();

        return new ResponseEntity<>(errorResponse, headers, status);
    }
    /**
     * Handles AccessDeniedException, specifically for Spring Security access denied cases.
     * This is a direct @ExceptionHandler as ResponseEntityExceptionHandler doesn't have a specific override for it.
     *
     * @param ex      the AccessDeniedException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handleAccessDeniedException(AccessDeniedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage(), "Access is denied", request);
    }
    
    /**
     * Overrides the default handling of MissingServletRequestParameterException.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          @NotNull HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        HttpServletRequest httpServletRequest = ((HttpServletRequest) request.resolveReference(WebRequest.REFERENCE_REQUEST));
        assert httpServletRequest != null;

        ApiResponse errorResponse = ApiResponse.builder()
                .success(0)
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Required request parameter is missing")
                .data(ex.getMessage())
                .meta(Map.of(
                        "method", httpServletRequest.getMethod(),
                        "endpoint", httpServletRequest.getRequestURI()
                ))
                .build();

        return new ResponseEntity<>(errorResponse, headers, status);
    }
    
    /**
     * Handles DataAccessException, typically for database access issues.
     * This is a direct @ExceptionHandler as it's a more generic Spring Data exception.
     *
     * @param ex      the DataAccessException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse> handleDataAccessException(DataAccessException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Database access error",
                ex.getMessage(),
                request
        );
    }
    
    /**
     * Handles TransactionSystemException, for issues within Spring's transaction infrastructure.
     * This is a direct @ExceptionHandler.
     *
     * @param ex      the TransactionSystemException encountered.
     * @param request the current HTTP request.
     * @return a ResponseEntity containing the standardized ApiResponse.
     */
    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<ApiResponse> handleTransactionSystemException(TransactionSystemException ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Transaction failed",
                ex.getMessage(),
                request
        );
    }

    /**
    * Handles PersistenceException, a JPA/Hibernate specific exception.
    * This is a direct @ExceptionHandler.
    *
    * @param ex      the PersistenceException encountered.
    * @param request the current HTTP request.
    * @return a ResponseEntity containing the standardized ApiResponse.
    */
   @ExceptionHandler(PersistenceException.class)
   public ResponseEntity<ApiResponse> handlePersistenceException(PersistenceException ex, HttpServletRequest request) {
       return buildErrorResponse(
               HttpStatus.INTERNAL_SERVER_ERROR,
               "Persistence error",
               ex.getMessage(),
               request
       );
   }




    /**
     * Utility method to construct a standardized error response.
     *
     * @param status  the HTTP status.
     * @param message a brief error description.
     * @param details additional details about the error.
     * @param request the HTTP request causing the error.
     * @return a ResponseEntity containing the ApiResponse.
     */
    private ResponseEntity<ApiResponse> buildErrorResponse(HttpStatus status, String message, String details, HttpServletRequest request) {
        ApiResponse errorResponse = ApiResponse.builder()
                .success(0)
                .code(status.value())
                .message(message)
                .data(details)
                .meta(Map.of(
                        "method", request.getMethod(),
                        "endpoint", request.getRequestURI()
                ))
                .build();

        return new ResponseEntity<>(errorResponse, status);
    }
}