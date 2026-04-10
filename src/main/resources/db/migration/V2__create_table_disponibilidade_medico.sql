create table turno_atendimento_medico(
   id bigint generated always as identity primary key,
   dia_semana varchar(50) not null,
   hora_inicio time not null,
   hora_fim time not null,
   medico_id bigint references medico(id) not null,
   constraint chk_dia_semana check (dia_semana in ('SEGUNDA', 'TERCA', 'QUARTA', 'QUINTA', 'SEXTA', 'SABADO', 'DOMINGO'))
);