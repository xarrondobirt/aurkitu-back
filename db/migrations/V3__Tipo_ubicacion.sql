/* Cambio de tipo geography a geometry en public.objeto*/
ALTER TABLE public.objeto
ALTER COLUMN ubicacion TYPE geometry(Point, 4326)
USING ST_SetSRID(ST_MakePoint(ST_X(ubicacion::geometry), ST_Y(ubicacion::geometry)), 4326);
      #USING ubicacion::geometry;