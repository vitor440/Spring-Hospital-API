ALTER TABLE resultado_consulta
ALTER COLUMN sintomas TYPE text[]
USING sintomas::text[];