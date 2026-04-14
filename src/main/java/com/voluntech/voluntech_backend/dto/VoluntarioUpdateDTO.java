package com.voluntech.voluntech_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VoluntarioUpdateDTO(
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100)
    @Schema(example = "João da Silva Atualizado")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(example = "novo.email.joao@email.com")
    String email
) {}