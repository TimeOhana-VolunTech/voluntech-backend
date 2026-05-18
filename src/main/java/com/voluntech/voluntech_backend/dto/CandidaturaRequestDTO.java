package com.voluntech.voluntech_backend.dto;

import jakarta.validation.constraints.NotNull;

public record CandidaturaRequestDTO(
    @NotNull(message = "O ID da vaga é obrigatório")
    Long projetoId,
    
    @NotNull(message = "O ID do voluntário é obrigatório")
    Long voluntarioId
) {}