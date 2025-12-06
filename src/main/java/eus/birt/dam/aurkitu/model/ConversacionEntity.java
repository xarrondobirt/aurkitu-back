package eus.birt.dam.aurkitu.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla conversacion
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "conversacion")
public class ConversacionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "participante1_id", nullable = false)
	private UsuarioEntity participante1;

	@ManyToOne
	@JoinColumn(name = "participante2_id", nullable = false)
	private UsuarioEntity participante2;

	@OneToMany(mappedBy = "conversacion")
	@OrderBy("fechaEnvio ASC")
	@Builder.Default
	private List<MensajeEntity> mensajes = new ArrayList<>();

	@ManyToOne
	@JoinColumn(name = "objeto_id", nullable = false)
	private ObjetoEntity objeto;

	@Column(name = "create_date")
	private Instant createDate;

	@Column(name = "last_update_date")
	private Instant lastUpdateDate;

}
