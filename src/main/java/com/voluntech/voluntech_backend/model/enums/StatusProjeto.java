package com.voluntech.voluntech_backend.model.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Estados possíveis de um projeto no sistema")
public enum StatusProjeto {
    ATIVA,
    PAUSADA,
    FINALIZADA
}
