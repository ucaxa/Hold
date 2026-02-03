package com.hold.backend.service.impl;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;
import com.hold.backend.task.PessoaTask;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PessoaServiceImpl")
class PessoaServiceImplTest {

	@Mock
	private PessoaTask pessoaTask;

	@InjectMocks
	private PessoaServiceImpl service;

	@Test
	void pesquisarDelegaParaTask() {
		List<PessoaResponse> list = List.of(new PessoaResponse());
		when(pessoaTask.pesquisar("Silva")).thenReturn(list);

		List<PessoaResponse> result = service.pesquisar("Silva");

		assertThat(result).isEqualTo(list);
		verify(pessoaTask).pesquisar("Silva");
	}

	@Test
	void listarPaginadoDelegaParaTask() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<PessoaResponse> page = new PageImpl<>(List.of(new PessoaResponse()), pageable, 1);
		when(pessoaTask.listarPaginado(pageable)).thenReturn(page);

		Page<PessoaResponse> result = service.listarPaginado(pageable);

		assertThat(result).isEqualTo(page);
		verify(pessoaTask).listarPaginado(pageable);
	}

	@Test
	void pesquisarPorIdDelegaParaTask() {
		Long id = 1L;
		PessoaResponse response = new PessoaResponse();
		response.setId(id);
		when(pessoaTask.pesquisarPorId(id)).thenReturn(response);

		PessoaResponse result = service.pesquisarPorId(id);

		assertThat(result).isEqualTo(response);
		verify(pessoaTask).pesquisarPorId(id);
	}

	@Test
	void incluirDelegaParaTask() {
		PessoaRequest request = new PessoaRequest();
		request.setNome("Novo");
		PessoaResponse response = new PessoaResponse();
		response.setId(1L);
		when(pessoaTask.incluir(request)).thenReturn(response);

		PessoaResponse result = service.incluir(request);

		assertThat(result).isEqualTo(response);
		verify(pessoaTask).incluir(request);
	}

	@Test
	void alterarDelegaParaTask() {
		Long id = 1L;
		PessoaRequest request = new PessoaRequest();
		request.setNome("Alterado");
		PessoaResponse response = new PessoaResponse();
		when(pessoaTask.alterar(id, request)).thenReturn(response);

		PessoaResponse result = service.alterar(id, request);

		assertThat(result).isEqualTo(response);
		verify(pessoaTask).alterar(id, request);
	}

	@Test
	void excluirDelegaParaTask() {
		Long id = 1L;
		service.excluir(id);
		verify(pessoaTask).excluir(id);
	}

	@Test
	void calcularPesoIdealDelegaParaTask() {
		Long id = 1L;
		PesoIdealResponse response = new PesoIdealResponse(id, "Teste", 69.23);
		when(pessoaTask.calcularPesoIdeal(id)).thenReturn(response);

		PesoIdealResponse result = service.calcularPesoIdeal(id);

		assertThat(result).isEqualTo(response);
		verify(pessoaTask).calcularPesoIdeal(id);
	}
}
