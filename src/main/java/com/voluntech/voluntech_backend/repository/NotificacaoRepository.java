package com.voluntech.voluntech_backend.repository;

import com.voluntech.voluntech_backend.model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
    // Busca notificações não lidas para o sino brilhar
    List<Notificacao> findByDestinatarioIdAndTipoUsuarioOrderByDataCriacaoDesc(Long destinatarioId, String tipoUsuario);
    
    // Conta quantas notificações não lidas existem (para a bolinha vermelha)
    long countByDestinatarioIdAndTipoUsuarioAndLidaFalse(Long destinatarioId, String tipoUsuario);
}