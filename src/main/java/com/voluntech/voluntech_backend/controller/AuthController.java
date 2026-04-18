package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired  
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciais) {
        try {
            String email = credenciais.get("email");
            String senha = credenciais.get("senha");

            Object usuario = authService.autenticar(email, senha);
            
            // Retorna o objeto (ONG ou Voluntário) para o Angular
            return ResponseEntity.ok(usuario);
            
        } catch (RuntimeException e) {
            // Se der erro (senha errada ou e-mail inexistente), retorna 401
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}