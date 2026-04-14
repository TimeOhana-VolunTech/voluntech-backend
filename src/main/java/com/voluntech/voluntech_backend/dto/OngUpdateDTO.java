package com.voluntech.voluntech_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OngUpdateDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100)
    @Schema(example = "Ong Refúgio Atualizada")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(example = "novo.email@ong.com")
    String email,

    @NotBlank(message = "A razão social é obrigatória")
    String razaoSocial
) {}