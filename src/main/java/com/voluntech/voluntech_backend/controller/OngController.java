package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.service.OngService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/ongs")
@Tag(name = "ONGs", description = "Endpoints para gerenciamento de Ongs")
public class OngController {

    @Autowired
    private OngService service;

    @PostMapping
    @Operation(summary = "Cadastrar nova Ong", description = "Cria um registro de Ong com nome, CNPJ, e-mail, razão social e senha.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ong cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos (erro de validação ou duplicidade)"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor"),
            @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    public Ong cadastrar(@Valid @RequestBody Ong ong) {
        return service.salvar(ong);
    }

    @GetMapping
    @Operation(summary = "Listar todos as Ongs", description = "Retorna uma lista completa de todas as Ongs cadastradas.")
    public List<Ong> listar() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Ong por ID", description = "Retorna os detalhes de uma Ong específico através do ID.")
    public Ong buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar dados da Ong", description = "Atualiza nome, razão social de uma Ong existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "ID não encontrado")    
    })
    public Ong atualizar(@Valid @PathVariable Long id, @RequestBody Ong ong) {
        return service.atualizar(id, ong);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir Ong", description = "Remove permanentemente uma Ong do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "ID não encontrado")    
    })
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}