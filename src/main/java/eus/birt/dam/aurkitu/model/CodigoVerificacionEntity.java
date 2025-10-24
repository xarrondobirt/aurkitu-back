package eus.birt.dam.aurkitu.model;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla codigos_verificacion
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "codigos_verificacion")
public class CodigoVerificacionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UsuarioEntity usuario;

	@NotBlank
	@Size(max = 6)
	@Column(name = "code")
	private String codigo;

	@Nonnull
	@Column(name = "create_date")
	private final Instant createDate = Instant.now();

	@Nonnull
	@Column(name = "expiration_date")
	private final Instant expirationDate = Instant.now().plus(1, ChronoUnit.HOURS);

}
