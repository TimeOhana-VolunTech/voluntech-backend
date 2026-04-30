package com.voluntech.voluntech_backend.service;

import com.voluntech.voluntech_backend.dto.ProjetoRequestDTO;
import com.voluntech.voluntech_backend.dto.ProjetoResponseDTO;
import com.voluntech.voluntech_backend.model.Ong;
import com.voluntech.voluntech_backend.model.Projeto;
import com.voluntech.voluntech_backend.model.enums.Categoria;
import com.voluntech.voluntech_backend.model.enums.Modalidade;
import com.voluntech.voluntech_backend.model.enums.StatusProjeto;
import com.voluntech.voluntech_backend.repository.OngRepository;
import com.voluntech.voluntech_backend.repository.ProjetoRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
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
        // Regra de Negócio: Exclusão só é permitida sem candidatos
        // Por enquanto, como não há tabela de inscrições, a exclusão é livre.
        projetoRepository.delete(projeto);
    }

    public List<ProjetoResponseDTO> explorarProjetos(Categoria categoria, Modalidade modalidade, String termo) {
        return projetoRepository.buscarOportunidades(categoria, modalidade, termo)
                .stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }


    // Método auxiliar para transformar Entity em DTO de saída
    private ProjetoResponseDTO converterParaResponseDTO(Projeto projeto) {
        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getDescricao(),
                projeto.getPrazo(),
                projeto.getStatus(),
                projeto.getModalidade()!= null ? projeto.getModalidade().toString() : null,
                projeto.getCategoria() != null ? projeto.getCategoria().toString() : null,
                projeto.getOng().getId(),
                projeto.getOng().getNome()
        );
    }
}