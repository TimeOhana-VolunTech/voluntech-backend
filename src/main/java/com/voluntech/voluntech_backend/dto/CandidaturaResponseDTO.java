package com.voluntech.voluntech_backend.dto;

import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;

import java.time.LocalDateTime;

public record CandidaturaResponseDTO(
    Long id,
    Long projetoId,
    String projetoTitulo,
    String nomeOng,
    LocalDateTime dataCandidatura,
    StatusCandidatura status,
    StatusProjeto statusProjeto
) {}