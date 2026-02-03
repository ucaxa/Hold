package com.hold.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;
import com.hold.backend.exception.PessoaNotFoundException;
import com.hold.backend.model.Sexo;
import com.hold.backend.service.PessoaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.hold.backend.exception.GlobalExceptionHandler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PessoaController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("PessoaController")
class PessoaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PessoaService pessoaService;

	@Nested
	@DisplayName("GET /api/pessoas")
	class Pesquisar {

		@Test
		void retornaListaQuandoPesquisa() throws Exception {
			PessoaResponse r = new PessoaResponse();
			r.setId(1L);
			r.setNome("João");
			when(pessoaService.pesquisar("João")).thenReturn(List.of(r));

			mockMvc.perform(get("/api/pessoas").param("nome", "João"))
					.andExpect(status().isOk())
					.andExpect(content().contentType(MediaType.APPLICATION_JSON))
					.andExpect(jsonPath("$", hasSize(1)))
					.andExpect(jsonPath("$[0].id").value(1))
					.andExpect(jsonPath("$[0].nome").value("João"));

			verify(pessoaService).pesquisar("João");
		}

		@Test
		void aceitaSemParametroNome() throws Exception {
			when(pessoaService.pesquisar(null)).thenReturn(List.of());

			mockMvc.perform(get("/api/pessoas"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$", hasSize(0)));

			verify(pessoaService).pesquisar(null);
		}
	}

	@Nested
	@DisplayName("GET /api/pessoas/paginado")
	class ListarPaginado {

		@Test
		void retornaPagina() throws Exception {
			PessoaResponse r = new PessoaResponse();
			r.setId(1L);
			r.setNome("Maria");
			Page<PessoaResponse> page = new PageImpl<>(List.of(r), PageRequest.of(0, 5), 1);
			when(pessoaService.listarPaginado(any())).thenReturn(page);

			mockMvc.perform(get("/api/pessoas/paginado").param("page", "0").param("size", "5"))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.content", hasSize(1)))
					.andExpect(jsonPath("$.content[0].nome").value("Maria"))
					.andExpect(jsonPath("$.totalElements").value(1));
		}
	}

	@Nested
	@DisplayName("GET /api/pessoas/{id}")
	class BuscarPorId {

		@Test
		void retornaPessoaQuandoExiste() throws Exception {
			Long id = 1L;
			PessoaResponse r = new PessoaResponse();
			r.setId(id);
			r.setNome("Teste");
			when(pessoaService.pesquisarPorId(id)).thenReturn(r);

			mockMvc.perform(get("/api/pessoas/{id}", id))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.id").value(1))
					.andExpect(jsonPath("$.nome").value("Teste"));
		}

		@Test
		void retorna404QuandoNaoEncontrada() throws Exception {
			Long id = 999L;
			when(pessoaService.pesquisarPorId(id)).thenThrow(new PessoaNotFoundException(id));

			mockMvc.perform(get("/api/pessoas/{id}", id))
					.andExpect(status().isNotFound());
		}
	}

	@Nested
	@DisplayName("POST /api/pessoas")
	class Incluir {

		@Test
		void retorna201ComBody() throws Exception {
			PessoaRequest request = new PessoaRequest();
			request.setNome("Novo");
			request.setSexo(Sexo.M);
			request.setAltura(1.75);
			PessoaResponse response = new PessoaResponse();
			response.setId(1L);
			response.setNome("Novo");
			when(pessoaService.incluir(any(PessoaRequest.class))).thenReturn(response);

			mockMvc.perform(post("/api/pessoas")
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request)))
					.andExpect(status().isCreated())
					.andExpect(jsonPath("$.id").value(1))
					.andExpect(jsonPath("$.nome").value("Novo"));

			verify(pessoaService).incluir(any(PessoaRequest.class));
		}
	}

	@Nested
	@DisplayName("PUT /api/pessoas/{id}")
	class Alterar {

		@Test
		void retorna200ComBody() throws Exception {
			Long id = 1L;
			PessoaRequest request = new PessoaRequest();
			request.setNome("Alterado");
			request.setSexo(Sexo.F);
			request.setAltura(1.65);
			PessoaResponse response = new PessoaResponse();
			response.setId(id);
			response.setNome("Alterado");
			when(pessoaService.alterar(eq(id), any(PessoaRequest.class))).thenReturn(response);

			mockMvc.perform(put("/api/pessoas/{id}", id)
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(request)))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.nome").value("Alterado"));

			verify(pessoaService).alterar(eq(id), any(PessoaRequest.class));
		}
	}

	@Nested
	@DisplayName("DELETE /api/pessoas/{id}")
	class Excluir {

		@Test
		void retorna204() throws Exception {
			Long id = 1L;

			mockMvc.perform(delete("/api/pessoas/{id}", id))
					.andExpect(status().isNoContent());

			verify(pessoaService).excluir(id);
		}
	}

	@Nested
	@DisplayName("GET /api/pessoas/{id}/peso-ideal")
	class CalcularPesoIdeal {

		@Test
		void retornaPesoIdeal() throws Exception {
			Long id = 1L;
			PesoIdealResponse response = new PesoIdealResponse(id, "Teste", 69.23);
			when(pessoaService.calcularPesoIdeal(id)).thenReturn(response);

			mockMvc.perform(get("/api/pessoas/{id}/peso-ideal", id))
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.pessoaId").value(1))
					.andExpect(jsonPath("$.nome").value("Teste"))
					.andExpect(jsonPath("$.pesoIdealKg").value(69.23));
		}
	}
}
