package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.OngRequestDTO;
import com.voluntech.voluntech_backend.dto.OngUpdateDTO;
import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.repository.OngRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class OngService {

    @Autowired
    private OngRepository repository;

    @Transactional // Garante a integridade da transação
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
                .orElseThrow(() -> new EntityNotFoundException("ONG não encontrada com o ID: " + id));
    }

    @Transactional
    public void excluir(Long id) {
        Ong ong = buscarPorId(id);
        repository.delete(ong);
    }

    @Transactional
    public Ong atualizar(Long id, OngUpdateDTO ong) {
        Ong ongExistente = buscarPorId(id);

        // Se o e-mail mudou, verifica se o novo já existe
        if (!ongExistente.getEmail().equals(ong.email()) && repository.existsByEmail(ong.email())) {
            throw new RuntimeException("O novo e-mail já está em uso por outra instituição.");
        }

        ongExistente.setNome(ong.nome());
        ongExistente.setEmail(ong.email());
        ongExistente.setRazaoSocial(ong.razaoSocial());

        return repository.save(ongExistente);
    }
}