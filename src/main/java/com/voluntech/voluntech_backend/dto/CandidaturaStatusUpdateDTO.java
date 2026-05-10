package com.voluntech.voluntech_backend.dto;

import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import jakarta.validation.constraints.NotNull;

public record CandidaturaStatusUpdateDTO(
    @NotNull(message = "O novo status é obrigatório")
    StatusCandidatura novoStatus
) {}