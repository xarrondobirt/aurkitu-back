package eus.birt.dam.aurkitu.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidad que mapea la tabla mensaje
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensajes")
public class MensajeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "conversacion_id")
	private ConversacionEntity conversacion;

	@ManyToOne
	@JoinColumn(name = "remitente_id")
	private UsuarioEntity remitente;

	@NotNull
	@Column
	private String contenido;

	@Builder.Default
	private boolean leido = false;

	@Column(name = "create_date")
	private Instant createDate;

}
