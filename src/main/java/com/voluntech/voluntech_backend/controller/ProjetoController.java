package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import com.voluntech.voluntech_backend.service.ProjetoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Criar novo projeto", description = "Cadastra uma nova oportunidade de voluntariado vinculada a uma ONG.")
    @ApiResponse(responseCode = "201", description = "Projeto criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou data retroativa")
    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> criar(@Valid @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO novoProjeto = projetoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProjeto);
    }

    @Operation(summary = "Listar projetos por ONG", description = "Retorna todos os projetos cadastrados por uma ONG específica, ordenados pelos mais recentes.")
    @GetMapping("/ong/{ongId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarPorOng(@PathVariable Long ongId) {
        List<ProjetoResponseDTO> projetos = projetoService.listarPorOng(ongId);
        return ResponseEntity.ok(projetos);
    }

    @Operation(summary = "Buscar projeto por ID", description = "Retorna os detalhes de um projeto específico.")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        // Note que você já tem o método buscarPorId no Service (ou precisa criá-lo)
        ProjetoResponseDTO projeto = projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @Operation(summary = "Atualizar projeto", description = "Edita as informações de um projeto existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO atualizado = projetoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Alterar status do projeto", description = "Permite pausar, ativar ou finalizar um projeto (Inativação de Oportunidade).")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjetoResponseDTO> alterarStatus( @PathVariable Long id, @RequestParam StatusProjeto novoStatus) {
        ProjetoResponseDTO atualizado = projetoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir projeto", description = "Remove um projeto do sistema. Regra: Apenas se não houver voluntários inscritos.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        projetoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Explorar oportunidades", description = "Lista todos os projetos ativos com filtros opcionais para voluntários.")
    @GetMapping("/explorar")
    public ResponseEntity<List<ProjetoResponseDTO>> explorar(
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Modalidade modalidade,
            @RequestParam(required = false) String termo) {
        
        List<ProjetoResponseDTO> oportunidades = projetoService.explorarProjetos(categoria, modalidade, termo);
        return ResponseEntity.ok(oportunidades);
    }

}