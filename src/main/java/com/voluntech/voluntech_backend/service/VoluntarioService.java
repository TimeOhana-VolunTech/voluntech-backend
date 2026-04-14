package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.VoluntarioRequestDTO;
import com.voluntech.voluntech_backend.dto.VoluntarioUpdateDTO;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

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
                .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public Voluntario atualizar(Long id, VoluntarioUpdateDTO voluntario) {
        // Buscamos o voluntário existente para garantir que ele existe
        Voluntario voluntarioExistente = buscarPorId(id);

        // Atualizamos os campos (exceto o ID e o CPF, que geralmente são fixos)
        voluntarioExistente.setNome(voluntario.nome());
        voluntarioExistente.setEmail(voluntario.email());

        return repository.save(voluntarioExistente);
    }
}