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

        // Mapeamento manual do DTO para Entity
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
        // Refinamento: Buscamos antes para disparar o 404 caso não exista
        Voluntario voluntario = buscarPorId(id); 
        repository.delete(voluntario);
    }

    @Transactional
    public Voluntario atualizar(Long id, VoluntarioUpdateDTO voluntarioDto) {
        // 1. Buscamos o voluntário existente
        Voluntario voluntarioExistente = buscarPorId(id);

        // 2. Validação de e-mail (Mantenha sua lógica atual, ela está correta)
        if (!voluntarioExistente.getEmail().equals(voluntarioDto.email()) && 
            repository.existsByEmail(voluntarioDto.email())) {
            throw new RuntimeException("O novo e-mail já está em uso por outro usuário.");
        }

        // 3. Atualizamos os campos básicos
        voluntarioExistente.setNome(voluntarioDto.nome());
        voluntarioExistente.setEmail(voluntarioDto.email());

        // 4. ATUALIZAÇÃO DOS NOVOS CAMPOS DO ONBOARDING
        voluntarioExistente.setTelefone(voluntarioDto.telefone());
        voluntarioExistente.setHabilidades(voluntarioDto.habilidades());
        voluntarioExistente.setCausas(voluntarioDto.causas());
        voluntarioExistente.setBio(voluntarioDto.bio());
        voluntarioExistente.setDisponibilidades(voluntarioDto.disponibilidades());
        voluntarioExistente.setOnboardingCompleto(voluntarioDto.onboardingCompleto());

        // 5. Salva as alterações
        return repository.save(voluntarioExistente);
    }
    
}