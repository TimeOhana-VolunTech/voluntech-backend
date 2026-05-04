package com.voluntech.voluntech_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public record VoluntarioUpdateDTO(
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100)
    @Schema(example = "João da Silva Atualizado")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(example = "novo.email.joao@email.com")
    String email,

    @Schema(description = "Telefone", example = "99999999999")
    String telefone,

    @Schema(description = "Lista de habilidades do voluntário", example = "[\"Cozinha\", \"Programação\"]")
    List<String> habilidades,

    @Schema(description = "Causas de interesse", example = "[\"Meio Ambiente\", \"Educação\"]")
    List<String> causas,

    @Schema(example = "Sou apaixonado por tecnologia e quero ajudar ONGs a crescerem.")
    String bio,
    
    @Schema(example = "[\"Manhã\", \"Finais de Semana\"]")
    List<String> disponibilidades,

    boolean onboardingCompleto
) {}