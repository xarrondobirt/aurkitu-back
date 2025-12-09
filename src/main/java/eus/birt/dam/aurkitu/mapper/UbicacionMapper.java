package eus.birt.dam.aurkitu.mapper;

import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import eus.birt.dam.aurkitu.dto.UbicacionDTO;

/**
 * Mapper para la ubicación
 */
@Mapper
public abstract class UbicacionMapper {

	public static final UbicacionMapper MAPPER = Mappers.getMapper(UbicacionMapper.class);

	public Point toPoint(UbicacionDTO ubicacionDTO) {

		if (ubicacionDTO == null) {
			return null;
		}

		GeometryFactory geometryFactory = new GeometryFactory();
		CoordinateXY coord = new CoordinateXY(ubicacionDTO.getLongitud(), ubicacionDTO.getLatitud());
		Point point = geometryFactory.createPoint(coord);
		point.setSRID(4326);

		return point;
	}

	public UbicacionDTO toDTO(Point point) {

		if (point == null) {
			return null;
		}

		return new UbicacionDTO(point.getX(), point.getY());
	}
}