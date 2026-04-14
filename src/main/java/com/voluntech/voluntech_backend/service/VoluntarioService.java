package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class VoluntarioService {

    @Autowired
    private VoluntarioRepository repository;

    public Voluntario salvar(Voluntario voluntario) {
        if (repository.existsByEmail(voluntario.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado para outro voluntário.");
        }
        if (repository.existsByCpf(voluntario.getCpf())) {
            throw new RuntimeException("Este CPF já está cadastrado.");
        }
        return repository.save(voluntario);
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

    public Voluntario atualizar(Long id, Voluntario voluntarioAtualizado) {
        // Buscamos o voluntário existente para garantir que ele existe
        Voluntario voluntarioExistente = buscarPorId(id);

        // Atualizamos os campos (exceto o ID e o CPF, que geralmente são fixos)
        voluntarioExistente.setNome(voluntarioAtualizado.getNome());
        voluntarioExistente.setEmail(voluntarioAtualizado.getEmail());

        return repository.save(voluntarioExistente);
    }
}