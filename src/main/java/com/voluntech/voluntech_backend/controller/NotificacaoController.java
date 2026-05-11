package com.voluntech.voluntech_backend.controller;

import com.voluntech.voluntech_backend.model.Notificacao;
import com.voluntech.voluntech_backend.repository.NotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificacoes")
public class NotificacaoController {

    @Autowired
    private NotificacaoRepository repository;

    @GetMapping("/{tipo}/{id}")
    public List<Notificacao> listar(@PathVariable String tipo, @PathVariable Long id) {
        return repository.findByDestinatarioIdAndTipoUsuarioOrderByDataCriacaoDesc(id, tipo.toUpperCase());
    }

    @GetMapping("/{tipo}/{id}/pendentes")
    public long contarPendentes(@PathVariable String tipo, @PathVariable Long id) {
        return repository.countByDestinatarioIdAndTipoUsuarioAndLidaFalse(id, tipo.toUpperCase());
    }

    @PatchMapping("/{id}/ler")
    public void marcarComoLida(@PathVariable Long id) {
        repository.findById(id).ifPresent(n -> {
            n.setLida(true);
            repository.save(n);
        });
    }
}