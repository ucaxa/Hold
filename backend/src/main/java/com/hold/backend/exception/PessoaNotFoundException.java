package com.hold.backend.exception;

public class PessoaNotFoundException extends RuntimeException {

	public PessoaNotFoundException(Long id) {
		super("Pessoa não encontrada com ID: " + id);
	}
}
