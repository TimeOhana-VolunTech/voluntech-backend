package com.voluntech.voluntech_backend.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandler {

    // 1. Erros de Validação (@Valid) - STATUS 400
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream()
                .map(erro -> erro.getField() + ": " + erro.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(erros);
    }

    // 2. Erro de Recurso Não Encontrado (ID inexistente) - STATUS 404
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 3. Captura duplicidade no Banco de Dados (Email/CPF/CNPJ repetido) - STATUS 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity tratarErro409(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Conflito de dados: E-mail, CPF ou CNPJ já cadastrado no sistema.");
    }

    // 3. Captura erros genéricos de negócio - STATUS 400
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> tratarErroNegocio(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}