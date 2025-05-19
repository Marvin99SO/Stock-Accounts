package com.example.stockaccounts.config;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, Exception exception){
        logger.error(message, exception);
        ErrorResponse errorResponse = new ErrorResponse(
                status.value(),
                message,
                exception.getClass().getSimpleName(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorResponse> handleIOException(IOException exception) {
        return buildErrorResponse(
                HttpStatus.BAD_GATEWAY,
                "Erro ao comunicar com o serviço externo",
                exception
        );
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException exception) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                exception
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        String message = "Erro de integridade de dados";
        if (exception.getMessage() != null){
            if(exception.getMessage().contains("unique") || exception.getMessage().contains("duplicate")){
                message = "Erro de duplicidade";
            }
        }
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                message,
                exception);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFound(NoSuchElementException exception) {
        String message = "Recurso não encontrado";
        if(!exception.getMessage().isBlank()) message = exception.getMessage();
        return buildErrorResponse(
                HttpStatus.NOT_FOUND,
                message,
                exception
        );
    }

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException exception) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Requisição inválida",
                exception
        );
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException.Forbidden exception) {
        return buildErrorResponse(
                HttpStatus.FORBIDDEN,
                "Operação não autorizada",
                exception
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exception) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor",
                exception
        );
    }
}
