package com.hold.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hold.backend.model.Sexo;
import com.hold.backend.validation.ValidCpf;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "DTO de requisição para Pessoa")
public class PessoaRequest {

	@NotBlank(message = "Nome é obrigatório")
	@Schema(description = "Nome completo", example = "João Silva")
	private String nome;

	@ValidCpf(message = "CPF inválido")
	@Schema(description = "CPF", example = "123.456.789-09")
	private String cpf;

	@JsonFormat(pattern = "yyyy-MM-dd")
	@Schema(description = "Data de nascimento", example = "1990-05-15")
	private LocalDate dataNascimento;

	@NotNull(message = "Sexo é obrigatório")
	@Schema(description = "Sexo (M ou F)", example = "M")
	private Sexo sexo;

	@NotNull(message = "Altura é obrigatória")
	@Schema(description = "Altura em metros", example = "1.75")
	private Double altura;

	@Email(message = "E-mail inválido")
	@Schema(description = "E-mail", example = "joao@email.com")
	private String email;
}
