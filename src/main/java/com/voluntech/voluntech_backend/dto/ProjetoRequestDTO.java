package com.voluntech.voluntech_backend.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record ProjetoRequestDTO(

    @NotBlank(message = "O título é obrigatório")
    @Size(min = 5, max = 100, message = "O título deve ter entre 5 e 100 caracteres")
    @Schema(example = "Alfabetização de Adultos")
    String titulo,

    @NotBlank(message = "A descrição é obrigatória")
    @Size(min = 10, max = 1000, message = "A descrição deve ter entre 10 e 1000 caracteres")
    @Schema(example = "Vaga de projeto voltado para o ensino básico de leitura e escrita...")
    String descricao,

    @NotNull(message = "O prazo é obrigatório")
    @FutureOrPresent(message = "O prazo não pode ser retroativo")
    @Schema(example = "2026-12-31")
    LocalDate prazo,

    @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
    @Schema(example = "REMOTA")
    Modalidade modalidade,

    @JsonSetter(nulls = Nulls.AS_EMPTY, contentNulls = Nulls.AS_EMPTY)
    @Schema(example = "EDUCACAO")
    Categoria categoria,

    @NotNull(message = "O ID da ONG é obrigatório")
    @Schema(example = "1")
    Long ongId
) {}