package com.voluntech.voluntech_backend.repository;

import com.voluntech.voluntech_backend.model.Candidatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CandidaturaRepository extends JpaRepository<Candidatura, Long> {
    List<Candidatura> findByVoluntarioId(Long voluntarioId);
    List<Candidatura> findByProjetoId(Long projetoId);
    boolean existsByProjetoId(Long projetoId);

    // Para evitar que o voluntário se candidate duas vezes ao mesmo projeto
    Optional<Candidatura> findByVoluntarioIdAndProjetoId(Long voluntarioId, Long projetoId);

    // Conta quantos candidatos ativos (PENDENTE ou APROVADO) existem no projeto
    @Query("SELECT COUNT(c) FROM Candidatura c WHERE c.projeto.id = :projetoId AND c.status != 'RECUSADO'")
    long countCandidatosAtivos(@Param("projetoId") Long projetoId);

    // Deleta todas as candidaturas de um projeto (usaremos para limpar os recusados antes de excluir o projeto)
    void deleteByProjetoId(Long projetoId);
}