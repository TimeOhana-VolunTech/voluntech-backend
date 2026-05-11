package com.voluntech.voluntech_backend.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_notificacao")
@Data
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String mensagem;

    @Column(nullable = false)
    private boolean lida = false;

    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao = LocalDateTime.now();

    // ID do destinatário (pode ser o ID da ONG ou do Voluntário)
    @Column(nullable = false)
    private Long destinatarioId;

    // Tipo de usuário para facilitar a filtragem no Front
    @Column(nullable = false)
    private String tipoUsuario; // "ONG" ou "VOLUNTARIO"
}
