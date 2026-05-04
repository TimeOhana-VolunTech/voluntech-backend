package com.voluntech.voluntech_backend.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "voluntarios")
@Data
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String senha;

    private String telefone;

    @ElementCollection // Cria uma tabela auxiliar automaticamente para as strings
    @Column(name = "habilidade")
    private List<String> habilidades;

    @ElementCollection
    @Column(name = "causa")
    private List<String> causas;

    @Column(columnDefinition = "TEXT") 
    private String bio;

    @ElementCollection
    @Column(name = "disponibilidade")
    private List<String> disponibilidades;

    @Column(nullable = false)
    private boolean onboardingCompleto = false;

    
}