package com.hold.backend.service.impl;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;
import com.hold.backend.service.PessoaService;
import com.hold.backend.task.PessoaTask;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaServiceImpl implements PessoaService {

	private final PessoaTask pessoaTask;

	@Override
	public List<PessoaResponse> pesquisar(String nome) {
		return pessoaTask.pesquisar(nome);
	}

	@Override
	public Page<PessoaResponse> listarPaginado(Pageable pageable) {
		return pessoaTask.listarPaginado(pageable);
	}

	@Override
	public PessoaResponse pesquisarPorId(Long id) {
		return pessoaTask.pesquisarPorId(id);
	}

	@Override
	public PessoaResponse incluir(PessoaRequest request) {
		return pessoaTask.incluir(request);
	}

	@Override
	public PessoaResponse alterar(Long id, PessoaRequest request) {
		return pessoaTask.alterar(id, request);
	}

	@Override
	public void excluir(Long id) {
		pessoaTask.excluir(id);
	}

	@Override
	public PesoIdealResponse calcularPesoIdeal(Long id) {
		return pessoaTask.calcularPesoIdeal(id);
	}
}
