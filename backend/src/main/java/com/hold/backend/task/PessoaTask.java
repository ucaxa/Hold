package com.hold.backend.task;

import com.hold.backend.dto.PessoaRequest;
import com.hold.backend.dto.PessoaResponse;
import com.hold.backend.dto.PesoIdealResponse;
import com.hold.backend.exception.PessoaNotFoundException;
import com.hold.backend.mapper.PessoaMapper;
import com.hold.backend.model.Pessoa;
import com.hold.backend.model.Sexo;
import com.hold.backend.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Classe Task que efetivamente executa as operações de CRUD e cálculo de peso ideal.
 * Chamada pelo Service conforme especificação do desafio.
 */
@Component
@RequiredArgsConstructor
public class PessoaTask {

	private final PessoaRepository pessoaRepository;
	private final PessoaMapper pessoaMapper;

	@Transactional
	public PessoaResponse incluir(PessoaRequest request) {
		Pessoa pessoa = pessoaMapper.toEntity(request);
		Pessoa salva = pessoaRepository.save(pessoa);
		return pessoaMapper.toResponse(salva);
	}

	@Transactional
	public PessoaResponse alterar(Long id, PessoaRequest request) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException(id));
		pessoaMapper.updateEntityFromRequest(request, pessoa);
		Pessoa atualizada = pessoaRepository.save(pessoa);
		return pessoaMapper.toResponse(atualizada);
	}

	@Transactional
	public void excluir(Long id) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException(id));
		pessoaRepository.delete(pessoa);
	}

	public List<PessoaResponse> pesquisar(String nome) {
		if (nome == null || nome.isBlank()) {
			return pessoaMapper.toResponseList(pessoaRepository.findAll());
		}
		return pessoaMapper.toResponseList(pessoaRepository.findByNomeContainingIgnoreCase(nome.trim()));
	}

	public Page<PessoaResponse> listarPaginado(Pageable pageable) {
		return pessoaRepository.findAll(pageable).map(pessoaMapper::toResponse);
	}

	public PessoaResponse pesquisarPorId(Long id) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException(id));
		return pessoaMapper.toResponse(pessoa);
	}

	/**
	 * Calcula o peso ideal conforme especificação:
	 * Homens: (72,7 * altura) - 58
	 * Mulheres: (62,1 * altura) - 44,7
	 */
	public PesoIdealResponse calcularPesoIdeal(Long id) {
		Pessoa pessoa = pessoaRepository.findById(id)
				.orElseThrow(() -> new PessoaNotFoundException(id));
		double altura = pessoa.getAltura().doubleValue();
		double pesoIdeal;
		if (pessoa.getSexo() == Sexo.M) {
			pesoIdeal = (72.7 * altura) - 58;
		} else {
			pesoIdeal = (62.1 * altura) - 44.7;
		}
		return new PesoIdealResponse(pessoa.getId(), pessoa.getNome(), Math.round(pesoIdeal * 100.0) / 100.0);
	}
}
