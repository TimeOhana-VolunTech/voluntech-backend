package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.model.Notificacao;
import com.voluntech.voluntech_backend.repository.NotificacaoRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Endpoints para gerenciamento de notificações de voluntários e ONGs")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository repository;

    @Operation(
        summary = "Lista notificações por usuário",
        description = "Retorna todas as notificações de um usuário específico (ONG ou VOLUNTARIO) ordenadas pela data mais recente."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Tipo de usuário inválido")
    })
    @GetMapping("/{tipo}/{id}")
    public List<Notificacao> listar(@PathVariable String tipo, @PathVariable Long id) {
        return repository.findByDestinatarioIdAndTipoUsuarioOrderByDataCriacaoDesc(id, tipo.toUpperCase());
    }

    @Operation(
        summary = "Conta notificações não lidas",
        description = "Retorna a quantidade de notificações que ainda não foram marcadas como lidas para um determinado usuário."
    )
    @GetMapping("/{tipo}/{id}/pendentes")
    public long contarPendentes(@PathVariable String tipo, @PathVariable Long id) {
        return repository.countByDestinatarioIdAndTipoUsuarioAndLidaFalse(id, tipo.toUpperCase());
    }

    @Operation(
        summary = "Marca uma notificação como lida",
        description = "Altera o status da notificação pelo seu ID único para 'lida = true'."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificação atualizada com sucesso"),
        @ApiResponse(responseCode = "404", description = "ID da notificação não encontrado")
    })
    @PatchMapping("/{id}/ler")
    public void marcarComoLida(@PathVariable Long id) {
        repository.findById(id).ifPresent(n -> {
            n.setLida(true);
            repository.save(n);
        });
    }
}