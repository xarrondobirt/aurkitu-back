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
import eus.birt.dam.aurkitu.dto.UbicacionDTO;
import eus.birt.dam.aurkitu.model.ObjetoEntity;
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

	@BeforeEach
	void setup() {
		request = new BuscarObjetoRequest();
		objetosMock = Arrays.asList(
				ObjetoEntity.builder().id(1).descripcion("Móvil Samsung negro").marca("Samsung").serie("SN123")
						.fecha(Instant.now().minus(2, ChronoUnit.DAYS)).build(),
				ObjetoEntity.builder().id(2).descripcion("Cartera de cuero marrón").marca("Desconocida")
						.fecha(Instant.now().minus(1, ChronoUnit.DAYS)).build());
		spec = Mockito.argThat(specification -> true);
	}

	@Test
	void testBuscarObjetos_SinFiltros() {

		// Arrange
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(objetosMock);

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorDescripcion() {

		// Arrange
		request.setDescripcion("móvil");
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Móvil Samsung negro", resultado.getFirst().getDescripcion());
	}

	@Test
	void testBuscarObjetos_FiltroPorMarca() {

		// Arrange
		request.setMarca("samsung");
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Samsung", resultado.getFirst().getMarca());
	}

	@Test
	void testBuscarObjetos_FiltroPorNumSerie() {

		// Arrange
		request.setSerie("SN123");
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("SN123", resultado.getFirst().getSerie());
	}

	@Test
	void testBuscarObjetos_FiltroPorTipoObjeto() {

		// Arrange
		ClaveValorDTO tipoObjeto = new ClaveValorDTO(1, "MOVIL");
		request.setTipo(tipoObjeto);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorColor() {

		// Arrange
		ClaveValorDTO color = new ClaveValorDTO(2, "NEGRO");
		request.setColor(color);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
	}

	@Test
	void testBuscarObjetos_FiltroPorEstado() {

		// Arrange
		ClaveValorDTO estado = new ClaveValorDTO(1, "PERDIDO");
		request.setEstado(estado);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

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
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

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
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

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
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(2, resultado.size());
	}

	@Test
	void testBuscarObjetos_MultiplesFiltros() {

		// Arrange
		request.setDescripcion("samsung");
		request.setMarca("Samsung");
		ClaveValorDTO tipo = new ClaveValorDTO(1, "MOVIL");
		request.setTipo(tipo);
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.singletonList(objetosMock.getFirst()));

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertEquals(1, resultado.size());
		assertEquals("Móvil Samsung negro", resultado.getFirst().getDescripcion());
	}

	@Test
	void testBuscarObjetos_SinResultados() {

		// Arrange
		request.setDescripcion("inexistente");
		Mockito.when(objetoRepo.findAll(spec)).thenReturn(Collections.emptyList());

		// Act
		List<BuscarObjetoResponse> resultado = objetoService.buscarObjetos(request);

		// Assert
		assertNotNull(resultado);
		assertTrue(resultado.isEmpty());
	}
}