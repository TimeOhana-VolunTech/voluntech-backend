package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.repository.OngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OngService {

    @Autowired
    private OngRepository repository;

    public Ong salvar(Ong ong) {
        if (repository.existsByEmail(ong.getEmail())) {
            throw new RuntimeException("Este e-mail já está cadastrado para outra ONG.");
        }
        if (repository.existsByCnpj(ong.getCnpj())) {
            throw new RuntimeException("Este CNPJ já está cadastrado.");
        }
        return repository.save(ong);
    }

    public List<Ong> listarTodas() {
        return repository.findAll();
    }

    public Ong buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ONG não encontrada com o ID: " + id));
    }

    public void excluir(Long id) {
        Ong ong = buscarPorId(id);
        repository.delete(ong);
    }

    public Ong atualizar(Long id, Ong ongAtualizada) {
        Ong ongExistente = buscarPorId(id);

        ongExistente.setNome(ongAtualizada.getNome());
        ongExistente.setEmail(ongAtualizada.getEmail());
        ongExistente.setRazaoSocial(ongAtualizada.getRazaoSocial());

        return repository.save(ongExistente);
    }
}