package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
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
@Tag(name = "Vagas dos Projetos da ONG", description = "Endpoints para gerenciamento das vagas dos Projetos oferecido por uma ONG")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @Operation(summary = "Criar nova vaga de um projeto", description = "Cadastra uma nova oportunidade de voluntariado vinculada a uma ONG.")
    @ApiResponse(responseCode = "201", description = "Vaga criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou data retroativa")
    @PostMapping
    public ResponseEntity<ProjetoResponseDTO> criar(@Valid @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO novoProjeto = projetoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoProjeto);
    }

    @Operation(summary = "Listar vagas por ONG", description = "Retorna todas as vagas cadastrados por uma ONG específica, ordenados pelos mais recentes.")
    @GetMapping("/ong/{ongId}")
    public ResponseEntity<List<ProjetoResponseDTO>> listarPorOng(@PathVariable Long ongId) {
        List<ProjetoResponseDTO> projetos = projetoService.listarPorOng(ongId);
        return ResponseEntity.ok(projetos);
    }

    @Operation(summary = "Buscar vaga do projeto por ID", description = "Retorna os detalhes de uma vaga específica.")
    @GetMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProjetoResponseDTO projeto = projetoService.buscarPorId(id);
        return ResponseEntity.ok(projeto);
    }

    @Operation(summary = "Atualizar vaga do projeto", description = "Edita as informações de uma vaga existente.")
    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ProjetoRequestDTO dto) {
        ProjetoResponseDTO atualizado = projetoService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Alterar status da vaga", description = "Permite pausar, ativar ou finalizar uma vaga (Inativação de Oportunidade).")
    @PatchMapping("/{id}/status")
    public ResponseEntity<ProjetoResponseDTO> alterarStatus( @PathVariable Long id, @RequestParam StatusProjeto novoStatus) {
        ProjetoResponseDTO atualizado = projetoService.alterarStatus(id, novoStatus);
        return ResponseEntity.ok(atualizado);
    }

    @Operation(summary = "Excluir vaga do projeto", description = "Remove uma vaga do sistema. Regra: Apenas se não houver voluntários inscritos.")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable Long id) {
        try {
            projetoService.excluir(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            // Retorna o erro 400 (Bad Request) com a mensagem personalizada
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(summary = "Explorar oportunidades", description = "Lista todas as vagas ativas com filtros opcionais para voluntários. Opcionalmente oculta vagas em que o voluntário já se inscreveu.")
    @GetMapping("/explorar")
    public ResponseEntity<List<ProjetoResponseDTO>> explorar(
            @RequestParam(required = false) Categoria categoria,
            @RequestParam(required = false) Modalidade modalidade,
            @RequestParam(required = false) String termo,
            @RequestParam(required = false) Long voluntarioId) {
        
        List<ProjetoResponseDTO> oportunidades = projetoService.explorarProjetos(categoria, modalidade, termo, voluntarioId);
        return ResponseEntity.ok(oportunidades);
    }

    @Operation(summary = "Candidatar-se a uma vaga", description = "Vincula o voluntário logado a uma oportunidade.")
    @PostMapping("/{projetoId}/candidatar")
    public ResponseEntity<String> candidatar(@PathVariable Long projetoId, @RequestParam Long voluntarioId) {
        projetoService.candidatar(projetoId, voluntarioId);
        return ResponseEntity.ok("Candidatura realizada com sucesso!");
    }

}