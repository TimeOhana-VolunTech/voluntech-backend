package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.OngRequestDTO;
import com.voluntech.voluntech_backend.dto.OngUpdateDTO;
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
    @Operation(summary = "Cadastrar nova Ong")
    public Ong cadastrar(@Valid @RequestBody OngRequestDTO ong) {
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
    @Operation(summary = "Editar dados da ONG", description = "Atualiza nome, e-mail e razão social de uma ONG existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    public Ong atualizar(@PathVariable Long id, @Valid @RequestBody OngUpdateDTO ong) {
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