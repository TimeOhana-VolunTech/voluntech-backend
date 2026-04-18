package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.repository.OngRepository;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    public Object autenticar(String email, String senha) {
        // Primeiro, procura na tabela de ONGs
        Optional<Ong> ong = ongRepository.findByEmail(email);
        if (ong.isPresent()) {
            if (ong.get().getSenha().equals(senha)) {
                return ong.get(); // Sucesso: é uma ONG
            }
            throw new RuntimeException("Senha incorreta.");
        }

        // Se não achou ONG, procura na tabela de Voluntários
        Optional<Voluntario> voluntario = voluntarioRepository.findByEmail(email);
        if (voluntario.isPresent()) {
            if (voluntario.get().getSenha().equals(senha)) {
                return voluntario.get(); // Sucesso: é um Voluntário
            }
            throw new RuntimeException("Senha incorreta.");
        }

        // Se chegou aqui, o e-mail não existe em nenhuma tabela
        throw new RuntimeException("E-mail não encontrado no sistema.");
    }
}