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

        Optional<Ong> ong = ongRepository.findByEmail(email);
        if (ong.isPresent()) {
            if (ong.get().getSenha().equals(senha)) {
                return ong.get(); // Sucesso: é uma ONG
            }
            throw new RuntimeException("Senha incorreta.");
        }

        Optional<Voluntario> voluntario = voluntarioRepository.findByEmail(email);
        if (voluntario.isPresent()) {
            if (voluntario.get().getSenha().equals(senha)) {
                return voluntario.get();
            }
            throw new RuntimeException("Senha incorreta.");
        }

        throw new RuntimeException("E-mail não encontrado no sistema.");
    }
}