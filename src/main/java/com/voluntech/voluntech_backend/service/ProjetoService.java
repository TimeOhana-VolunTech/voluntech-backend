package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.CandidaturaRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
import com.voluntech.voluntech_backend.model.Candidatura;
import com.voluntech.voluntech_backend.model.Notificacao;
import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.Voluntario;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusCandidatura;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import com.voluntech.voluntech_backend.repository.CandidaturaRepository;
import com.voluntech.voluntech_backend.repository.NotificacaoRepository;
import com.voluntech.voluntech_backend.repository.OngRepository;
import com.voluntech.voluntech_backend.repository.ProjetoRepository;
import com.voluntech.voluntech_backend.repository.VoluntarioRepository;

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
    private VoluntarioRepository voluntarioRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private CandidaturaService candidaturaService;

    // Método auxiliar privado para evitar repetição de código e garantir o 404
    private Projeto buscarProjetoPorId(Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com o ID: " + id));
    }

    @Transactional
    public ProjetoResponseDTO criar(ProjetoRequestDTO dto) {
        // 1. Buscar a ONG (Garante que o projeto será vinculado a uma ONG existente)
        Ong ong = ongRepository.findById(dto.ongId())
                .orElseThrow(() -> new RuntimeException("ONG não encontrada com o ID: " + dto.ongId()));

        // 2. Converter DTO para Entity
        Projeto projeto = new Projeto();
        projeto.setTitulo(dto.titulo());
        projeto.setDescricao(dto.descricao());
        projeto.setPrazo(dto.prazo());
        projeto.setModalidade(dto.modalidade());
        projeto.setCategoria(dto.categoria());
        projeto.setOng(ong);
        
        // Regras de Negócio
        projeto.setStatus(StatusProjeto.ATIVA);
        projeto.setDataCriacao(LocalDate.now());

        // 3. Salvar no Banco
        Projeto projetoSalvo = projetoRepository.save(projeto);

        // 4. Retornar o ResponseDTO
        return converterParaResponseDTO(projetoSalvo);
    }

    public List<ProjetoResponseDTO> listarPorOng(Long ongId) {
        return projetoRepository.findByOngIdOrderByDataCriacaoDesc(ongId)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    public ProjetoResponseDTO buscarPorId(Long id) {
        // Reutiliza o método privado
        return converterParaResponseDTO(buscarProjetoPorId(id));
    }

    @Transactional
    public ProjetoResponseDTO atualizar(Long id, ProjetoRequestDTO dto) {
        // Agora dispara 404 corretamente através do método auxiliar
        Projeto projeto = buscarProjetoPorId(id);

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
        
        // 1. Verifica se existem candidatos que NÃO estão recusados
        long candidatosAtivos = candidaturaRepository.countCandidatosAtivos(id);
        
        if (candidatosAtivos > 0) {
            // Lançamos a exceção com a mensagem que o Front-end vai exibir
            throw new RuntimeException("Não é permitido excluir este projeto pois ainda existem voluntários aguardando resposta ou aprovados. Recuse os candidatos restantes antes de excluir.");
        }
        
        // 2. Se chegou aqui, significa que só existem candidatos RECUSADOS (ou nenhum).
        // Deletamos as candidaturas recusadas primeiro para evitar erro de chave estrangeira.
        candidaturaRepository.deleteByProjetoId(id);
        
        // 3. Excluímos o projeto
        projetoRepository.delete(projeto);
    }

    public List<ProjetoResponseDTO> explorarProjetos(Categoria categoria, Modalidade modalidade, String termo) {
        return projetoRepository.buscarOportunidades(categoria, modalidade, termo)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void candidatar(Long projetoId, Long voluntarioId) {
        // Em vez de duplicar a lógica aqui, chame o método salvar que já tem a notificação!
        CandidaturaRequestDTO dto = new CandidaturaRequestDTO(projetoId, voluntarioId);
        candidaturaService.salvar(dto);
    }


    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia às 08:00 da manhã
    @Transactional
    public void alertarPrazosProximos() {
        // Busca projetos que vencem amanhã
        LocalDate amanha = LocalDate.now().plusDays(1);
        
        List<Projeto> vencemAmanha = projetoRepository.findByPrazoAndStatus(amanha, StatusProjeto.ATIVA);

        vencemAmanha.forEach(p -> {
            Notificacao notif = new Notificacao();
            notif.setDestinatarioId(p.getOng().getId());
            notif.setTipoUsuario("ONG");
            notif.setMensagem("Atenção: O prazo de inscrição para o projeto '" + p.getTitulo() + "' encerra amanhã!");
            notificacaoRepository.save(notif);
        });
    }


    @Scheduled(cron = "0 0 0 * * *") // Roda todo dia à meia-noite
    //@Scheduled(fixedRate = 10000)
    @Transactional
    public void verificarPrazosExpirados() {
        LocalDate hoje = LocalDate.now();
        System.out.println(">>> Iniciando varredura de prazos encerrados em: " + hoje);
        
        // 1. Busca projetos que precisam ser finalizados (Ativos ou Pausados com prazo vencido)
        List<Projeto> paraFinalizar = projetoRepository.buscarProjetosParaFinalizar(hoje);

        System.out.println(">>> Encontrados " + paraFinalizar.size() + " projetos para encerrar.");

        // 2. Transição definitiva para FINALIZADA
        paraFinalizar.forEach(p -> {
            p.setStatus(StatusProjeto.FINALIZADA);
            System.out.println(">>> Projeto [" + p.getTitulo() + "] FINALIZADO automaticamente por decurso de prazo.");

            // Notifica a ONG que o projeto foi encerrado pelo sistema
            Notificacao notif = new Notificacao();
            notif.setDestinatarioId(p.getOng().getId());
            notif.setTipoUsuario("ONG");
            notif.setMensagem("O projeto '" + p.getTitulo() + "' foi finalizado automaticamente devido ao prazo.");
            notificacaoRepository.save(notif);
        });

        projetoRepository.saveAll(paraFinalizar);
    }


    // Método auxiliar para transformar Entity em DTO de saída
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