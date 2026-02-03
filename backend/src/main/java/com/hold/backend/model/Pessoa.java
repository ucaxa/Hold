package com.hold.backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pessoas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Nome é obrigatório")
	@Column(nullable = false)
	private String nome;

	@Column(length = 14)
	private String cpf;

	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascimento;

	@Enumerated(EnumType.STRING)
	@NotNull(message = "Sexo é obrigatório")
	@Column(nullable = false)
	private Sexo sexo;

	@NotNull(message = "Altura é obrigatória")
	@Column(nullable = false, precision = 4, scale = 2)
	private BigDecimal altura;

	@Column(length = 255)
	private String email;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	private LocalDateTime updatedAt;
}
