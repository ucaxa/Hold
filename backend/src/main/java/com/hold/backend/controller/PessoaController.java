package com.hold.backend.controller;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;
import com.hold.backend.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
@RequiredArgsConstructor
@Tag(name = "Pessoas", description = "API para gestão de pessoas - Hold")
public class PessoaController {

	private final PessoaService pessoaService;

	@GetMapping
	@Operation(summary = "Pesquisar pessoas (por nome ou todas)")
	public ResponseEntity<List<PessoaResponse>> pesquisar(
			@RequestParam(required = false) String nome) {
		return ResponseEntity.ok(pessoaService.pesquisar(nome));
	}

	@GetMapping("/paginado")
	@Operation(summary = "Listar pessoas com paginação")
	public ResponseEntity<Page<PessoaResponse>> listarPaginado(
			@PageableDefault(size = 5, sort = "nome") Pageable pageable) {
		return ResponseEntity.ok(pessoaService.listarPaginado(pageable));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar pessoa por ID")
	public ResponseEntity<PessoaResponse> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(pessoaService.pesquisarPorId(id));
	}

	@PostMapping
	@Operation(summary = "Incluir pessoa")
	public ResponseEntity<PessoaResponse> incluir(@Valid @RequestBody PessoaRequest request) {
		PessoaResponse response = pessoaService.incluir(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Alterar pessoa")
	public ResponseEntity<PessoaResponse> alterar(
			@PathVariable Long id,
			@Valid @RequestBody PessoaRequest request) {
		return ResponseEntity.ok(pessoaService.alterar(id, request));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir pessoa")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		pessoaService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/peso-ideal")
	@Operation(summary = "Calcular peso ideal da pessoa (homens: 72,7*altura-58; mulheres: 62,1*altura-44,7)")
	public ResponseEntity<PesoIdealResponse> calcularPesoIdeal(@PathVariable Long id) {
		return ResponseEntity.ok(pessoaService.calcularPesoIdeal(id));
	}
}
