package com.voluntech.voluntech_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginRequestDTO(
    @Schema(example = "ong@email.com") String email,
    @Schema(example = "123456") String senha
) {}
