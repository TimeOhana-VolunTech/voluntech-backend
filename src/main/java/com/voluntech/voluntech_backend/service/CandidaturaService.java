package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.CandidatoExibicaoDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaRequestDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaResponseDTO;
import com.voluntech.voluntech_backend.model.Candidatura;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.repository.CandidaturaRepository;
import com.voluntech.voluntech_backend.repository.ProjetoRepository;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CandidaturaService {

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private VoluntarioRepository voluntarioRepository;

    @Transactional
    public void salvar(CandidaturaRequestDTO dto) {
        // 1. Validação: Verificar se o voluntário já está inscrito para evitar duplicidade
        boolean jaExiste = candidaturaRepository
            .findByVoluntarioIdAndProjetoId(dto.voluntarioId(), dto.projetoId())
            .isPresent();
        
        if (jaExiste) {
            throw new RuntimeException("Voluntário já está inscrito neste projeto.");
        }

        // 2. Buscar as entidades completas no banco
        Projeto projeto = projetoRepository.findById(dto.projetoId())
                .orElseThrow(() -> new RuntimeException("Projeto não encontrado"));
        
        Voluntario voluntario = voluntarioRepository.findById(dto.voluntarioId())
                .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));

        // 3. Criar a nova entidade Candidatura
        Candidatura candidatura = new Candidatura();
        candidatura.setProjeto(projeto);
        candidatura.setVoluntario(voluntario);
        candidatura.setDataCandidatura(LocalDateTime.now());
        candidatura.setStatus(StatusCandidatura.PENDENTE); // Status inicial padrão

        candidaturaRepository.save(candidatura);
    }

    public List<CandidaturaResponseDTO> listarCandidaturasDoVoluntario(Long voluntarioId) {
        return candidaturaRepository.findByVoluntarioId(voluntarioId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<CandidatoExibicaoDTO> listarCandidatosPorProjeto(Long projetoId) {
        return candidaturaRepository.findByProjetoId(projetoId)
            .stream()
            .map(c -> new CandidatoExibicaoDTO(
                    c.getId(),
                    c.getVoluntario().getId(),
                    c.getVoluntario().getNome(),
                    c.getVoluntario().getEmail(),
                    c.getVoluntario().getTelefone(),
                    c.getVoluntario().getHabilidades(),
                    c.getVoluntario().getBio(),
                    c.getDataCandidatura(),
                    c.getStatus()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void atualizarStatus(Long candidaturaId, StatusCandidatura novoStatus) {
        Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
                .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
        candidatura.setStatus(novoStatus);
        candidaturaRepository.save(candidatura);
    }


    private CandidaturaResponseDTO mapToDTO(Candidatura c) {
        return new CandidaturaResponseDTO(
            c.getId(),
            c.getProjeto().getId(),
            c.getProjeto().getTitulo(),
            c.getProjeto().getOng().getNome(),
            c.getDataCandidatura(),
            c.getStatus(),
            c.getProjeto().getStatus()
        );
    }
}