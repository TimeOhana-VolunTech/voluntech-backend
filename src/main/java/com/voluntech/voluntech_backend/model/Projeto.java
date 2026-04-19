package com.voluntech.voluntech_backend.model;

import java.time.LocalDate;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "tb_projeto")
@Data
public class Projeto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Retiramos as mensagens de erro, pois o DTO já cuida disso
    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descricao;

    @Column(nullable = false)
    private LocalDate prazo;

    @Column(name = "data_criacao", updatable = false)
    private LocalDate dataCriacao = LocalDate.now();

    @Enumerated(EnumType.STRING)
    private StatusProjeto status = StatusProjeto.ATIVA; 

    @Enumerated(EnumType.STRING)
    private Modalidade modalidade;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "ong_id", nullable = false)
    private Ong ong;
}