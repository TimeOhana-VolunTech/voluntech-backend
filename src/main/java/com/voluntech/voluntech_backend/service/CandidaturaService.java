package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.CandidatoExibicaoDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaRequestDTO;
import com.voluntech.voluntech_backend.dto.CandidaturaResponseDTO;
import com.voluntech.voluntech_backend.model.Candidatura;
import com.voluntech.voluntech_backend.model.Notificacao;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.repository.CandidaturaRepository;
import com.voluntech.voluntech_backend.repository.NotificacaoRepository;
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

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Transactional
    public void salvar(CandidaturaRequestDTO dto) {

        boolean jaExiste = candidaturaRepository
            .findByVoluntarioIdAndProjetoId(dto.voluntarioId(), dto.projetoId())
            .isPresent();
        
        if (jaExiste) {
            throw new RuntimeException("Voluntário já está inscrito nesta vaga.");
        }

        Projeto projeto = projetoRepository.findById(dto.projetoId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrado"));
        
        Voluntario voluntario = voluntarioRepository.findById(dto.voluntarioId())
                .orElseThrow(() -> new RuntimeException("Voluntário não encontrado"));

        Candidatura candidatura = new Candidatura();
        candidatura.setProjeto(projeto);
        candidatura.setVoluntario(voluntario);
        candidatura.setDataCandidatura(LocalDateTime.now());
        candidatura.setStatus(StatusCandidatura.PENDENTE); 

        candidaturaRepository.save(candidatura);

        // GATILHO 1: Notificar a ONG que há um novo interessado
        Notificacao notif = new Notificacao();
        notif.setDestinatarioId(projeto.getOng().getId());
        notif.setTipoUsuario("ONG");
        notif.setMensagem("Novo voluntário inscrito na vaga: " + projeto.getTitulo());
        notificacaoRepository.save(notif);
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
            .filter(c -> !c.getStatus().toString().equals("CANCELADO"))
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

        if (novoStatus == StatusCandidatura.PENDENTE) {
            return;
        }

        Notificacao notif = new Notificacao();
        notif.setDestinatarioId(candidatura.getVoluntario().getId());
        notif.setTipoUsuario("VOLUNTARIO");
        
        String msg = novoStatus.toString().equals("APROVADO") 
            ? "Parabéns! Você foi aprovado para a vaga do projeto: " 
            : "Infelizmente sua candidatura não foi selecionada para: ";
            
        notif.setMensagem(msg + candidatura.getProjeto().getTitulo());
        notificacaoRepository.save(notif);
    }

    @Transactional
    public void atualizarStatus(Long candidaturaId, String novoStatusStr) {
        if (novoStatusStr == null) {
            throw new IllegalArgumentException("O status não pode ser nulo");
        }
        
        String statusFormatado = novoStatusStr.trim().toUpperCase();

        if (statusFormatado.equals("PENDENTE")) {
            Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
                    .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
            
            candidatura.setStatus(StatusCandidatura.PENDENTE);
            candidaturaRepository.save(candidatura);

            Notificacao notifONG = new Notificacao();
            notifONG.setDestinatarioId(candidatura.getProjeto().getOng().getId());
            notifONG.setTipoUsuario("ONG");
            notifONG.setMensagem("O voluntário " + candidatura.getVoluntario().getNome() 
                + " reativou a sua inscrição na vaga do projeto: " + candidatura.getProjeto().getTitulo());
            
            notificacaoRepository.save(notifONG);
            return; 
        }

        if (statusFormatado.equals("CANCELADO")) { 
            Candidatura candidatura = candidaturaRepository.findById(candidaturaId)
                    .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));
            
            candidatura.setStatus(StatusCandidatura.valueOf("CANCELADO"));
            candidaturaRepository.save(candidatura);

            Notificacao notifONG = new Notificacao();
            notifONG.setDestinatarioId(candidatura.getProjeto().getOng().getId());
            notifONG.setTipoUsuario("ONG");
            notifONG.setMensagem("O voluntário " + candidatura.getVoluntario().getNome() 
                + " cancelou a inscrição na vaga do projeto: " + candidatura.getProjeto().getTitulo());
            
            notificacaoRepository.save(notifONG);
            return; 
        }

        StatusCandidatura statusEnum = StatusCandidatura.valueOf(statusFormatado);
        this.atualizarStatus(candidaturaId, statusEnum);
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