package eus.birt.proyecto.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla usuario
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuario")
public class UsuarioEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	@Size(max = 20)
	private String username;

	@Column(name = "email")
	@NotBlank
	private String email;

	@NotBlank
	@Column(name = "password")
	private String password;

	@Column(name = "last_updated_by")
	private final Instant updatedBy = Instant.now();

	@Column(name = "created_by")
	private final Instant createdBy = Instant.now();

}
