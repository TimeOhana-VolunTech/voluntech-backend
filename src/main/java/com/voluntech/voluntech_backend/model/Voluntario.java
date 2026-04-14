package com.voluntech.voluntech_backend.model;

import org.hibernate.validator.constraints.br.CPF;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "voluntarios")
@Data
public class Voluntario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    @Schema(example = "João da Silva", description = "Nome completo do voluntário")
    private String nome;

    @Column(nullable = false, unique = true)
    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    @Schema(description = "CPF do voluntário (deve ser único e válido)")
    private String cpf;

    @Column(nullable = false, unique = true)
    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Schema(example = "joao.silva@email.com")
    private String email;

    @Column(nullable = false)
    @Size(min = 6, max = 12, message = "A senha deve ter entre 6 e 12 caracteres")
    @jakarta.validation.constraints.Pattern(regexp = "^\\S+$", message = "A senha não pode conter espaços")
    private String senha;
}