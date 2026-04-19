package com.voluntech.voluntech_backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.voluntech.voluntech_backend.model.Projeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    // A mágica do Spring Data JPA: ele gera a query de ordenação automaticamente
    List<Projeto> findByOngIdOrderByDataCriacaoDesc(Long ongId);
}
