package com.voluntech.voluntech_backend.dto;

import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import java.time.LocalDateTime;
import java.util.List;

public record CandidatoExibicaoDTO(
    Long candidaturaId,
    Long voluntarioId,
    String nome,
    String email,
    String telefone,
    List<String> habilidades,
    String bio,
    LocalDateTime dataCandidatura,
    StatusCandidatura status
) {}