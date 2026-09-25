package com.altis.library.common.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorDTO> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Requisição Inválida",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<StandardErrorDTO> handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Regra de Negócio Violada",
                ex.getMessage(),
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        List<StandardErrorDTO.FieldErrorDTO> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(f -> new StandardErrorDTO.FieldErrorDTO(f.getField(), f.getDefaultMessage()))
                .toList();

        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Erro de validação nos campos",
                "Um ou mais campos estão inválidos",
                request.getRequestURI(),
                fieldErrors
        );

        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardErrorDTO> handleGenericException(Exception ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        StandardErrorDTO error = new StandardErrorDTO(
                Instant.now(),
                status.value(),
                "Erro Interno do Servidor",
                "Ocorreu um erro inesperado no sistema. Contate o administrador.",
                request.getRequestURI(),
                null
        );

        return ResponseEntity.status(status).body(error);
    }
}