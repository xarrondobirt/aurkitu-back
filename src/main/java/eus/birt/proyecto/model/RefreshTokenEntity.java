package eus.birt.proyecto.model;

import java.time.Instant;

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
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne
	@JoinColumn(name = "usuario_id", referencedColumnName = "id")
	private UsuarioEntity usuario;

	@NotBlank
	@Column(name = "token")
	private String token;

	@Nonnull
	@Column(name = "created_date")
	private final Instant createDate = Instant.now();

	@Nonnull
	@Column(name = "fecha_expiracion")
	private Instant expiracion;

	/**
	 * Método que comprueba si un refreshtoken está caducado
	 * 
	 * @return true si ha caducado, false en caso contrario
	 */
	public boolean isExpirado() {
		return this.expiracion.isBefore(Instant.now());
	}

}
