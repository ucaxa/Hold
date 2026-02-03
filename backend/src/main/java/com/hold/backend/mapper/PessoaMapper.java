package com.hold.backend.mapper;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.model.Pessoa;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PessoaMapper {

	public Pessoa toEntity(PessoaRequest request) {
		if (request == null) {
			return null;
		}
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(request.getNome());
		pessoa.setCpf(request.getCpf());
		pessoa.setDataNascimento(request.getDataNascimento());
		pessoa.setSexo(request.getSexo());
		pessoa.setAltura(request.getAltura() != null ? BigDecimal.valueOf(request.getAltura()) : null);
		pessoa.setEmail(request.getEmail());
		return pessoa;
	}

	public PessoaResponse toResponse(Pessoa pessoa) {
		if (pessoa == null) {
			return null;
		}
		PessoaResponse response = new PessoaResponse();
		response.setId(pessoa.getId());
		response.setNome(pessoa.getNome());
		response.setCpf(pessoa.getCpf());
		response.setDataNascimento(pessoa.getDataNascimento());
		response.setSexo(pessoa.getSexo());
		response.setAltura(pessoa.getAltura() != null ? pessoa.getAltura().doubleValue() : null);
		response.setEmail(pessoa.getEmail());
		response.setCreatedAt(pessoa.getCreatedAt());
		response.setUpdatedAt(pessoa.getUpdatedAt());
		return response;
	}

	public void updateEntityFromRequest(PessoaRequest request, Pessoa pessoa) {
		if (request == null || pessoa == null) {
			return;
		}
		pessoa.setNome(request.getNome());
		pessoa.setCpf(request.getCpf());
		pessoa.setDataNascimento(request.getDataNascimento());
		pessoa.setSexo(request.getSexo());
		pessoa.setAltura(request.getAltura() != null ? BigDecimal.valueOf(request.getAltura()) : null);
		pessoa.setEmail(request.getEmail());
	}

	public List<PessoaResponse> toResponseList(List<Pessoa> pessoas) {
		if (pessoas == null) {
			return List.of();
		}
		return pessoas.stream().map(this::toResponse).collect(Collectors.toList());
	}
}
