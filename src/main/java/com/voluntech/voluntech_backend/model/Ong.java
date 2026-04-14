package com.voluntech.voluntech_backend.model;

import org.hibernate.validator.constraints.br.CNPJ;

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
@Table(name = "ongs")
@Data // Gera getters, setters, equals e hashcode automaticamente
public class Ong {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 3, max = 100, message = "O nome deve conter entre 3 e 100 caracteres")
    @Schema(example = "Ong Refúgio de Animais", description = "Nome completo da Ong")
    private String nome;

    @Column(nullable = false, unique = true)
    @Email(message = "E-mail inválido")
    @NotBlank(message = "O e-mail é obrigatório")
    @Schema(example = "ong@email.com")
    private String email;

    @Column(nullable = false, unique = true)
    @CNPJ(message = "CNPJ inválido")
    @NotBlank(message = "O CNPJ é obrigatório")
    @Schema(description = "CNPJ do voluntário (deve ser único e válido)")
    private String cnpj;

    @Column(nullable = false)
    @Size(min = 6, max = 12, message = "A senha deve ter entre 6 e 12 caracteres")
    @jakarta.validation.constraints.Pattern(regexp = "^\\S+$", message = "A senha não pode conter espaços")
    private String senha;

    @Column(nullable = false)
    @NotBlank(message = "A razão social é obrigatória")
    private String razaoSocial;
}