package com.voluntech.voluntech_backend.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    // A mágica do Spring Data JPA: ele gera a query de ordenação automaticamente
    List<Projeto> findByOngIdOrderByDataCriacaoDesc(Long ongId);

    @Query("SELECT p FROM Projeto p WHERE p.status = 'ATIVA' " +
       "AND (:voluntarioId IS NULL OR NOT EXISTS (" +
       "    SELECT c FROM Candidatura c WHERE c.projeto.id = p.id AND c.voluntario.id = :voluntarioId" +
       ")) " +
       "AND (:categoria IS NULL OR p.categoria = :categoria) " +
       "AND (:modalidade IS NULL OR p.modalidade = :modalidade) " +
       "AND (:termo IS NULL OR LOWER(p.titulo) LIKE LOWER(CONCAT('%', CAST(:termo AS string), '%')) " +
       "OR LOWER(p.descricao) LIKE LOWER(CONCAT('%', CAST(:termo AS string), '%'))) " +
       "ORDER BY p.dataCriacao DESC")
    List<Projeto> buscarOportunidades(
        @Param("categoria") Categoria categoria, 
        @Param("modalidade") Modalidade modalidade, 
        @Param("termo") String termo,
        @Param("voluntarioId") Long voluntarioId
    );

    @Query("SELECT p FROM Projeto p WHERE p.status != 'FINALIZADA' AND p.prazo <= :data")
    List<Projeto> buscarProjetosParaFinalizar(@Param("data") LocalDate data);

    List<Projeto> findByPrazoAndStatus(LocalDate prazo, StatusProjeto status);
}
