package com.voluntech.voluntech_backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;

public record OngRequestDTO(
    
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    @Schema(example = "Ong Refúgio de Animais")
    String nome,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    @Schema(example = "ong@email.com")
    String email,

    @NotBlank(message = "O CNPJ é obrigatório")
    @CNPJ(message = "CNPJ inválido")
    @Schema(description = "CNPJ da ONG")
    String cnpj,

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, max = 12, message = "A senha deve ter entre 6 e 12 caracteres")
    @Pattern(regexp = "^\\S+$", message = "A senha não pode conter espaços")
    String senha,

    @NotBlank(message = "A razão social é obrigatória")
    String razaoSocial
) {}