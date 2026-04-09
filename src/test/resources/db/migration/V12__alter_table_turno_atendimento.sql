alter table turno_atendimento_medico alter column dia_semana type varchar(60);

ALTER TABLE turno_atendimento_medico ALTER COLUMN dia_semana SET NOT NULL;

alter table consulta alter column dia_semana type varchar(60);

ALTER TABLE consulta ALTER COLUMN dia_semana SET NOT NULL;


alter table turno_atendimento_medico add constraint chk_dia_semana check (dia_semana in ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

alter table consulta add constraint chk_dia_semana check (dia_semana in ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));