package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.LoginRequestDTO;
import com.voluntech.voluntech_backend.service.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired  
    private AuthService authService;

    @PostMapping("/login")
    @Tag(name = "Login", description = "Endpoints para Login de ONG e Voluntário")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO dto) {
        
        Object usuario = authService.autenticar(dto.email(), dto.senha());
        return ResponseEntity.ok(usuario);
    }
}