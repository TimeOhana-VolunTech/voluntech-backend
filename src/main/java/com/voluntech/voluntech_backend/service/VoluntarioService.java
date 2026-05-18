package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.VoluntarioRequestDTO;
import com.voluntech.voluntech_backend.dto.VoluntarioUpdateDTO;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    @Transactional
    public Voluntario salvar(VoluntarioRequestDTO voluntario) {
        if (repository.existsByEmail(voluntario.email())) {
            throw new RuntimeException("Este e-mail já está cadastrado para outro voluntário.");
        }
        if (repository.existsByCpf(voluntario.cpf())) {
            throw new RuntimeException("Este CPF já está cadastrado.");
        }

        Voluntario novoVoluntario = new Voluntario();
        novoVoluntario.setNome(voluntario.nome());
        novoVoluntario.setCpf(voluntario.cpf());
        novoVoluntario.setEmail(voluntario.email());
        novoVoluntario.setSenha(voluntario.senha());

        return repository.save(novoVoluntario);
    }

    public List<Voluntario> listarTodos() {
        return repository.findAll();
    }

    public Voluntario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Voluntario não encontrada com o ID: " + id));
    }

    @Transactional
    public void excluir(Long id) {

        Voluntario voluntario = buscarPorId(id); 
        repository.delete(voluntario);
    }

    @Transactional
    public Voluntario atualizar(Long id, VoluntarioUpdateDTO voluntarioDto) {
        Voluntario voluntarioExistente = buscarPorId(id);

        if (!voluntarioExistente.getEmail().equals(voluntarioDto.email()) && 
            repository.existsByEmail(voluntarioDto.email())) {
            throw new RuntimeException("O novo e-mail já está em uso por outro usuário.");
        }

        voluntarioExistente.setNome(voluntarioDto.nome());
        voluntarioExistente.setEmail(voluntarioDto.email());
        voluntarioExistente.setTelefone(voluntarioDto.telefone());
        voluntarioExistente.setBio(voluntarioDto.bio());

        voluntarioExistente.setHabilidades(voluntarioDto.habilidades() != null ? voluntarioDto.habilidades() : List.of());
        voluntarioExistente.setCausas(voluntarioDto.causas() != null ? voluntarioDto.causas() : List.of());
        voluntarioExistente.setDisponibilidades(voluntarioDto.disponibilidades() != null ? voluntarioDto.disponibilidades() : List.of());

        voluntarioExistente.setOnboardingCompleto(voluntarioDto.onboardingCompleto());

        return repository.save(voluntarioExistente);
    }
    
}