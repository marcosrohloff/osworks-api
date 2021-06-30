package com.algaworks.osworks.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algaworks.osworks.exceptionhandler.EntidadeNaoEncontradaException;
import com.algaworks.osworks.exceptionhandler.NegocioException;
import com.algaworks.osworks.model.Cliente;
import com.algaworks.osworks.model.Comentario;
import com.algaworks.osworks.model.OrdemServico;
import com.algaworks.osworks.model.StatusOrdemServico;
import com.algaworks.osworks.repository.ClienteRepository;
import com.algaworks.osworks.repository.ComentarioRepository;
import com.algaworks.osworks.repository.OrdemServicoRepository;

@Service
public class GestaoOrdemServicoService {

	@Autowired
	public OrdemServicoRepository ordemServicoRepository;

	@Autowired
	public ComentarioRepository comentarioRepository;

	@Autowired
	public ClienteRepository clienteRepository;

	public OrdemServico criar(OrdemServico ordemServico) {
		Cliente cliente = clienteRepository.findById(ordemServico.getCliente().getId())
				.orElseThrow(() -> new NegocioException("cliente não encontrado!"));

		ordemServico.setCliente(cliente);
		ordemServico.setStatus(StatusOrdemServico.ABERTA);
		ordemServico.setDataAbertura(OffsetDateTime.now());

		return ordemServicoRepository.save(ordemServico);
	}

	public void finalizar(Long ordemServicoId) {
		OrdemServico ordemServico = buscar(ordemServicoId);
		
		ordemServico.finalizar();		
		ordemServicoRepository.save(ordemServico);
	}

	public Comentario adicionarComentario(Long ordemServicoId, String descricao) {
		OrdemServico ordemServico = buscar(ordemServicoId);

		Comentario comentario = new Comentario();
		comentario.setDataEnvio(OffsetDateTime.now());
		comentario.setDescricao(descricao);
		comentario.setOrdemServico(ordemServico);

		return comentarioRepository.save(comentario);

	}

	private OrdemServico buscar(Long ordemServicoId) {
		return ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de Serviço não encontrada!"));
	}

}
