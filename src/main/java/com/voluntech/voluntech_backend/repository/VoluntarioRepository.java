package com.voluntech.voluntech_backend.repository;

import com.voluntech.voluntech_backend.model.Voluntario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoluntarioRepository extends JpaRepository<Voluntario, Long> {
    boolean existsByEmail(String email);
    boolean existsByCpf(String cpf);
}