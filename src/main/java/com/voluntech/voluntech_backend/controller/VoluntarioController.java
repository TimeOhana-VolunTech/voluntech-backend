package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.dto.VoluntarioRequestDTO;
import com.voluntech.voluntech_backend.dto.VoluntarioUpdateDTO;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.service.VoluntarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/voluntarios")
@Tag(name = "Voluntários", description = "Endpoints para gerenciamento de voluntários")
public class VoluntarioController {

    @Autowired
    private VoluntarioService service;

    @PostMapping
    @Operation(summary = "Cadastrar novo voluntário")
    public Voluntario cadastrar(@Valid @RequestBody VoluntarioRequestDTO voluntario) {
        return service.salvar(voluntario);
    }

    @GetMapping
    @Operation(summary = "Listar todos os voluntários", description = "Retorna uma lista completa de todos os voluntários cadastrados.")
    public List<Voluntario> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar voluntário por ID", description = "Retorna os detalhes de um voluntário específico através do ID.")
    public Voluntario buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar dados do voluntário", description = "Atualiza nome de um voluntário existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    public Voluntario atualizar(@Valid @PathVariable Long id, @RequestBody VoluntarioUpdateDTO voluntario) {
        return service.atualizar(id, voluntario);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir voluntário", description = "Remove permanentemente um voluntário do banco de dados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404", description = "ID não encontrado")
    })
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}