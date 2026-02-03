package com.hold.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hold.backend.model.Sexo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO de resposta para Pessoa")
public class PessoaResponse {

	private Long id;
	private String nome;
	private String cpf;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;

	private Sexo sexo;
	private Double altura;
	private String email;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createdAt;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updatedAt;
}
