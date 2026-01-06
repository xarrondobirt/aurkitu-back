package eus.birt.dam.aurkitu.model;

import java.time.Instant;

import org.locationtech.jts.geom.Point;

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
import jakarta.validation.constraints.NotNull;
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

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estado_id", referencedColumnName = "id")
	private EstadoObjetoEntity estado;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private UsuarioEntity usuario;

	// Coordenadas. 4326 estándar GPS
	@NotNull
	@Column(columnDefinition = "geometry(Point, 4326)")
	private Point ubicacion;

	@NotNull
	@Column(name = "radio_metros")
	private Integer radio;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "tipo", referencedColumnName = "id")
	private TipoObjetoEntity tipo;

	@NotBlank
	private String descripcion;

	private String marca;

	@Column(name = "numero_serie")
	private String serie;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "color", referencedColumnName = "id")
	private ColorEntity color;

	@Column(name = "foto_url")
	private String foto;

	@Column(name = "factura_url")
	private String factura;

	@NotNull
	@Column(name = "fecha_perdida")
	private Instant fecha;

	@NotNull
	@Column(name = "create_date")
	private final Instant createDate = Instant.now();

	@NotNull
	@Column(name = "last_update_date")
	private Instant lastUpdateDate;
}
