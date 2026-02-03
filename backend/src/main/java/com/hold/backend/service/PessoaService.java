package com.hold.backend.service;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PessoaService {

	List<PessoaResponse> pesquisar(String nome);
	Page<PessoaResponse> listarPaginado(Pageable pageable);
	PessoaResponse pesquisarPorId(Long id);
	PessoaResponse incluir(PessoaRequest request);
	PessoaResponse alterar(Long id, PessoaRequest request);
	void excluir(Long id);
	PesoIdealResponse calcularPesoIdeal(Long id);
}
