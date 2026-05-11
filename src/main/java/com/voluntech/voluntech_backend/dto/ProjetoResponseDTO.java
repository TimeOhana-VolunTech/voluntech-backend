package com.voluntech.voluntech_backend.dto;

import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import java.time.LocalDate;

public record ProjetoResponseDTO(
    Long id,
    String titulo,
    String descricao,
    LocalDate prazo,
    StatusProjeto status,
    String modalidade,
    String categoria,
    Long ongId,
    String nomeOng,
    long totalCandidatosPendentes
) {}