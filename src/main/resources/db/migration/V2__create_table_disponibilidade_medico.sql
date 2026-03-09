create table disponibilidade_medico(
   id bigint generated always as identity primary key,
   dia_semana integer not null,
   hora_inicio time not null,
   hora_fim time not null,
   medico_id bigint references medico(id) not null
);