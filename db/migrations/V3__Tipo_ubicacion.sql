/* Cambio de tipo geography a geometry en public.objeto*/
ALTER TABLE public.objeto
ALTER COLUMN ubicacion TYPE geometry(Point, 4326)
USING ubicacion::geometry; --POSGIS puede castear un tipo a otro para los datos existentes