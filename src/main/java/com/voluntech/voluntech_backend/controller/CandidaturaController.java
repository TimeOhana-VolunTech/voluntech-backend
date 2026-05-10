package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.CandidatoExibicaoDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaRequestDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaResponseDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaStatusUpdateDTO;
import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.service.CandidaturaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/candidaturas")
@Tag(name = "Candidaturas", description = "Endpoints para gerenciamento de inscrições em projetos")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @PostMapping
    @Operation(summary = "Cadastrar nova candidatura", description = "Permite que um voluntário se inscreva em um projeto")
    public ResponseEntity<Void> criar(@RequestBody @Valid CandidaturaRequestDTO request) {
        candidaturaService.salvar(request); // Você precisará adaptar o service para receber o DTO
        return ResponseEntity.status(HttpStatus.CREATED).build();
   }

   @PatchMapping("/{id}/status")
   @Operation(summary = "Atualizar status da candidatura", description = "Permite que a ONG aprove ou recuse um candidato")
    public ResponseEntity<Void> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid CandidaturaStatusUpdateDTO statusUpdate) {
        
        candidaturaService.atualizarStatus(id, statusUpdate.novoStatus());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/voluntario/{voluntarioId}")
    @Operation(summary = "Listar candidaturas por voluntário", description = "Retorna todas as inscrições de um voluntário específico")
    public ResponseEntity<List<CandidaturaResponseDTO>> listarPorVoluntario(@PathVariable Long voluntarioId) {
        return ResponseEntity.ok(candidaturaService.listarCandidaturasDoVoluntario(voluntarioId));
    }

    @GetMapping("/projeto/{projetoId}")
    @Operation(summary = "Listar candidatos por projeto", description = "Retorna todos os voluntários inscritos em um projeto específico (Visão da ONG)")
    public ResponseEntity<List<CandidatoExibicaoDTO>> listarCandidatosPorProjeto(@PathVariable Long projetoId) {
        return ResponseEntity.ok(candidaturaService.listarCandidatosPorProjeto(projetoId));
    }

}