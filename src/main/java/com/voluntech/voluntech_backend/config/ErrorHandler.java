package com.voluntech.voluntech_backend.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    // 1. Captura erros de @Valid (Validation do Hibernate)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());

        return ResponseEntity.badRequest().body(erros);
    }

    // 2. Captura erros de Duplicidade ou Negócio que lançamos no Service
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> tratarErroNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}