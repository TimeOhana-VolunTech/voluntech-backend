package com.voluntech.voluntech_backend.repository;

import com.voluntech.voluntech_backend.model.Ong;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {
    // Aqui o Spring já nos dá métodos como save(), findAll(), etc.

    boolean existsByEmail(String email);
    boolean existsByCnpj(String cnpj);

    Optional<Ong> findByEmail(String email);
}