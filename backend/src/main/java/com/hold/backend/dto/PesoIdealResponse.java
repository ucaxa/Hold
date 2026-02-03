package com.hold.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta do cálculo de peso ideal")
public class PesoIdealResponse {

	@Schema(description = "ID da pessoa")
	private Long pessoaId;

	@Schema(description = "Nome da pessoa")
	private String nome;

	@Schema(description = "Peso ideal em kg")
	private Double pesoIdealKg;
}
