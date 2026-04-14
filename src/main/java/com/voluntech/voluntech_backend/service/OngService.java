package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.OngRequestDTO;
import com.voluntech.voluntech_backend.dto.OngUpdateDTO;
import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.repository.OngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OngService {

    @Autowired
    private OngRepository repository;

    public Ong salvar(OngRequestDTO ong) {
        if (repository.existsByEmail(ong.email())) {
            throw new RuntimeException("Este e-mail já está cadastrado para outra ONG.");
        }
        if (repository.existsByCnpj(ong.cnpj())) {
            throw new RuntimeException("Este CNPJ já está cadastrado.");
        }

        // Convertendo DTO para Entity
        Ong novaOng = new Ong();
        novaOng.setNome(ong.nome());
        novaOng.setEmail(ong.email());
        novaOng.setCnpj(ong.cnpj());
        novaOng.setSenha(ong.senha());
        novaOng.setRazaoSocial(ong.razaoSocial());

        return repository.save(novaOng);
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

    public Ong atualizar(Long id, OngUpdateDTO ong) {
        Ong ongExistente = buscarPorId(id);

        ongExistente.setNome(ong.nome());
        ongExistente.setEmail(ong.email());
        ongExistente.setRazaoSocial(ong.razaoSocial());

        return repository.save(ongExistente);
    }
}