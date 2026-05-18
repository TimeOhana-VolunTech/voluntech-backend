package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.CandidaturaRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
import com.voluntech.voluntech_backend.model.Notificacao;
import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import com.voluntech.voluntech_backend.repository.CandidaturaRepository;
import com.voluntech.voluntech_backend.repository.NotificacaoRepository;
import com.voluntech.voluntech_backend.repository.OngRepository;
import com.voluntech.voluntech_backend.repository.ProjetoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private CandidaturaService candidaturaService;

    private Projeto buscarProjetoPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + id));
    }

    @Transactional
    public ProjetoResponseDTO criar(ProjetoRequestDTO dto) {

        Ong ong = ongRepository.findById(dto.ongId())
                .orElseThrow(() -> new RuntimeException("ONG não encontrada com o ID: " + dto.ongId()));

        Projeto projeto = new Projeto();
        projeto.setTitulo(dto.titulo());
        projeto.setDescricao(dto.descricao());
        projeto.setPrazo(dto.prazo());
        projeto.setModalidade(dto.modalidade());
        projeto.setCategoria(dto.categoria());
        projeto.setOng(ong);
        
        projeto.setStatus(StatusProjeto.ATIVA);
        projeto.setDataCriacao(LocalDate.now());

        Projeto projetoSalvo = projetoRepository.save(projeto);

        return converterParaResponseDTO(projetoSalvo);
    }

    public List<ProjetoResponseDTO> listarPorOng(Long ongId) {
        return projetoRepository.findByOngIdOrderByDataCriacaoDesc(ongId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjetoResponseDTO buscarPorId(Long id) {
        return converterParaResponseDTO(buscarProjetoPorId(id));
    }

    @Transactional
    public ProjetoResponseDTO atualizar(Long id, ProjetoRequestDTO dto) {

        Projeto projeto = buscarProjetoPorId(id);

        if (projeto.getStatus() == StatusProjeto.FINALIZADA) {
            projeto.setStatus(StatusProjeto.ATIVA);
            System.out.println(">>> Projeto [" + projeto.getTitulo() + "] REATIVADO automaticamente por atualização de prazo.");
        }

        projeto.setTitulo(dto.titulo());
        projeto.setDescricao(dto.descricao());
        projeto.setPrazo(dto.prazo());
        projeto.setModalidade(dto.modalidade());
        projeto.setCategoria(dto.categoria());

        return converterParaResponseDTO(projetoRepository.save(projeto));
    }


    @Transactional
    public ProjetoResponseDTO alterarStatus(Long id, StatusProjeto novoStatus) {
        Projeto projeto = buscarProjetoPorId(id);
        projeto.setStatus(novoStatus);
        
        return converterParaResponseDTO(projetoRepository.save(projeto));
    }

    @Transactional
    public void excluir(Long id) {
        Projeto projeto = buscarProjetoPorId(id);
        
        long candidatosAtivos = candidaturaRepository.countCandidatosAtivos(id);
        
        if (candidatosAtivos > 0) {
            throw new RuntimeException("Não é permitido excluir esta vaga pois ainda existem voluntários aguardando resposta ou aprovados. Recuse os candidatos restantes antes de excluir.");
        }
        
        
        candidaturaRepository.deleteByProjetoId(id);
        
        projetoRepository.delete(projeto);
    }

    public List<ProjetoResponseDTO> explorarProjetos(Categoria categoria, Modalidade modalidade, String termo, Long voluntarioId) {
        return projetoRepository.buscarOportunidades(categoria, modalidade, termo, voluntarioId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void candidatar(Long projetoId, Long voluntarioId) {

        CandidaturaRequestDTO dto = new CandidaturaRequestDTO(projetoId, voluntarioId);
        candidaturaService.salvar(dto);
    }


    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia às 08:00 da manhã
    @Transactional
    public void alertarPrazosProximos() {

        LocalDate amanha = LocalDate.now().plusDays(1);
        
        List<Projeto> vencemAmanha = projetoRepository.findByPrazoAndStatus(amanha, StatusProjeto.ATIVA);

        vencemAmanha.forEach(p -> {
            Notificacao notif = new Notificacao();
            notif.setDestinatarioId(p.getOng().getId());
            notif.setTipoUsuario("ONG");
            notif.setMensagem("Atenção: O prazo de inscrição para a vaga '" + p.getTitulo() + "' encerra amanhã!");
            notificacaoRepository.save(notif);
        });
    }


    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia à meia-noite
    //@Scheduled(fixedRate = 10000)
    @Transactional
    public void verificarPrazosExpirados() {
        LocalDate hoje = LocalDate.now();
        System.out.println(">>> Iniciando varredura de prazos encerrados em: " + hoje);
        
        List<Projeto> paraFinalizar = projetoRepository.buscarProjetosParaFinalizar(hoje);

        System.out.println(">>> Encontrados " + paraFinalizar.size() + " vagas para encerrar.");

        paraFinalizar.forEach(p -> {
            p.setStatus(StatusProjeto.FINALIZADA);
            System.out.println(">>> Projeto [" + p.getTitulo() + "] FINALIZADO automaticamente por decurso de prazo.");

            Notificacao notif = new Notificacao();
            notif.setDestinatarioId(p.getOng().getId());
            notif.setTipoUsuario("ONG");
            notif.setMensagem("A vaga do projeto '" + p.getTitulo() + "' foi finalizado automaticamente devido ao prazo.");
            notificacaoRepository.save(notif);
        });

        projetoRepository.saveAll(paraFinalizar);
    }

    private ProjetoResponseDTO converterParaResponseDTO(Projeto projeto) {

        long pendentes = candidaturaRepository.countByProjetoIdAndStatus(projeto.getId(), StatusCandidatura.PENDENTE);

        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getDescricao(),
                projeto.getPrazo(),
                projeto.getStatus(),
                projeto.getModalidade()!= null ? projeto.getModalidade().toString() : null,
                projeto.getCategoria() != null ? projeto.getCategoria().toString() : null,
                projeto.getOng().getId(),
                projeto.getOng().getNome(),
                pendentes
            );
    }
}