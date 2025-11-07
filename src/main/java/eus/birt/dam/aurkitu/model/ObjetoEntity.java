package eus.birt.dam.aurkitu.model;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla objeto
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "objeto")
public class ObjetoEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotBlank
	private String estado;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false, insertable = false)
	private UsuarioEntity usuario;

	// Coordenadas. 4326 estándar GPS
	@Nonnull
	@Column(columnDefinition = "geography(Point, 4326)")
	private Point ubicacion;

	@Nonnull
	@Column(name = "radio_metros")
	private Integer radio;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tipo", referencedColumnName = "id")
	private TipoObjetoEntity tipoObjeto;

	@NotBlank
	private String descripcion;

	private String marca;

	@Column(name = "numero_serie")
	private String numSerie;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "color", referencedColumnName = "id")
	private ColorEntity color;

	@Column(name = "foto_url")
	private String foto;

	@Column(name = "factura_url")
	private String factura;

	@Nonnull
	@Column(name = "fecha_perdida")
	private Instant fechaPerdida;

	@Nonnull
	@Column(name = "create_date")
	private Instant createDate;

	@Nonnull
	@Column(name = "last_update_date")
	private Instant lastUpdateDate;
}
