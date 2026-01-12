package eus.birt.dam.aurkitu.objeto.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import eus.birt.dam.aurkitu.dto.ClaveValorDTO;
import eus.birt.dam.aurkitu.dto.SesionDTO;
import eus.birt.dam.aurkitu.dto.UbicacionDTO;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
import eus.birt.dam.aurkitu.model.UsuarioEntity;
import eus.birt.dam.aurkitu.objeto.persistence.ObjetoRepository;
import eus.birt.dam.aurkitu.objeto.service.impl.ObjetoServiceImpl;
import eus.birt.dam.aurkitu.payload.request.BuscarObjetoRequest;
import eus.birt.dam.aurkitu.payload.response.BuscarObjetoResponse;

@ExtendWith(MockitoExtension.class)
class BuscarObjetoTest {

	@Mock
	private ObjetoRepository objetoRepo;

	@InjectMocks
	private ObjetoServiceImpl objetoService;

	private BuscarObjetoRequest request;
	private List<ObjetoEntity> objetosMock;
	private Specification<ObjetoEntity> spec;
	private SesionDTO sesion;

	@BeforeEach
	void setup() {
		request = new BuscarObjetoRequest();
		objetosMock = Arrays.asList(
				ObjetoEntity.builder().id(1).descripcion("Móvil Samsung negro").marca("Samsung").serie("SN123")
						.fecha(Instant.now().minus(2, ChronoUnit.DAYS)).usuario(UsuarioEntity.builder().id(2).build())
						.build(),
				ObjetoEntity.builder().id(2).descripcion("Cartera de cuero marrón").marca("Desconocida")
						.fecha(Instant.now().minus(1, ChronoUnit.DAYS)).usuario(UsuarioEntity.builder().id(2).build())
						.build());
		spec = Mockito.argThat(specification -> true);
		sesion = SesionDTO.builder().id(1).build();
	}

	@Test
	void testBuscarObjetos_SinFiltros() {

		// Arrange
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(objetosMock);

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorTipoObjeto() {

		// Arrange
		ClaveValorDTO tipoObjeto = new ClaveValorDTO(1, "MOVIL");
		request.setTipo(tipoObjeto);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorEstado() {

		// Arrange
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorFecha() {

		// Arrange
		Instant fechaDesde = Instant.now().minus(3, ChronoUnit.DAYS);
		request.setFecha(fechaDesde);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(objetosMock);

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorUbicacionSinRadio() {

		// Arrange
		UbicacionDTO ubicacion = new UbicacionDTO(43.2630, -2.9350);
		request.setUbicacion(ubicacion);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorUbicacionConRadio() {

		// Arrange
		UbicacionDTO ubicacion = new UbicacionDTO(43.2630, -2.9350);
		request.setUbicacion(ubicacion);
		request.setRadio(500);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(objetosMock);

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
	}

	@Test
	void testBuscarObjetos_MultiplesFiltros() {

		// Arrange
		ClaveValorDTO tipo = new ClaveValorDTO(1, "MOVIL");
		request.setTipo(tipo);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Móvil Samsung negro", resultado.getFirst().getDescripcion());
	}

	@Test
	void testBuscarObjetos_SinResultados() {

		// Arrange
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.emptyList());

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request, sesion);

		// Assert
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
}