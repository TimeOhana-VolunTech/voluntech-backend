package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.service.ProjetoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projetos")
@Tag(name = "Projetos da ONG", description = "Endpoints para gerenciamento dos Projetos oferecido por uma ONG")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> criar(@Valid @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO novoProjeto = projetoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProjeto);
    }

    @GetMapping("/ong/{ongId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarPorOng(@PathVariable Long ongId) {
        List<ProjetoResponseDTO> projetos = projetoService.listarPorOng(ongId);
        return ResponseEntity.ok(projetos);
    }
}