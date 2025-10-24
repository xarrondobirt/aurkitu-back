package eus.birt.dam.aurkitu.model;

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

	@NotBlank
	@Column(name = "hashed_password")
	private String password;

	@NotBlank
	@Column(name = "email")
	private String email;

	@Column(name = "verificado")
	private boolean verificado;

	@Column(name = "last_update_date")
	private final Instant updateDate = Instant.now();

	@Column(name = "create_date")
	private final Instant createDate = Instant.now();

}
