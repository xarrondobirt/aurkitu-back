/*
  SCRIPT DE MIGRACIÓN (v1 -> v2)
  Propósito: Cambia el ENUM 'estado_objeto' por una tabla de referencia.
  Soluciona el conflicto de nombres.
*/

-- 1. Renombrar el TIPO (ENUM) antiguo para liberar el nombre.
-- Ahora se llamará 'estado_objeto_old' y 'estado_objeto' queda libre.
ALTER TYPE public.estado_objeto RENAME TO estado_objeto_old;

-- 2. Ahora SÍ podemos crear la TABLA nueva
CREATE TABLE public.estado_objeto (
    id int NOT NULL,
    codigo text NOT NULL,
    CONSTRAINT estado_objeto_pkey PRIMARY KEY (id),
    CONSTRAINT estado_objeto_codigo_key UNIQUE (codigo)
); 

-- 3. Poblar la tabla de referencia 
INSERT INTO public.estado_objeto(id, codigo) VALUES
(1, 'perdido'),
(2, 'encontrado'),
(3, 'devuelto');

-- 4. Añadir la nueva columna 'estado_id' a 'objeto'
ALTER TABLE public.objeto
ADD COLUMN estado_id int;

-- 5. Migrar los datos: Rellenar la nueva columna 'estado_id'
-- basándose en los valores del antiguo 'estado' (tipo 'estado_objeto_old')
-- ¡Importante! Hacemos CAST (::text) al ENUM para compararlo como texto.
UPDATE public.objeto
SET estado_id = CASE estado::text
    WHEN 'perdido' THEN 1
    WHEN 'encontrado' THEN 2
    WHEN 'devuelto' THEN 3
    ELSE 1 -- Default de seguridad
END;

-- 6. Hacer la nueva columna 'estado_id' oficial
-- (Separamos SET NOT NULL y SET DEFAULT en dos comandos)
ALTER TABLE public.objeto
ALTER COLUMN estado_id SET NOT NULL;

ALTER TABLE public.objeto
ALTER COLUMN estado_id SET DEFAULT 1;

-- 7. Crear la Foreign Key
ALTER TABLE public.objeto
ADD CONSTRAINT objeto_estado_id_fkey 
    FOREIGN KEY (estado_id)
    REFERENCES public.estado_objeto(id)
    ON DELETE NO ACTION;

-- 8. Limpieza: Eliminar la columna 'estado' (ENUM) original
-- Ahora podemos borrarla, liberando la dependencia del tipo antiguo.
ALTER TABLE public.objeto
DROP COLUMN estado;

-- 9. Limpieza: Eliminar el TIPO (ENUM) antiguo que ya no se usa
-- Ahora que ninguna columna depende de él, podemos borrarlo.
DROP TYPE public.estado_objeto_old;