alter table disponibilidade_medico alter column dia_semana varchar(50) not null;

alter table consulta alter column dia_semana varchar(50) not null;

alter table disponibilidade_medico add constraint chk_dia_semana check (dia_semana in ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));

alter table consulta add constraint chk_dia_semana check (dia_semana in ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'));